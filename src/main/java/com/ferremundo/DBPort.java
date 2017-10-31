package com.ferremundo;

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

import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ferremundo.db.FilterDBObject;
import com.ferremundo.db.Mongoi;
import com.google.gson.Gson;
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
			new Log().entry();
			OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
			Log log = new Log(onlineClient);
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
						/*new ArrayList<AccessPermission>(Arrays.asList(
								AccessPermission.ADMIN,
								AccessPermission.PRODUCT_READ)),
								*/
						new AccessPermission[]{ADMIN,READ_PROVIDERS},
						new String[]{"searchRequestId","search"},
						request, response, onlineClient);
				return;
			}
			else if(dcMatch(dc,UPDATE_PRODUCTS)){
				updateProducts(request, response, onlineClient);
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
		String arg=request.getParameter(parameter);
		if(arg!=null)return true;
		return false;
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
					response.getWriter().write(fromLang(onlineClient.getLocale(),"UNSPECIFIED_ARGUMENTS"));
					return;
				}
			}
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
