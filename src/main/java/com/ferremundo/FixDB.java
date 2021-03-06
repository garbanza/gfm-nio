package com.ferremundo;

import java.net.UnknownHostException;

import com.ferremundo.db.Mongoi;
import com.ferremundo.stt.GSettings;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class FixDB {

	public static String fix(int n){
		Mongoi mongoi = new Mongoi();
		//DBObject latest = mongoi.doFindLast(Mongoi.DB_FIXES);
		//DBCollection fixes = mongoi.getCollection(Mongoi.DB_FIXES);
		if(n==0){
			mongoi.getCollection(Mongoi.DB_FIXES).ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
			mongoi.doInsert(Mongoi.DB_FIXES, "{ \"fixed\" : false , \"id\" : 0}");
			fixProductHashToSimplyBeTheCode();
			new Mongoi().doUpdate(Mongoi.DB_FIXES, "{ \"id\" : 0 }", "{ \"fixed\" : true }");
			return "ok";
		}
		else if(n==1){
			writeMostImportantToBlockchain();
			return "ok";
		}
		else if(n>1)return "fix not yet implemented : "+n;
		else if(n == -1){
			fixRecordsCollectionNotAvailable();
			return "ok";
		}
		
		else return "fix not yet implemented : "+n;
	}
	
	
	private static void writeMostImportantToBlockchain(){
		Log log = new Log();
		Blockchain blockchain = Blockchain.instance();
		DBCollection collection = new Mongoi().getCollection(Mongoi.AGENTS);
		DBCursor cursor=collection.find();
		/*while(cursor.hasNext()){
			DBObject n=cursor.next();
			String code=n.get("code").toString();
			String query =
					"{\"method\" : \"publish\","
					+"\"params\" : [\""+GSettings.get("BLOCKCHAIN_STREAM_NAME")+
					"\", [\""+code+"\"], {\"json\" : "+n.toString()+"}], \"id\" : \""+code+"\"}";
			String result = blockchain.query(query);
		}
		collection = new Mongoi().getCollection(Mongoi.CLIENTS);
		cursor=collection.find();
		while(cursor.hasNext()){
			DBObject n=cursor.next();
			String code=n.get("code").toString();
			String query =
					"{\"method\" : \"publish\","
					+"\"params\" : [\""+GSettings.get("BLOCKCHAIN_STREAM_NAME")+
					"\", [\""+code+"\"], {\"json\" : "+n.toString()+"}], \"id\" : \""+code+"\"}";
			String result = blockchain.query(query);
		}*/
		/*collection = new Mongoi().getCollection(Mongoi.INVOICES);
		cursor=collection.find();
		while(cursor.hasNext()){
			DBObject n=cursor.next();
			String code=n.get("reference").toString();
			String query =
					"{\"method\" : \"publish\","
					+"\"params\" : [\""+GSettings.get("BLOCKCHAIN_STREAM_NAME")+
					"\", [\""+code+"\"], {\"json\" : "+n.toString()+"}], \"id\" : \""+code+"\"}";
			String result = blockchain.query(query);
		}*/
		collection = new Mongoi().getCollection(Mongoi.PRODUCTS);
		cursor=collection.find();
		while(cursor.hasNext()){
			DBObject n=cursor.next();
			String code=n.get("code").toString();
			String query =
					"{\"method\" : \"publish\","
					+"\"params\" : [\""+GSettings.get("BLOCKCHAIN_STREAM_NAME")+
					"\", [\""+code+"\"], {\"json\" : "+n.toString()+"}], \"id\" : \""+code+"\"}";
			String result = blockchain.query(query);
		}
	}
	
	private static void fixProductHashToSimplyBeTheCode(){
		Log log = new Log();
		DBCollection collection = new Mongoi().getCollection("products");
		DBCursor cursor=collection.find();
		while(cursor.hasNext()){
			DBObject n=cursor.next();
			String code=n.get("code").toString();
			DBObject dbObject=(DBObject)JSON.parse("{ \"code\" : \""+code+"\"}");
			String js="{ \"$set\" : "+"{\"hash\" : \""+code+"\" }"+"}";
			DBObject dbObject2=(DBObject)JSON.parse(js);
			collection.update(dbObject, dbObject2);
			log.info("updatig :"+ code);
		}
	}
	
	private static void fixRecordsCollectionNotAvailable(){
		new Mongoi().getCollection(Mongoi.TEMPORAL_RECORDS).ensureIndex(new BasicDBObject("login", 1));
		new Mongoi().getCollection(Mongoi.TEMPORAL_RECORDS).ensureIndex(new BasicDBObject("todo", 1));
		new Mongoi().getCollection(Mongoi.TEMPORAL_RECORDS).ensureIndex(new BasicDBObject("id", 1), new BasicDBObject("unique", true));
		
		new Mongoi().getCollection(Mongoi.TEMPORAL_RECORDS_COUNTER).ensureIndex(new BasicDBObject("unique", 1), new BasicDBObject("unique", true));
		new Mongoi().doInsert(Mongoi.TEMPORAL_RECORDS_COUNTER, "{ \"unique\" : \"unique\" , \"id\" : 0}");
		
	}
	


}
