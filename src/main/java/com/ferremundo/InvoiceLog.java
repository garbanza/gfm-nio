package com.ferremundo;

import java.util.Date;

import com.google.gson.Gson;

public class InvoiceLog {
	
	public static enum LogKind{
		CREATED,
		PAYMENT,
		AGENT_PAYMENT,
		FACTURE,
		CLOSE,
		CANCEL,
		PAST_DUE,
		AGENT_INCREMENT_EARNINGS
	}
	
	private LogKind kind;
	private Long date=new Date().getTime();
	private Object value;
	private String login;
	
	public InvoiceLog(LogKind kind,Object value,String login){
		this.kind=kind;
		this.value=value;
		this.login=login;
	}

	public LogKind kind() {
		return kind;
	}

	public Object getValue() {
		return value;
	}
	
	public Long getDate() {
		return date;
	}
	
	public String getLogin(){
		return login;
	}
	
	public static void main(String[] args) {
		InvoiceLog log=new InvoiceLog(LogKind.PAYMENT,12.04f,"login");
		System.out.println(new Gson().toJson(log));
		//System.out.println(log.kind()+" : "+((Float)log.getValue())+" : "+log.getDate());
	}
	
}
