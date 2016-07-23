package com.ferremundo;

public class PACResponse {

	private boolean success;
	private String message;
	private String content;
	private String responseCode;
	
	public PACResponse(boolean success, String message, String content, String responseCode) {
		this.success = success;
		this.message = message;
		this.content = content;
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

	public String getContent() {
		return content;
	}	
	
}
