package com.ferremundo;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import mx.nafiux.Rhino;

import com.ferremundo.InvoiceLog.LogKind;
import com.ferremundo.db.Inventory;
import com.ferremundo.db.Mongoi;
import com.ferremundo.mailing.HotmailSend;
import com.ferremundo.stt.GSettings;
import com.google.gson.Gson;
import com.mongodb.DBObject;

public class InvoiceCancelling extends HttpServlet{

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
					onlineClient.hasAccess(AccessPermission.INVOICE_CANCEL)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					)){
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("acceso denegado");
			}
			String argsParam=request.getParameter("args");
			String args=URLDecoder.decode(argsParam,"utf-8").toUpperCase();
			String invoiceREF=null;
			DBObject oInvoice=null;
			Invoice invoice=null;
			String[] argsspl=null;
			if(!args.equals("")){
				argsspl=args.split(" ");
				if(argsspl.length==1){
					invoiceREF=argsspl[0].toUpperCase();
					oInvoice=new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\" }");
					if(oInvoice==null){
						response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
						response.getWriter().write("error: referencia no encontrada '"+argsspl[0]+"'");
						return;
					}
					invoice=new Gson().fromJson(oInvoice.toString(), InvoiceFM01.class);
					if(invoice.attemptToLog(LogKind.CANCEL).isAllowed()){
						if(invoice.hasElectronicVersion()){
							DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
						    DocumentBuilder builder=null;
						    Document document=null;
						    try  
						    {  
						        builder = factory.newDocumentBuilder();  
						        document = builder.parse(
						        		new InputSource( 
						        				new StringReader( invoice.getElectronicVersion().getXml() ) ) );  
						    } catch (Exception e) {  
						        e.printStackTrace();  
						    }
						    Node node=document.getElementsByTagName("tfd:TimbreFiscalDigital").item(0);
						    Element e=(Element)node;
						    String uuid=e.getAttribute("UUID");
						    System.out.println("UUID:"+uuid);
							GSettings g=GSettings.instance();
							Rhino rhino= new Rhino(
									g.getKey("CERTIFICATE"),
									g.getKey("PRIVATE_KEY"),
									g.getKey("PRIVATE_KEY_PASS"));
							rhino.setOpenSSL(g.getKey("SSL"));
							String cancel=rhino.cancelar(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_USER"), 
									g.getKey("INVOICE_CERTIFICATE_AUTHORITY_PASS"),
									g.getKey("INVOICE_SENDER_TAX_CODE"),
									uuid);
							document = builder.parse(new ByteArrayInputStream(cancel.getBytes()));
							NodeList nlist=document.getElementsByTagName("codigo");
							System.out.println("CANCEL RESPONSE: "+cancel);
							if(nlist.item(0).getTextContent().equals("0")||nlist.item(0).getTextContent().equals("-5")){
								String xml=document.getElementsByTagName("xmlretorno").item(0).getTextContent();
								new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{ \"electronicVersion.cancelXml\" : \""+StringEscapeUtils.unescapeXml(xml)+"\"}");
								new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{ \"electronicVersion.active\" : false }");
								JGet.stringTofile(
										StringEscapeUtils.unescapeXml(xml),
										GSettings.get("TMP_FOLDER")+invoice.getReference()+"-CANCELADO.xml");
								if(!invoice.getClient().getEmail().equals("")){
									HotmailSend.send(
										"factura CANCELADA "+invoice.getReference(),
										"la factura con folio fiscal \n"+uuid+"\nha sido cancelada.\n"+GSettings.get("EMAIL_BODY"),
										invoice.getClient().getEmail().split(" ")/*,
										new String[]{
											GSettings.get("TMP_FOLDER")+invoice.getReference()+"-CANCELADO.xml"},
										new String[]{invoice.getReference()+"-CANCELADO.xml"}
										*/
										);
									
								}
								if(!invoice.getAgent().getEmail().equals("")){
									HotmailSend.send(
											"factura CANCELADA "+invoice.getReference(),
											"la factura con folio fiscal \n"+uuid+"\nha sido cancelada.\n"+GSettings.get("EMAIL_BODY"),
											invoice.getAgent().getEmail().split(" ")/*,
											new String[]{
												GSettings.get("TMP_FOLDER")+invoice.getReference()+"-CANCELADO.xml"},
											new String[]{invoice.getReference()+"-CANCELADO.xml"}
											*/
											
											);
								}
							}
							else{
								response.setStatus( HttpServletResponse.SC_SERVICE_UNAVAILABLE);
								response.getWriter().write("ERROR: "+document.getElementsByTagName("mensaje").item(0).getTextContent());
								return;
							}
						}
						InvoiceLog log=new InvoiceLog(InvoiceLog.LogKind.CANCEL,true,onlineClient.getShopman().getLogin());
						InvoiceLog closeLog=new InvoiceLog(InvoiceLog.LogKind.CLOSE,true,onlineClient.getShopman().getLogin());
						new Mongoi().doPush(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"logs\" : "+new Gson().toJson(log)+" }");
						new Mongoi().doPush(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"logs\" : "+new Gson().toJson(closeLog)+" }");
						new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{\"updated\" : "+closeLog.getDate()+" }");
						float cashIn=0;
						if(invoice.hasLog(LogKind.AGENT_PAYMENT))cashIn=invoice.getAgentPayment();
						float cashOut=invoice.getTotal()-invoice.getDebt();
						TheBox.instance().plus(cashIn-cashOut);
						TheBox.instance().addLog(new TheBoxLog(
								cashOut-cashIn, 
								log.getDate(),
								invoice.getReference(),
								LogKind.CANCEL.toString(),
								onlineClient.getShopman().getLogin()
								));
						List<InvoiceItem> invoiceItems=invoice.getItems();
						for(int i=0;i<invoiceItems.size();i++){
							InvoiceItem item=invoiceItems.get(i);
							
							if(Inventory.exists(item)&&!item.isDisabled())
								Inventory.incrementStored(item);
						}
						String successResponse="CANCELADO "+invoice.getReference()+": se realizó entrada-salida en caja $"+cashIn+"-$"+cashOut+" --> $"+(cashIn-cashOut);
						response.getWriter().write("{ \"message\":\""+successResponse+"\" }");
						
						return;
					}
					else {
						System.out.println("error al intentar cancelar documento");
						response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
						response.getWriter().write(invoice.attemptToLog(LogKind.CANCEL).getMessage());
						return;
					}
				}
				else{
					response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write("numero de parametros incorrecto, especifica la referencia de un solo documento");
					return;
				}
			
			}
			else{
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("error: define una referencia");
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
