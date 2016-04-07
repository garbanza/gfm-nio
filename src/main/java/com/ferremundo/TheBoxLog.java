package com.ferremundo;

public class TheBoxLog {

	private float cash;
	private long date;
	private String reference;
	private String concept;
	private String shopman;
	
	public TheBoxLog(float cash,long date,String reference, String concept,String shopman){
		this.cash=cash;
		this.date=date;
		this.reference=reference;
		this.concept=concept;
		this.shopman=shopman;
	}

	public float getCash() {
		return cash;
	}

	public long getDate() {
		return date;
	}

	public String getReference() {
		return reference;
	}

	public String getConcept() {
		return concept;
	}
	
	public String getShopman() {
		return shopman;
	}
	
}
