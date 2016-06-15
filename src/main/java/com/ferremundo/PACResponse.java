package com.ferremundo;

public class PACResponse {

	private boolean success;
	private String message;
	private String invoice;
	private String responseCode;
	
	public PACResponse(boolean success, String message, String invoice, String responseCode) {
		this.success = success;
		this.message = message;
		this.invoice = invoice;
		this.responseCode=responseCode;
	}

	public boolean isSuccess() {
		return success;
	}
	
	public String getResponseCode(){
		return responseCode;
	}
	
	public String getMessage() {
		return message;
	}

	public String getInvoice() {
		return invoice;
	}	
	
}
