package com.ferremundo;

public class PACResponse {

	private boolean success;
	private String message;
	private String invoice;
	
	public PACResponse(boolean success, String message, String invoice) {
		this.success = success;
		this.message = message;
		this.invoice = invoice;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public String getInvoice() {
		return invoice;
	}	
	
}
