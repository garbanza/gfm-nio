package com.ferremundo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.ferremundo.db.Mongoi;
import com.mongodb.BasicDBList;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class TheBox {
	
	private float cash=0;
	private List<TheBoxLog> boxLogs=new LinkedList<TheBoxLog>();
	private static TheBox tb=null;
	
	private TheBox(){
		try{
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			Date today = null;
			try {
				today=dateFormat.parse(dateFormat.format(new Date()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DBCursor cursor=new Mongoi().doFindGreaterThan(Mongoi.INVOICES, "updated", today.getTime());
			while(cursor.hasNext()){
				DBObject document=cursor.next();
				BasicDBList logs=(BasicDBList)document.get("logs");
				if(logs!=null){
					for(int i=0;i<logs.size();i++){
						DBObject log=(DBObject)logs.get(i);
						String kind=(String)log.get("kind");
						if(kind.equals(InvoiceLog.LogKind.PAYMENT.toString())){
							Float value=new Float(log.get("value").toString());
							cash+=value;
							TheBoxLog boxLog=new TheBoxLog(value, 
									new Long(log.get("date").toString()), 
									document.get("reference").toString(), 
									InvoiceLog.LogKind.PAYMENT.toString(),
									log.get("login").toString()
								);
							addLog(boxLog);
						}
						else if(kind.equals(InvoiceLog.LogKind.AGENT_PAYMENT.toString())){
							Float value=new Float(log.get("value").toString());
							cash-=value;
							TheBoxLog boxLog=new TheBoxLog(value, 
									new Long(
											log.get("date").toString()), 
											document.get("reference").toString(), 
											InvoiceLog.LogKind.AGENT_PAYMENT.toString(),
											log.get("login").toString()
											);
							addLog(boxLog);
						}
					}
				}
			}
		
		}
		catch(Exception e){
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public static TheBox instance(){
		if(tb==null)tb=new TheBox();
		return tb;
	}
	
	public void plus(float value){
		cash+=value;
	}
	
	public void minus(float value){
		cash-=value;
	}
	
	public float account(){
		return cash;
	}
	
	public void addLog(TheBoxLog boxLog){
		this.boxLogs.add(boxLog);
	}
	
	public List<TheBoxLog> getLogs(){
		return boxLogs; 
	}
	
}
