package com.ferremundo.db;


import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

//import org.apache.naming.resources.BaseDirContext;
import org.bson.BSONObject;
import org.jongo.Find;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.json.JSONException;
import org.json.JSONObject;

import com.ferremundo.Client;
import com.ferremundo.Invoice;
import com.ferremundo.InvoiceFM01;
import com.ferremundo.InvoiceItem;
import com.ferremundo.Log;
import com.ferremundo.MD5;
import com.ferremundo.Product;
import com.ferremundo.stt.GSettings;
import com.google.common.collect.Iterators;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;
import com.mongodb.util.Hash;
import com.mongodb.util.JSON;

public class Mongoi {
	
	private static Mongo mongo=null;
	public static final String HOST=GSettings.get("MONGO_DB_HOST");
	public static final String PORT=GSettings.get("MONGO_DB_PORT");
	public static final String GLOBAL_DB=GSettings.get("MONGO_DB_NAME");
	public static final String INVOICES="invoices";
	public static final String CLIENTS="clients";
	public static final String CLIENTS_COUNTER="clientscounter";
	
	public static final String AGENTS="agents";
	public static final String AGENTS_COUNTER="agentscounter";
	
	public static final String SHOPMANS="shopmans";
	public static final String SHOPMANS_COUNTER="shopmanscounter";
	
	public static final String TEMPORAL_RECORDS="temporalrecords";
	public static final String TEMPORAL_RECORDS_COUNTER="temporalrecordscounter";
	
	public static final String PRODUCTS="products";
	public static final String PRODUCTS_COUNTER = "productscounter";
	public static final String REFERENCE="reference";
	
	public static final String PRODUCT_TENDENCY="producttendency";
	public static final String PRODUCT_PROVIDER_PRICES_HISTORY="productproviderpriceshistory";
	
	public static final String SYSTEM_IPS="systemips";
	
	public static final String PROVIDERS="providers";
	
	public static final String DB_FIXES="dbFixes";
	
	public static final String CACHE_SESSION="cachesession";
	
	public static final int STARTS=0;
	public static final int CONTAINS=1;
	public static final int MATCHES=2;
	
	private static DB db;
	
	public static final int INSERT=0;
	
	public static final int UPDATE=0;
	
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	private static final String EXPRIRATION_DATE="2113/12/21";
	
