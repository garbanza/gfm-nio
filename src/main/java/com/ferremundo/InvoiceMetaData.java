package com.ferremundo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ferremundo.db.Mongoi;

public class InvoiceMetaData {
	

	
	private int invoiceType;
	private String date;
	private String reference="notfixed";
	private int serial=-1;
	
	private InvoiceMetaData(){}
	
	public InvoiceMetaData(int invoiceType, boolean persist) {
		this.invoiceType=invoiceType;
		date= ((DateFormat)(new SimpleDateFormat("dd/MM/yyyy"))).format(new Date((new Date()).getTime() ));
		if(persist){
			reference=REF.next36();
			if(invoiceType==Invoice.INVOICE_TYPE_TAXES_APLY)serial=InvoiceSerial.next();
		}
	}
	
	public InvoiceMetaData(int invoiceType){
		this.invoiceType=invoiceType;
		this.date=new Date().getTime()+"";
		int count=new Mongoi().doIncrement(Mongoi.REFERENCE, "{ \"reference\" : \"unique\" }", "count");
		this.reference=(count+"").toUpperCase();//Integer.toString(count,Character.MAX_RADIX).toUpperCase();
		serial=0;
	}
	
	public InvoiceMetaData(int invoiceType, String date, String reference, int serial) {
		this.invoiceType = invoiceType;
		this.date = date;
		this.reference = reference;
		this.serial = serial;
	}

	public int getInvoiceType() {
		return invoiceType;
	}
	public String getDate() {
		return date;
	}
	public String getReference() {
		return reference;
	}
	
	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public void setDate(String date){
		this.date=date;
	}
	
	public String toJson(){
		return "{"+
		"\"invoicetype\":\""+invoiceType+"\","+
		"\"date\":\""+date+"\","+
		"\"serial\":\""+serial+"\","+
		"\"reference\":\""+reference+"\"}";

	}
	
	public static void main(String[] args) {
		Date date= new Date();
		System.out.println(date.getTime());
	}

}
