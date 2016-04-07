package com.ferremundo.db;

import java.util.LinkedList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class FilterDBObject {

	public static DBObject keep(String[] keys, DBObject dbObject){
		BasicDBObject basicDBObject= new BasicDBObject();
		for(String st : keys){
			basicDBObject.put(st, dbObject.get(st));
		}
		return basicDBObject;
	}
	
	public static List<DBObject> keep(String[] keys, List<DBObject> list){
		List<DBObject> list2= new LinkedList<DBObject>();
		for(DBObject dbObject : list){
			list2.add(keep(keys, dbObject));
		}
		return list2;
	}
}
