package com.ferremundo;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ferremundo.InvoiceLog.LogKind;
import com.ferremundo.db.Mongoi;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mongodb.DBObject;

public class InvoicePayment extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
		int clientReference=new Integer(request.getParameter("clientReference"));
		OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		if(!onlineClient.isAuthenticated(request)&&!(
				onlineClient.hasAccess(AccessPermission.INVOICE_ORDER_PAY_ON_CREDIT)||
				onlineClient.hasAccess(AccessPermission.BASIC)||
				onlineClient.hasAccess(AccessPermission.ADMIN)
				)){
			response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("acceso denegado");
		}
		
		System.out.println("INVOICE PAYMENT");
		String commandParam=URLDecoder.decode(request.getParameter("command"),"utf-8");
		String argsParam=request.getParameter("args");
		String args=URLDecoder.decode(argsParam,"utf-8").toUpperCase();
		String invoiceREF=null;
		DBObject oInvoice=null;
		Invoice invoice=null;
		System.out.println(commandParam+" "+invoiceREF);
		String[] argsspl=null;
		Float payment=0f;
		if(!args.equals("")){
			argsspl=args.split(" ");
			invoiceREF=argsspl[0];
			oInvoice=new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\" }");
			if(oInvoice==null){
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("error: referencia no encontrada '"+argsspl[0]+"'");
				return;
			}
			invoice=new Gson().fromJson(oInvoice.toString(), InvoiceFM01.class);
			if(argsspl.length>1){
				try{
					payment=new Float(argsspl[1]);
				}
				catch(NumberFormatException e){
					//TODO responde que el numero vale verga
					response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
					response.getWriter().write("error: cantidad invalida '"+argsspl[1]+"'");
					e.printStackTrace();
				}
				
			}
			else{
				payment=Float.MAX_VALUE;
			}
		}
		else {
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
			response.getWriter().write("nada por hacer : sin referencia");
			return;
		}
		/**TODO consider the case when the invoice is not payable**/
		String successResponse="";
		float toTheBox;
		float newDebt;
		if(commandParam.equals("$d")){
			if(invoice.attemptToLog(LogKind.PAYMENT).isAllowed()){
				float debt=invoice.getDebt();
				newDebt=debt;
				if(payment==Float.MAX_VALUE){
					newDebt=0;
					toTheBox=debt;
					successResponse="documento "+invoice.getReference()+" liquidado; Regresa $indefinido ; ingreso a caja $"+debt;
				}
				else if(payment>=debt){
					newDebt=0;
					toTheBox=debt;
					successResponse="documento "+invoice.getReference()+" liquidado; Regresa $"+(payment-debt);
				}
				else{
					newDebt=debt-payment;
					toTheBox=payment;
					successResponse="se abonó a documento "+invoice.getReference()+" $"+payment;
				}
				InvoiceLog log=new InvoiceLog(InvoiceLog.LogKind.PAYMENT,toTheBox,onlineClient.getShopman().getLogin());
				new Mongoi().doPush(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"logs\" : "+new Gson().toJson(log)+" }");
				new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{ \"debt\" : "+newDebt+" }");
				new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"updated\" : "+log.getDate()+" }");
				TheBox.instance().plus(toTheBox);
				TheBox.instance().addLog(new TheBoxLog(
						toTheBox, 
						log.getDate(),
						invoice.getReference(),
						LogKind.PAYMENT.toString(),
						onlineClient.getShopman().getLogin()
						));
			}
			else {
				
				System.out.println(invoice.attemptToLog(LogKind.PAYMENT).getMessage());
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(invoice.attemptToLog(LogKind.PAYMENT).getMessage());
				return;
			}
			
		}
		else if(commandParam.equals("$a")){
			if(invoice.attemptToLog(LogKind.AGENT_PAYMENT).isAllowed()){
				float agentPayment=invoice.getAgentPayment();
				
				InvoiceLog log=new InvoiceLog(InvoiceLog.LogKind.AGENT_PAYMENT,agentPayment,onlineClient.getShopman().getLogin());
				new Mongoi().doPush(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"logs\" : "+new Gson().toJson(log)+" }");
				new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"updated\" : "+log.getDate()+" }");
				TheBox.instance().minus(agentPayment);
				TheBox.instance().addLog(new TheBoxLog(
						-agentPayment, 
						log.getDate(),
						invoice.getReference(),
						LogKind.PAYMENT.toString(),
						onlineClient.getShopman().getLogin()
						));
				successResponse="se retiró de caja $"+invoice.getAgentPayment()+" por concepto de liquidación de agente para documento "+invoice.getReference();
			}
			else {
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(invoice.attemptToLog(LogKind.AGENT_PAYMENT).getMessage());
				return;
			}
		}
		else {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("acceso denegado o comando invalido");
			return;
		}
		Invoice invoice2=new Gson().fromJson(
				(new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\" }")).toString(),InvoiceFM01.class
				);
		if(invoice2.attemptToLog(LogKind.CLOSE).isAllowed()){
			InvoiceLog closeLog=new InvoiceLog(LogKind.CLOSE, true, onlineClient.getShopman().getLogin());
			new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"updated\" : "+closeLog.getDate()+" }");
		}
		response.getWriter().write("{ \"successResponse\" : \""+successResponse+"\"}");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		DBObject oInvoice=new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"reference\" : \"40RZ\" }");
		String json=oInvoice.toString();
		Invoice invoice=new Gson().fromJson(json, InvoiceFM01.class);
		//Type listType = new TypeToken<List<String>>() {}.getType();
		ArrayList list=(ArrayList)oInvoice.get("logs");
		for(int i=0;i<list.size();i++){
			
			DBObject log=(DBObject)(list.get(i));
			//if(log.kind().equals(log.))
			System.out.println(new Gson().toJson(log));
		}
		System.out.println(oInvoice);
		System.out.println(new Gson().toJson(invoice));
		System.out.println("total="+invoice.getTotal()+" taxes="+invoice.getTaxes()+" subtotal="+invoice.getSubtotal());
	}
	
}
