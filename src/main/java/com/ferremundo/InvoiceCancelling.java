package com.ferremundo;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.ferremundo.InvoiceLog.LogKind;
import com.ferremundo.db.Inventory;
import com.ferremundo.db.Mongoi;
import com.ferremundo.mailing.Hotmail;
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
		int clientReference = new Integer(request.getParameter("clientReference"));
		
		ClientReference.set(clientReference);
		Log lg= new Log();
		lg.entry();
		lg.entry(request.getParameterMap());
		try{
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
						response.getWriter().write("error : referencia no encontrada '"+argsspl[0]+"'");
						return;
					}
					invoice=new Gson().fromJson(oInvoice.toString(), InvoiceFM01.class);
					if(invoice.attemptToLog(LogKind.CANCEL).isAllowed()){
						
						if(invoice.hasElectronicVersion()){
							ElectronicInvoice ei=new Profact();
							PACResponse pacResponse=ei.cancel(invoice,!new Boolean(GSettings.get("TEST")));
							/*DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
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
							*/
							
							if(pacResponse.isSuccess()){
								String xml=pacResponse.getContent();//document.getElementsByTagName("xmlretorno").item(0).getTextContent();
								lg.object("PAC response success xml = "+xml);
								new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{ \"electronicVersion.cancelXml\" : \""+StringEscapeUtils.escapeXml(xml)+"\"}");
								new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoiceREF+"\"}", "{ \"electronicVersion.active\" : false }");
								Date creationDate = new Date(invoice.getCreationTime());
								String pathToWrite = GSettings.getPathToDirectory(GSettings.get("INVOICES_FOLDER")+"-"+Utils.getDateString(creationDate,"yyyy-MM"));
								String reference = invoice.getReference();
								JGet.stringTofile(
										xml,
										pathToWrite+reference+"-ACUSE-DE-CANCELADO.xml");
								// TODO has parse de mails please
								String[] recipients={};//new String[]{};
								
								if (!invoice.getClient().getEmail().equals("")) {
									recipients=(String[])ArrayUtils.addAll(recipients, invoice.getClient().getEmail().split(" "));
								}
								if (!invoice.getAgent().getEmail().equals("")) {
									recipients=(String[])ArrayUtils.addAll(recipients, invoice.getAgent().getEmail().split(" "));
								}
								if (recipients.length>0) {
									new Hotmail().send(
											"factura CANCELADA "+ GSettings.get("INVOICE_SERIAL") + " " + reference
													+ " $" + invoice.getTotal(),
											GSettings.get("EMAIL_BODY"), recipients,
											new String[] { 
													pathToWrite+reference+"-ACUSE-DE-CANCELADO.xml",
													pathToWrite + reference + ".csv",
													pathToWrite + reference + ".xml",
													pathToWrite + reference + ".pdf" },
											new String[] {
													reference+"-ACUSE-DE-CANCELADO.xml",
													reference + "-CANCELADO.csv",
													reference + "-CANCELADO.xml",
													reference + "-CANCELADO.pdf" });
								}
								/*if(!invoice.getClient().getEmail().equals("")){
									new Hotmail().send(
										"factura CANCELADA "+invoice.getReference(),
										"la factura \n"+invoiceREF+"\nha sido cancelada.\n"+GSettings.get("EMAIL_BODY"),
										invoice.getClient().getEmail().split(" "),
										new String[]{
											GSettings.getPathTo("TMP_FOLDER")+invoice.getReference()+"-CANCELADO.xml"},
										new String[]{invoice.getReference()+"-CANCELADO.xml"}
										);
									
								}
								if(!invoice.getAgent().getEmail().equals("")){
									new Hotmail().send(
											"factura CANCELADA "+invoice.getReference(),
											"la factura\n"+invoiceREF+"\nha sido cancelada.\n"+GSettings.get("EMAIL_BODY"),
											invoice.getAgent().getEmail().split(" "),
											new String[]{
												GSettings.getPathTo("TMP_FOLDER")+invoice.getReference()+"-CANCELADO.xml"},
											new String[]{invoice.getReference()+"-CANCELADO.xml"}
											);
								}*/
							}
							else{
								response.setStatus( HttpServletResponse.SC_SERVICE_UNAVAILABLE);
								response.getWriter().write("ERROR: "+pacResponse.getResponseCode()+" - "+pacResponse.getMessage());//document.getElementsByTagName("mensaje").item(0).getTextContent());
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

	public static void main(String[] args) {
		
		String xml ="\u0026lt;?xml version\u003d\u0026quot;1.0\u0026quot; \u0026gt;";
		System.out.println(StringEscapeUtils.unescapeXml(xml));
	}
}
