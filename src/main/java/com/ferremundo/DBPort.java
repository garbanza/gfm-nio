package com.ferremundo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
import com.ferremundo.db.FilterDBObject;
import com.ferremundo.db.Inventory;
import com.ferremundo.db.Mongoi;
import com.ferremundo.mailing.Hotmail;
import com.ferremundo.stt.GSettings;
import com.ferremundo.util.Util;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

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
				makeRecord(request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,DEACTIVATE_RECORD)){
				deactivateRecord(request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,RETURN_RECORDS)){
				returnRecords(request, response, onlineClient);
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
						/*new ArrayList<AccessPermission>(Arrays.asList(
								AccessPermission.ADMIN,
								AccessPermission.PRODUCT_READ)),
								*/
						new AccessPermission[]{ADMIN,PRODUCT_READ,BASIC},
						new String[]{"searchRequestId","search"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,SEARCH_PROVIDERS)){
				searchProviders(
						new AccessPermission[]{ADMIN,READ_PROVIDERS},
						new String[]{"searchRequestId","search"},
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
								"cfdiUse",
								"documentType",
								"paymentMethod",
								"paymentWay",
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
								"cfdiUse",
								"documentType",
								"paymentMethod",
								"paymentWay",
								"printCopies",
								"coin"},
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
		System.out.println("DEACTIVATE_RECORD".equals(Commands.DEACTIVATE_RECORD.toString()));
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
				List<DBObject> list=Product.find(patterns,onlineClient,searchRequestId);
				List<DBObject> filteredList=FilterDBObject.keep(
						new String[]{"code","description","mark","unit"},
						list);
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
				Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData);
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
				Utils.XML_TO_HTML(xmlPath, xsltPath, htmlPath, additionalData);
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

}
