package com.ferremundo;

import com.ferremundo.db.Mongoi;
import com.google.gson.Gson;
import com.mongodb.WriteResult;

public class ProductProviderPricesHistory {

	private Long id;
	
	private double quantity;
	
	private double price;
	
	private long time;

	private String login;
	
	public ProductProviderPricesHistory(long id, double quantity, double price, long time,String login) {
		this.id = id;
		this.quantity=quantity;
		this.price = price;
		this.time = time;
		this.login=login;
	}
	
	public WriteResult persist(){
		String js=new Gson().toJson(this);
		WriteResult wr=new Mongoi().doInsert(Mongoi.PRODUCT_PROVIDER_PRICES_HISTORY,js);
		return wr;
	}
	
}
