package com.ferremundo;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.ArrayUtils;

//import mx.nafiux.Rhino;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.ferremundo.InvoiceLog.LogKind;
import com.ferremundo.db.Inventory;
import com.ferremundo.db.Mongoi;
import com.ferremundo.mailing.Hotmail;
import com.ferremundo.stt.GSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.DBObject;

/**
 * Servlet implementation class Wishing
 */
public class Wishing extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Wishing() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		//try {
			
			int clientReference = new Integer(request.getParameter("clientReference"));
			
			ClientReference.set(clientReference);
			OnlineClient onlineClient = OnlineClients.instance().get(clientReference);
			onlineClient.getShopman().setPassword("");
			Log log=new Log();
			log.object("onlineClient",onlineClient);
			log.entry(request.getParameterMap());
			GSettings g=GSettings.instance();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			Gson gson=new Gson();
			if (!(onlineClient.isAuthenticated(request) && (onlineClient.hasAccess(AccessPermission.INVOICE_SAMPLE)
					|| onlineClient.hasAccess(AccessPermission.INVOICE_ORDER)
					|| onlineClient.hasAccess(AccessPermission.INVOICE_FACTURE)
					|| onlineClient.hasAccess(AccessPermission.BASIC)
					|| onlineClient.hasAccess(AccessPermission.ADMIN)))) {
				try {
					response.sendError(response.SC_UNAUTHORIZED, "acceso denegado");
				} catch (IOException e) {
					log.trace("access denied", e);
				}
				return;
			}

			String clientParam = request.getParameter("client");
			String listParam = request.getParameter("list");
			String sellerParam = request.getParameter("seller");
			String agentParam = request.getParameter("agent");
			String requesterParam = request.getParameter("requester");
			String shopmanParam = request.getParameter("shopman");
			String destinyParam = request.getParameter("destiny");
			String argsParam = request.getParameter("args");
			String commandParam = request.getParameter("command");
			String paymentMethod = request.getParameter("paymentMethod");
			String paymentWay = request.getParameter("paymentWay");
			String documentType = request.getParameter("documentType");
			String coin = request.getParameter("coin");
			String cfdiUse = request.getParameter("cfdiUse");
			System.out.println("wishing '" + argsParam + "'");
			Float payment = 0f;
			String encoding = "utf-8";
			if (null != clientParam && null != listParam) {
				String listJson = null;
				String clientJson = null;
				String sellerJson = null;
				String agentJson = null;
				String requesterJson = null;
				String shopmanJson = null;
				
				String destinyJson = null;
				String args = null;
				String command = null;
				try {
					listJson = URLDecoder.decode(listParam, encoding);
				
					clientJson = URLDecoder.decode(clientParam, encoding);
					sellerJson = URLDecoder.decode(sellerParam, encoding);
					agentJson = URLDecoder.decode(agentParam, encoding);
					requesterJson = URLDecoder.decode(requesterParam, encoding);
					shopmanJson = URLDecoder.decode(shopmanParam, encoding);
					
					destinyJson = URLDecoder.decode(destinyParam, encoding);
					args = URLDecoder.decode(argsParam, encoding);
					command = URLDecoder.decode(commandParam, encoding);
				} catch (UnsupportedEncodingException e1) {
					log.trace("", e1);
				}
				JSONArray jList = null;
				JSONObject jClient = null;
				JSONObject jSeller = null;
				JSONObject jAgent = null;
				JSONObject jRequester = null;
				JSONObject jShopman = null;
				JSONObject jDestiny = null;
				LinkedList<InvoiceItem> items = new LinkedList<InvoiceItem>();
				Client client = null;
				Client agent = null;
				Seller seller = null;
				Shopman shopman = onlineClient.getShopman();;
				Destiny destiny = null;
				Requester requester = null;

				try {
					
					jList = new JSONArray(listJson);
					jClient = new JSONObject(clientJson);
					jSeller = new JSONObject(sellerJson);
					jAgent = new JSONObject(agentJson);
					jRequester = new JSONObject(requesterJson);
					jShopman = new JSONObject(shopmanJson);
					jDestiny = new JSONObject(destinyJson);

					seller = gson.fromJson(sellerJson, Seller.class);//new Seller(jSeller);
					shopman = onlineClient.getShopman();
					log.object("shopman",shopman);
					destiny = new Gson().fromJson(destinyJson,Destiny.class);
					requester = new Requester(jRequester);
					for (int i = jList.length() - 1; i >= 0; i--) {
						// InvoiceItem item=new
						// InvoiceItem(jList.getJSONObject(i));
						InvoiceItem item = new Gson().fromJson(jList.getJSONObject(i).toString(), InvoiceItem.class);
						// System.out.println(item.toJson());
						items.add(item);
					}
					client = ClientFactory.create(jClient);
					agent = ClientFactory.create(jAgent);
					log.object("client is", client);
					//System.out.println(new Gson().toJson(agent));
				} catch (JSONException e) {
					log.trace(e);
				}

				String[] argsspl = null;
				if (!args.equals(""))
					argsspl = args.split(" ");
				String csv = null;
				//System.out.println("command=" + command);
				if (
						command.equals("@oc")
						|| command.equals("$ef")
						|| command.equals("$fc")
						|| command.equals("$oc") 
						|| command.equals("@ic")
						|| command.equals("@pcot")
						|| command.equals("@ecot")) {
					// String printer=argsspl[1];
					// long re

					InvoiceMetaData metaData = null;
					if (command.equals("$fc") || command.equals("$ef")) {
						if (!(onlineClient.hasAccess(AccessPermission.INVOICE_FACTURE)
								|| onlineClient.hasAccess(AccessPermission.BASIC)
								|| onlineClient.hasAccess(AccessPermission.ADMIN))) {
							try {
								response.sendError(response.SC_UNAUTHORIZED, "acceso denegado");
							} catch (IOException e) {
								log.trace("", e);
							}
							return;
						}
						metaData = new InvoiceMetaData(Invoice.INVOICE_TYPE_TAXES_APLY);
					} else if (command.equals("@ic") || command.equals("@ia")||
							command.equals("@pcot") || command.equals("@ecot")) {
						if (!(onlineClient.hasAccess(AccessPermission.INVOICE_SAMPLE)
								|| onlineClient.hasAccess(AccessPermission.BASIC)
								|| onlineClient.hasAccess(AccessPermission.ADMIN))) {
							try {
								response.sendError(response.SC_UNAUTHORIZED, "acceso denegado");
							} catch (IOException e) {
								log.trace("", e);
							}
							return;
						}
						metaData = new InvoiceMetaData(Invoice.INVOICE_TYPE_SAMPLE);
					} else if (command.equals("@oc") || command.equals("$oc") ) {
						if (!(onlineClient.hasAccess(AccessPermission.INVOICE_ORDER)
								|| onlineClient.hasAccess(AccessPermission.BASIC)
								|| onlineClient.hasAccess(AccessPermission.ADMIN))) {
							try {
								response.sendError(response.SC_UNAUTHORIZED, "acceso denegado");
							} catch (IOException e) {
								log.trace("", e);
							}
							return;
						}
						metaData = new InvoiceMetaData(Invoice.INVOICE_TYPE_ORDER);
					} else {
						try {
							response.sendError(response.SC_UNAUTHORIZED, "acceso denegado");
						} catch (IOException e) {
							log.trace("", e);
						}
						return;
					}
					System.out.println("invoiceType (int): " + metaData.getInvoiceType());

					if (command.equals("$fc") || command.equals("$oa")
							|| command.equals("$oc")) {
						if (argsspl != null) {
							try {
								payment = new Float(argsspl[0]);
							} catch (NumberFormatException e) {
								// TODO responde que el numero vale verga
								e.printStackTrace();
							}
						} else
							payment = Float.MAX_VALUE;
					} else if (command.equals("@oc")) {
						payment = 0f;
					}
					log.info("trying to create invoice:");
					log.object("shopman",shopman);
					Invoice invoice = new InvoiceFM01(client, seller, shopman, items, metaData, requester, destiny,
							agent, payment, paymentMethod, paymentWay, documentType, coin);
					log.object("invoice",invoice);
					// System.out.println("invoice: "+invoice.toJson());
					try{
						client.persist(Mongoi.CLIENTS);
					}
					catch(NullPointerException e){
						log.trace(e);
					}
					agent.persist(Mongoi.AGENTS);
					String hashC = client.getHash();
					String hashA = agent.getHash();

					new Mongoi().doUpdate(Mongoi.CLIENTS, "{ \"code\" : \"" + hashC + "\" }",
							"{ \"agentCode\" : \"" + hashA + "\" }");

					invoice.getShopman().setPassword(null);
					InvoiceLog createdLog = invoice.getLog(LogKind.CREATED);

					String adRef = (client.getConsummer() != null
							? (!client.getConsummer().equals("") ? (client.getConsummer()) : ("")) : (""))
							+ (client.getAddress() != null
									? (!client.getAddress().equals("") ? (". " + client.getAddress()) : ("")) : (""))
							+ (client.getExteriorNumber() != null ? (!client.getExteriorNumber().equals("")
									? (". #" + client.getExteriorNumber()) : ("")) : (""))
							+ (client.getInteriorNumber() != null ? (!client.getInteriorNumber().equals("")
									? ("-" + client.getInteriorNumber()) : ("")) : (""))
							+ (client.getSuburb() != null
									? (!client.getSuburb().equals("") ? (". " + client.getSuburb()) : ("")) : (""))
							+ (client.getTel() != null
									? (!client.getTel().equals("") ? (". TEL:" + client.getTel()) : ("")) : (""));

					// *TODO controlar inventario
					if (command.equals("@ic")||
							command.equals("@pcot")||
							command.equals("@ecot")) {
						// invoice.getLogs().add(new
						// InvoiceLog(InvoiceLog.LogKind.CLOSE,true,shopman.getLogin()));
						if(client.getCfdiUse()==null||client.getCfdiUse().equals(""))client.setCfdiUse(GSettings.get("CONSUMER_CFDI_USE"));
						invoice.setUpdated(createdLog.getDate());
						invoice.setPrintedTo(client);
						invoice.persist();
						String reference=invoice.getReference();
						
						String xml=Utils.GEN_CFD_STRING(invoice);
						String 	id=invoice.getReference(),
								tmp=g.getPathTo("TMP_FOLDER"),
								xsltPath=g.getPathTo("XSLT_SAMPLE"),
								xmlPath=tmp+id+".xml",
								htmlPath=tmp+id+".html",
								pdfPath=tmp+id+".pdf",
								additionalData=g.getKey("INVOICE_SENDER_ADDITIONAL_DATA");
						Utils.saveStringToFile(xml, xmlPath);						
						Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData);
						Utils.HTML_TO_PDF(htmlPath, pdfPath, "Cotizacion "+id+" - pagina [page]/[topage]", "Los precios actuales estan sujetos a cambios sin previo aviso");
						
						org.jsoup.nodes.Document document=null;
						document = Jsoup.parse(xml);
						
						String total=document.select("cfdi|Comprobante").get(0).attr("total");
						if (command.equals("@ic")||
								command.equals("@pcot")
								) {
							
							new PrinterFM01(new File(pdfPath), GSettings.get("PRINTER_TWO")).print(new Integer(GSettings.get("PRINTER_TWO_COPIES")));
						}
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
									"Cotización " + reference
											+ " $" + total,
									GSettings.get("EMAIL_BODY"), recipients,
									new String[] { GSettings.getPathTo("TMP_FOLDER") + reference + ".xml",
											GSettings.getPathTo("TMP_FOLDER") + reference + ".pdf" },
									new String[] { reference + ".xml",
											reference + ".pdf" });
						}

						// TODO print this electronic representation to
						// lazer or whatever
						try {
							response.getWriter().print("{ \"invoice\" : " + new Gson().toJson(invoice)
									+ ", \"successResponse\" : \"Cotizado\"}");
						} catch (IOException e) {
							log.trace("", e);
						}
						
						return;

					}else if (command.equals("@oc")) {
						invoice.setUpdated(createdLog.getDate());
						invoice.setPrintedTo(client);
						invoice.persist();
						System.out.println("INVOICE:" + new Gson().toJson(invoice));
						List<InvoiceItem> invoiceItems = invoice.getItems();
						for (int i = 0; i < invoiceItems.size(); i++) {
							InvoiceItem item = invoiceItems.get(i);
							if (Inventory.exists(item) && !item.isDisabled())
								Inventory.decrementStored(item);
						}
					}  else if (command.equals("$oc")) {
						InvoiceLog paymentLog = new InvoiceLog(InvoiceLog.LogKind.PAYMENT,
								invoice.getTotal() - invoice.getDebt(), shopman.getLogin());
						invoice.getLogs().add(paymentLog);
						invoice.setUpdated(paymentLog.getDate());

						invoice.setPrintedTo(client);
						invoice.persist();
						TheBox.instance().plus(invoice.getTotal());
						TheBox.instance()
								.addLog(new TheBoxLog(invoice.getTotal(), paymentLog.getDate(), invoice.getReference(),
										LogKind.PAYMENT.toString(), onlineClient.getShopman().getLogin()));
						List<InvoiceItem> invoiceItems = invoice.getItems();
						for (int i = 0; i < invoiceItems.size(); i++) {
							InvoiceItem item = invoiceItems.get(i);
							if (Inventory.exists(item) && !item.isDisabled()){
								log.object("inventory",item);
								Inventory.decrementStored(item);
							}
						}
					} else if (command.equals("$fc") || command.equals("$ef")) {
						InvoiceLog paymentLog = new InvoiceLog(InvoiceLog.LogKind.PAYMENT,
								invoice.getTotal() - invoice.getDebt(), shopman.getLogin());
						//if(client.getCfdiUse()==null||client.getCfdiUse()==""){
						if(cfdiUse!=null&&!cfdiUse.equals("")){
								new Mongoi().doUpdate(Mongoi.CLIENTS, "{ \"code\" : \""+client.getCode()+"\"}", "{\"cfdiUse\" : \""+cfdiUse+"\" }"+"}");
								client.setCfdiUse(cfdiUse);
						}
						else {
								try {
									response.sendError(response.SC_NOT_ACCEPTABLE, "uso cfdi no definido");
								} catch (IOException e) {
									e.printStackTrace();
								}
								return;
							}
						/*}
						else if(cfdiUse!=null&&cfdiUse!=""){
							client.setCfdiUse(cfdiUse);
						}
						else {
							try {
								response.sendError(response.SC_NOT_ACCEPTABLE, "uso cfdi no definido");
							} catch (IOException e) {
								e.printStackTrace();
							}
							return;
						}*/
						invoice.getLogs().add(new InvoiceLog(InvoiceLog.LogKind.FACTURE, true, shopman.getLogin()));
						invoice.getLogs().add(paymentLog);
						invoice.setUpdated(paymentLog.getDate());
						invoice.setFacturedTo(client);
						invoice.setPrintedTo(client);
						invoice.persist();
						/*if (command.equals("$fc")) {
							invoice.setFacturedTo(client);
							invoice.setPrintedTo(client);
							invoice.persist();
						} else {
							invoice.setFacturedTo(agent);

							invoice.setPrintedTo(agent);
							invoice.persist();
						}*/

						// DBObject dbo=new Mongoi().doFindOne(Mongoi.INVOICES,
						// "{ \"reference\" : \""+invoice.getReference()+"\"}");
						// InvoiceFM01 fm01=new Gson().fromJson(dbo.toString(),
						// InvoiceFM01.class);
						// System.out.println("generating electronic version of
						// -> \n"+new Gson().toJson(fm01));
						// ElectronicInvoiceGenerator test= new
						// ElectronicInvoiceGenerator(fm01);
						// Rhino rhino=test.inflateRhino(fm01);
						//String signed=ElectronicInvoiceFactory.gen(invoice);
						ElectronicInvoice electronicInvoice=new Profact(invoice);
						boolean production=!new Boolean(GSettings.get("TEST"));
						PACResponse pacResponse=electronicInvoice.submit(production);
						
						//String cfdiResponse = RhinoGen.gen(invoice);
						// ElectronicInvoiceGenerator generator=new
						// ElectronicInvoiceGenerator(fm01);
						// String cfdiResponse=generator.create();
						// System.out.println("response from server
						// "+cfdiResponse);
						
						/*DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
						DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
						Document doc = dBuilder.parse(new ByteArrayInputStream(cfdiResponse.getBytes()));
						NodeList nlist = doc.getElementsByTagName("codigo");
						*/
						if (pacResponse.isSuccess()) {
							String xml = pacResponse.getContent();
							String xmlEscaped=StringEscapeUtils.escapeXml(xml);
							log.object("xmlEscaped is : ",xmlEscaped);
							String reference=invoice.getReference();
							ElectronicInvoiceFactory.saveCFDI(xml, reference,null);
							ElectronicInvoiceFactory.genQRCode(reference,null);
							ElectronicInvoiceFactory.genHTML(reference,null);
							ElectronicInvoiceFactory.genPDF(reference,null);
							
							org.jsoup.nodes.Document document=null;
							document = Jsoup.parse(xml);
							
							String total=document.select("cfdi|Comprobante").get(0).attr("Total");
							//String pdfUrl = doc.getElementsByTagName("urlpdf").item(0).getTextContent();
							//String xmlUrl = doc.getElementsByTagName("urlxml").item(0).getTextContent();
							/*InvoiceElectronicVersion electronicVersion = new InvoiceElectronicVersion(
									StringEscapeUtils.unescapeXml(xml), "", xmlUrl, "", pdfUrl);*/
							// TODO HARDCODED HERE
							String pdf = GSettings.getPathTo("TMP_FOLDER") + reference + ".pdf";

							new Mongoi().doUpdate(Mongoi.INVOICES,
									"{ \"reference\" : \"" + reference + "\" }",
									"{ \"electronicVersion\" : {\"xml\" : \"" + xmlEscaped + "\"} }");
							new Mongoi().doUpdate(Mongoi.INVOICES,
									"{ \"reference\" : \"" + reference + "\" }",
									"{ \"hasElectronicVersion\" : true }");
							if (command.equals("$ef")) {
								new PrinterFM01(new File(pdf), GSettings.get("PRINTER_TWO")).print(new Integer(GSettings.get("PRINTER_TWO_COPIES")));
							}
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
										"factura (" +invoice.getDocumentType()+") "+ GSettings.get("INVOICE_SERIAL") + " " + reference
												+ " $" + total,
										GSettings.get("EMAIL_BODY"), recipients,
										new String[] { GSettings.getPathTo("TMP_FOLDER") + reference + ".xml",
												GSettings.getPathTo("TMP_FOLDER") + reference + ".pdf" },
										new String[] { reference + ".xml",
												reference + ".pdf" });
							}
							/*if (!invoice.getAgent().getEmail().equals("")) {
								new Hotmail().send(
										"factura (" +invoice.getDocumentType()+") "+ GSettings.get("INVOICE_SERIAL") + " " + reference
										+ " $" + total,
												GSettings.get("EMAIL_BODY"), invoice.getAgent().getEmail().split(" "),
										new String[] { GSettings.getPathTo("TMP_FOLDER") + reference + ".xml",
												GSettings.getPathTo("TMP_FOLDER") + reference + ".pdf" },
										new String[] { reference + ".xml",
												reference + ".pdf" });
							}*/
							// TODO print this electronic representation to
							// lazer or whatever
							try {
								response.getWriter().print("{ \"invoice\" : " + new Gson().toJson(invoice)
										+ ", \"successResponse\" : \"facturado\"}");
							} catch (IOException e) {
								log.trace("", e);
							}
							TheBox.instance().plus(invoice.getTotal());
							TheBox.instance()
									.addLog(new TheBoxLog(invoice.getTotal(), paymentLog.getDate(),
											invoice.getReference(), LogKind.PAYMENT.toString(),
											onlineClient.getShopman().getLogin()));
							List<InvoiceItem> invoiceItems = invoice.getItems();
							for (int i = 0; i < invoiceItems.size(); i++) {
								InvoiceItem item = invoiceItems.get(i);
								if (Inventory.exists(item) && !item.isDisabled())
									Inventory.decrementStored(item);
							}
							return;
						} else {
							// TODO has que esta piñates te diga el mensage
							String serverMessage = "ERROR - El servidor de facturacion dijo: codigo "
									+ pacResponse.getResponseCode() + " - mensaje "
									+ pacResponse.getMessage();
							System.out.println(serverMessage);
							try {
								response.sendError(response.SC_SERVICE_UNAVAILABLE, serverMessage);
							} catch (IOException e) {
								log.trace("", e);
							}
							try {
								response.getWriter().print("{\"failed\":\"true\", \"message\": \"" + serverMessage + "\"}");
							} catch (IOException e) {
								log.trace("", e);
							}
							InvoiceLog cancelLog = new InvoiceLog(LogKind.CANCEL, true,
									onlineClient.getShopman().getLogin());
							new Mongoi().doPush(Mongoi.INVOICES,
									"{ \"reference\" : \"" + invoice.getReference() + "\"}",
									"{\"logs\" : " + new Gson().toJson(cancelLog) + " }");

							return;
						}
						// nlist.item(0).

					}

					if (invoice.getAgentPayment() == 0) {
						InvoiceLog ilog = new InvoiceLog(LogKind.AGENT_PAYMENT, 0, onlineClient.getShopman().getLogin());
						new Mongoi().doPush(Mongoi.INVOICES, "{ \"reference\" : \"" + invoice.getReference() + "\"}",
								"{\"logs\" : " + new Gson().toJson(ilog) + " }");
						invoice.getLogs().add(ilog);
					}

					try {
						//ElectronicInvoice electronicInvoice=new Profact(invoice);
						boolean production=!new Boolean(GSettings.get("TEST"));
						
						// Create file
						// HARD CODED HERE, write to *pdf.csv
						String path = GSettings.getPathTo("TMP_FOLDER");
						path += invoice.getReference() + ".pdf.csv";
						csv = path;
						System.out.println("csv path is: "+csv);
						FileWriter fstream = new FileWriter(path);
						BufferedWriter out = new BufferedWriter(fstream);
						//out.write(invoice.getConsummer() + "\n" + invoice.getAddress() + "\n" + invoice.getCity() + "\n"
						//		+ invoice.getCp() + "\n" + invoice.getRfc() + "\n");
						List<InvoiceItem> items2 = invoice.getItems();
						for (InvoiceItem item : items2) {
							out.write(item.getQuantity() + "\t" + item.getCode() + "\t" + item.getUnit() + "\t"
									+ item.getDescription() + "\t" + item.getMark() + "\t" + item.getUnitPrice()
									+ "\n");
						}
						// Close the output stream
						out.close();
					} catch (Exception e) {// Catch exception if any
						System.err.println("Error: " + e.getMessage());
					}

					System.out
							.println("INVOICE is : " + new GsonBuilder().setPrettyPrinting().create().toJson(invoice));
					Invoice[] invoices = invoice.subdivide(InvoiceFormFM01.ROWS_NUMBER);

					String[] paths = new String[invoices.length + 1];
					String[] fileNames = new String[invoices.length + 1];
					for (int i = 0; i < invoices.length; i++) {
						String pathname = GSettings.getPathTo("TMP_FOLDER") + invoices[i].getReference() + "." + i;
						
						File pdf = new PDF(invoices[i], pathname).make();
						// System.out.println("invoices["+i+"]:
						// "+invoices[i].toJson());
						// TODO this is printing time
						new PrinterFM01(pdf, GSettings.get("PRINTER_ONE")).print(new Integer(GSettings.get("PRINTER_ONE_COPIES")));
						paths[i] = pathname;
						fileNames[i] = pdf.getName() + ".pdf";
						// emis.persist(invoices[i]);
					}
					System.out.println("csv path is: "+csv);
					paths[invoices.length] = csv;
					fileNames[invoices.length] = new File(csv).getName().replace(".pdf", "");
					String[] mails = client.getEmail().split(" ");

					String subject = null;
					if (invoice.getInvoiceMetaData().getInvoiceType() == InvoiceFM01.INVOICE_TYPE_ORDER) {
						subject = "Pedido " + GSettings.get("STORE_ID") + " ";
					} else if (invoice.getInvoiceMetaData().getInvoiceType() == InvoiceFM01.INVOICE_TYPE_SAMPLE) {
						subject = "Cotización " + GSettings.get("STORE_ID") + " ";
					} else if (invoice.getInvoiceMetaData().getInvoiceType() == InvoiceFM01.INVOICE_TYPE_TAXES_APLY) {
						subject = "Factura " + GSettings.get("STORE_ID") + ":" + GSettings.get("INVOICE_SERIAL") + " ";
					}
					subject += invoice.getInvoiceMetaData().getReference() + " : $" + invoice.getTotal();
					System.out.println(subject);
					for (int i = 0; i < paths.length; i++) {
						System.out.print("mailing " + paths[i]);
						for (String mail : mails)
							System.out.println(" a '" + mail + "' como " + fileNames[i]);
					}
					System.out.println("mails->" + mails.length);
					// TODO handle sent var
					String body = GSettings.get("EMAIL_BODY");
					boolean sent = new Hotmail().send(subject, body, mails, paths, fileNames);
					mails = agent.getEmail().split(" ");
					sent = new Hotmail().send(subject, body, mails, paths, fileNames);
					// if(!sent)HotmailSend.send("no enviado", "FERREMUNDO
					// AGRADECE SU PREFERENCIA", new
					// String[]{"ferremundo@live.com"}, paths,fileNames);

					// emis.getTransaction().commit();
					// emis.close();

					String successResponse = "Cotizado " + invoice.getReference() + " por $" + invoice.getTotal();
					if (command.equals("$fa") || command.equals("$fc") || command.equals("$oa")
							|| command.equals("$oc")) {
						if (argsspl != null) {
							try {
								if (new Float(argsspl[0]) >= invoice.getTotal())
									successResponse = "Regresar $" + (new Float(argsspl[0]) - invoice.getTotal());
								else
									successResponse = "Se abonó $" + (new Float(argsspl[0])) + " a "
											+ invoice.getReference();
							} catch (NumberFormatException e) {
								// TODO responde que el numero vale verga
								e.printStackTrace();
							}
						} else
							successResponse = "Se liquidó " + invoice.getReference() + " por $" + invoice.getTotal();
					} else if (command.equals("@oa") || command.equals("@oc")) {
						successResponse = "Se otorgó credito por $" + invoice.getTotal() + " al "
								+ invoice.getReference();
					}
					try {
						response.getWriter().print("{ \"invoice\" : " + new Gson().toJson(invoice)
								+ ", \"successResponse\" : \"" + successResponse + "\"}");
					} catch (IOException e) {
						log.trace("", e);
					}

					System.out.println(new Gson().toJson(onlineClient));

					return;
				}

			} else {
				try {
					response.getWriter().print("{\"failed\":\"true\"}");
				} catch (IOException e) {
					log.trace("", e);
				}
			}
		//}catch (Exception e) {
			//System.out.println("algo salio de la mierda:");
			//Log log =new Log();
			//log.error(e);
			//e.printStackTrace();
		//}
	}

	private static Invoice getInvoice(JSONObject client, JSONArray products) {
		return null;
	}

}
