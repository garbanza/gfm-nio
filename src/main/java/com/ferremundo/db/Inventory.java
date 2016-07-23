package com.ferremundo.db;

import com.ferremundo.InvoiceItem;
import com.ferremundo.Product;
import com.mongodb.DBObject;

public abstract class Inventory {
	
	public static void incrementStored(Product product,float amount){
		incrementStored(product.getCode(), amount);
	}
	
	public static void incrementStored(String code,float amount){
		String c=code.replaceAll("\\.", "");
		new Mongoi().doIncrement(Mongoi.PRODUCTS, "{ \"code\" : \""+c+"\" }", "stored", amount);
	}
	
	public static void decrementStored(String code,float amount){
		String c=code.replaceAll("\\.", "");
		new Mongoi().doIncrement(Mongoi.PRODUCTS, "{ \"code\" : \""+c+"\" }", "stored", -amount);
	}
	
	public static void incrementStored(InvoiceItem item){
		incrementStored(item.getCode(),item.getQuantity());
	}
	
	public static void decrementStored(InvoiceItem item){
		decrementStored(item.getCode(),item.getQuantity());
	}
	
	public static boolean exists(long id){
		DBObject dbObject=new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"id\" : "+id+" }");
		if(dbObject!=null)return true;
		return false;
	}
	
	public static boolean exists(String code){
		DBObject dbObject=new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\" }");
		if(dbObject!=null)return true;
		return false;
	}
	
	public static boolean exists(InvoiceItem item){
		if(exists(item.getId()))return true;
		else if(exists(item.getCode().replace("\\.", "")))return true;
		return false;
	}
	
}
