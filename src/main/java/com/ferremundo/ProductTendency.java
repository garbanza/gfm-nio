package com.ferremundo;

import java.util.Date;
import java.util.List;

import com.ferremundo.db.Mongoi;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

public class ProductTendency {

	private Long id;
	
	private double quantity;
	
	private long time;
	
	private String login;

	public ProductTendency(Long id, double quantity, long time, String login) {
		this.id = id;
		this.quantity = quantity;
		this.time = time;
		this.login=login;
	}
	
	public WriteResult persist(){
		String js=new Gson().toJson(this);
		WriteResult wr=new Mongoi().doInsert(Mongoi.PRODUCT_TENDENCY,js);
		return wr;
	}
	
	public static DBCursor tendency(Long id,int days){
		DBCursor cursor=new Mongoi().doFindGreaterThan(Mongoi.PRODUCT_TENDENCY, "time", 
				new Date().getTime()-days*1000*60*60*24L).sort(new BasicDBObject("time",1));
		List<DBObject> list=cursor.toArray();
		float[] freq=new float[days];
		for(int i=0;i<days;i++){
			for(int j=0;j<list.size();j++){
				DBObject dbObject=list.get(j);
				
			}
		}
		//TODO
		return null;
	}
	
}
