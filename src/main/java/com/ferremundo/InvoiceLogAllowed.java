package com.ferremundo;

public class InvoiceLogAllowed{
	private boolean allowed;
	private String message;
	
	public InvoiceLogAllowed(boolean allowed, String message){
		this.allowed=allowed;
		this.message=message;
	}
	
	public boolean isAllowed(){
		return allowed;
	}
	public String getMessage(){
		return message;
	}
	
}