	public Mongoi(){
		if(mongo==null){
			Date today = null;
			Date expires = null;
			try {
				today=dateFormat.parse(dateFormat.format(new Date()));
				expires=dateFormat.parse(EXPRIRATION_DATE);
				
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(today.getTime()>expires.getTime()){
				try {
					throw new Exception("FBI WARNING!!! it seems you or your organization has being incurring in federal law violations since "+EXPRIRATION_DATE+" you better go an suck a cock!!, thanks!!");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
			}	
			try {
				mongo = new Mongo(HOST,new Integer(PORT));
				db = mongo.getDB(GLOBAL_DB);
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public DBCursor doFindFieldsMatches(String where, String[] fields, Object[] patterns){
		ArrayList<DBObject> and=new ArrayList<DBObject>();
		for(int i=0;i<fields.length;i++){
			and.add(new BasicDBObject(fields[i],patterns[i]));
		}
		DBObject query=new BasicDBObject("$and",and);
		DBCollection collection=db.getCollection(where);
		DBCursor cursor=collection.find(query);
		return cursor;
	}
	
	public 	DBCursor doFindThisThen(int mode, String where, String[] thisFields, String[] thenFields, String[] patterns){
		ArrayList<DBObject> and=new ArrayList<DBObject>();
		ArrayList<DBObject> or=new ArrayList<DBObject>();
		if(thisFields!=null){
			for(int i=0;i<patterns.length;i++){
				for(int j=0;j<thisFields.length;j++){
					BasicDBObject dbo=null;
					if(mode==MATCHES)dbo=new BasicDBObject(thisFields[j],patterns[i].toUpperCase());
					else if(mode==CONTAINS)dbo=new BasicDBObject(thisFields[j],Pattern.compile(patterns[i].toUpperCase()));
					else if(mode==STARTS)dbo=new BasicDBObject(thisFields[j],Pattern.compile("^"+patterns[i].toUpperCase()));
					/*or.add(new BasicDBObject(thisFields[j],
							mode==MATCHES?patterns[i]:
								(mode==STARTS?Pattern.compile("^"+patterns[i]):Pattern.compile(patterns[i]))));
					 */
					or.add(dbo);
				}
			}
			and.add(new BasicDBObject("$or",or));
		}
		String[] C=null;
		if(thisFields!=null){
			C= new String[thisFields.length+thenFields.length];
			System.arraycopy(thisFields, 0, C, 0, thisFields.length);
			System.arraycopy(thenFields, 0, C, thisFields.length, thenFields.length);
		}
		else{C=thenFields;}
		
		for(int i=0;i<patterns.length;i++){
			ArrayList<DBObject> or_=new ArrayList<DBObject>();
			for(int j=0;j<C.length;j++){
				BasicDBObject dbo=null;
				if(mode==MATCHES)dbo=new BasicDBObject(C[j],patterns[i].toUpperCase());
				else if(mode==CONTAINS)dbo=new BasicDBObject(C[j],Pattern.compile(patterns[i].toUpperCase()));
				else if(mode==STARTS)dbo=new BasicDBObject(C[j],Pattern.compile("^"+patterns[i].toUpperCase()));
				or_.add(dbo);
			}
			and.add(new BasicDBObject("$or",or_));
		}
		
		DBObject query=new BasicDBObject("$and",and);
		DBCollection collection=db.getCollection(where);
		DBCursor cursor=collection.find(query).limit(200);
		return cursor;
	}
	
	public DBCursor doFindLike(String where, String[] fields, String[] patterns){
		ArrayList and=new ArrayList();
		for(int i=0;i<patterns.length;i++){
			ArrayList or=new ArrayList();
			for(int j=0;j<fields.length;j++){
				or.add(new BasicDBObject(fields[j],Pattern.compile(patterns[i])));
			}
			and.add(new BasicDBObject("$or",or));
		}
		DBObject query=new BasicDBObject("$and",and);
		DBCollection collection=db.getCollection(where);
		DBCursor cursor=collection.find(query);
		return cursor;
	}
	
	public DBCursor doFindLike(String where, String[] fields, String[] patterns, int limit){
		ArrayList and=new ArrayList();
		for(int i=0;i<patterns.length;i++){
			ArrayList or=new ArrayList();
			for(int j=0;j<fields.length;j++){
				or.add(new BasicDBObject(fields[j],Pattern.compile(patterns[i])));
			}
			and.add(new BasicDBObject("$or",or));
		}
		DBObject query=new BasicDBObject("$and",and);
		DBCollection collection=db.getCollection(where);
		DBCursor cursor=collection.find(query).limit(limit);
		return cursor;
	}
	
	public DBCursor doFindLikeV2(String where, String[] fields, String[] patterns, String sort, int limit){
		Log log = new Log();
		ArrayList and=new ArrayList();
		for(int i=0;i<patterns.length;i++){
			ArrayList or=new ArrayList();
			for(int j=0;j<fields.length;j++){
				or.add(new BasicDBObject(fields[j],Pattern.compile(patterns[i])));
			}
			and.add(new BasicDBObject("$or",or));
		}
		DBObject query=new BasicDBObject("$and",and);
		DBObject sorting = (DBObject)JSON.parse(sort);
		DBCollection collection=db.getCollection(where);
		log.object("query",query.toString());
		DBCursor cursor=collection.find(query).sort(sorting).limit(limit);
		return cursor;
	}
	
	public DBCursor doFindGreaterThan(String where,String field, float n){
		BasicDBObject query = new BasicDBObject();
		query.put(field, new BasicDBObject("$gt", n));
		DBCollection collection=db.getCollection(where);
		DBCursor cursor=collection.find(query);
		return cursor;
	}
	public DBCursor doFindGreaterThan(String where,String field, Long n){
		BasicDBObject query = new BasicDBObject();
		query.put(field, new BasicDBObject("$gt", n));
		DBCollection collection=db.getCollection(where);
		DBCursor cursor=collection.find(query);
		return cursor;
	}
	
	public DBCursor doFind(String where){
		DBCollection collection=db.getCollection(where);
		DBCursor cursor=collection.find();
		return cursor;
	}
	
	public DBCursor doFindMatches(String where, String[] fields, String[] patterns){
		ArrayList<DBObject> and=new ArrayList<DBObject>();
		for(int i=0;i<patterns.length;i++){
			ArrayList<DBObject> or=new ArrayList<DBObject>();
			for(int j=0;j<fields.length;j++){
				or.add(new BasicDBObject(fields[j],patterns[i]));
			}
			and.add(new BasicDBObject("$or",or));
		}
		DBObject query=new BasicDBObject("$and",and);
		DBCollection collection=db.getCollection(where);
		DBCursor cursor=collection.find(query);
		return cursor;
	}
	
	public DBCursor doFindMatches(String where, String field, String pattern){
		DBCollection collection=db.getCollection(where);
		DBCursor cursor=collection.find(new BasicDBObject(field,pattern));
		return cursor;
	}
		
	public WriteResult doInsert(String where, String json) {
		DBCollection collection = db.getCollection(where);
		DBObject dbObject=(DBObject)JSON.parse(json);
		WriteResult wr=collection.insert(dbObject);
		return wr;
	}
	
	public void doInsert(String where, Object src) {
		String json=new Gson().toJson(src);
		doInsert(where, json);
	}
	
	public void doUpdate(String where, String matches, String json) {
		DBCollection collection = db.getCollection(where);
		String js="{ \"$set\" : "+json+"}";
		DBObject dbObject=(DBObject)JSON.parse(matches);
		DBObject dbObject2=(DBObject)JSON.parse(js);
		collection.update(dbObject, dbObject2);
	}
	
	public DBObject doFindOne(String where, String json) {
		DBCollection collection = db.getCollection(where);
		DBObject dbObject =collection.findOne((DBObject)JSON.parse(json));
		return dbObject;
	}
	
	public DBObject doFindOne(String where, String field, String pattern) {
		DBCollection collection = db.getCollection(where);
		DBObject dbObject =collection.findOne((DBObject)JSON.parse("{\""+field+"\":\""+pattern+"\"}"));
		return dbObject;
	}
	
	public void doPush(String where, String matches, String json) {
		DBCollection collection = db.getCollection(where);
		String js="{ \"$push\" : "+json+"}";
		DBObject dbObject=(DBObject)JSON.parse(matches);
		DBObject dbObject2=(DBObject)JSON.parse(js);
		collection.update(dbObject, dbObject2);
	}
	
	public int doIncrement(String where, String matches, String field){
		DBCollection collection = db.getCollection(where);
		String js="{ \"$inc\" : { \""+field+"\" : 1 } }";
		DBObject dbObject=(DBObject)JSON.parse(js);
		DBObject dbObject2=(DBObject)JSON.parse(matches);
		//WriteResult wr=collection.update(dbObject2,dbObject);
		String st=collection.findAndModify(dbObject2,dbObject).get(field).toString();
		return new Integer(st);
	}
	public float doIncrement(String where, String matches, String field, float amount){
		Log log = new Log();
		log.entry(where,matches,field,amount);
		DBCollection collection = db.getCollection(where);
		String js="{ \"$inc\" : { \""+field+"\" : "+amount+" } }";
		DBObject dbObject=(DBObject)JSON.parse(js);
		DBObject dbObject2=(DBObject)JSON.parse(matches);
		//WriteResult wr=collection.update(dbObject2,dbObject);
		DBObject r=collection.findAndModify(dbObject2,dbObject);
		log.object("result",r);
		String st=r.get(field).toString();
		return new Float(st);
	}
	
	public DBCollection getCollection(String coll){
		return db.getCollection(coll);
	}
	
	
	private void deadcode(){
		//System.out.println(new Calendar().);
		//Long l=new Date(new Date().)
		/*DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date today = null;
		try {
			today=dateFormat.parse(dateFormat.format(new Date()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(today.getTime()+" = "+dateFormat.format(today));
		DBCursor cursor=new Mongoi().doFindGreaterThan(INVOICES, "creationTime", 0);
		while(cursor.hasNext()){
			System.out.println(cursor.next());
		}*/
		/*
		DBObject object=new Mongoi().doFindOne(INVOICES, "{ \"reference\" : \"40WA\" }");
		Invoice invoice=new Gson().fromJson(
				object.toString(),InvoiceFM01.class
				);
		System.out.println(new Gson().toJson(invoice));
		System.out.println(new Gson().toJson(invoice.getLogs()));
		
		DBCursor cursor=new Mongoi().doFindMatches(INVOICES, "agent.code", "5da6094e3871a2385248d4b0144b9cf3");
		while(cursor.hasNext()){
			System.out.println(cursor.next());
		}
		List<DBObject> list=new Mongoi().doFindAgentHistory("5da6094e3871a2385248d4b0144b9cf3", 10, 0);
		for(DBObject dbObject : list){
			System.out.println("list->"+dbObject);
		}
		list=new Mongoi().doFindAgentHistory("5da6094e3871a2385248d4b0144b9cf3", 10, 1);
		for(DBObject dbObject : list){
			System.out.println("list->"+dbObject);
		}
		*/
		//System.out.println(new Mongoi().doFindOne(SHOPMANS, "{ \"login\" : \"user1\" }"));
		//System.out.println(new Mongoi().doFindOne(SHOPMANS, "{ \"login\" : \"root\" }"));
		//new Mongoi().doUpdate(SHOPMANS, "{ \"login\" : \"\" }", "{ \"password\" : \""+MD5.get("not set")+"\" }");
		//new Mongoi().doUpdate(INVOICES, "{ \"reference\" : \"3ZVW\" }", "{ \"shopman.password\" : \"*\" }");
		/*Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = new Mongoi().doFindOne(PRODUCTS, "{ \"code\" : \"N70713\" }").toString();
		
		System.out.println(json);
		*/
		//System.out.println(new Mongoi().doFindOne(CLIENTS, "{ \"code\" : \"bbd4d562e38810087c9fa9b56e566e76\" }"));
		/*String json="{ \"reference\" : \"1OEW\"}";
		DBObject dbObject=(DBObject)JSON.parse(json);
		DBObject dbO=new Mongoi().doFindOne("testCollection", json);
		System.out.println(dbO);
		//json="{ \"json\" : \"1OEW\"}";
		String change="{ \"json\" : \"\"}";
		new Mongoi().doUpdate("testCollection", json,change);
		dbO=new Mongoi().doFindOne("testCollection", "{ \"reference\" : "+Pattern.compile("10")+"}");
		System.out.println(dbO);
		new Mongoi().doIncrement("testCollection", json, "metaData.serial");

		dbO=new Mongoi().doFindOne("testCollection", json);
		System.out.println(dbO);*/
		/*
		Mongo m = null;
		try {
			m = new Mongo(HOST);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DB db = m.getDB(GLOBAL_DB);
		DBCollection coll = db.getCollection(INVOICES);
		*/
		//DBCursor cursor=coll.find();
		//DBCursor c=cursor.sort((DBObject)JSON.parse("{ \"reference\" : 1 }"));
		//while(c.hasNext()){
		//	System.out.println(c.next());
		//}
		//coll.ensureIndex(new BasicDBObject("reference", 1), new BasicDBObject("unique", true));
		
		//new Mongoi().doIncrement(REFERENCE, "reference", field)
		
		//////////////////*new Mongoi().doUpdate(REFERENCE, "{ \"reference\" : \"unique\" }", "{ \"count\" : "+180000+" ");*/
		//System.out.println(new Mongoi().doIncrement(REFERENCE, "{ \"reference\" : \"unique\" }", "count"));
		//new Mongoi().doFindLike(INVOICES, new String[]{"MEZCL"});
		//System.out.println(new Mongoi().doFindOne(INVOICES, "{ \"reference\" : \"3VP7\" }"));
		//System.out.println(Integer.toString(180000 ,Character.MAX_RADIX).toUpperCase());
		
		/*DBCursor cursor=new Mongoi().doFindThisThen(CONTAINS,CLIENTS, new String[]{"consummer"}, new String[]{"consummer"},new String[]{"raynmune"});
		while(cursor.hasNext()){
			System.out.println(cursor.next());
		}*/
		//System.out.println(new Mongoi().doFindOne(CLIENTS, "{\"code\" : \"a7d6517bc148b6945837369f448fdde1\"}"));
		/*DBCursor cursor=new Mongoi().doFindLike(PRODUCTS, new String[]{"code"}, new String[]{"AFS"});
		for(DBObject ob:cursor){
			System.out.println(ob);
		}*/
		/*DBCursor cursor=new Mongoi().doFindLike(PRODUCTS, new String[]{"description"},new String[]{"CODO 1/2"});
		for(DBObject o : cursor){
			System.out.println(o);
		}*/
		
		/*List<DBObject> list=Client.find(new String[]{"raynmune"});
		for(DBObject ob:list){
			System.out.println(ob);
		}*/
		/*boolean cont=true;
		for(int i=0;cont;i++){
			String str=Integer.toString(i ,Character.MAX_RADIX).toUpperCase();
			if(str.equals("PSX")){
				System.out.println(i);
				cont=false;
			}
		}*/
		/*DBCursor cursor=new Mongoi().doFindInvoices("N70713");
		while(cursor.hasNext()){
			System.out.println(cursor.next());
		}*/
	}
	public static void main(String[] args) {
		DBCollection providersCollection=new Mongoi().getCollection(Mongoi.PROVIDERS);
		providersCollection.ensureIndex(new BasicDBObject("fullName", 1), new BasicDBObject("unique", true));
		
		/*
		DBObject dbObject=new Mongoi().doFindOne(PRODUCTS, "{ \"hash\" : \"e9a98effaff20dd392d38d9a5cd2d5e3\"}");
		JSONObject jsonObject=null;
		try {
			jsonObject=new JSONObject(dbObject.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InvoiceItem item=new Gson().fromJson(jsonObject.toString(),InvoiceItem.class);
		System.out.println(dbObject);
		System.out.println(item.getHash());
		*/
	}
	public DB getDB(){
		return db;
	}
	public Mongo getMongo(){
		return mongo;
	}
	/**WARNING: execute this method once for each project... this is here temporarily*/
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
		
		new Mongoi().getCollection(PRODUCT_PROVIDER_PRICES_HISTORY).ensureIndex(new BasicDBObject("time", 1));
		new Mongoi().getCollection(PRODUCT_PROVIDER_PRICES_HISTORY).ensureIndex(new BasicDBObject("id", 1));
		
		new Mongoi().getCollection(PRODUCT_TENDENCY).ensureIndex(new BasicDBObject("time", 1));
		new Mongoi().getCollection(PRODUCT_TENDENCY).ensureIndex(new BasicDBObject("id", 1));
		
		
		new Mongoi().getCollection(TEMPORAL_RECORDS).ensureIndex(new BasicDBObject("login", 1));
		new Mongoi().getCollection(TEMPORAL_RECORDS).ensureIndex(new BasicDBObject("todo", 1));
		new Mongoi().getCollection(TEMPORAL_RECORDS).ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
		
		new Mongoi().getCollection(TEMPORAL_RECORDS_COUNTER).ensureIndex(new BasicDBObject("unique", 1), new BasicDBObject("unique", true));
		new Mongoi().doInsert(TEMPORAL_RECORDS_COUNTER, "{ \"unique\" : \"unique\" , \"id\" : 0}");
		
		DBCollection clientsCollection=new Mongoi().getCollection(Mongoi.CLIENTS);
		clientsCollection.ensureIndex(new BasicDBObject("code", 1), new BasicDBObject("unique", true));
		clientsCollection.ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
		new Mongoi().getCollection(Mongoi.CLIENTS_COUNTER).ensureIndex(new BasicDBObject("unique", 1), new BasicDBObject("unique", true));
		new Mongoi().doInsert(Mongoi.CLIENTS_COUNTER, "{ \"unique\" : \"unique\" , \"id\" : 0}");		
		
		new Mongoi().getCollection(INVOICES).ensureIndex(new BasicDBObject("reference", 1), new BasicDBObject("unique", true));
		new Mongoi().getCollection(INVOICES).ensureIndex(new BasicDBObject("creationTime", 1));
		new Mongoi().getCollection(INVOICES).ensureIndex(new BasicDBObject("updated", 1));
		
		DBCollection collection= new Mongoi().getCollection(Mongoi.PRODUCTS);
		collection.ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
		collection.ensureIndex(new BasicDBObject("hash", 1), new BasicDBObject("unique", true));
		collection.ensureIndex(new BasicDBObject("code", 1), new BasicDBObject("unique", true));
		new Mongoi().getCollection(PRODUCTS_COUNTER).ensureIndex(new BasicDBObject("unique", 1), new BasicDBObject("unique", true));
		new Mongoi().doInsert(PRODUCTS_COUNTER, "{ \"unique\" : \"unique\" , \"id\" : 0}");
		
		new Mongoi().getCollection(REFERENCE).ensureIndex(new BasicDBObject("reference", 1), new BasicDBObject("unique", true));
		new Mongoi().doInsert(REFERENCE, "{ \"reference\" : \"unique\", \"count\" : "+33500+" }");
		
		
		DBCollection agentsCollection=new Mongoi().getCollection(Mongoi.AGENTS);
		agentsCollection.ensureIndex(new BasicDBObject("code", 1), new BasicDBObject("unique", true));
		agentsCollection.ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
		new Mongoi().getCollection(Mongoi.AGENTS_COUNTER).ensureIndex(new BasicDBObject("unique", 1), new BasicDBObject("unique", true));
		new Mongoi().doInsert(Mongoi.AGENTS_COUNTER, "{ \"unique\" : \"unique\" , \"id\" : 0}");
		
		
		DBCollection shopmansCollection=new Mongoi().getCollection(Mongoi.SHOPMANS);
		shopmansCollection.ensureIndex(new BasicDBObject("login", 1), new BasicDBObject("unique", true));
		shopmansCollection.ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
		new Mongoi().getCollection(Mongoi.SHOPMANS_COUNTER).ensureIndex(new BasicDBObject("unique", 1), new BasicDBObject("unique", true));
		new Mongoi().doInsert(Mongoi.SHOPMANS_COUNTER, "{ \"unique\" : \"unique\" , \"id\" : 0}");
		
		DBCollection providersCollection=new Mongoi().getCollection(Mongoi.PROVIDERS);
		providersCollection.ensureIndex(new BasicDBObject("fullName", 1), new BasicDBObject("unique", true));
		
	}
	
	public DBCursor doFindInvoices(String keys){
		return doFindInvoices(keys.split(" "));
	}
	
	public DBCursor doFindInvoices(String[] paths){
		
		List<String> list=new LinkedList<String>();
		for(String str:paths){
			if(str!=null)if(!str.equals(""))list.add(str);
		}
		String[] strl=new String[list.size()];
		for(int i=0;i<list.size();i++){
			strl[i]=list.get(i);
			System.out.println("'"+strl[i]+"'<-");
		}
		DBCursor cursor=doFindLike(INVOICES, new String[]{"reference","client.consummer","agent.consummer","client.address","agent.address","client.rfc","agent.rfc","items.code","items.mark","items.description"}, strl,50).sort(new BasicDBObject("$natural",-1));
		System.out.println(cursor.count()+" hallados");
		return cursor;
	}
	
	public DBCursor doFindCacheSession(String[] paths){
		
		List<String> list=new LinkedList<String>();
		for(String str:paths){
			if(str!=null)if(!str.equals(""))list.add(str);
		}
		String[] strl=new String[list.size()];
		for(int i=0;i<list.size();i++){
			strl[i]=list.get(i);
		}
		DBCursor cursor=doFindLikeV2(CACHE_SESSION, new String[]{"id","items.code","items.mark","items.description"}, strl,"{\"$natural\" : -1}",200);
		return cursor;
	}
	
	public DBObject doFindLast(String where){
		return this.doFind(where).sort(new BasicDBObject("$natural",-1)).next();
	}
	
	public List<DBObject> doFindAgentHistory(String code,int size, int page){
		return doFindHistory(code, "agent.code", size, page);
	}
	public List<DBObject> doFindClientHistory(String code,int size, int page){
		return doFindHistory(code, "client.code", size, page);
	}
	public List<DBObject> doFindHistory(String code,String where,int size, int page){
		DBCursor cursor=doFindMatches(INVOICES, where, code).sort(new BasicDBObject("reference",1));
		//System.out.println("cursor="+new Gson().toJson(cursor));
		List<DBObject> list=new LinkedList<DBObject>();
		int i=0;
		while(cursor.hasNext()){
			if(i>=size*page&&i<(size*page+size))
				list.add(cursor.next());
			if(i>=(size*page+size))break;
			i++;
		}
		return list;
	}
}
