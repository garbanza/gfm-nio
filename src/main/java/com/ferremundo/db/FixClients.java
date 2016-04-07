package com.ferremundo.db;

import org.json.JSONException;
import org.json.JSONObject;

import com.ferremundo.Client;
import com.ferremundo.ClientFactory;
import com.google.gson.Gson;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class FixClients {
/*
	public static void main(String[] args) {
		DBCursor cursor=new Mongoi().doFind(Mongoi.CLIENTS);
		while(cursor.hasNext()){
			DBObject dbObject=cursor.next();
			String code=dbObject.get("code").toString();
			
			Client client= Client.fromDBObject(dbObject);
			String newCode=client.getHash();
			
			DBCursor matches=new Mongoi().doFindMatches(Mongoi.INVOICES, new String[]{"client.code"}, new String[]{dbObject.get("code").toString()});
			new Mongoi().doUpdate(Mongoi.CLIENTS, "{ \"code\" : \""+code+"\"}", "{ \"code\" : \""+newCode+"\"}");
			if(matches!=null){
				while(matches.hasNext()){
					DBObject invoice=matches.next();
					
					new Mongoi().doUpdate(Mongoi.INVOICES, "{ \"reference\" : \""+invoice.get("reference").toString()+"\"}", "{ \"client.code\" : \""+newCode+"\"}");
					
					System.out.println(code+" -> "+invoice);
					
				}
			}
			//new Mongoi().doUpdate(Mongoi.CLIENTS, "{ \"code\" : \""+code+"\"}", "{ \"code\" : \""+newCode+"\"}");
			//System.out.println(new Mongoi().doFindMatches(Mongoi.CLIENTS, new String[]{"code"}, new String[]{dbObject.get("code").toString()}).next());
			//System.out.println(new Gson().toJson(client));
		}
	}
	*/
}
