package com.ferremundo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ferremundo.InvoiceLog.LogKind;
import com.ferremundo.InvoiceLog.Payment;
import com.ferremundo.db.FilterDBObject;
import com.ferremundo.db.Inventory;
import com.ferremundo.db.Mongoi;
import com.ferremundo.mailing.Hotmail;
import com.ferremundo.stt.GSettings;
import com.ferremundo.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.LinkedTreeMap;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

import mx.bigdata.sat.common.pagos.schema.CMetodoPagoPago;
import mx.bigdata.sat.common.pagos.schema.CMoneda;
import mx.bigdata.sat.common.pagos.schema.CMonedaPago;
import mx.bigdata.sat.common.pagos.schema.Pagos.Pago.DoctoRelacionado;
import mx.timbracfdi33.ArrayOfAnyType;
import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoinUtil;

import static com.ferremundo.AccessPermission.*;
import static com.ferremundo.Commands.*;
import static com.ferremundo.db.Mongoi.*;
public class DBPort extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3780805200111442981L;
	
	private boolean dcMatch(String dc, Enum e){
		String dc_=dc.replace("_","").toLowerCase();
		return dc_.equals(e.toString().replace("_","").toLowerCase());
	}
	private boolean dcMatch(String dc, String e){
		String dc_=dc.replace("_","").toLowerCase();
		return dc_.equals(e.replace("_","").toLowerCase());
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		String command=request.getParameter("command");
		if(command==null){
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
			try {
				response.getWriter().write("UNDEFINED_COMMAND");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		String dc=null;
		try {
			dc = URLDecoder.decode(command,"utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if(dcMatch(dc,DB_INIT)){
			dbInit(request, response);
			return;
		}
		else{
			System.out.println("dc='"+dc+"'");
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
			try {
				response.getWriter().write("UNDEFINED_COMMAND");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{
			
			int clientReference=new Integer(request.getParameter("clientReference"));
			ClientReference.set(clientReference);
			OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
			Log log = new Log();
			log.entry(request.getParameterMap());
			String locale=onlineClient.getLocale();
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			String command=request.getParameter("command");
			if(command==null){
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(fromLang(locale,"UNDEFINED_COMMAND"));
				return;
			}
			String dc=URLDecoder.decode(command,"utf-8");
			log.info("command is: '"+dc+"'");
			if(dcMatch(dc,MAKE_RECORD)){
				makeRecord(
						new AccessPermission[]{ADMIN,MAKE_RECORD,BASIC},
						new String[]{"message"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,DEACTIVATE_RECORD)){
				deactivateRecord(request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,READ_RECORDS)){
				returnRecords(
						new AccessPermission[]{ADMIN,READ_RECORDS,BASIC},
						new String[]{"limit"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,PRODUCT_INVENTORY_ADD)){
				productInventoryAdd(request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,Commands.RESET_PRODUCT_INVENTORY)){
				resetProductInventory(request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,Commands.REQUEST_SHOPMAN)){
				resetProductInventory(request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,SEARCH_PRODUCTS)){
				searchProducts(
						new AccessPermission[]{ADMIN,PRODUCT_READ,BASIC},
						new String[]{"requestNumber","search"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,"CLIENT")){
				searchClients(
						new AccessPermission[]{ADMIN,CONSUMMER_READ,BASIC},
						new String[]{"requestNumber","search"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,"AGENT")){
				searchClients(
						new AccessPermission[]{ADMIN,AGENT_READ,BASIC},
						new String[]{"requestNumber","search"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,SEARCH_PROVIDERS)){
				searchProviders(
						new AccessPermission[]{ADMIN,READ_PROVIDERS},
						new String[]{"requestNumber","search"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,UPDATE_PRODUCTS)){
				updateProducts(request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,Commands.ABSOLUTE_DISCOUNT)){
				calculateRelativeDiscount(
						new AccessPermission[]{ADMIN,BASIC},
						new String[]{"absoluteDiscount","list"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,AccessPermission.EMIT_INVOICE)){
				emitInvoice(
						new AccessPermission[]{ADMIN,BASIC,EMIT_INVOICE},
						new String[]{
								"client",
								"list",
								"shopman",
								"agent",
								"destiny",
								"clientReference",
								"cfdiUse",
								"documentType",
								"paymentMethod",
								"paymentWay",
								"printCopies",
								"coin"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,"EMIT_INVOICE_TICKET")){
				emitInvoiceTicket(
						new AccessPermission[]{ADMIN,BASIC,EMIT_INVOICE},
						new String[]{
								"client",
								"list",
								"shopman",
								"agent",
								"destiny",
								"clientReference",
								"cfdiUse",
								"documentType",
								"paymentMethod",
								"paymentWay",
								"printCopies",
								"coin"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,AccessPermission.EMIT_ORDER)){
				emitOrder(
						new AccessPermission[]{ADMIN,BASIC,EMIT_ORDER},
						new String[]{
								"client",
								"list",
								"shopman",
								"agent",
								"destiny",
								"clientReference",
								"documentType",
								"printCopies",
								"coin"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,"EMIT_ORDER_TICKET")){
				emitOrderTicket(
						new AccessPermission[]{ADMIN,BASIC,EMIT_ORDER},
						new String[]{
								"client",
								"list",
								"shopman",
								"agent",
								"destiny",
								"clientReference",
								"documentType",
								"printCopies",
								"coin"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,AccessPermission.EMIT_SAMPLE)){
				emitSample(
						new AccessPermission[]{ADMIN,BASIC,EMIT_SAMPLE},
						new String[]{
								"client",
								"list",
								"shopman",
								"agent",
								"destiny",
								"clientReference",
								"documentType",
								"printCopies",
								"coin"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,"EMIT_SAMPLE_TICKET")){
				emitSampleTicket(
						new AccessPermission[]{ADMIN,BASIC,EMIT_SAMPLE},
						new String[]{
								"client",
								"list",
								"shopman",
								"agent",
								"destiny",
								"clientReference",
								"documentType",
								"printCopies",
								"coin"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,"INVOICE_PAYMENT")){
				invoicePayment(
						new AccessPermission[]{ADMIN,BASIC,EMIT_INVOICE},
						new String[]{
								"amount",
								"datetime",
								"paymentWay",
								"operationNumber",
								"refs",
								"amounts"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,"PRINT")){
				printDocument(
						new AccessPermission[]{ADMIN,BASIC,PRINT_DOCUMENT},
						new String[]{
								"clientReference",
								"reference",
								"printCopies"
								},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,"TPRINT")){
				printDocumentTicket(
						new AccessPermission[]{ADMIN,BASIC,PRINT_DOCUMENT},
						new String[]{
								"clientReference",
								"reference",
								"printCopies"
								},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,"MAIL")){
				mailDocument(
						new AccessPermission[]{ADMIN,BASIC,MAIL_DOCUMENT},
						new String[]{
								"clientReference",
								"reference",
								"recipients"
								},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,CANCEL_DOCUMENT)){
				cancelDocument(
						new AccessPermission[]{ADMIN,BASIC,CANCEL_DOCUMENT},
						new String[]{
								"clientReference",
								"reference",
								},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,Commands.FIX_DB)){
				fixDB(
						new AccessPermission[]{ADMIN},
						new String[]{"fixNumber"},
						request, response, onlineClient);
				return;
			}
			else{
				System.out.println("dc='"+dc+"'");
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write(fromLang(locale,"UNDEFINED_COMMAND"));
				return;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		/*DBCursor dbCursor=new Mongoi().
				doFindFieldsMatches(
						Mongoi.TEMPORAL_RECORDS, 
						new String[]{"todo","login"},
						new Object[]{true,"root"});
		for(DBObject dbo : dbCursor){
			System.out.println(dbo);
		}*/
		/*String callerClassName = new Exception().getStackTrace()[0].getClassName();
		System.out.println(callerClassName);
		System.out.println(Langed.get("UNDEFINED_COMMAND", "des","yeh"));
		int i=10;
		System.out.println("\"${10}\"".replaceAll("\\$\\{"+i+"\\}", "repl"));
		*/
		/*myCaller(1, new Command() {
			public void execute(Object data) {
				System.out.println(data);
			}});*/
		System.out.println("DEACTIVATE_RECORD".equals(DEACTIVATE_RECORD.toString()));
	}
	private interface Command 
    {
		public void execute(Map<String, String> parametersMap, HttpServletResponse response, OnlineClient onlineClient);
    }
	

	private void returnRecords(HttpServletRequest request,
			HttpServletResponse response, OnlineClient onlineClient) {
		try{
			if(
					!onlineClient.isAuthenticated(request)&&!(
					onlineClient.hasAccess(AccessPermission.BASIC)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					)){
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"ACCESS_DENIED"));
				return;
			}
			if(requestHasParameter(request, "args")){
				String args=request.getParameter("args");
				if(!args.equals("")){
					if(Utils.isIntegerParseInt(args)){
						int n=Integer.parseInt(args);
						DBCursor dbCursor=new Mongoi().
								doFindFieldsMatches(
										Mongoi.TEMPORAL_RECORDS, 
										new String[]{"todo","login"},
										new Object[]{true,onlineClient.getShopman().getLogin()}).limit(n);
						String json="{ \"records\" : [";
						
						int i=0;
						while(dbCursor.hasNext()){
							json+=(i!=0?",":"")+dbCursor.next().toString();
							i++;
						}
						json+="] } ";
						response.getWriter().write(json);
						return;
					}
					else{
						response.setStatus( HttpServletResponse.SC_NOT_ACCEPTABLE);
						response.getWriter().write(fromLang(onlineClient.getLocale(),"INVALID_ARGUMENT"));
						return;
					}
				}
				else{
					DBCursor dbCursor=new Mongoi().
							doFindFieldsMatches(
									Mongoi.TEMPORAL_RECORDS, 
									new String[]{"todo","login"},
									new Object[]{true,onlineClient.getShopman().getLogin()});
					String json="{ \"records\" : [";
					
					int i=0;
					while(dbCursor.hasNext()){
						json+=(i!=0?",":"")+dbCursor.next().toString();
						i++;
					}
					json+="] } ";
					response.getWriter().write(json);
					return;
				}
			}
			else{
				response.setStatus( HttpServletResponse.SC_NOT_ACCEPTABLE);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void returnRecords(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				String limit=request.getParameter("limit");
				if(!limit.equals("")){
					if(Utils.isIntegerParseInt(limit)){
						int n=Integer.parseInt(limit);
						DBCursor dbCursor=new Mongoi().
								doFindFieldsMatches(
										Mongoi.TEMPORAL_RECORDS, 
										new String[]{"todo","login"},
										new Object[]{true,onlineClient.getShopman().getLogin()}).limit(n);
						String json="{ \"records\" : [";
						
						int i=0;
						while(dbCursor.hasNext()){
							json+=(i!=0?",":"")+dbCursor.next().toString();
							i++;
						}
						json+="] } ";
						try {
							response.getWriter().write(json);
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
					else{
						response.setStatus( HttpServletResponse.SC_NOT_ACCEPTABLE);
						try {
							response.getWriter().write(fromLang(onlineClient.getLocale(),"INVALID_ARGUMENT"));
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
				}
				else{
					DBCursor dbCursor=new Mongoi().
							doFindFieldsMatches(
									Mongoi.TEMPORAL_RECORDS, 
									new String[]{"todo","login"},
									new Object[]{true,onlineClient.getShopman().getLogin()});
					String json="{ \"records\" : [";
					
					int i=0;
					while(dbCursor.hasNext()){
						json+=(i!=0?",":"")+dbCursor.next().toString();
						i++;
					}
					json+="] } ";
					try {
						response.getWriter().write(json);
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				
			}
		});
		
	}
	
	private void deactivateRecord(HttpServletRequest request,
			HttpServletResponse response, OnlineClient onlineClient) {
		try{
			if(
					!onlineClient.isAuthenticated(request)&&!(
					onlineClient.hasAccess(AccessPermission.BASIC)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					)){
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"ACCESS_DENIED"));
				return;
			}
			if(requestHasParameter(request, "args")){
				String args=request.getParameter("args");
				if(!args.equals("")){
					String id=URLDecoder.decode(args,"utf-8");
					TemporalRecord tr=TemporalRecord.deactivate(new Long(id));
					if(tr!=null){
						response.getWriter().write("{\"record\" : "+new Gson().toJson(tr)+"}");	
						return;
					}
					else{
						response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
						response.getWriter().write(fromLang(onlineClient.getLocale(),"INVALID_ID"));
						return;	
					}
					
				}
				else{
					response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
					return;
				}
			}
			else{
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	private void deactivateRecord(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				String record=request.getParameter("record");
				TemporalRecord tm=new TemporalRecord(record, onlineClient.getShopman().getLogin());
				boolean success=tm.persist();
				if(success){
					try {
						response.getWriter().write("{\"message\" : \"record creado\", \"record\" : "+new Gson().toJson(tm)+" }");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				else{
					response.setStatus( HttpServletResponse.SC_CONFLICT);
					try {
						response.getWriter().write("ERROR: no se registro. error desconocido");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
			}
		});
		
	}
	
	private void makeRecord(HttpServletRequest request,
			HttpServletResponse response, OnlineClient onlineClient) {
		try{
			if(
					!onlineClient.isAuthenticated(request)&&!(
					onlineClient.hasAccess(AccessPermission.BASIC)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					)){
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"ACCESS_DENIED"));
				return;
			}
			if(requestHasParameter(request, "args")){
				String args=request.getParameter("args");
				if(!args.equals("")){
					String record=URLDecoder.decode(args,"utf-8");
					TemporalRecord tm=new TemporalRecord(record, onlineClient.getShopman().getLogin());
					boolean success=tm.persist();
					if(success){
						response.getWriter().write("{\"message\" : \"record creado\", \"record\" : "+new Gson().toJson(tm)+" }");
						return;
					}
					else{
						response.setStatus( HttpServletResponse.SC_CONFLICT);
						response.getWriter().write("ERROR: no se registro. error desconocido");
						return;
					}
				}
				else{
					response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
					return;
				}
			}
			else{
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void makeRecord(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				String record=request.getParameter("record");
				TemporalRecord tm=new TemporalRecord(record, onlineClient.getShopman().getLogin());
				boolean success=tm.persist();
				if(success){
					try {
						response.getWriter().write("{\"message\" : \"record creado\", \"record\" : "+new Gson().toJson(tm)+" }");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				else{
					response.setStatus( HttpServletResponse.SC_CONFLICT);
					try {
						response.getWriter().write("ERROR: no se registro. error desconocido");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
			}
		});
		
	}
		
	
	private boolean requestHasParameter(HttpServletRequest request,String parameter){
		Log log = new Log();
		log.entry("Looking for required parameter",parameter);
		String arg=request.getParameter(parameter);
		if(arg!=null){
			log.info("parameter found");
			return true;
		}
		else {
			log.info("parameter not found");
			return false;
		}
	}
	private void incrementAgentEarning(HttpServletRequest request, HttpServletResponse response,OnlineClient onlineClient){
		try{
			if(
					!onlineClient.isAuthenticated(request)&&!(
					onlineClient.hasAccess(AccessPermission.AGENT_INCREMENT_EARNINGS)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					)){
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"ACCESS_DENIED"));
				return;
			}
			if(requestHasParameter(request, "args")){
				String args=request.getParameter("args");
				if(!args.equals("")){
					String[] argsspl=URLDecoder.decode(args,"utf-8").split(" ");
					if(argsspl.length==3){
						String ref=argsspl[0].toUpperCase();
						String cancelIssue="";
						for(int i=3;i<argsspl.length;i++){
							if(i!=3)cancelIssue+=" ";
							cancelIssue+=argsspl[i];
						}
						DBObject oInvoice=new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"reference\" : \""+ref+"\" }");
						if(oInvoice==null){
							response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
							response.getWriter().write("ERROR: referencia no encontrada '"+argsspl[0]+"'");
							return;
						}
						
						Float amount=null;
						try{
							amount=new Float(argsspl[1]);
						}
						catch(NumberFormatException exception){
							response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
							response.getWriter().write("ERROR: monto inválido "+argsspl[1]);
							return;
						}
						Float oldAgentPayment=new Float(oInvoice.get("agentPayment").toString());
						Float oldTotalValue=new Float(oInvoice.get("totalValue").toString());
						
						float agentPayment=oldAgentPayment+amount;
						float newTotalValue=oldTotalValue-amount;
						if(agentPayment>=oldTotalValue){
							response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
							response.getWriter().write("ERROR: monto inválido, no puede se mayor o igual que $"+(oldTotalValue-oldAgentPayment));
							return;
						}
						InvoiceLog log=new InvoiceLog(InvoiceLog.LogKind.AGENT_INCREMENT_EARNINGS,amount,onlineClient.getShopman().getLogin());
						new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+ref+"\" }", "{ \"agentPayment\" : \""+agentPayment+"\" }");
						new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+ref+"\" }", "{ \"totalValue\" : \""+newTotalValue+"\" }");
						new Mongoi().doPush(Mongoi.INVOICES, "{ \"reference\" : \""+ref+"\"}", "{\"logs\" : "+new Gson().toJson(log)+" }");
						new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+ref+"\"}", "{\"updated\" : "+log.getDate()+" }");
						DBObject oInvoice2=new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"reference\" : \""+ref+"\" }");
						response.getWriter().write("{ \"invoice\" :"+oInvoice2.toString()+" , \"message\": \"se agregó "+amount+" exitosamente\" }");
					}
					else if(argsspl.length<2){
						response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
						response.getWriter().write("ERROR: especifica el monto");
						return;
					}
					else {
						response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
						response.getWriter().write("ERROR: argumentos invalidos");
						return;
					}
				}
				else{
					response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
					return;
				}
			}
			else{
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private void resetProductInventory(HttpServletRequest request, HttpServletResponse response,OnlineClient onlineClient){
		try{
			if(
					!onlineClient.isAuthenticated(request)&&!(
					onlineClient.hasAccess(AccessPermission.RESET_PRODUCT_INVENTORY)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					)){
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"ACCESS_DENIED"));
				return;
			}
			if(requestHasParameter(request, "args")){
				String args=request.getParameter("args");
				if(!args.equals("")){
					String[] argsspl=URLDecoder.decode(args,"utf-8").split(" ");
				}
				else{
					response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
					return;
				}
			}
			else{
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	private void productInventoryAdd(HttpServletRequest request, HttpServletResponse response,OnlineClient onlineClient){
		try{
			if(
					!onlineClient.isAuthenticated(request)&&!(
					onlineClient.hasAccess(AccessPermission.PRODUCT_UPDATE)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					)){
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"ACCESS_DENIED"));
				return;
			}

			String itemsArgs=request.getParameter("items");
			if(!itemsArgs.equals("")){
				String itemsdec=URLDecoder.decode(itemsArgs,"utf-8");
				JSONArray jList=null;
				LinkedList<InvoiceItem> items=new LinkedList<InvoiceItem>();
				jList=new JSONArray(itemsdec);
				System.out.println(itemsdec);
				for(int i=jList.length()-1;i>=0;i--){
					JSONObject jsonObject=jList.getJSONObject(i);
					System.out.println();
					InvoiceItem item=new Gson().fromJson(jList.getJSONObject(i).toString(),InvoiceItem.class);
					System.out.println(new Gson().toJson(item));
					System.out.println("item is:"+new Gson().toJson(item));
					String itemHash=jList.getJSONObject(i).getString("hash");
					JSONObject product=new JSONObject(new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"hash\" : \""+itemHash+"\" }").toString());
					System.out.println("product for hash "+itemHash+" is:"+product);
					Object fti=null;
					if(product.has("firstTimeInventored"))fti=product.get("firstTimeInventored");
					if(item.getId()==-1){//means edited
						//boolean inv=new Boolean(product.get("firstTimeInventored").toString());
						ProductProviderPricesHistory history=new ProductProviderPricesHistory(
								product.getLong("id"),
								item.getQuantity(),
								product.getDouble("unitPrice"),
								new Date().getTime(),
								onlineClient.getShopman().getLogin());
						//history.persist();
						if(fti!=null){
							if(new Boolean(fti.toString())){
								System.out.println("inventored : "+product.toString());
								new Mongoi().doIncrement(Mongoi.PRODUCTS, "{ \"hash\" : \""+itemHash+"\" }", "{ \"stored\" : "+item.getQuantity()+" }");
							}
							else{
								System.out.println("not inventored : "+product.toString());
								new Mongoi().doUpdate(Mongoi.PRODUCTS, "{ \"hash\" : \""+itemHash+"\" }", "{ \"stored\" : "+item.getQuantity()+" }");
							}
						}
						else{
							System.out.println("not inventored : "+product.toString());
							new Mongoi().doUpdate(Mongoi.PRODUCTS, "{ \"hash\" : \""+itemHash+"\" }", "{ \"stored\" : "+item.getQuantity()+" }");
						}
					}
					else{
						ProductProviderPricesHistory history=new ProductProviderPricesHistory(
								product.getLong("id"),
								item.getQuantity(),
								0,
								new Date().getTime(),
								onlineClient.getShopman().getLogin());
						history.persist();
						if(fti!=null){
							if(new Boolean(fti.toString())){
								System.out.println("inventored : "+product.toString());
								new Mongoi().doIncrement(Mongoi.PRODUCTS, "{ \"hash\" : \""+itemHash+"\" }", "{ \"stored\" : "+item.getQuantity()+" }");
							}
							else{
								System.out.println("not inventored : "+product.toString());
								new Mongoi().doUpdate(Mongoi.PRODUCTS, "{ \"hash\" : \""+itemHash+"\" }", "{ \"stored\" : "+item.getQuantity()+" }");
							}
						}
						else{
							System.out.println("not inventored : "+product.toString());
							new Mongoi().doUpdate(Mongoi.PRODUCTS, "{ \"hash\" : \""+itemHash+"\" }", "{ \"stored\" : "+item.getQuantity()+" }");
						}
					}
					items.add(item);
				}
			}
			else {
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("lista vacía");
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private static void doSetFMDBase(){
		/*Mongo mongo = null;
		try {
			mongo = new Mongo("localhost");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (MongoException e) {
			e.printStackTrace();
		}
		DB db = mongo.getDB(Mongoi.GLOBAL_DB);*/
		Mongoi m = new Mongoi();
		m.getCollection(PRODUCT_PROVIDER_PRICES_HISTORY).ensureIndex(new BasicDBObject("time", 1));
		m.getCollection(PRODUCT_PROVIDER_PRICES_HISTORY).ensureIndex(new BasicDBObject("id", 1));
		
		m.getCollection(PRODUCT_TENDENCY).ensureIndex(new BasicDBObject("time", 1));
		m.getCollection(PRODUCT_TENDENCY).ensureIndex(new BasicDBObject("id", 1));
		
		
		m.getCollection(TEMPORAL_RECORDS).ensureIndex(new BasicDBObject("login", 1));
		m.getCollection(TEMPORAL_RECORDS).ensureIndex(new BasicDBObject("todo", 1));
		m.getCollection(TEMPORAL_RECORDS).ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
		
		m.getCollection(TEMPORAL_RECORDS_COUNTER).ensureIndex(new BasicDBObject("unique", 1), new BasicDBObject("unique", true));
		m.doInsert(TEMPORAL_RECORDS_COUNTER, "{ \"unique\" : \"unique\" , \"id\" : 0}");
		
		DBCollection clientsCollection=m.getCollection(Mongoi.CLIENTS);
		clientsCollection.ensureIndex(new BasicDBObject("code", 1), new BasicDBObject("unique", true));
		clientsCollection.ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
		m.getCollection(Mongoi.CLIENTS_COUNTER).ensureIndex(new BasicDBObject("unique", 1), new BasicDBObject("unique", true));
		m.doInsert(Mongoi.CLIENTS_COUNTER, "{ \"unique\" : \"unique\" , \"id\" : 0}");		
		
		m.getCollection(INVOICES).ensureIndex(new BasicDBObject("reference", 1), new BasicDBObject("unique", true));
		m.getCollection(INVOICES).ensureIndex(new BasicDBObject("creationTime", 1));
		m.getCollection(INVOICES).ensureIndex(new BasicDBObject("updated", 1));
		
		DBCollection collection= m.getCollection(Mongoi.PRODUCTS);
		collection.ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
		collection.ensureIndex(new BasicDBObject("hash", 1), new BasicDBObject("unique", true));
		collection.ensureIndex(new BasicDBObject("code", 1), new BasicDBObject("unique", true));
		m.getCollection(PRODUCTS_COUNTER).ensureIndex(new BasicDBObject("unique", 1), new BasicDBObject("unique", true));
		m.doInsert(PRODUCTS_COUNTER, "{ \"unique\" : \"unique\" , \"id\" : 0}");
		
		m.getCollection(REFERENCE).ensureIndex(new BasicDBObject("reference", 1), new BasicDBObject("unique", true));
		m.doInsert(REFERENCE, "{ \"reference\" : \"unique\", \"count\" : "+1+" }");
		
		
		DBCollection agentsCollection=m.getCollection(Mongoi.AGENTS);
		agentsCollection.ensureIndex(new BasicDBObject("code", 1), new BasicDBObject("unique", true));
		agentsCollection.ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
		m.getCollection(Mongoi.AGENTS_COUNTER).ensureIndex(new BasicDBObject("unique", 1), new BasicDBObject("unique", true));
		m.doInsert(Mongoi.AGENTS_COUNTER, "{ \"unique\" : \"unique\" , \"id\" : 0}");
		
		
		DBCollection shopmansCollection=m.getCollection(Mongoi.SHOPMANS);
		shopmansCollection.ensureIndex(new BasicDBObject("login", 1), new BasicDBObject("unique", true));
		shopmansCollection.ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
		m.getCollection(Mongoi.SHOPMANS_COUNTER).ensureIndex(new BasicDBObject("unique", 1), new BasicDBObject("unique", true));
		m.doInsert(Mongoi.SHOPMANS_COUNTER, "{ \"unique\" : \"unique\" , \"id\" : 0}");
		
		DBCollection providersCollection=m.getCollection(Mongoi.PROVIDERS);
		providersCollection.ensureIndex(new BasicDBObject("fullName", 1), new BasicDBObject("unique", true));
		
		Shopman shopman= new Shopman("admin","root", "ready",new ArrayList<AccessPermission>(Arrays.asList(
		AccessPermission.ADMIN
				)));	

		shopman.persist();
		
		
	}
	private void dbInit(HttpServletRequest request, HttpServletResponse response){
		try{
			DB db = new Mongoi().getDB();
			if (db.getCollectionNames().size() > 0){
				response.getWriter().write(
						"PERMISSION_DENIED DB already scafolded");
				return;
			}
			doSetFMDBase();
			response.getWriter().write("Scafolded");
			return;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void sample(HttpServletRequest request, HttpServletResponse response,OnlineClient onlineClient){
		try{
			if(
					!onlineClient.isAuthenticated(request)&&!(
					onlineClient.hasAccess(AccessPermission.INVOICE_SAMPLE)||
					onlineClient.hasAccess(AccessPermission.BASIC)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					)){
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(
						fromLang(onlineClient.getLocale(),"PERMISSION_DENIED")+". "+
						fromLang(onlineClient.getLocale(),"PERMISSION_REQUIRED","[INVOICE_SAMPLE|BASIC|ADMIN]")
						);
				return;
			}
			if(requestHasParameter(request, "args")){
				String args=request.getParameter("args");
				if(!args.equals("")){
					String[] argsspl=URLDecoder.decode(args,"utf-8").split(" ");
				}
				else{
					response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
					return;
				}
			}
			else{
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void requestShopman(HttpServletRequest request, HttpServletResponse response,OnlineClient onlineClient){
		try{
			if(
					!onlineClient.isAuthenticated(request)&&!(
					onlineClient.hasAccess(AccessPermission.REQUEST_SHOPMAN)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					)){
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"ACCESS_DENIED"));
				return;
			}
			if(requestHasParameter(request, "login")){
				String login=request.getParameter("login");
				if(!login.equals("")){
					DBObject dbLogin=new Mongoi().doFindOne(Mongoi.SHOPMANS, "{\"login\":\""+login+"\"}");
					response.getWriter().print("{\"result\":"+dbLogin.toString()+"}");
					return;
				}
				else{
					response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS","login"));
					return;
				}
			}
			else{
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void invoicePayment(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				Log log = new Log();
				log.entry(parametersMap);
				String amountStr=parametersMap.get("amount"),
					datetime=parametersMap.get("datetime"),
					paymentWay=parametersMap.get("paymentWay"),
					refsParameter=parametersMap.get("refs"),
					amountsParameter=parametersMap.get("amounts"),
					operationNumber = parametersMap.get("operationNumber");
				String[] refs=refsParameter.split(" "),
						amountsS = amountsParameter.split(" ");
				if(refs.length != amountsS.length){
					try {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST,fromLang(onlineClient.getLocale(),"CODE_11"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				float amount = new Float(amountStr);

				float[] docPayments = new float[amountsS.length];
				float[] docDues = new float[refs.length];
				float[] docInsoluteDues = new float[refs.length];
				String[] docFiscalReferences = new String[refs.length];
				String[] docSeries = new String[refs.length];
				String[] docReferences = new String[refs.length];
				int[] docPartialities = new int[refs.length];
				Client[] clients = new Client[refs.length];
				String[] emails={};
				String fiscalCodeMatch = "";
				DoctoRelacionado[] relatedDocs = new DoctoRelacionado[refs.length];
				for(int i = 0; i < amountsS.length; i++){
					docPayments[i] = new Float(amountsS[i]);
					docPartialities[i] = 1;
				}
				DBObject[] dbInvoices = new DBObject[refs.length];
				InvoiceFM01[] invoices = new InvoiceFM01[refs.length];
				
				float restTotal = 0;
				Mongoi mongoi = new Mongoi();
				for(int i = 0; i < refs.length; i++){
					dbInvoices[i] = mongoi.doFindOne(Mongoi.INVOICES, "{\"reference\" : \""+refs[i]+"\"}");
					if(dbInvoices[i] == null){
						try {
							response.sendError(HttpServletResponse.SC_NOT_FOUND,fromLang(onlineClient.getLocale(),"REQUESTED_DOCUMENT_NOT_FOUND", refs[i]));
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
					log.object("Generating invoice "+i+" from DBObject", dbInvoices[i]);
					
					InvoiceFM01 invoice = new Gson().fromJson(dbInvoices[i].toString(), InvoiceFM01.class);
					invoices[i] = invoice;
					clients[i] = invoice.getClient();
					log.info("invoice "+i+" from DBObject generated");
					if(invoice.invoiceType()!=0){
						try {
							response.sendError(HttpServletResponse.SC_BAD_REQUEST,fromLang(onlineClient.getLocale(),"CODE_8", refs[i]));
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
					log.info("STEP 1");
					if(!invoice.hasElectronicVersion()){
						try {
							response.sendError(HttpServletResponse.SC_BAD_REQUEST,fromLang(onlineClient.getLocale(),"CODE_9", refs[i]));
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
					log.info("STEP 2");
					Client client = invoice.getClient();
					Client agent = invoice.getAgent();
					String fiscalCode = client.getRfc();
					if(i!=0){
						if(!fiscalCode.equals(fiscalCodeMatch)){
							try {
								response.sendError(HttpServletResponse.SC_BAD_REQUEST,fromLang(onlineClient.getLocale(),"CODE_10", refs[i], fiscalCode, fiscalCodeMatch));
							} catch (IOException e) {
								e.printStackTrace();
							}
							return;
						}
					}
					else fiscalCodeMatch = fiscalCode;
					log.info("STEP 3");
					String[] emails1 = invoice.getClient().getEmail().split(" ");
					String[] emails2 = invoice.getAgent().getEmail().split(" ");
					if(i == 0)emails = invoice.getClient().getEmail().split(" ");
					log.info("STEP 3.1. emails.length="+emails.length+" emails1.length="+emails1.length+" emails2.length="+emails2.length);
					for(int j = 0; j < emails.length; j++){
						for(int k = 0; k < emails1.length; k++){
							if(! emails[j].equals(emails1[k])) emails = (String[])ArrayUtils.add(emails,emails1[k]);
						}
						for(int k = 0; k < emails2.length; k++){
							if(! emails[j].equals(emails2[k])) emails = (String[])ArrayUtils.add(emails,emails2[k]);
						}
					}
					log.info("STEP 4");
					List<InvoiceLog> logs = invoice.getLogs();
					docDues[i] = invoice.getTotal();
					log.info("looping through logs, size="+logs.size());
					for(int j = 0; j < logs.size(); j++){
						if(logs.get(j).kind().equals(LogKind.PAYMENT)){
							Payment payment =  new Gson().fromJson(new Gson().toJson(logs.get(j).getValue()),Payment.class);
							docDues[i] -= payment.amount;
							if(docDues[i] <= 0 ){
								try {
									response.sendError(HttpServletResponse.SC_BAD_REQUEST,fromLang(onlineClient.getLocale(),"CODE_3", refs[i]));
								} catch (IOException e) {
									e.printStackTrace();
								}
								return;
							}
							docPartialities[i] += 1;
						}
					}
					if(docPayments[i] > docDues[i]){
						try {
							response.sendError(HttpServletResponse.SC_BAD_REQUEST,fromLang(onlineClient.getLocale(),"CODE_4", docPayments[i]+"", docDues[i]+""));
						} catch (IOException e) {
							e.printStackTrace();
						}
						return;
					}
					docInsoluteDues[i] = docDues[i]-docPayments[i];
					String xmlEscapedInvoice = invoice.getElectronicVersion().getXml();
					String xmlInvoice = StringEscapeUtils.unescapeXml(xmlEscapedInvoice);
					org.jsoup.nodes.Document document = Jsoup.parse(xmlInvoice);
					Element tfd = document.select("cfdi|Comprobante > cfdi|Complemento > tfd|TimbreFiscalDigital").get(0);
					Element cfd = document.select("cfdi|Comprobante").get(0);
					docFiscalReferences[i] = tfd.attr("UUID");
					docSeries[i] = cfd.attr("Serie");
					docReferences[i] = cfd.attr("Folio");
					restTotal += docDues[i];
					relatedDocs[i] = new mx.bigdata.sat.common.pagos.schema.Pagos.Pago.DoctoRelacionado();
					relatedDocs[i].setIdDocumento(docFiscalReferences[i]);
					relatedDocs[i].setFolio(docReferences[i]);
					relatedDocs[i].setSerie(docSeries[i]);
					relatedDocs[i].setMonedaDR(CMonedaPago.MXN);
					relatedDocs[i].setMetodoDePagoDR(CMetodoPagoPago.fromValue(cfd.attr("MetodoPago")));
					relatedDocs[i].setImpSaldoAnt(Util.bigRound2(docDues[i]));
					relatedDocs[i].setImpPagado(Util.bigRound2(docPayments[i]));
					relatedDocs[i].setImpSaldoInsoluto(Util.bigRound2(docInsoluteDues[i]));
					relatedDocs[i].setNumParcialidad(new BigInteger(docPartialities[i]+""));
					log.object("relatedDocs["+i+"]",relatedDocs[i]);
				}
				if(restTotal < amount){
					try {
						response.sendError(HttpServletResponse.SC_BAD_REQUEST,fromLang(onlineClient.getLocale(),"CODE_4", refsParameter, amount+"",restTotal+""));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				Shopman shopman = onlineClient.getShopman();
				List<InvoiceItem> items = new LinkedList<InvoiceItem>();
				items.add(new InvoiceItem(1, "SAT-PAGO", 0, "PAGO", "SAT", "Pago", 0, 0, 0, "84111506", "ACT"));
				
				Long dateTimePayment = null;
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
				try {
					dateTimePayment = format.parse(datetime).getTime();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				String series = GSettings.get("INVOICE_SERIAL");
				int count=mongoi.doIncrement(Mongoi.REFERENCE, "{ \"reference\" : \"unique\" }", "count");
				String reference = count+"";
				log.info("Trying to construct InvoiceTypePayment");
				InvoiceTypePayment invoice = new InvoiceTypePayment(clients[0], shopman, reference, series, amount, dateTimePayment, paymentWay, operationNumber, relatedDocs, items, "XXX", emails);
				Boolean production = ! new Boolean(GSettings.get("TEST")); 
				invoice.persist();
				log.info("Trying to construct GEN_CFD_TYPE_PAYMENT_STRING");
				String out = Utils.GEN_CFD_TYPE_PAYMENT_STRING(invoice);
				byte[] cfd64=out.getBytes();
				log.object("cfd is:",out);
				String invoice64 = DatatypeConverter.printBase64Binary(cfd64);
				
				GSettings g = GSettings.instance();
				Object objects=null;
				List l = null;
				log.info("going to send invoice to PAC. production="+production);
				try {
					if(production){
						objects = new mx.timbracfdi33.Timbrado(new URL(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_WEB_SERVICE")))
								.getTimbradoSoap()
								.timbraCFDI(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_USER"), invoice64, invoice.reference);
						l = ((ArrayOfAnyType)objects).getAnyType();
					}
					else{
						objects = new mx.buzoncfdi.cfdi33_pruebas.Timbrado(new URL(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_WEB_SERVICE")))
								.getTimbradoSoap()
								.timbraCFDI(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_USER"), invoice64, invoice.reference);
						l = ((mx.buzoncfdi.cfdi33_pruebas.ArrayOfAnyType)objects).getAnyType();
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
				log.object("PAC raw response",objects);
				PACResponse pacResponse = null;
				if(l.get(1).equals("0")){
					String xml=StringEscapeUtils.unescapeJava((String)l.get(3));
					log.object("xml cfdi is",xml);
					String message=(String)l.get(2);
					String code="0";
					boolean success=true;
					pacResponse = new PACResponse(success, message, xml, code);
				}
				else{
					String xml="<null></null>";
					String message=(String)l.get(2);
					String code=l.get(1)+"";
					boolean success=false;
					pacResponse = new PACResponse(success, message, xml, code);
				}
				
				if (pacResponse.isSuccess()) {
					mongoi.doUpdate(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\" }",
							"{ \"hasElectronicVersion\" : true }");
					
					InvoiceLog facture = new InvoiceLog(LogKind.FACTURE, true,
							onlineClient.getShopman().getLogin());
					mongoi.doPush(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\"}",
							"{\"logs\" : " + new Gson().toJson(facture) + " }");
					for(int i = 0; i < invoices.length; i++){
						InvoiceLog ilog = new InvoiceLog(
								LogKind.PAYMENT,
								new Payment(docPayments[i], dateTimePayment, operationNumber, reference),
								shopman.getLogin());
						mongoi.doPush(Mongoi.INVOICES, "{\"reference\" : \""+refs[i]+"\"}",
								"{\"logs\" : "+new Gson().toJson(ilog)+" }");
					}
					
					String xml = pacResponse.getContent();
					String xmlEscaped=StringEscapeUtils.escapeXml(xml);
					
					String pathToWrite = GSettings.getPathToDirectory(GSettings.get("INVOICES_TYPE_PAYMENT_FOLDER")+"-"+Utils.getDateString("yyyy-MM"));
					String pdf = pathToWrite + reference + ".pdf";
					String 	id=invoice.reference,
							xsltPath=GSettings.getPathTo("XSLT_INVOICE_TYPE_PAYMENT_TICKET"),
							xmlPath=pathToWrite+id+".xml",
							htmlPath=pathToWrite+id+".html",
							pdfPath=pathToWrite+id+".pdf",
							additionalData=GSettings.get("INVOICE_SENDER_ADDITIONAL_DATA");
					log.object("xmlEscaped is : ",xmlEscaped);
					//String reference=invoice.getReference();
					//ElectronicInvoiceFactory.saveCFDI(xml, reference, pathToWrite);
					Utils.saveStringToFile(xml, xmlPath);
					ElectronicInvoiceFactory.genQRCode(reference, pathToWrite);
					
					Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData,"");
					//Utils.HTML_TO_PDF(htmlPath, pdfPath, "Cotizacion "+id+" - pagina [page]/[topage]", "");
					
					//Utils.XML_TO_HTML(xmlPath, xsltTicketPath, htmlTicketPath, additionalData,destiny.getAddress());
					Utils.HTML_TO_PDF_TICKET(htmlPath, pdfPath);
					
					
					mongoi.doUpdate(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\" }",
							"{ \"electronicVersion\" : {\"xml\" : \"" + xmlEscaped + "\"} }");
					
					String printer = GSettings.get("INVOICE_PRINTER_TICKET");
					String printerOptions = GSettings.get("INVOICE_PRINTER_TICKET_OPTIONS");

					new PrinterFM01(new File(pdfPath), printer)
					.print(printerOptions+" -# 2");		
					// TODO has parse de mails please
					String[] recipients=emails;//new String[]{};
					if (recipients.length>0) {
						new Hotmail().send(
								"Factura/Pago "+ GSettings.get("INVOICE_SERIAL") + " " + reference
										+ " $" + invoice.amount,
								GSettings.get("EMAIL_BODY"), recipients,
								new String[] { 
										pathToWrite + reference + ".xml",
										pathToWrite + reference + ".pdf" },
								new String[] {
										reference + ".xml",
										reference + ".pdf" });
					}
					try {
						DBObject dbObject = mongoi.doFindOne(Mongoi.INVOICES, "{ \"reference\" : \"" + invoice.reference + "\"}");
						String resp = "{ \"invoice\" : " + dbObject.toString()
						+ ", \"successResponse\" : \"facturado/pago "+reference+" por "+invoice.amount+"\"}";
						log.exit("response",resp);
						response.getWriter().print(resp);
					} catch (IOException e) {
						log.trace("", e);
					}
					return;
				} else {
					// TODO has que esta piñates te diga el mensage
					String serverMessage = "ERROR - El servidor de facturacion dijo: codigo "
							+ pacResponse.getResponseCode() + " - mensaje "
							+ pacResponse.getMessage()
							+ new Gson().toJson(objects);
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
					/*InvoiceLog cancelLog = new InvoiceLog(LogKind.CANCEL, true,
							onlineClient.getShopman().getLogin());
					mongo.doPush(Mongoi.INVOICES,
							"{ \"reference\" : \"" + invoice.getReference() + "\"}",
							"{\"logs\" : " + new Gson().toJson(cancelLog) + " }");
					*/
					
					return;
				}
				
			}
		});
		
	}
	

	private void searchProducts(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				int requestNumber=new Integer(parametersMap.get("requestNumber"));
				String search=parametersMap.get("search");
				List<String> matchList = new ArrayList<String>();
				Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
				Matcher regexMatcher = regex.matcher(search);
				while (regexMatcher.find()) {
				    if (regexMatcher.group(1) != null) {
				        // Add double-quoted string without the quotes
				        matchList.add(regexMatcher.group(1));
				    } else if (regexMatcher.group(2) != null) {
				        // Add single-quoted string without the quotes
				        matchList.add(regexMatcher.group(2));
				    } else {
				        // Add unquoted word
				        matchList.add(regexMatcher.group());
				    }
				}
				String[] patterns=matchList.toArray(new String[1]);
				List<DBObject> list=Product.find(patterns,onlineClient,requestNumber);
				List<DBObject> filteredList=FilterDBObject.keep(
						new String[]{
								"id",
								"code",
								"description",
								"mark",
								"unit",
								"unitPrice",
								"incrementPercent",
								"providerPrice",
								"providerOffer",
								"prodservCode",
								"unitCode",
								"stored"},
						list);
				if(requestNumber<onlineClient.getRequestNumber()){
					try {
						response.getWriter().print("{\"result\" : [ ],\"requestNumber\" : \""+requestNumber+"\"}");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				String resp="{\"result\":"+new Gson().toJson(list)+",\"requestNumber\":\""+requestNumber+"\"} ";
				try {
					response.getWriter().print(resp);
					System.out.println(resp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	
	private void fixDB(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				int fixNumber = new Integer(parametersMap.get("fixNumber"));
				
				String result = FixDB.fix(fixNumber);
				String resp="{\"result\": "+result+"}";
				try {
					response.getWriter().print(resp);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		});
		
	}

	private void cancelDocument(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				Log lg= new Log();
				lg.entry();
				String reference = request.getParameter("reference");
				DBObject oInvoice=null;
				Invoice invoice=null;
				oInvoice=new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"reference\" : \""+reference+"\" }");
				if(oInvoice==null){
					response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
					try {
						response.getWriter().write("error : referencia no encontrada '"+reference+"'");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				invoice=new Gson().fromJson(oInvoice.toString(), InvoiceFM01.class);
				if(invoice.attemptToLog(LogKind.CANCEL).isAllowed()){
					if(invoice.hasElectronicVersion()){
						ElectronicInvoice ei=new Profact();
						PACResponse pacResponse=ei.cancel(invoice,!new Boolean(GSettings.get("TEST")));
						if(pacResponse.isSuccess()){
							String xml=pacResponse.getContent();//document.getElementsByTagName("xmlretorno").item(0).getTextContent();
							lg.object("PAC response success xml = "+xml);
							new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+reference+"\"}", "{ \"electronicVersion.cancelXml\" : \""+StringEscapeUtils.escapeXml(xml)+"\"}");
							new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+reference+"\"}", "{ \"electronicVersion.active\" : false }");
							Date creationDate = new Date(invoice.getCreationTime());
							String pathToWrite = GSettings.getPathToDirectory(GSettings.get("INVOICES_FOLDER")+"-"+Utils.getDateString(creationDate,"yyyy-MM"));
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
						}
						else{
							response.setStatus( HttpServletResponse.SC_SERVICE_UNAVAILABLE);
							try {
								response.getWriter().write("ERROR: "+pacResponse.getResponseCode()+" - "+pacResponse.getMessage());
							} catch (IOException e) {
								e.printStackTrace();
							}//document.getElementsByTagName("mensaje").item(0).getTextContent());
							return;
						}
					}
					InvoiceLog log=new InvoiceLog(InvoiceLog.LogKind.CANCEL,true,onlineClient.getShopman().getLogin());
					InvoiceLog closeLog=new InvoiceLog(InvoiceLog.LogKind.CLOSE,true,onlineClient.getShopman().getLogin());
					new Mongoi().doPush(Mongoi.INVOICES, "{ \"reference\" : \""+reference+"\"}", "{\"logs\" : "+new Gson().toJson(log)+" }");
					new Mongoi().doPush(Mongoi.INVOICES, "{ \"reference\" : \""+reference+"\"}", "{\"logs\" : "+new Gson().toJson(closeLog)+" }");
					new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+reference+"\"}", "{\"updated\" : "+closeLog.getDate()+" }");
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
					try {
						response.getWriter().write("{ \"message\":\""+successResponse+"\" }");
					} catch (IOException e) {
						e.printStackTrace();
					}
					
					return;
				}
				else {
					System.out.println("error al intentar cancelar documento");
					response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					try {
						response.getWriter().write(invoice.attemptToLog(LogKind.CANCEL).getMessage());
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
			}
		});
		
	}
	private void searchClients(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				String search=request.getParameter("search");
				int requestNumber=new Integer(request.getParameter("requestNumber"));
				onlineClient.setRequestNumber(requestNumber);
				
				String decSearch=null;
				try {
					decSearch = URLDecoder.decode(search,"utf-8").toUpperCase();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				List<String> matchList = new ArrayList<String>();
				Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
				Matcher regexMatcher = regex.matcher(decSearch);
				while (regexMatcher.find()) {
				    if (regexMatcher.group(1) != null) {
				        // Add double-quoted string without the quotes
				        matchList.add(regexMatcher.group(1));
				    } else if (regexMatcher.group(2) != null) {
				        // Add single-quoted string without the quotes
				        matchList.add(regexMatcher.group(2));
				    } else {
				        // Add unquoted word
				        matchList.add(regexMatcher.group());
				    }
				}
				String[] patterns=matchList.toArray(new String[1]);
				
				List<DBObject> list=Client.find(patterns,Mongoi.CLIENTS);
				if(requestNumber<onlineClient.getRequestNumber()){
					try {
						response.getWriter().print("{\"result\" : [ ],\"requestNumber\" : \""+requestNumber+"\"}");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("request no enviada -> "+requestNumber+"<"+onlineClient.getRequestNumber()+" :"+patterns);
					return;
				}
				String json="{ \"result\" : [ ";
				for(int i=0;i<list.size();i++){
					DBObject ob=list.get(i);
					//ob.put("code","");
					json+=ob;
					if(i<list.size()-1)json+=" , ";
				}
				json+="], \"requestNumber\" : \""+requestNumber+"\"}";
				try {
					response.getWriter().print(json);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
		});
		
	}
	
	private void searchAgents(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				String search=request.getParameter("search");
				int requestNumber=new Integer(request.getParameter("requestNumber"));
				onlineClient.setRequestNumber(requestNumber);
				
				String decSearch=null;
				try {
					decSearch = URLDecoder.decode(search,"utf-8").toUpperCase();
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				List<String> matchList = new ArrayList<String>();
				Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
				Matcher regexMatcher = regex.matcher(decSearch);
				while (regexMatcher.find()) {
				    if (regexMatcher.group(1) != null) {
				        // Add double-quoted string without the quotes
				        matchList.add(regexMatcher.group(1));
				    } else if (regexMatcher.group(2) != null) {
				        // Add single-quoted string without the quotes
				        matchList.add(regexMatcher.group(2));
				    } else {
				        // Add unquoted word
				        matchList.add(regexMatcher.group());
				    }
				}
				String[] patterns=matchList.toArray(new String[1]);
				
				List<DBObject> list=Client.find(patterns,Mongoi.AGENTS);
				if(requestNumber<onlineClient.getRequestNumber()){
					try {
						response.getWriter().print("{\"result\" : [ ],\"requestNumber\" : \""+requestNumber+"\"}");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("request no enviada -> "+requestNumber+"<"+onlineClient.getRequestNumber()+" :"+patterns);
					return;
				}
				String json="{ \"result\" : [ ";
				for(int i=0;i<list.size();i++){
					DBObject ob=list.get(i);
					//ob.put("code","");
					json+=ob;
					if(i<list.size()-1)json+=" , ";
				}
				json+="], \"requestNumber\" : \""+requestNumber+"\"}";
				try {
					response.getWriter().print(json);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
		});
		
	}
	
	private void emitInvoice(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				Log log = new Log();
				Gson gson = new Gson();
				Mongoi mongo = new Mongoi();
				JSONObject jClient = null;
				JSONObject jAgent = null;
				Client client = null;
				Client agent = null;
				try {
					jClient = new JSONObject(parametersMap.get("client"));
					jAgent = new JSONObject(parametersMap.get("agent"));
					client = ClientFactory.create(jClient);
					agent = ClientFactory.create(jAgent);
				} catch (JSONException e1) {
					log.trace(e1);
				}
				JSONArray jList = null;
				try {
					jList = new JSONArray(parametersMap.get("list"));
				} catch (JSONException e) {
					log.trace(e);
				} 
				List<InvoiceItem> list = new LinkedList<InvoiceItem>();
				for (int i = jList.length() - 1; i >= 0; i--) {
					InvoiceItem item = null;
					try {
						log.object("parsing item",jList.getJSONObject(i).toString());
						item = gson.fromJson(jList.getJSONObject(i).toString(), InvoiceItem.class);
					} catch (JsonSyntaxException | JSONException e) {
						log.trace(e);
					}
					list.add(item);
				}
				Shopman shopman = OnlineClients.instance().get(new Integer(parametersMap.get("clientReference"))).getShopman();
				
				Destiny destiny = gson.fromJson(parametersMap.get("destiny"), Destiny.class);
				String clientReference = parametersMap.get("clientReference");
				String cfdiUse = parametersMap.get("cfdiUse");
				String paymentMethod = parametersMap.get("paymentMethod");
				String paymentWay = parametersMap.get("paymentWay");
				String documentType = parametersMap.get("documentType");
				int printCopies = -1;
				if(!(parametersMap.get("printCopies")==null||parametersMap.get("printCopies").equals(""))){
					printCopies = new Integer(parametersMap.get("printCopies"));
				}
				String coin = parametersMap.get("coin");
				String series = GSettings.get("INVOICE_SERIAL");
				
				if(paymentMethod==null || paymentMethod.equals("")){
					try {
						response.sendError(response.SC_NOT_ACCEPTABLE, fromLang(onlineClient.getLocale(),"CODE_5"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				if(paymentWay==null || paymentWay.equals("")){
					try {
						response.sendError(response.SC_NOT_ACCEPTABLE, fromLang(onlineClient.getLocale(),"CODE_6"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				if(cfdiUse!=null&&!cfdiUse.equals("")){
					mongo.doUpdate(Mongoi.CLIENTS, "{ \"code\" : \""+client.getCode()+"\"}", "{\"cfdiUse\" : \""+cfdiUse+"\" }"+"}");
					client.setCfdiUse(cfdiUse);
				}
				else {
					try {
						response.sendError(response.SC_NOT_ACCEPTABLE, fromLang(onlineClient.getLocale(),"CODE_7"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				int count=mongo.doIncrement(Mongoi.REFERENCE, "{ \"reference\" : \"unique\" }", "count");
				String reference = count+"";
				Invoice invoice = new InvoiceFM01(
						client, shopman, list, reference, series, destiny,
						agent, paymentMethod, paymentWay, documentType, coin);
				invoice.persist();
				ElectronicInvoice electronicInvoice=new Profact(invoice);
				boolean production=!new Boolean(GSettings.get("TEST"));
				PACResponse pacResponse=electronicInvoice.submit(production);
				
				if (pacResponse.isSuccess()) {
					mongo.doUpdate(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\" }",
							"{ \"hasElectronicVersion\" : true }");
					
					InvoiceLog facture = new InvoiceLog(LogKind.FACTURE, true,
							onlineClient.getShopman().getLogin());
					mongo.doPush(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\"}",
							"{\"logs\" : " + new Gson().toJson(facture) + " }");
					
					String xml = pacResponse.getContent();
					String xmlEscaped=StringEscapeUtils.escapeXml(xml);
					
					String pathToWrite = GSettings.getPathToDirectory(GSettings.get("INVOICES_FOLDER")+"-"+Utils.getDateString("yyyy-MM"));
					String pdf = pathToWrite + reference + ".pdf";
					String 	id=invoice.getReference(),
							xsltPath=GSettings.getPathTo("XSLT_INVOICE"),
							xmlPath=pathToWrite+id+".xml",
							htmlPath=pathToWrite+id+".html",
							pdfPath=pathToWrite+id+".pdf",
							additionalData=GSettings.get("INVOICE_SENDER_ADDITIONAL_DATA");
					log.object("xmlEscaped is : ",xmlEscaped);
					//String reference=invoice.getReference();
					//ElectronicInvoiceFactory.saveCFDI(xml, reference, pathToWrite);
					Utils.saveStringToFile(xml, xmlPath);
					ElectronicInvoiceFactory.genQRCode(reference, pathToWrite);
					Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData,destiny.getAddress());
					Utils.HTML_TO_PDF(htmlPath, pdfPath, "Factura "+GSettings.get("INVOICE_SERIAL")+" "+id+" - pagina [page]/[topage]", "");
					

					//ElectronicInvoiceFactory.genHTML(reference, pathToWrite);
					//ElectronicInvoiceFactory.genPDF(reference, pathToWrite);
					
					org.jsoup.nodes.Document document=null;
					document = Jsoup.parse(xml);
					
					String total=document.select("cfdi|Comprobante").get(0).attr("Total");
					String subTotal=document.select("cfdi|Comprobante").get(0).attr("SubTotal");
					String taxes=document.select("cfdi|Comprobante > cfdi|Impuestos").get(0).attr("TotalImpuestosTrasladados");
					
					mongo.doUpdate(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\" }",
							"{ \"electronicVersion\" : {\"xml\" : \"" + xmlEscaped + "\"} }");
					
					String csv = pathToWrite + invoice.getReference() +".csv";
					FileWriter fstream;
					try {
						fstream = new FileWriter(csv);
					
						BufferedWriter out = new BufferedWriter(fstream);
						// out.write(invoice.getConsummer() + "\n" +
						// invoice.getAddress() + "\n" + invoice.getCity() + "\n"
						// + invoice.getCp() + "\n" + invoice.getRfc() + "\n");
						
						Elements elements = document.select("cfdi|Comprobante > cfdi|Conceptos > cfdi|Concepto");
						for(int i = 0 ; i < elements.size(); i ++){
							Element element = elements.get(i);
							String prodservCode = element.attr("ClaveProdServ");
							String quantity = element.attr("Cantidad");
							String unitCode = element.attr("ClaveUnidad");
							String unit = element.attr("Unidad");
							String description = element.attr("Descripcion");
							String unitPrice = element.attr("ValorUnitario");
							String itotal = element.attr("Importe");
							out.write(
									quantity + "\t" +
											prodservCode + "\t" +
											unitCode + "\t" +
											unit + "\t" +
											description + "\t" +
											unitPrice + "\t" +
											itotal + "\n"
									);
							
						}
						/*List<InvoiceItem> items2 = invoice.getItems();
						for (InvoiceItem item : items2) {
							out.write(item.getQuantity() + "\t" + item.getCode() + "\t" + item.getUnit() + "\t"
									+ item.getDescription() + "\t" + item.getMark() + "\t" + item.getUnitPrice() + "\n");
						}*/
						out.write("subtotal\t" + subTotal);
						out.write("\niva\t" + taxes);
						out.write("\ntotal\t" + total);
						// Close the output stream
						out.close();
						Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","chmod 444 "+csv});
					} catch (IOException e1) {
						log.trace(e1);
					}
					
					
					if (printCopies == -1) {
						new PrinterFM01(new File(pdf), GSettings.get("PRINTER_TWO")).print(new Integer(GSettings.get("PRINTER_TWO_COPIES")));
					}
					else if(printCopies > 0){
						new PrinterFM01(new File(pdf), GSettings.get("PRINTER_TWO")).print(printCopies);
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
								"factura "+ GSettings.get("INVOICE_SERIAL") + " " + reference
										+ " $" + total,
								GSettings.get("EMAIL_BODY"), recipients,
								new String[] { 
										pathToWrite + reference + ".csv",
										pathToWrite + reference + ".xml",
										pathToWrite + reference + ".pdf" },
								new String[] {
										reference + ".csv",
										reference + ".xml",
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
					
					/*TheBox.instance().plus(invoice.getTotal());
					TheBox.instance()
							.addLog(new TheBoxLog(invoice.getTotal(), paymentLog.getDate(),
									invoice.getReference(), LogKind.PAYMENT.toString(),
									onlineClient.getShopman().getLogin()));*/
					List<InvoiceItem> invoiceItems = invoice.getItems();
					for (int i = 0; i < invoiceItems.size(); i++) {
						InvoiceItem item = invoiceItems.get(i);
						if (Inventory.exists(item) && !item.isDisabled())
							Inventory.decrementStored(item);
					}
					try {
						DBObject dbObject = mongo.doFindOne(Mongoi.INVOICES, "{ \"reference\" : \"" + invoice.getReference() + "\"}");
						String resp = "{ \"invoice\" : " + dbObject.toString()
						+ ", \"successResponse\" : \"facturado "+reference+" por "+total+"\"}";
						log.exit("response",resp);
						response.getWriter().print(resp);
					} catch (IOException e) {
						log.trace("", e);
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
					/*InvoiceLog cancelLog = new InvoiceLog(LogKind.CANCEL, true,
							onlineClient.getShopman().getLogin());
					mongo.doPush(Mongoi.INVOICES,
							"{ \"reference\" : \"" + invoice.getReference() + "\"}",
							"{\"logs\" : " + new Gson().toJson(cancelLog) + " }");
					*/
					
					return;
				}
			}
		});
		
	}
	

	private void emitOrder(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				Log log = new Log();
				Gson gson = new Gson();
				Mongoi mongo = new Mongoi();
				JSONObject jClient = null;
				JSONObject jAgent = null;
				Client client = null;
				Client agent = null;
				try {
					jClient = new JSONObject(parametersMap.get("client"));
					jAgent = new JSONObject(parametersMap.get("agent"));
					client = ClientFactory.create(jClient);
					agent = ClientFactory.create(jAgent);
				} catch (JSONException e1) {
					log.trace(e1);
				}
				JSONArray jList = null;
				try {
					jList = new JSONArray(parametersMap.get("list"));
				} catch (JSONException e) {
					log.trace(e);
				} 
				List<InvoiceItem> list = new LinkedList<InvoiceItem>();
				for (int i = jList.length() - 1; i >= 0; i--) {
					InvoiceItem item = null;
					try {
						log.object("parsing item",jList.getJSONObject(i).toString());
						item = gson.fromJson(jList.getJSONObject(i).toString(), InvoiceItem.class);
					} catch (JsonSyntaxException | JSONException e) {
						log.trace(e);
					}
					list.add(item);
				}
				Shopman shopman = OnlineClients.instance().get(new Integer(parametersMap.get("clientReference"))).getShopman();
				
				Destiny destiny = gson.fromJson(parametersMap.get("destiny"), Destiny.class);
				String clientReference = parametersMap.get("clientReference");
				String cfdiUse = "G01";
				String paymentMethod = "PUE";
				String paymentWay = "99";
				String documentType = parametersMap.get("documentType");
				int printCopies = -1;
				if(!(parametersMap.get("printCopies")==null||parametersMap.get("printCopies").equals(""))){
					printCopies = new Integer(parametersMap.get("printCopies"));
				}
				String coin = parametersMap.get("coin");
				String series = GSettings.get("INVOICE_SERIAL");
				if(cfdiUse!=null&&!cfdiUse.equals("")){
					mongo.doUpdate(Mongoi.CLIENTS, "{ \"code\" : \""+client.getCode()+"\"}", "{\"cfdiUse\" : \""+cfdiUse+"\" }"+"}");
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
				int count=mongo.doIncrement(Mongoi.REFERENCE, "{ \"reference\" : \"unique\" }", "count");
				String reference = count+"";
				Invoice invoice = new InvoiceFM01(
						client, shopman, list, reference, series, destiny,
						agent, paymentMethod, paymentWay, documentType, coin);
				invoice.persist();
				Invoice[] invoices = invoice.subdivide(InvoiceFormFM01.ROWS_NUMBER);
				
				String[] paths = new String[invoices.length + 1];
				String[] fileNames = new String[invoices.length + 1];
				String pathToWrite = GSettings.getPathToDirectory(GSettings.get("ORDERS_FOLDER")+"-"+Utils.getDateString("yyyy-MM"));
				
				String xml=Utils.GEN_CFD_STRING(invoice);
				String 	id=invoice.getReference(),
						xsltPath=GSettings.getPathTo("XSLT_ORDER"),
						xmlPath=pathToWrite+id+".xml",
						htmlPath=pathToWrite+id+".html",
						pdfPath=pathToWrite+id+".pdf",
						additionalData=GSettings.get("INVOICE_SENDER_ADDITIONAL_DATA");
				Utils.saveStringToFile(xml, xmlPath);						
				Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData,destiny.getAddress());
				Utils.HTML_TO_PDF(htmlPath, pdfPath, "Pedido "+id+" - pagina [page]/[topage]", "");
				
				
				String csv = pathToWrite + invoice.getReference() +".csv";
				org.jsoup.nodes.Document document=null;
				document = Jsoup.parse(xml);
				String total=document.select("cfdi|Comprobante").get(0).attr("Total");
				String subTotal=document.select("cfdi|Comprobante").get(0).attr("SubTotal");
				String taxes=document.select("cfdi|Comprobante > cfdi|Impuestos").get(0).attr("TotalImpuestosTrasladados");


				FileWriter fstream;
				try {
					fstream = new FileWriter(csv);
				
					BufferedWriter out = new BufferedWriter(fstream);
					// out.write(invoice.getConsummer() + "\n" +
					// invoice.getAddress() + "\n" + invoice.getCity() + "\n"
					// + invoice.getCp() + "\n" + invoice.getRfc() + "\n");
					
					Elements elements = document.select("cfdi|Comprobante > cfdi|Conceptos > cfdi|Concepto");
					for(int i = 0 ; i < elements.size(); i ++){
						Element element = elements.get(i);
						String prodservCode = element.attr("ClaveProdServ");
						String quantity = element.attr("Cantidad");
						String unitCode = element.attr("ClaveUnidad");
						String unit = element.attr("Unidad");
						String description = element.attr("Descripcion");
						String unitPrice = element.attr("ValorUnitario");
						String itotal = element.attr("Importe");
						out.write(
								quantity + "\t" +
										prodservCode + "\t" +
										unitCode + "\t" +
										unit + "\t" +
										description + "\t" +
										unitPrice + "\t" +
										itotal + "\n"
								);
						
					}
					/*List<InvoiceItem> items2 = invoice.getItems();
					for (InvoiceItem item : items2) {
						out.write(item.getQuantity() + "\t" + item.getCode() + "\t" + item.getUnit() + "\t"
								+ item.getDescription() + "\t" + item.getMark() + "\t" + item.getUnitPrice() + "\n");
					}*/
					out.write("subtotal\t" + subTotal);
					out.write("\niva\t" + taxes);
					out.write("\ntotal\t" + total);
					// Close the output stream
					out.close();
					Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","chmod 444 "+csv});
				} catch (IOException e1) {
					log.trace(e1);
				}
				
				
				for (int i = 0; i < invoices.length; i++) {
					String pathname = pathToWrite + invoices[i].getReference() + "-" + i+".pdf";
					
					File pdf = new PDF(invoices[i], pathname).make();
					try {
						Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","chmod 444 "+pathname});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// System.out.println("invoices["+i+"]:
					// "+invoices[i].toJson());
					// TODO this is printing time
					if (printCopies == -1) {
						new PrinterFM01(pdf, GSettings.get("PRINTER_ONE")).print(new Integer(GSettings.get("PRINTER_ONE_COPIES")));
					}
					else if(printCopies > 0){
						new PrinterFM01(pdf, GSettings.get("PRINTER_ONE")).print(printCopies);
					}
					
					//new PrinterFM01(pdf, GSettings.get("PRINTER_ONE")).print(new Integer(GSettings.get("PRINTER_ONE_COPIES")));
					paths[i] = pathname;
					fileNames[i] = pdf.getName();
					// emis.persist(invoices[i]);
				}
				
				String[] recipients={};//new String[]{};
				
				if (!invoice.getClient().getEmail().equals("")) {
					recipients=(String[])ArrayUtils.addAll(recipients, invoice.getClient().getEmail().split(" "));
				}
				if (!invoice.getAgent().getEmail().equals("")) {
					recipients=(String[])ArrayUtils.addAll(recipients, invoice.getAgent().getEmail().split(" "));
				}
				if (recipients.length>0) {
					new Hotmail().send(
							"Pedido "+ reference
									+ " $" + invoice.getTotal(),
							GSettings.get("EMAIL_BODY"), recipients,
							new String[] { pathToWrite + reference + ".csv",
									pathToWrite + reference + ".pdf" },
							new String[] { reference + ".csv",
									reference + ".pdf" });
				}
				
				List<InvoiceItem> invoiceItems = invoice.getItems();
				for (int i = 0; i < invoiceItems.size(); i++) {
					InvoiceItem item = invoiceItems.get(i);
					if (Inventory.exists(item) && !item.isDisabled())
						Inventory.decrementStored(item);
				}
				try {
					DBObject dbObject = mongo.doFindOne(Mongoi.INVOICES, "{ \"reference\" : \"" + invoice.getReference() + "\"}");
					String resp = "{ \"invoice\" : " + dbObject.toString()
					+ ", \"successResponse\" : \"pedido "+id+" por "+total+"\"}";
					log.exit("response",resp);
					response.getWriter().print(resp);
				} catch (IOException e) {
					log.trace("", e);
				}
				return;
				
				/*
				ElectronicInvoice electronicInvoice=new Profact(invoice);
				boolean production=!new Boolean(GSettings.get("TEST"));
				PACResponse pacResponse=electronicInvoice.submit(production);
				
				if (pacResponse.isSuccess()) {
					String xml = pacResponse.getContent();
					String xmlEscaped=StringEscapeUtils.escapeXml(xml);
					
					String pathToWrite = GSettings.getPathToDirectory(GSettings.get("INVOICES_FOLDER")+"-"+Utils.getDateString("yyyy-MM"));
					String pdf = pathToWrite + reference + ".pdf";
					
					log.object("xmlEscaped is : ",xmlEscaped);
					//String reference=invoice.getReference();
					ElectronicInvoiceFactory.saveCFDI(xml, reference, pathToWrite);
					ElectronicInvoiceFactory.genQRCode(reference, pathToWrite);
					ElectronicInvoiceFactory.genHTML(reference, pathToWrite);
					ElectronicInvoiceFactory.genPDF(reference, pathToWrite);
					
					org.jsoup.nodes.Document document=null;
					document = Jsoup.parse(xml);
					
					String total=document.select("cfdi|Comprobante").get(0).attr("Total");
					String subTotal=document.select("cfdi|Comprobante").get(0).attr("SubTotal");
					String taxes=document.select("cfdi|Comprobante > cfdi|Impuestos").get(0).attr("TotalImpuestosTrasladados");
					
					mongo.doUpdate(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\" }",
							"{ \"electronicVersion\" : {\"xml\" : \"" + xmlEscaped + "\"} }");
					mongo.doUpdate(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\" }",
							"{ \"hasElectronicVersion\" : true }");
					if (printCopies == -1) {
						new PrinterFM01(new File(pdf), GSettings.get("PRINTER_TWO")).print(new Integer(GSettings.get("PRINTER_TWO_COPIES")));
					}
					else if(printCopies > 0){
						new PrinterFM01(new File(pdf), GSettings.get("PRINTER_TWO")).print(printCopies);
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
								new String[] { pathToWrite + reference + ".xml",
										pathToWrite + reference + ".pdf" },
								new String[] { reference + ".xml",
										reference + ".pdf" });
					}

					List<InvoiceItem> invoiceItems = invoice.getItems();
					for (int i = 0; i < invoiceItems.size(); i++) {
						InvoiceItem item = invoiceItems.get(i);
						if (Inventory.exists(item) && !item.isDisabled())
							Inventory.decrementStored(item);
					}
					try {
						DBObject dbObject = mongo.doFindOne(Mongoi.INVOICES, "{ \"reference\" : \"" + invoice.getReference() + "\"}");
						String resp = "{ \"invoice\" : " + dbObject.toString()
						+ ", \"successResponse\" : \"facturado\"}";
						log.exit("response",resp);
						response.getWriter().print(resp);
					} catch (IOException e) {
						log.trace("", e);
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
			
					
					return;
				}*/
			}
		});
		
	}
	

	private void emitSample(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				Log log = new Log();
				Gson gson = new Gson();
				Mongoi mongo = new Mongoi();
				JSONObject jClient = null;
				JSONObject jAgent = null;
				Client client = null;
				Client agent = null;
				try {
					jClient = new JSONObject(parametersMap.get("client"));
					jAgent = new JSONObject(parametersMap.get("agent"));
					client = ClientFactory.create(jClient);
					agent = ClientFactory.create(jAgent);
				} catch (JSONException e1) {
					log.trace(e1);
				}
				JSONArray jList = null;
				try {
					jList = new JSONArray(parametersMap.get("list"));
				} catch (JSONException e) {
					log.trace(e);
				} 
				List<InvoiceItem> list = new LinkedList<InvoiceItem>();
				for (int i = jList.length() - 1; i >= 0; i--) {
					InvoiceItem item = null;
					try {
						log.object("parsing item",jList.getJSONObject(i).toString());
						item = gson.fromJson(jList.getJSONObject(i).toString(), InvoiceItem.class);
					} catch (JsonSyntaxException | JSONException e) {
						log.trace(e);
					}
					list.add(item);
				}
				Shopman shopman = OnlineClients.instance().get(new Integer(parametersMap.get("clientReference"))).getShopman();
				
				Destiny destiny = gson.fromJson(parametersMap.get("destiny"), Destiny.class);
				String clientReference = parametersMap.get("clientReference");
				String cfdiUse = "G01";
				String paymentMethod = "PUE";
				String paymentWay = "99";
				String documentType = parametersMap.get("documentType");
				int printCopies = -1;
				if(!(parametersMap.get("printCopies")==null||parametersMap.get("printCopies").equals(""))){
					printCopies = new Integer(parametersMap.get("printCopies"));
				}
				String coin = parametersMap.get("coin");
				String series = GSettings.get("INVOICE_SERIAL");
				if(cfdiUse!=null&&!cfdiUse.equals("")){
					mongo.doUpdate(Mongoi.CLIENTS, "{ \"code\" : \""+client.getCode()+"\"}", "{\"cfdiUse\" : \""+cfdiUse+"\" }"+"}");
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
				int count=mongo.doIncrement(Mongoi.REFERENCE, "{ \"reference\" : \"unique\" }", "count");
				String reference = count+"";
				Invoice invoice = new InvoiceFM01(
						client, shopman, list, reference, series, destiny,
						agent, paymentMethod, paymentWay, documentType, coin);
				invoice.persist();
				Invoice[] invoices = invoice.subdivide(InvoiceFormFM01.ROWS_NUMBER);
				
				//String[] paths = new String[invoices.length + 1];
				//String[] fileNames = new String[invoices.length + 1];
				String pathToWrite = GSettings.getPathToDirectory(GSettings.get("SAMPLES_FOLDER")+"-"+Utils.getDateString("yyyy-MM"));
				
				String xml=Utils.GEN_CFD_STRING(invoice);
				String 	id=invoice.getReference(),
						xsltPath=GSettings.getPathTo("XSLT_SAMPLE"),
						xmlPath=pathToWrite+id+".xml",
						htmlPath=pathToWrite+id+".html",
						pdfPath=pathToWrite+id+".pdf",
						additionalData=GSettings.get("INVOICE_SENDER_ADDITIONAL_DATA");
				Utils.saveStringToFile(xml, xmlPath);						
				Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData,destiny.getAddress());
				Utils.HTML_TO_PDF(htmlPath, pdfPath, "Cotizacion "+id+" - pagina [page]/[topage]", "");
				
				
				String csv = pathToWrite + invoice.getReference() +".csv";
				org.jsoup.nodes.Document document=null;
				document = Jsoup.parse(xml);
				String total=document.select("cfdi|Comprobante").get(0).attr("Total");
				String subTotal=document.select("cfdi|Comprobante").get(0).attr("SubTotal");
				String taxes=document.select("cfdi|Comprobante > cfdi|Impuestos").get(0).attr("TotalImpuestosTrasladados");
				FileWriter fstream;
				try {
					fstream = new FileWriter(csv);
				
					BufferedWriter out = new BufferedWriter(fstream);
					// out.write(invoice.getConsummer() + "\n" +
					// invoice.getAddress() + "\n" + invoice.getCity() + "\n"
					// + invoice.getCp() + "\n" + invoice.getRfc() + "\n");
					
					Elements elements = document.select("cfdi|Comprobante > cfdi|Conceptos > cfdi|Concepto");
					for(int i = 0 ; i < elements.size(); i ++){
						Element element = elements.get(i);
						String prodservCode = element.attr("ClaveProdServ");
						String quantity = element.attr("Cantidad");
						String unitCode = element.attr("ClaveUnidad");
						String unit = element.attr("Unidad");
						String description = element.attr("Descripcion");
						String unitPrice = element.attr("ValorUnitario");
						String itotal = element.attr("Importe");
						out.write(
								quantity + "\t" +
										prodservCode + "\t" +
										unitCode + "\t" +
										unit + "\t" +
										description + "\t" +
										unitPrice + "\t" +
										itotal + "\n"
								);
						
					}
					/*List<InvoiceItem> items2 = invoice.getItems();
					for (InvoiceItem item : items2) {
						out.write(item.getQuantity() + "\t" + item.getCode() + "\t" + item.getUnit() + "\t"
								+ item.getDescription() + "\t" + item.getMark() + "\t" + item.getUnitPrice() + "\n");
					}*/
					out.write("subtotal\t" + subTotal);
					out.write("\niva\t" + taxes);
					out.write("\ntotal\t" + total);
					// Close the output stream
					out.close();
					Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","chmod 444 "+csv});
				} catch (IOException e1) {
					log.trace(e1);
				}
				
				
				/*for (int i = 0; i < invoices.length; i++) {
					String pathname = pathToWrite + invoices[i].getReference() + "-" + i+".pdf";
					
					File pdf = new PDF(invoices[i], pathname).make();
					// System.out.println("invoices["+i+"]:
					// "+invoices[i].toJson());
					// TODO this is printing time
					if (printCopies == -1) {
						new PrinterFM01(pdf, GSettings.get("PRINTER_TWO")).print(new Integer(GSettings.get("PRINTER_TWO_COPIES")));
					}
					else if(printCopies > 0){
						new PrinterFM01(pdf, GSettings.get("PRINTER_TWO")).print(printCopies);
					}
					
					//new PrinterFM01(pdf, GSettings.get("PRINTER_ONE")).print(new Integer(GSettings.get("PRINTER_ONE_COPIES")));
					paths[i] = pathname;
					fileNames[i] = pdf.getName();
					// emis.persist(invoices[i]);
				}*/
				String pdf = pathToWrite + reference + ".pdf";
				if (printCopies == -1) {
					new PrinterFM01(new File(pdf), GSettings.get("PRINTER_TWO")).print(new Integer(GSettings.get("PRINTER_TWO_COPIES")));
				}
				else if(printCopies > 0){
					new PrinterFM01(new File(pdf), GSettings.get("PRINTER_TWO")).print(printCopies);
				}
				String[] recipients={};//new String[]{};
				
				if (!invoice.getClient().getEmail().equals("")) {
					recipients=(String[])ArrayUtils.addAll(recipients, invoice.getClient().getEmail().split(" "));
				}
				if (!invoice.getAgent().getEmail().equals("")) {
					recipients=(String[])ArrayUtils.addAll(recipients, invoice.getAgent().getEmail().split(" "));
				}
				
				if (recipients.length>0) {
					new Hotmail().send(
							"Cotizacion "+ reference
									+ " $" + invoice.getTotal(),
							GSettings.get("EMAIL_BODY"), recipients,
							new String[] { pathToWrite + reference + ".csv",
									pathToWrite + reference + ".pdf" },
							new String[] { reference + ".csv",
									reference + ".pdf" });
				}
				
				List<InvoiceItem> invoiceItems = invoice.getItems();
				for (int i = 0; i < invoiceItems.size(); i++) {
					InvoiceItem item = invoiceItems.get(i);
					if (Inventory.exists(item) && !item.isDisabled())
						Inventory.decrementStored(item);
				}
				try {
					DBObject dbObject = mongo.doFindOne(Mongoi.INVOICES, "{ \"reference\" : \"" + invoice.getReference() + "\"}");
					String resp = "{ \"invoice\" : " + dbObject.toString()
					+ ", \"successResponse\" : \"cotizado "+id+" por "+total+"\"}";
					log.exit("response",resp);
					response.getWriter().print(resp);
				} catch (IOException e) {
					log.trace("", e);
				}
				return;
			}
		});
		
	}
	
	private void calculateRelativeDiscount(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				JSONArray jList = null;
				try {
					jList = new JSONArray(URLDecoder.decode(parametersMap.get("list"),"utf-8"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				List<InvoiceItem> items = new LinkedList<InvoiceItem>();
				float D = (100 - new Float(parametersMap.get("absoluteDiscount")))/100;
				float total = 0;
				float gain = 0;
				float init = 0;
				Mongoi mongo = new Mongoi();
				for (int i = 0; i < jList.length(); i++) {
					InvoiceItem item = null;
					try {
						item = new Gson().fromJson(jList.getJSONObject(i).toString(), InvoiceItem.class);
					} catch (JsonSyntaxException | JSONException e) {
						e.printStackTrace();
					}
					DBObject dbo = mongo.doFindOne(Mongoi.PRODUCTS, "{\"code\" : \""+item.getCode()+"\"}");
					if(dbo==null){
						float t = item.getQuantity()*item.getUnitPrice();
						total += t;
						init += t;
					}
					else {
						float q = item.getQuantity();
						float j = new Float(dbo.get("unitPrice").toString());
						float t = new Float(dbo.get("providerOffer").toString());
						total += q * j;
						init += q * t;
						gain += q * (j - t);
					}
				}
				float d = Util.round2((1 - (D * total - init) / gain) * 100);
				String resp="{\"result\": "+d+"}";
				try {
					response.getWriter().print(resp);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		});
		
	}

	
	private void searchProviders(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				int searchRequestId=new Integer(parametersMap.get("searchRequestId"));
				String search=parametersMap.get("search");
				List<String> matchList = new ArrayList<String>();
				Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
				Matcher regexMatcher = regex.matcher(search);
				while (regexMatcher.find()) {
				    if (regexMatcher.group(1) != null) {
				        // Add double-quoted string without the quotes
				        matchList.add(regexMatcher.group(1));
				    } else if (regexMatcher.group(2) != null) {
				        // Add single-quoted string without the quotes
				        matchList.add(regexMatcher.group(2));
				    } else {
				        // Add unquoted word
				        matchList.add(regexMatcher.group());
				    }
				}
				String[] patterns=matchList.toArray(new String[1]);
				DBCursor cursor=new Mongoi()
				.doFindLike(Mongoi.PROVIDERS, new String[]{"fullName"}, patterns).limit(15);
				List<DBObject> list=cursor.toArray();
				List<DBObject> filteredList=FilterDBObject.keep(
						new String[]{"fullName"},list);
				if(searchRequestId<onlineClient.getRequestNumber()){
					try {
						response.getWriter().print("{\"result\" : [ ],\"requestNumber\" : \""+searchRequestId+"\"}");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				String resp="{\"result\":"+new Gson().toJson(filteredList)+",\"searchRequestID\":\""+searchRequestId+"\"} ";
				try {
					response.getWriter().print(resp);
					System.out.println(resp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	
	private void searchEInvoice(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				int searchRequestId=new Integer(parametersMap.get("searchRequestId"));
				//String 
				String search=parametersMap.get("search");
				List<String> matchList = new ArrayList<String>();
				Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
				Matcher regexMatcher = regex.matcher(search);
				while (regexMatcher.find()) {
				    if (regexMatcher.group(1) != null) {
				        // Add double-quoted string without the quotes
				        matchList.add(regexMatcher.group(1));
				    } else if (regexMatcher.group(2) != null) {
				        // Add single-quoted string without the quotes
				        matchList.add(regexMatcher.group(2));
				    } else {
				        // Add unquoted word
				        matchList.add(regexMatcher.group());
				    }
				}
				String[] patterns=matchList.toArray(new String[1]);
				DBCursor cursor=new Mongoi()
				.doFindLike(Mongoi.PROVIDERS, new String[]{"fullName"}, patterns).limit(15);
				List<DBObject> list=cursor.toArray();
				List<DBObject> filteredList=FilterDBObject.keep(
						new String[]{"fullName"},list);
				if(searchRequestId<onlineClient.getRequestNumber()){
					try {
						response.getWriter().print("{\"result\" : [ ],\"requestNumber\" : \""+searchRequestId+"\"}");
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				String resp="{\"result\":"+new Gson().toJson(filteredList)+",\"searchRequestID\":\""+searchRequestId+"\"} ";
				try {
					response.getWriter().print(resp);
					System.out.println(resp);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
	}
	
	private void doCommand(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient,
			Command command){
		Log log = new Log();
		log.entry(parameters);
		log.entry(request.getParameterMap());
		try{
			if(!onlineClient.isAuthenticated(request)){
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"ACCESS_DENIED"));
				return;
			}
			boolean hasPermission=onlineClient.hasAccess(permissions);
			if(!hasPermission){
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(
						fromLang(onlineClient.getLocale(),"ACCESS_DENIED")+", "+
						fromLang(
								onlineClient.getLocale(),
								"PERMISSION_REQUIRED",
								Arrays.toString(permissions)));
				return;
			}
			Map<String,String> parametersMap= new HashMap<String, String>();
			for(int i=0;i<parameters.length;i++){
				if(requestHasParameter(request, parameters[i])){
					String pd=URLDecoder.decode(request.getParameter(parameters[i]),"utf-8");
					parametersMap.put(parameters[i], pd);
				}
				else{
					response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS",parameters[i]));
					return;
				}
			}
			log.info("executing");
			command.execute(parametersMap, response, onlineClient);
			return;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void methodTemplate(HttpServletRequest request, HttpServletResponse response,OnlineClient onlineClient){
		try{
			if(
					!onlineClient.isAuthenticated(request)&&!(
					onlineClient.hasAccess(AccessPermission.AGENT_INCREMENT_EARNINGS)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					)){
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"ACCESS_DENIED"));
				return;
			}
			if(requestHasParameter(request, "args")){
				String args=request.getParameter("args");
				if(!args.equals("")){
					String[] argsspl=URLDecoder.decode(args,"utf-8").split(" ");
				}
				else{
					response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
					return;
				}
			}
			else{
				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
				return;
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void updateProducts(HttpServletRequest req, HttpServletResponse resp,OnlineClient onlineClient){
		try{
			resp.setCharacterEncoding("utf-8");
			resp.setContentType("application/json");
			int clientReference=new Integer(req.getParameter("clientReference"));
			if(!(onlineClient.isAuthenticated(req)&&(
					onlineClient.hasAccess(AccessPermission.PRODUCT_UPDATE)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					))){
				resp.sendError(resp.SC_UNAUTHORIZED,"acceso denegado");return;
			}
			try {
				resp.getWriter().print(Updater.update(req,resp));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private String fromLang(String locale, String key, String... args){
		return Langed.get(locale, key, args);
	}
	private void emitInvoiceTicket(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				Log log = new Log();
				Gson gson = new Gson();
				Mongoi mongo = new Mongoi();
				JSONObject jClient = null;
				JSONObject jAgent = null;
				Client client = null;
				Client agent = null;
				try {
					jClient = new JSONObject(parametersMap.get("client"));
					jAgent = new JSONObject(parametersMap.get("agent"));
					client = ClientFactory.create(jClient);
					agent = ClientFactory.create(jAgent);
				} catch (JSONException e1) {
					log.trace(e1);
				}
				JSONArray jList = null;
				try {
					jList = new JSONArray(parametersMap.get("list"));
				} catch (JSONException e) {
					log.trace(e);
				} 
				List<InvoiceItem> list = new LinkedList<InvoiceItem>();
				for (int i = jList.length() - 1; i >= 0; i--) {
					InvoiceItem item = null;
					try {
						log.object("parsing item",jList.getJSONObject(i).toString());
						item = gson.fromJson(jList.getJSONObject(i).toString(), InvoiceItem.class);
					} catch (JsonSyntaxException | JSONException e) {
						log.trace(e);
					}
					list.add(item);
				}
				Shopman shopman = OnlineClients.instance().get(new Integer(parametersMap.get("clientReference"))).getShopman();
				
				Destiny destiny = gson.fromJson(parametersMap.get("destiny"), Destiny.class);
				String clientReference = parametersMap.get("clientReference");
				String cfdiUse = parametersMap.get("cfdiUse");
				String paymentMethod = parametersMap.get("paymentMethod");
				String paymentWay = parametersMap.get("paymentWay");
				String documentType = parametersMap.get("documentType");
				int printCopies = -1;
				if(!(parametersMap.get("printCopies")==null||parametersMap.get("printCopies").equals(""))){
					printCopies = new Integer(parametersMap.get("printCopies"));
				}
				String coin = parametersMap.get("coin");
				String series = GSettings.get("INVOICE_SERIAL");
				if(paymentMethod==null || paymentMethod.equals("")){
					try {
						response.sendError(response.SC_NOT_ACCEPTABLE, fromLang(onlineClient.getLocale(),"CODE_5"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				if(paymentWay==null || paymentWay.equals("")){
					try {
						response.sendError(response.SC_NOT_ACCEPTABLE, fromLang(onlineClient.getLocale(),"CODE_6"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				if(cfdiUse!=null&&!cfdiUse.equals("")){
					mongo.doUpdate(Mongoi.CLIENTS, "{ \"code\" : \""+client.getCode()+"\"}", "{\"cfdiUse\" : \""+cfdiUse+"\" }"+"}");
					client.setCfdiUse(cfdiUse);
				}
				else {
					try {
						response.sendError(response.SC_NOT_ACCEPTABLE, fromLang(onlineClient.getLocale(),"CODE_7"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				
				int count=mongo.doIncrement(Mongoi.REFERENCE, "{ \"reference\" : \"unique\" }", "count");
				String reference = count+"";
				Invoice invoice = new InvoiceFM01(
						client, shopman, list, reference, series, destiny,
						agent, paymentMethod, paymentWay, documentType, coin);
				invoice.persist();
				ElectronicInvoice electronicInvoice=new Profact(invoice);
				boolean production=!new Boolean(GSettings.get("TEST"));
				PACResponse pacResponse=electronicInvoice.submit(production);
				
				if (pacResponse.isSuccess()) {
					mongo.doUpdate(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\" }",
							"{ \"hasElectronicVersion\" : true }");
					InvoiceLog facture = new InvoiceLog(LogKind.FACTURE, true,
							onlineClient.getShopman().getLogin());
					mongo.doPush(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\"}",
							"{\"logs\" : " + new Gson().toJson(facture) + " }");
					
					String xml = pacResponse.getContent();
					String xmlEscaped=StringEscapeUtils.escapeXml(xml);
					
					String pathToWrite = GSettings.getPathToDirectory(GSettings.get("INVOICES_FOLDER")+"-"+Utils.getDateString("yyyy-MM"));
					String pdf = pathToWrite + reference + ".pdf";
					String 	id=invoice.getReference(),
							xsltPath=GSettings.getPathTo("XSLT_INVOICE"),
							xmlPath=pathToWrite+id+".xml",
							htmlPath=pathToWrite+id+".html",
							pdfPath=pathToWrite+id+".pdf",
							xsltTicketPath=GSettings.getPathTo("XSLT_INVOICE_TICKET"),
							htmlTicketPath=pathToWrite+id+".ticket.html",
							pdfTicketPath=pathToWrite+id+".ticket.pdf",
							additionalData=GSettings.get("INVOICE_SENDER_ADDITIONAL_DATA");
					log.object("xmlEscaped is : ",xmlEscaped);
					//String reference=invoice.getReference();
					//ElectronicInvoiceFactory.saveCFDI(xml, reference, pathToWrite);
					Utils.saveStringToFile(xml, xmlPath);
					ElectronicInvoiceFactory.genQRCode(reference, pathToWrite);
					Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData,destiny.getAddress());
					Utils.HTML_TO_PDF(htmlPath, pdfPath, "Factura "+GSettings.get("INVOICE_SERIAL")+" "+id+" - pagina [page]/[topage]", "");
					
					Utils.XML_TO_HTML(xmlPath, xsltTicketPath, htmlTicketPath, additionalData,destiny.getAddress());
					Utils.HTML_TO_PDF_TICKET(htmlTicketPath, pdfTicketPath);
					
	
					//ElectronicInvoiceFactory.genHTML(reference, pathToWrite);
					//ElectronicInvoiceFactory.genPDF(reference, pathToWrite);
					
					org.jsoup.nodes.Document document=null;
					document = Jsoup.parse(xml);
					
					String total=document.select("cfdi|Comprobante").get(0).attr("Total");
					String subTotal=document.select("cfdi|Comprobante").get(0).attr("SubTotal");
					String taxes=document.select("cfdi|Comprobante > cfdi|Impuestos").get(0).attr("TotalImpuestosTrasladados");
					
					mongo.doUpdate(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\" }",
							"{ \"electronicVersion\" : {\"xml\" : \"" + xmlEscaped + "\"} }");
					mongo.doUpdate(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\" }",
							"{ \"hasElectronicVersion\" : true }");
					
					String csv = pathToWrite + invoice.getReference() +".csv";
					FileWriter fstream;
					try {
						fstream = new FileWriter(csv);
					
						BufferedWriter out = new BufferedWriter(fstream);
						// out.write(invoice.getConsummer() + "\n" +
						// invoice.getAddress() + "\n" + invoice.getCity() + "\n"
						// + invoice.getCp() + "\n" + invoice.getRfc() + "\n");
						
						Elements elements = document.select("cfdi|Comprobante > cfdi|Conceptos > cfdi|Concepto");
						for(int i = 0 ; i < elements.size(); i ++){
							Element element = elements.get(i);
							String prodservCode = element.attr("ClaveProdServ");
							String quantity = element.attr("Cantidad");
							String unitCode = element.attr("ClaveUnidad");
							String unit = element.attr("Unidad");
							String description = element.attr("Descripcion");
							String unitPrice = element.attr("ValorUnitario");
							String itotal = element.attr("Importe");
							out.write(
									quantity + "\t" +
											prodservCode + "\t" +
											unitCode + "\t" +
											unit + "\t" +
											description + "\t" +
											unitPrice + "\t" +
											itotal + "\n"
									);
							
						}
						/*List<InvoiceItem> items2 = invoice.getItems();
						for (InvoiceItem item : items2) {
							out.write(item.getQuantity() + "\t" + item.getCode() + "\t" + item.getUnit() + "\t"
									+ item.getDescription() + "\t" + item.getMark() + "\t" + item.getUnitPrice() + "\n");
						}*/
						out.write("subtotal\t" + subTotal);
						out.write("\niva\t" + taxes);
						out.write("\ntotal\t" + total);
						// Close the output stream
						out.close();
						Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","chmod 444 "+csv});
					} catch (IOException e1) {
						log.trace(e1);
					}
					
					
					if (printCopies == -1) {
						new PrinterFM01(new File(pdfTicketPath), GSettings.get("PRINTER_THREE"))
						.print(GSettings.get("PRINTER_THREE_OPTIONS")+" -# "+GSettings.get("PRINTER_THREE_COPIES"));
					}
					else if(printCopies > 0){
						new PrinterFM01(new File(pdfTicketPath), GSettings.get("PRINTER_TRHEE"))
						.print(GSettings.get("PRINTER_THREE_OPTIONS")+" -# "+printCopies);
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
								"factura "+ GSettings.get("INVOICE_SERIAL") + " " + reference
										+ " $" + total,
								GSettings.get("EMAIL_BODY"), recipients,
								new String[] { 
										pathToWrite + reference + ".csv",
										pathToWrite + reference + ".xml",
										pathToWrite + reference + ".pdf" },
								new String[] {
										reference + ".csv",
										reference + ".xml",
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
					
					/*TheBox.instance().plus(invoice.getTotal());
					TheBox.instance()
							.addLog(new TheBoxLog(invoice.getTotal(), paymentLog.getDate(),
									invoice.getReference(), LogKind.PAYMENT.toString(),
									onlineClient.getShopman().getLogin()));*/
					List<InvoiceItem> invoiceItems = invoice.getItems();
					for (int i = 0; i < invoiceItems.size(); i++) {
						InvoiceItem item = invoiceItems.get(i);
						if (Inventory.exists(item) && !item.isDisabled())
							Inventory.decrementStored(item);
					}
					try {
						DBObject dbObject = mongo.doFindOne(Mongoi.INVOICES, "{ \"reference\" : \"" + invoice.getReference() + "\"}");
						String resp = "{ \"invoice\" : " + dbObject.toString()
						+ ", \"successResponse\" : \"facturado "+reference+" por "+total+"\"}";
						log.exit("response",resp);
						response.getWriter().print(resp);
					} catch (IOException e) {
						log.trace("", e);
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
					/*InvoiceLog cancelLog = new InvoiceLog(LogKind.CANCEL, true,
							onlineClient.getShopman().getLogin());
					mongo.doPush(Mongoi.INVOICES,
							"{ \"reference\" : \"" + invoice.getReference() + "\"}",
							"{\"logs\" : " + new Gson().toJson(cancelLog) + " }");
					*/
					
					return;
				}
			}
		});
		
	}
	private void emitOrderTicket(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				Log log = new Log();
				Gson gson = new Gson();
				Mongoi mongo = new Mongoi();
				JSONObject jClient = null;
				JSONObject jAgent = null;
				Client client = null;
				Client agent = null;
				try {
					jClient = new JSONObject(parametersMap.get("client"));
					jAgent = new JSONObject(parametersMap.get("agent"));
					client = ClientFactory.create(jClient);
					agent = ClientFactory.create(jAgent);
				} catch (JSONException e1) {
					log.trace(e1);
				}
				JSONArray jList = null;
				try {
					jList = new JSONArray(parametersMap.get("list"));
				} catch (JSONException e) {
					log.trace(e);
				} 
				List<InvoiceItem> list = new LinkedList<InvoiceItem>();
				for (int i = jList.length() - 1; i >= 0; i--) {
					InvoiceItem item = null;
					try {
						log.object("parsing item",jList.getJSONObject(i).toString());
						item = gson.fromJson(jList.getJSONObject(i).toString(), InvoiceItem.class);
					} catch (JsonSyntaxException | JSONException e) {
						log.trace(e);
					}
					list.add(item);
				}
				Shopman shopman = OnlineClients.instance().get(new Integer(parametersMap.get("clientReference"))).getShopman();
				
				Destiny destiny = gson.fromJson(parametersMap.get("destiny"), Destiny.class);
				String clientReference = parametersMap.get("clientReference");
				String cfdiUse = "G01";
				String paymentMethod = "PUE";
				String paymentWay = "99";
				String documentType = parametersMap.get("documentType");
				int printCopies = -1;
				if(!(parametersMap.get("printCopies")==null||parametersMap.get("printCopies").equals(""))){
					printCopies = new Integer(parametersMap.get("printCopies"));
				}
				String coin = parametersMap.get("coin");
				String series = GSettings.get("INVOICE_SERIAL");
				if(cfdiUse!=null&&!cfdiUse.equals("")){
					mongo.doUpdate(Mongoi.CLIENTS, "{ \"code\" : \""+client.getCode()+"\"}", "{\"cfdiUse\" : \""+cfdiUse+"\" }"+"}");
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
				int count=mongo.doIncrement(Mongoi.REFERENCE, "{ \"reference\" : \"unique\" }", "count");
				String reference = count+"";
				Invoice invoice = new InvoiceFM01(
						client, shopman, list, reference, series, destiny,
						agent, paymentMethod, paymentWay, documentType, coin);
				invoice.persist();
				Invoice[] invoices = invoice.subdivide(InvoiceFormFM01.ROWS_NUMBER);
				
				String[] paths = new String[invoices.length + 1];
				String[] fileNames = new String[invoices.length + 1];
				String pathToWrite = GSettings.getPathToDirectory(GSettings.get("ORDERS_FOLDER")+"-"+Utils.getDateString("yyyy-MM"));
				
				String xml=Utils.GEN_CFD_STRING(invoice);
				String 	id=invoice.getReference(),
						xsltPath=GSettings.getPathTo("XSLT_ORDER"),
						xmlPath=pathToWrite+id+".xml",
						htmlPath=pathToWrite+id+".html",
						pdfPath=pathToWrite+id+".pdf",
						xsltTicketPath=GSettings.getPathTo("XSLT_ORDER_TICKET"),
						htmlTicketPath=pathToWrite+id+".ticket.html",
						pdfTicketPath=pathToWrite+id+".ticket.pdf",
						additionalData=GSettings.get("INVOICE_SENDER_ADDITIONAL_DATA");
				Utils.saveStringToFile(xml, xmlPath);						
				Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData,destiny.getAddress());
				Utils.HTML_TO_PDF(htmlPath, pdfPath, "Pedido "+id+" - pagina [page]/[topage]", "");
				
				Utils.XML_TO_HTML(xmlPath, xsltTicketPath, htmlTicketPath, additionalData,destiny.getAddress());
				Utils.HTML_TO_PDF_TICKET(htmlTicketPath, pdfTicketPath);
				
				String csv = pathToWrite + invoice.getReference() +".csv";
				org.jsoup.nodes.Document document=null;
				document = Jsoup.parse(xml);
				String total=document.select("cfdi|Comprobante").get(0).attr("Total");
				String subTotal=document.select("cfdi|Comprobante").get(0).attr("SubTotal");
				String taxes=document.select("cfdi|Comprobante > cfdi|Impuestos").get(0).attr("TotalImpuestosTrasladados");
	
	
				FileWriter fstream;
				try {
					fstream = new FileWriter(csv);
				
					BufferedWriter out = new BufferedWriter(fstream);
					// out.write(invoice.getConsummer() + "\n" +
					// invoice.getAddress() + "\n" + invoice.getCity() + "\n"
					// + invoice.getCp() + "\n" + invoice.getRfc() + "\n");
					
					Elements elements = document.select("cfdi|Comprobante > cfdi|Conceptos > cfdi|Concepto");
					for(int i = 0 ; i < elements.size(); i ++){
						Element element = elements.get(i);
						String prodservCode = element.attr("ClaveProdServ");
						String quantity = element.attr("Cantidad");
						String unitCode = element.attr("ClaveUnidad");
						String unit = element.attr("Unidad");
						String description = element.attr("Descripcion");
						String unitPrice = element.attr("ValorUnitario");
						String itotal = element.attr("Importe");
						out.write(
								quantity + "\t" +
										prodservCode + "\t" +
										unitCode + "\t" +
										unit + "\t" +
										description + "\t" +
										unitPrice + "\t" +
										itotal + "\n"
								);
						
					}
					/*List<InvoiceItem> items2 = invoice.getItems();
					for (InvoiceItem item : items2) {
						out.write(item.getQuantity() + "\t" + item.getCode() + "\t" + item.getUnit() + "\t"
								+ item.getDescription() + "\t" + item.getMark() + "\t" + item.getUnitPrice() + "\n");
					}*/
					out.write("subtotal\t" + subTotal);
					out.write("\niva\t" + taxes);
					out.write("\ntotal\t" + total);
					// Close the output stream
					out.close();
					Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","chmod 444 "+csv});
				} catch (IOException e1) {
					log.trace(e1);
				}
				
				/*
				for (int i = 0; i < invoices.length; i++) {
					String pathname = pathToWrite + invoices[i].getReference() + "-" + i+".pdf";
					
					File pdf = new PDF(invoices[i], pathname).make();
					try {
						Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","chmod 444 "+pathname});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// System.out.println("invoices["+i+"]:
					// "+invoices[i].toJson());
					// TODO this is printing time
					if (printCopies == -1) {
						new PrinterFM01(pdf, GSettings.get("PRINTER_ONE")).print(new Integer(GSettings.get("PRINTER_ONE_COPIES")));
					}
					else if(printCopies > 0){
						new PrinterFM01(pdf, GSettings.get("PRINTER_ONE")).print(printCopies);
					}
					
					//new PrinterFM01(pdf, GSettings.get("PRINTER_ONE")).print(new Integer(GSettings.get("PRINTER_ONE_COPIES")));
					paths[i] = pathname;
					fileNames[i] = pdf.getName();
					// emis.persist(invoices[i]);
				}*/
				
				if (printCopies == -1) {
					new PrinterFM01(new File(pdfTicketPath), GSettings.get("PRINTER_THREE"))
					.print(GSettings.get("PRINTER_THREE_OPTIONS")+" -# "+GSettings.get("PRINTER_THREE_COPIES"));
				}
				else if(printCopies > 0){
					new PrinterFM01(new File(pdfTicketPath), GSettings.get("PRINTER_TRHEE"))
					.print(GSettings.get("PRINTER_THREE_OPTIONS")+" -# "+printCopies);
				}
				
				String[] recipients={};//new String[]{};
				
				if (!invoice.getClient().getEmail().equals("")) {
					recipients=(String[])ArrayUtils.addAll(recipients, invoice.getClient().getEmail().split(" "));
				}
				if (!invoice.getAgent().getEmail().equals("")) {
					recipients=(String[])ArrayUtils.addAll(recipients, invoice.getAgent().getEmail().split(" "));
				}
				if (recipients.length>0) {
					new Hotmail().send(
							"Pedido "+ reference
									+ " $" + invoice.getTotal(),
							GSettings.get("EMAIL_BODY"), recipients,
							new String[] { pathToWrite + reference + ".csv",
									pathToWrite + reference + ".pdf" },
							new String[] { reference + ".csv",
									reference + ".pdf" });
				}
				
				List<InvoiceItem> invoiceItems = invoice.getItems();
				for (int i = 0; i < invoiceItems.size(); i++) {
					InvoiceItem item = invoiceItems.get(i);
					if (Inventory.exists(item) && !item.isDisabled())
						Inventory.decrementStored(item);
				}
				try {
					DBObject dbObject = mongo.doFindOne(Mongoi.INVOICES, "{ \"reference\" : \"" + invoice.getReference() + "\"}");
					String resp = "{ \"invoice\" : " + dbObject.toString()
					+ ", \"successResponse\" : \"pedido "+id+" por "+total+"\"}";
					log.exit("response",resp);
					response.getWriter().print(resp);
				} catch (IOException e) {
					log.trace("", e);
				}
				return;
				
				/*
				ElectronicInvoice electronicInvoice=new Profact(invoice);
				boolean production=!new Boolean(GSettings.get("TEST"));
				PACResponse pacResponse=electronicInvoice.submit(production);
				
				if (pacResponse.isSuccess()) {
					String xml = pacResponse.getContent();
					String xmlEscaped=StringEscapeUtils.escapeXml(xml);
					
					String pathToWrite = GSettings.getPathToDirectory(GSettings.get("INVOICES_FOLDER")+"-"+Utils.getDateString("yyyy-MM"));
					String pdf = pathToWrite + reference + ".pdf";
					
					log.object("xmlEscaped is : ",xmlEscaped);
					//String reference=invoice.getReference();
					ElectronicInvoiceFactory.saveCFDI(xml, reference, pathToWrite);
					ElectronicInvoiceFactory.genQRCode(reference, pathToWrite);
					ElectronicInvoiceFactory.genHTML(reference, pathToWrite);
					ElectronicInvoiceFactory.genPDF(reference, pathToWrite);
					
					org.jsoup.nodes.Document document=null;
					document = Jsoup.parse(xml);
					
					String total=document.select("cfdi|Comprobante").get(0).attr("Total");
					String subTotal=document.select("cfdi|Comprobante").get(0).attr("SubTotal");
					String taxes=document.select("cfdi|Comprobante > cfdi|Impuestos").get(0).attr("TotalImpuestosTrasladados");
					
					mongo.doUpdate(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\" }",
							"{ \"electronicVersion\" : {\"xml\" : \"" + xmlEscaped + "\"} }");
					mongo.doUpdate(Mongoi.INVOICES,
							"{ \"reference\" : \"" + reference + "\" }",
							"{ \"hasElectronicVersion\" : true }");
					if (printCopies == -1) {
						new PrinterFM01(new File(pdf), GSettings.get("PRINTER_TWO")).print(new Integer(GSettings.get("PRINTER_TWO_COPIES")));
					}
					else if(printCopies > 0){
						new PrinterFM01(new File(pdf), GSettings.get("PRINTER_TWO")).print(printCopies);
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
								new String[] { pathToWrite + reference + ".xml",
										pathToWrite + reference + ".pdf" },
								new String[] { reference + ".xml",
										reference + ".pdf" });
					}
	
					List<InvoiceItem> invoiceItems = invoice.getItems();
					for (int i = 0; i < invoiceItems.size(); i++) {
						InvoiceItem item = invoiceItems.get(i);
						if (Inventory.exists(item) && !item.isDisabled())
							Inventory.decrementStored(item);
					}
					try {
						DBObject dbObject = mongo.doFindOne(Mongoi.INVOICES, "{ \"reference\" : \"" + invoice.getReference() + "\"}");
						String resp = "{ \"invoice\" : " + dbObject.toString()
						+ ", \"successResponse\" : \"facturado\"}";
						log.exit("response",resp);
						response.getWriter().print(resp);
					} catch (IOException e) {
						log.trace("", e);
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
			
					
					return;
				}*/
			}
		});
		
	}
	private void printDocument(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				Log log = new Log();
				Gson gson = new Gson();
				Mongoi mongo = new Mongoi();
				
				String reference = parametersMap.get("reference");

				
				DBObject dbInvoice = mongo.doFindOne("invoices", "{\"reference\" : \""+reference+"\"}");
				Invoice invoice = null;
				if(dbInvoice!=null)
					invoice = gson.fromJson(dbInvoice.toString(), InvoiceFM01.class);/*new InvoiceFM01(
						client, shopman, list, reference, series, destiny,
						agent, paymentMethod, paymentWay, documentType, coin);*/
				else {
					try {
						response.sendError(response.SC_NOT_FOUND, fromLang(onlineClient.getLocale(), "REQUESTED_DOCUMENT_NOT_FOUND", reference));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				Date createdDate = new Date(invoice.getCreationTime());
				String pathToWrite = null,
						printer = null,
						printerOptions = null,
						pdfPath = null,
						xsltPath = null,
						xmlPath = null,
						htmlPath = null,
						additionalData = GSettings.get("INVOICE_SENDER_ADDITIONAL_DATA"),
						destiny = invoice.getDestiny().getAddress(),
						documentType = invoice.getDocumentType(),
						printCopies = parametersMap.get("printCopies");
				if(documentType.equals("I")){
					pathToWrite = GSettings.getPathToDirectory(GSettings.get("INVOICES_FOLDER")+"-"+Utils.getDateString(createdDate,"yyyy-MM"));
					xsltPath = GSettings.getPathTo("XSLT_INVOICE");
					printer = GSettings.get("INVOICE_PRINTER");
					printerOptions = GSettings.get("INVOICE_PRINTER_OPTIONS");
				}
				else if(documentType.equals("ORDER")){
					pathToWrite = GSettings.getPathToDirectory(GSettings.get("ORDERS_FOLDER")+"-"+Utils.getDateString(createdDate,"yyyy-MM"));
					xsltPath = GSettings.getPathTo("XSLT_ORDER");
					printer = GSettings.get("ORDER_PRINTER");
					printerOptions = GSettings.get("ORDER_PRINTER_OPTIONS");
				}
				else if(documentType.equals("SAMPLE")){
					pathToWrite = GSettings.getPathToDirectory(GSettings.get("SAMPLES_FOLDER")+"-"+Utils.getDateString(createdDate,"yyyy-MM"));
					xsltPath = GSettings.getPathTo("XSLT_SAMPLE");
					printer = GSettings.get("SAMPLE_PRINTER");
					printerOptions = GSettings.get("SAMPLE_PRINTER_OPTIONS");
				}
				
				xmlPath = pathToWrite+reference+".xml";
				htmlPath = pathToWrite+reference+".html";
				pdfPath = pathToWrite+reference+".pdf";
				if(new File(pdfPath).exists()){
					new PrinterFM01(new File(pdfPath), printer)
					.print(printerOptions+" -# "+printCopies);
					String resp = "{ " + 
					"\"successResponse\" : \"reimpreso "+reference+"\" }";
					log.exit("response",resp);
					try {
						response.getWriter().print(resp);
					} catch (IOException e) {
					}
					return;
				}
				else{
					if(invoice.hasElectronicVersion()&&!(new File(xmlPath).exists())){
						try {
							response.sendError(response.SC_PARTIAL_CONTENT, Langed.get(onlineClient.getLocale(), "CODE_1"));
						} catch (IOException e) {
						}
						return;
					}
					else if(invoice.hasElectronicVersion()){
						Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData,destiny);
						Utils.HTML_TO_PDF(htmlPath, pdfPath, "Factura "+GSettings.get("INVOICE_SERIAL")+" "+reference+" - pagina [page]/[topage]", "");
						new PrinterFM01(new File(pdfPath), printer)
						.print(printerOptions+" -# "+printCopies);
						return;
					}
					try {
						response.sendError(response.SC_NOT_FOUND, Langed.get(onlineClient.getLocale(), "CODE_2", reference));
					} catch (IOException e) {
					}
					return;
				}
			}
		});
		
	}
	
	private void printDocumentTicket(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				Log log = new Log();
				Gson gson = new Gson();
				Mongoi mongo = new Mongoi();
				
				String reference = parametersMap.get("reference");

				
				DBObject dbInvoice = mongo.doFindOne("invoices", "{\"reference\" : \""+reference+"\"}");
				Invoice invoice = null;
				if(dbInvoice!=null)
					invoice = gson.fromJson(dbInvoice.toString(), InvoiceFM01.class);/*new InvoiceFM01(
						client, shopman, list, reference, series, destiny,
						agent, paymentMethod, paymentWay, documentType, coin);*/
				else {
					try {
						response.sendError(response.SC_NOT_FOUND, fromLang(onlineClient.getLocale(), "REQUESTED_DOCUMENT_NOT_FOUND", reference));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				Date createdDate = new Date(invoice.getCreationTime());
				String pathToWrite = null,
						printer = null,
						printerOptions = null,
						pdfPath = null,
						xsltPath = null,
						xmlPath = null,
						htmlPath = null,
						additionalData = GSettings.get("INVOICE_SENDER_ADDITIONAL_DATA"),
						destiny = invoice.getDestiny().getAddress(),
						documentType = invoice.getDocumentType(),
						printCopies = parametersMap.get("printCopies");
				if(documentType.equals("I")){
					pathToWrite = GSettings.getPathToDirectory(GSettings.get("INVOICES_FOLDER")+"-"+Utils.getDateString(createdDate,"yyyy-MM"));
					xsltPath = GSettings.getPathTo("XSLT_INVOICE_TICKET");
					printer = GSettings.get("INVOICE_PRINTER_TICKET");
					printerOptions = GSettings.get("INVOICE_PRINTER_TICKET_OPTIONS");
				}
				else if(documentType.equals("ORDER")){
					pathToWrite = GSettings.getPathToDirectory(GSettings.get("ORDERS_FOLDER")+"-"+Utils.getDateString(createdDate,"yyyy-MM"));
					xsltPath = GSettings.getPathTo("XSLT_ORDER_TICKET");
					printer = GSettings.get("ORDER_PRINTER_TICKET");
					printerOptions = GSettings.get("ORDER_PRINTER_TICKET_OPTIONS");
				}
				else if(documentType.equals("SAMPLE")){
					pathToWrite = GSettings.getPathToDirectory(GSettings.get("SAMPLES_FOLDER")+"-"+Utils.getDateString(createdDate,"yyyy-MM"));
					xsltPath = GSettings.getPathTo("XSLT_SAMPLE_TICKET");
					printer = GSettings.get("SAMPLE_PRINTER_TICKET");
					printerOptions = GSettings.get("SAMPLE_PRINTER_TICKET_OPTIONS");
				}
				
				xmlPath = pathToWrite+reference+".xml";
				htmlPath = pathToWrite+reference+".ticket.html";
				pdfPath = pathToWrite+reference+".ticket.pdf";
				if(new File(pdfPath).exists()){
					new PrinterFM01(new File(pdfPath), printer)
					.print(printerOptions+" -# "+printCopies);
					String resp = "{ " + 
					"\"successResponse\" : \"reimpreso "+reference+"\" }";
					log.exit("response",resp);
					try {
						response.getWriter().print(resp);
					} catch (IOException e) {
					}
					return;
				}
				else{
					if(invoice.hasElectronicVersion()&&!(new File(xmlPath).exists())){
						try {
							response.sendError(response.SC_PARTIAL_CONTENT, Langed.get(onlineClient.getLocale(), "CODE_1"));
						} catch (IOException e) {
						}
						return;
					}
					else if(invoice.hasElectronicVersion()){
						Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData,destiny);
						Utils.HTML_TO_PDF(htmlPath, pdfPath, "Factura "+GSettings.get("INVOICE_SERIAL")+" "+reference+" - pagina [page]/[topage]", "");
						new PrinterFM01(new File(pdfPath), printer)
						.print(printerOptions+" -# "+printCopies);
						return;
					}
					try {
						response.sendError(response.SC_NOT_FOUND, Langed.get(onlineClient.getLocale(), "CODE_2", reference));
					} catch (IOException e) {
					}
					return;
				}
			}
		});
		
	}
	
	private void emitSampleTicket(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				Log log = new Log();
				Gson gson = new Gson();
				Mongoi mongo = new Mongoi();
				JSONObject jClient = null;
				JSONObject jAgent = null;
				Client client = null;
				Client agent = null;
				GSettings setts = GSettings.instance();
				try {
					jClient = new JSONObject(parametersMap.get("client"));
					jAgent = new JSONObject(parametersMap.get("agent"));
					client = ClientFactory.create(jClient);
					agent = ClientFactory.create(jAgent);
				} catch (JSONException e1) {
					log.trace(e1);
				}
				JSONArray jList = null;
				try {
					jList = new JSONArray(parametersMap.get("list"));
				} catch (JSONException e) {
					log.trace(e);
				} 
				List<InvoiceItem> list = new LinkedList<InvoiceItem>();
				for (int i = jList.length() - 1; i >= 0; i--) {
					InvoiceItem item = null;
					try {
						log.object("parsing item",jList.getJSONObject(i).toString());
						item = gson.fromJson(jList.getJSONObject(i).toString(), InvoiceItem.class);
					} catch (JsonSyntaxException | JSONException e) {
						log.trace(e);
					}
					list.add(item);
				}
				Shopman shopman = OnlineClients.instance().get(new Integer(parametersMap.get("clientReference"))).getShopman();
				
				Destiny destiny = gson.fromJson(parametersMap.get("destiny"), Destiny.class);
				String clientReference = parametersMap.get("clientReference");
				String cfdiUse = "G01";
				String paymentMethod = "PUE";
				String paymentWay = "99";
				String documentType = parametersMap.get("documentType");
				int printCopies = -1;
				if(!(parametersMap.get("printCopies")==null||parametersMap.get("printCopies").equals(""))){
					printCopies = new Integer(parametersMap.get("printCopies"));
				}
				String coin = parametersMap.get("coin");
				String series = setts.get("INVOICE_SERIAL");
				if(cfdiUse!=null&&!cfdiUse.equals("")){
					mongo.doUpdate(Mongoi.CLIENTS, "{ \"code\" : \""+client.getCode()+"\"}", "{\"cfdiUse\" : \""+cfdiUse+"\" }"+"}");
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
				int count=mongo.doIncrement(Mongoi.REFERENCE, "{ \"reference\" : \"unique\" }", "count");
				String reference = count+"";
				Invoice invoice = new InvoiceFM01(
						client, shopman, list, reference, series, destiny,
						agent, paymentMethod, paymentWay, documentType, coin);
				invoice.persist();
				Invoice[] invoices = invoice.subdivide(InvoiceFormFM01.ROWS_NUMBER);
				
				//String[] paths = new String[invoices.length + 1];
				//String[] fileNames = new String[invoices.length + 1];
				String pathToWrite = GSettings.getPathToDirectory(GSettings.get("SAMPLES_FOLDER")+"-"+Utils.getDateString("yyyy-MM"));
				
				String xml=Utils.GEN_CFD_STRING(invoice);
				String 	id=invoice.getReference(),
						xsltPath=setts.getPathTo("XSLT_SAMPLE"),
						xmlPath=pathToWrite+id+".xml",
						htmlPath=pathToWrite+id+".html",
						pdfPath=pathToWrite+id+".pdf",
								xsltTicketPath=setts.getPathTo("XSLT_SAMPLE_TICKET"),
								htmlTicketPath=pathToWrite+id+".ticket.html",
								pdfTicketPath=pathToWrite+id+".ticket.pdf",
						additionalData=setts.get("INVOICE_SENDER_ADDITIONAL_DATA");
				Utils.saveStringToFile(xml, xmlPath);						
				Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData,destiny.getAddress());
				Utils.HTML_TO_PDF(htmlPath, pdfPath, "Cotizacion "+id+" - pagina [page]/[topage]", "");
				
				Utils.XML_TO_HTML(xmlPath, xsltTicketPath, htmlTicketPath, additionalData,destiny.getAddress());
				Utils.HTML_TO_PDF_TICKET(htmlTicketPath, pdfTicketPath);
				
				
				String csv = pathToWrite + invoice.getReference() +".csv";
				org.jsoup.nodes.Document document=null;
				document = Jsoup.parse(xml);
				String total=document.select("cfdi|Comprobante").get(0).attr("Total");
				String subTotal=document.select("cfdi|Comprobante").get(0).attr("SubTotal");
				String taxes=document.select("cfdi|Comprobante > cfdi|Impuestos").get(0).attr("TotalImpuestosTrasladados");
				
				
				/*if(new Boolean(setts.get("BLOCKCHAIN_ENABLED"))){
					DBObject dbObject = mongo.doFindOne(Mongoi.INVOICES, "{\"reference\" : \""+reference+"\"}");
					Blockchain blockchain = Blockchain.instance();
					String query =
							"{\"method\" : \"publish\","
							+"\"params\" : [\""+GSettings.get("BLOCKCHAIN_STREAM_NAME")+
							"\", [\""+reference+"\"], {\"json\" : "+dbObject.toString()+"}], \"id\" : "+id+"}";
					String result = blockchain.query(query);
				}*/
				FileWriter fstream;
				try {
					fstream = new FileWriter(csv);
				
					BufferedWriter out = new BufferedWriter(fstream);
					// out.write(invoice.getConsummer() + "\n" +
					// invoice.getAddress() + "\n" + invoice.getCity() + "\n"
					// + invoice.getCp() + "\n" + invoice.getRfc() + "\n");
					
					Elements elements = document.select("cfdi|Comprobante > cfdi|Conceptos > cfdi|Concepto");
					for(int i = 0 ; i < elements.size(); i ++){
						Element element = elements.get(i);
						String prodservCode = element.attr("ClaveProdServ");
						String quantity = element.attr("Cantidad");
						String unitCode = element.attr("ClaveUnidad");
						String unit = element.attr("Unidad");
						String description = element.attr("Descripcion");
						String unitPrice = element.attr("ValorUnitario");
						String itotal = element.attr("Importe");
						out.write(
								quantity + "\t" +
										prodservCode + "\t" +
										unitCode + "\t" +
										unit + "\t" +
										description + "\t" +
										unitPrice + "\t" +
										itotal + "\n"
								);
						
					}
					/*List<InvoiceItem> items2 = invoice.getItems();
					for (InvoiceItem item : items2) {
						out.write(item.getQuantity() + "\t" + item.getCode() + "\t" + item.getUnit() + "\t"
								+ item.getDescription() + "\t" + item.getMark() + "\t" + item.getUnitPrice() + "\n");
					}*/
					out.write("subtotal\t" + subTotal);
					out.write("\niva\t" + taxes);
					out.write("\ntotal\t" + total);
					// Close the output stream
					out.close();
					Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","chmod 444 "+csv});
				} catch (IOException e1) {
					log.trace(e1);
				}
				
				
				/*for (int i = 0; i < invoices.length; i++) {
					String pathname = pathToWrite + invoices[i].getReference() + "-" + i+".pdf";
					
					File pdf = new PDF(invoices[i], pathname).make();
					// System.out.println("invoices["+i+"]:
					// "+invoices[i].toJson());
					// TODO this is printing time
					if (printCopies == -1) {
						new PrinterFM01(pdf, GSettings.get("PRINTER_TWO")).print(new Integer(GSettings.get("PRINTER_TWO_COPIES")));
					}
					else if(printCopies > 0){
						new PrinterFM01(pdf, GSettings.get("PRINTER_TWO")).print(printCopies);
					}
					
					//new PrinterFM01(pdf, GSettings.get("PRINTER_ONE")).print(new Integer(GSettings.get("PRINTER_ONE_COPIES")));
					paths[i] = pathname;
					fileNames[i] = pdf.getName();
					// emis.persist(invoices[i]);
				}*/
				
				if (printCopies == -1) {
					new PrinterFM01(new File(pdfTicketPath), GSettings.get("PRINTER_THREE"))
					.print(GSettings.get("PRINTER_THREE_OPTIONS")+" -# "+GSettings.get("PRINTER_THREE_COPIES"));
				}
				else if(printCopies > 0){
					new PrinterFM01(new File(pdfTicketPath), GSettings.get("PRINTER_TRHEE"))
					.print(GSettings.get("PRINTER_THREE_OPTIONS")+" -# "+printCopies);
				}
				String[] recipients={};//new String[]{};
				
				if (!invoice.getClient().getEmail().equals("")) {
					recipients=(String[])ArrayUtils.addAll(recipients, invoice.getClient().getEmail().split(" "));
				}
				if (!invoice.getAgent().getEmail().equals("")) {
					recipients=(String[])ArrayUtils.addAll(recipients, invoice.getAgent().getEmail().split(" "));
				}
				if (recipients.length>0) {
					new Hotmail().send(
							"Cotizacion "+ reference
									+ " $" + invoice.getTotal(),
							GSettings.get("EMAIL_BODY"), recipients,
							new String[] { pathToWrite + reference + ".csv",
									pathToWrite + reference + ".pdf" },
							new String[] { reference + ".csv",
									reference + ".pdf" });
				}
				
				List<InvoiceItem> invoiceItems = invoice.getItems();
				for (int i = 0; i < invoiceItems.size(); i++) {
					InvoiceItem item = invoiceItems.get(i);
					if (Inventory.exists(item) && !item.isDisabled())
						Inventory.decrementStored(item);
				}
				try {
					DBObject dbObject = mongo.doFindOne(Mongoi.INVOICES, "{ \"reference\" : \"" + invoice.getReference() + "\"}");
					String resp = "{ \"invoice\" : " + dbObject.toString()
					+ ", \"successResponse\" : \"cotizado "+id+" por "+total+"\"}";
					log.exit("response",resp);
					response.getWriter().print(resp);
				} catch (IOException e) {
					log.trace("", e);
				}
				return;
			}
		});
		
	}
	private void mailDocument(
			AccessPermission[] permissions,
			String[] parameters,
			HttpServletRequest request,
			HttpServletResponse response,
			OnlineClient onlineClient){
		doCommand(permissions,parameters,request,response, onlineClient, new Command(){
			@Override
			public void execute(Map<String,String> parametersMap,
					HttpServletResponse response, OnlineClient onlineClient) {
				Log log = new Log();
				Gson gson = new Gson();
				Mongoi mongo = new Mongoi();
				
				String reference = parametersMap.get("reference");
				String recipients = parametersMap.get("recipients");
				
				DBObject dbInvoice = mongo.doFindOne("invoices", "{\"reference\" : \""+reference+"\"}");
				Invoice invoice = null;
				if(dbInvoice!=null)
					invoice = gson.fromJson(dbInvoice.toString(), InvoiceFM01.class);/*new InvoiceFM01(
						client, shopman, list, reference, series, destiny,
						agent, paymentMethod, paymentWay, documentType, coin);*/
				else {
					try {
						response.sendError(response.SC_NOT_FOUND, fromLang(onlineClient.getLocale(), "REQUESTED_DOCUMENT_NOT_FOUND", reference));
					} catch (IOException e) {
						e.printStackTrace();
					}
					return;
				}
				Date createdDate = new Date(invoice.getCreationTime());
				String pathToRead = null,
						printer = null,
						printerOptions = null,
						pdfPath = null,
						xsltPath = null,
						xmlPath = null,
						htmlPath = null,
						additionalData = GSettings.get("INVOICE_SENDER_ADDITIONAL_DATA"),
						destiny = invoice.getDestiny().getAddress(),
						documentType = invoice.getDocumentType(),
						subject = "";
				if(documentType.equals("I")){
					pathToRead = GSettings.getPathToDirectory(GSettings.get("INVOICES_FOLDER")+"-"+Utils.getDateString(createdDate,"yyyy-MM"));
					subject = "factura "+ GSettings.get("INVOICE_SERIAL") + " " + reference
					+ " $" + invoice.getTotal();
				}
				else if(documentType.equals("ORDER")){
					pathToRead = GSettings.getPathToDirectory(GSettings.get("ORDERS_FOLDER")+"-"+Utils.getDateString(createdDate,"yyyy-MM"));
					subject = "Pedido "+ reference
							+ " $" + invoice.getTotal();
				}
				else if(documentType.equals("SAMPLE")){
					pathToRead = GSettings.getPathToDirectory(GSettings.get("SAMPLES_FOLDER")+"-"+Utils.getDateString(createdDate,"yyyy-MM"));
					subject = "Cotizacion "+ reference
							+ " $" + invoice.getTotal();
				}
				
				xmlPath = pathToRead+reference+".xml";
				htmlPath = pathToRead+reference+".html";
				pdfPath = pathToRead+reference+".pdf";
				if(new File(pdfPath).exists()){
					String[] recipients_={};
					if (!invoice.getClient().getEmail().equals("")) {
						recipients_=(String[])ArrayUtils.addAll(recipients_, invoice.getClient().getEmail().split(" "));
					}
					if (!invoice.getAgent().getEmail().equals("")) {
						recipients_=(String[])ArrayUtils.addAll(recipients_, invoice.getAgent().getEmail().split(" "));
					}
					if(!recipients.equals("")){
						recipients_ = recipients.split(" ");
					}
					if (recipients_.length>0) {
						new Hotmail().send(
								subject,
								GSettings.get("EMAIL_BODY"), recipients_,
								new String[] { 
										pathToRead + reference + ".csv",
										pathToRead + reference + ".xml",
										pathToRead + reference + ".pdf" },
								new String[] {
										reference + ".csv",
										reference + ".xml",
										reference + ".pdf" });
					}
					String mails = String.join(" ", recipients_);
					String resp = "{ " + 
					"\"successResponse\" : \"email doc "+reference+" to "+mails+"\" }";
					log.exit("response",resp);
					try {
						response.getWriter().print(resp);
					} catch (IOException e) {
					}
					return;
				}
				else{
					if(invoice.hasElectronicVersion()&&!(new File(xmlPath).exists())){
						try {
							response.sendError(response.SC_PARTIAL_CONTENT, Langed.get(onlineClient.getLocale(), "CODE_1"));
						} catch (IOException e) {
						}
						return;
					}
					else if(invoice.hasElectronicVersion()){
						Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData,destiny);
						Utils.HTML_TO_PDF(htmlPath, pdfPath, "Factura "+GSettings.get("INVOICE_SERIAL")+" "+reference+" - pagina [page]/[topage]", "");
						String[] recipients_={};
						if (!invoice.getClient().getEmail().equals("")) {
							recipients_=(String[])ArrayUtils.addAll(recipients_, invoice.getClient().getEmail().split(" "));
						}
						if (!invoice.getAgent().getEmail().equals("")) {
							recipients_=(String[])ArrayUtils.addAll(recipients_, invoice.getAgent().getEmail().split(" "));
						}
						if(!recipients.equals("")){
							recipients_ = recipients.split(" ");
						}
						if (recipients_.length>0) {
							new Hotmail().send(
									subject,
									GSettings.get("EMAIL_BODY"), recipients_,
									new String[] { 
											pathToRead + reference + ".csv",
											pathToRead + reference + ".xml",
											pathToRead + reference + ".pdf" },
									new String[] {
											reference + ".csv",
											reference + ".xml",
											reference + ".pdf" });
						}
						String mails = invoice.getClient().getEmail() +" "+ invoice.getAgent().getEmail();
						String resp = "{ " + 
						"\"successResponse\" : \"email doc "+reference+" to "+mails+"\" }";
						log.exit("response",resp);
						try {
							response.getWriter().print(resp);
						} catch (IOException e) {
						}
						return;
					}
					try {
						response.sendError(response.SC_NOT_FOUND, Langed.get(onlineClient.getLocale(), "CODE_2", reference));
					} catch (IOException e) {
					}
					return;
				}
			}
		});
		
	}

}
