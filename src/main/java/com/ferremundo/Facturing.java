package com.ferremundo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ferremundo.InvoiceLog.LogKind;
import com.ferremundo.db.Mongoi;
import com.ferremundo.stt.GSettings;
import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class Facturing extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			int clientReference=new Integer(request.getParameter("clientReference"));
			OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
			if(!onlineClient.isAuthenticated(request)&&!(
					onlineClient.hasAccess(AccessPermission.INVOICE_FACTURE)||
					onlineClient.hasAccess(AccessPermission.BASIC)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					)){
				response.sendError(response.SC_UNAUTHORIZED,"acceso denegado");return;
			}
			String commandParam=request.getParameter("command");
			String argsParam=URLDecoder.decode(request.getParameter("args"),"utf-8").toUpperCase();
			String[] argsspl=argsParam.split(" ");
			String invoiceREF=argsspl[0];
			String clientParam=request.getParameter("client");
			String agentParam=request.getParameter("agent");

			Invoice invoice=new Gson().fromJson(
					(new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\" }")).toString(),InvoiceFM01.class
					);
			invoice.setInvoiceType(Invoice.INVOICE_TYPE_TAXES_APLY);
			Client cClient=new Gson().fromJson(URLDecoder.decode(clientParam,"utf-8"), Client.class);
			invoice.setPrintedTo(cClient);
			float payment=invoice.getDebt();
			if(argsspl.length>1){
				payment=new Float(argsspl[1]);
			}
			if(invoice.attemptToLog(LogKind.FACTURE).isAllowed()){
				if(invoice.getDebt()>0){
					InvoiceLog paymentLog=new InvoiceLog(LogKind.PAYMENT, payment, onlineClient.getShopman().getLogin());
					InvoiceLog factureLog=new InvoiceLog(LogKind.FACTURE, true, onlineClient.getShopman().getLogin());
					
					float newdebt=invoice.getTotal()-payment;;
					new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"debt\" : "+newdebt+" }");
					new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"invoiceType\" : 0 }");
					new Mongoi().doPush(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"logs\" : "+new Gson().toJson(paymentLog)+" }");
					new Mongoi().doPush(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"logs\" : "+new Gson().toJson(factureLog)+" }");
					new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"updated\" : "+factureLog.getDate()+" }");
					new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"taxes\" : "+invoice.getTaxes()+" }");
					new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"facturedTo\" : "+new Gson().toJson(cClient)+" }");
					
					TheBox.instance().plus(invoice.getDebt());
					TheBox.instance().addLog(new TheBoxLog(invoice.getDebt(), factureLog.getDate(), invoiceREF, LogKind.PAYMENT.toString(), onlineClient.getShopman().getLogin()));
				}
				else{
					InvoiceLog factureLog=new InvoiceLog(LogKind.FACTURE, true, onlineClient.getShopman().getLogin());
					new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"invoiceType\" : 0 }");
					new Mongoi().doPush(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"logs\" : "+new Gson().toJson(factureLog)+" }");
					
					new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"updated\" : "+factureLog.getDate()+" }");
					new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"taxes\" : "+invoice.getTaxes()+" }");
					new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"facturedTo\" : "+new Gson().toJson(cClient)+" }");
				}
				Invoice invoice2=new Gson().fromJson(
						(new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\" }")).toString(),InvoiceFM01.class
						);
				if(invoice2.attemptToLog(LogKind.CLOSE).isAllowed()){
					InvoiceLog closeLog=new InvoiceLog(LogKind.CLOSE, true, onlineClient.getShopman().getLogin());
					new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"updated\" : "+closeLog.getDate()+" }");
				}
				
				//printing time
				/*InputStream stream=null;
				try{
					stream=new FileInputStream(new File(ProjectProperties.FORM_DESCRIPTOR));
				}catch(FileNotFoundException e){e.printStackTrace();}
				InvoiceForm form=new InvoiceFormFM01(stream);
				*/
				Invoice[] invoices=invoice.subdivide(InvoiceFormFM01.ROWS_NUMBER);
				//EntityManager emis=EMF.get(EMF.UNIT_INVOICEFM01).createEntityManager();
				//emis.getTransaction().begin();
				for(int i=0;i<invoices.length;i++){
					String pathname=GSettings.get("TMP_FOLDER")+invoices[i].getReference();
					File pdf= new PDF(invoices[i], pathname).make();
					//System.out.println("invoices["+i+"]: "+invoices[i].toJson());
					new PrinterFM01(pdf, PrinterFM01.PRINTER_ONE).print();
					//emis.persist(invoices[i]);
				}
			}
			else {
				response.sendError(response.SC_BAD_REQUEST,invoice.attemptToLog(LogKind.FACTURE).getMessage());
				return;
			}
		}
		catch(Exception e){
			
		}
	}
	

}
