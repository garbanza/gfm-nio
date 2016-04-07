package com.ferremundo.gth;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.lowagie.text.Paragraph;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.util.JSON;

public class Tst {
	public static void main(String[] args){
		Mongo m = null;
		try {
			m = new Mongo("localhost");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DB db = m.getDB("testdb");
		int reference=180551;
		DBCollection collection = db.getCollection("reference");
		
		DBObject dbObject=(DBObject)JSON.parse("{\"reference\" : "+reference+"}");
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("reference", reference);
		BasicDBObject newDocument3 = new BasicDBObject().append("$set", new BasicDBObject().append("reference", reference));
		collection.update(new BasicDBObject().append("reference", "101DB"), newDocument3);

		System.out.println(collection.getCount());
		System.out.println(dbObject);
		
		BasicDBObject query = new BasicDBObject();
		query.put("reference", "101DB");
		DBCursor cursor = collection.find(query);
		while(cursor.hasNext()) {
		    System.out.println(cursor.next());
		}

	}
		
}
