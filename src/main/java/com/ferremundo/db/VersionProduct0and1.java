package com.ferremundo.db;

import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class VersionProduct0and1 {

	public static void main(String[] args) {
		//fixProductVersion();
		//setProviderPriceAndGainPercentField();
		//setConsummerDuscountPercentage();
		setAgentDuscountPercentage();
		//testClientIDs();
		//findClientsIDLessThan(10);
	}
	
	private static void findClientsIDLessThan(int i){
		DBCursor cursor=new Mongoi().getCollection(Mongoi.CLIENTS).find(new BasicDBObject("id",new BasicDBObject("$lt",i)));
		while(cursor.hasNext()){
			System.out.println(cursor.next());
		}
	}
	
	private static void testClientIDs(){
		DBCursor cursor=new Mongoi().getCollection(Mongoi.CLIENTS).find(new BasicDBObject("id",5));
		
		System.out.println(cursor.count());
		System.out.println(cursor.next());
	}
	
	private static void setAgentDuscountPercentage(){
		DBCursor cursorc=new Mongoi().doFind(Mongoi.AGENTS);//.sort(new BasicDBObject("$natural",1)).limit(5000);
		DBCollection collectionc = new Mongoi().getCollection(Mongoi.AGENTS);
		int j=0;
		int idnull=0;
		LinkedList<DBObject> noidlist=new LinkedList<DBObject>();
		LinkedList<DBObject> reidlist=new LinkedList<DBObject>();
		for(int i=0;i<cursorc.count();i++,j++){
			DBObject dbObject=cursorc.next();
			int type=new Integer(dbObject.get("consummerType").toString());
			int id=0;
			if(dbObject.containsField("id")){
				id=new Integer(dbObject.get("id").toString());
			}
			else{
				idnull++;
				noidlist.add(dbObject);
				continue;
			}
			if(id==5){
				reidlist.add(dbObject);
				continue;
			}
			String js="";
			if(type==1){
				js="{ \"$set\" : {\"discountPercentage\": 0} }";
				i++;
			}
			else if(type==2){
				js="{ \"$set\" : {\"discountPercentage\": 30} }";
				i++;
			}
			else if(type==3){
				js="{ \"$set\" : {\"discountPercentage\": 55} }";
				i++;
			}
			//else continue;
			System.out.println(dbObject);
			DBObject dbObject2=(DBObject)JSON.parse(js);
			collectionc.update(dbObject, dbObject2);
			System.out.println(type+" "+i);
			//System.out.println(new Mongoi().doFindOne(Mongoi.CLIENTS, "{ \"code\":\""+dbObject.get("code").toString()+"\"}"));
		}
		System.out.println(cursorc.count());
		System.out.println(reidlist.size());
		System.out.println(noidlist.size());
		//System.out.println(i);
	}
	
	private static void setConsummerDuscountPercentage(){
		DBCursor cursorc=new Mongoi().doFind(Mongoi.CLIENTS);//.sort(new BasicDBObject("$natural",1)).limit(5000);
		DBCollection collectionc = new Mongoi().getCollection(Mongoi.CLIENTS);
		int j=0;
		int idnull=0;
		LinkedList<DBObject> noidlist=new LinkedList<DBObject>();
		LinkedList<DBObject> reidlist=new LinkedList<DBObject>();
		for(int i=0;i<cursorc.count();i++,j++){
			DBObject dbObject=cursorc.next();
			int type=new Integer(dbObject.get("consummerType").toString());
			int id=0;
			if(dbObject.containsField("id")){
				id=new Integer(dbObject.get("id").toString());
			}
			else{
				idnull++;
				noidlist.add(dbObject);
				continue;
			}
			if(id==5){
				reidlist.add(dbObject);
				continue;
			}
			String js="";
			if(type==1){
				js="{ \"$set\" : {\"discountPercentage\": 0} }";
				i++;
			}
			else if(type==2){
				js="{ \"$set\" : {\"discountPercentage\": 30} }";
				i++;
			}
			else if(type==3){
				js="{ \"$set\" : {\"discountPercentage\": 55} }";
				i++;
			}
			//else continue;
			System.out.println(dbObject);
			DBObject dbObject2=(DBObject)JSON.parse(js);
			collectionc.update(dbObject, dbObject2);
			System.out.println(type+" "+i);
			//System.out.println(new Mongoi().doFindOne(Mongoi.CLIENTS, "{ \"code\":\""+dbObject.get("code").toString()+"\"}"));
		}
		System.out.println(cursorc.count());
		System.out.println(reidlist.size());
		System.out.println(noidlist.size());
		//System.out.println(i);
	}
	
	//adds new field providerPrice 
	private static void setProviderPriceAndGainPercentField(){
		DBCursor cursor=new Mongoi().doFind(Mongoi.PRODUCTS);//.limit(1000);
		DBCollection collection = new Mongoi().getCollection(Mongoi.PRODUCTS);
		System.out.println(cursor.count());
		int i=0;
		while(cursor.hasNext()){
			DBObject dbObject=cursor.next();
			int kind=new Integer(dbObject.get("productPriceKind").toString());
			float price=new Float(dbObject.get("unitPrice").toString());
			if(kind==1){
				System.out.println(kind+": "+price+" -> "+price/1.3f);
				System.out.println(dbObject);
				
				String js="{ \"$set\" : {\"providerPrice\": "+(price/1.3f)+"} }";
				//DBObject dbObject=(DBObject)JSON.parse(matches);
				DBObject dbObject2=(DBObject)JSON.parse(js);
				collection.update(dbObject, dbObject2);
				System.out.println(new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"id\":"+dbObject.get("id").toString()+"}"));
				
				js="{ \"$set\" : {\"percentageGain\": 30 } }";
				//DBObject dbObject=(DBObject)JSON.parse(matches);
				dbObject2=(DBObject)JSON.parse(js);
				collection.update(dbObject, dbObject2);
				System.out.println(new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"id\":"+dbObject.get("id").toString()+"}"));
				
				i++;
			}
			else{
				System.out.println(kind+": "+price+" -> "+price/1.15f);
				System.out.println(dbObject);
				
				String js="{ \"$set\" : {\"providerPrice\": "+(price/1.15f)+"} }";
				//DBObject dbObject=(DBObject)JSON.parse(matches);
				DBObject dbObject2=(DBObject)JSON.parse(js);
				collection.update(dbObject, dbObject2);
				System.out.println(new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"id\":"+dbObject.get("id").toString()+"}"));
				
				js="{ \"$set\" : {\"percentageGain\": 15 } }";
				//DBObject dbObject=(DBObject)JSON.parse(matches);
				dbObject2=(DBObject)JSON.parse(js);
				collection.update(dbObject, dbObject2);
				System.out.println(new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"id\":"+dbObject.get("id").toString()+"}"));
				
				i++;
			}
			//System.out.println(cursor.next());
			//System.out.println(i);
		}
		System.out.println(cursor.count());
		System.out.println(i);
	}
	
	private static void fixProductVersion(){
		DBCursor cursor=new Mongoi().doFind(Mongoi.PRODUCTS);//.limit(1000);
		DBCollection collection = new Mongoi().getCollection(Mongoi.PRODUCTS);
		System.out.println(cursor.count());
		int i=0;
		while(cursor.hasNext()){
			DBObject dbObject=cursor.next();
			if(dbObject.containsField("priceHistory")){
				System.out.println(dbObject);
				
				String js="{ \"$set\" : {\"version\": 1} }";
				//DBObject dbObject=(DBObject)JSON.parse(matches);
				DBObject dbObject2=(DBObject)JSON.parse(js);
				collection.update(dbObject, dbObject2);
			}
			else{
				System.out.println(dbObject);
				
				String js="{ \"$set\" : {\"version\": 0} }";
				//DBObject dbObject=(DBObject)JSON.parse(matches);
				DBObject dbObject2=(DBObject)JSON.parse(js);
				collection.update(dbObject, dbObject2);
			}
			//System.out.println(cursor.next());
			i++;
			//System.out.println(i);
		}
		System.out.println(i);
	}
}
