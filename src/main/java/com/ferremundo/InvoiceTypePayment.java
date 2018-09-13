package com.ferremundo;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jsoup.Jsoup;

import com.ferremundo.db.Mongoi;
import com.google.gson.Gson;

import mx.bigdata.sat.common.pagos.schema.Pagos;
import mx.bigdata.sat.common.pagos.schema.Pagos.Pago;
import mx.bigdata.sat.common.pagos.schema.Pagos.Pago.DoctoRelacionado;

public class InvoiceTypePayment {

	public int version=4;
	
    public Long id;
    
    public String reference;
    
    public String series;
    
    public long dateTimePayment;
    
    public int invoiceType=Invoice.INVOICE_TYPE_PAYMENT;
    
    public long serial=8;
	
	public long creationTime;
	
	public float total;
	public float subTotal;
	
	public InvoiceElectronicVersion electronicVersion;
	
	public boolean hasElectronicVersion=false;


	public String paymentWay;

	public String documentType="P";

	public String coin;
	
	public Client client;
	
	public Shopman shopman;
	
	public List<InvoiceLog> logs;
	
	public DoctoRelacionado[] relatedDocs;
	
	public List<InvoiceItem> items=new LinkedList<InvoiceItem>();
	
	public float amount;
	
	public String operationNumber;
	
	public String[] emails;
	
	public InvoiceTypePayment(Client client, Shopman shopman,
			String reference,
			String series,
			float amount,
			Long dateTimePayment,
			String paymentWay,
			String operationNumber,
			DoctoRelacionado[] relatedDocs,
			List<InvoiceItem> items,
			String coin,
			String[] emails) {
		/**TODO implement commented fields*/
		Log log=new Log();
		System.out.println("after log");
		log.entry(client, shopman, reference, series, amount, dateTimePayment, paymentWay, operationNumber, relatedDocs, items, coin, emails);
		
		System.out.println("creating invoice");
		this.client = client;
		this.shopman = shopman;
		this.shopman.setPassword("");
		this.reference=reference;//metaData.getReference();
		this.series = series;
		this.amount = amount;
		this.dateTimePayment = dateTimePayment;
		this.paymentWay = paymentWay;
		if(operationNumber!=null || ! operationNumber.equals(""))this.operationNumber = operationNumber;
		this.relatedDocs = relatedDocs;
		this.items = items;
		this.documentType="P";
		this.coin=coin;
		this.emails=emails;
		
		InvoiceLog created=new InvoiceLog(InvoiceLog.LogKind.CREATED,true,shopman.getLogin());
		//InvoiceLog created=new InvoiceLog(InvoiceLog.LogKind.CREATED,true);
		this.creationTime=(Long)created.getDate();
		//metaData.setDate(creationTime+"");
		
		logs=new ArrayList<InvoiceLog>(Arrays.asList(
				new InvoiceLog(InvoiceLog.LogKind.CREATED,true,shopman.getLogin())
		));
		this.total=0;
		this.subTotal=0;
	}

	public static void main(String[] args) {
		XMLGregorianCalendar xmlGregorian=null;
	    DateFormat format = null;
	    String dateg="2018-06-06T15:12:11";
	    try {
			format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Long long_ = null;
			long_ = format.parse(dateg).getTime();
			System.out.println(dateg);
			xmlGregorian = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(format.parse(dateg)));
			System.out.println(xmlGregorian.toString());
			xmlGregorian = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(new Date(long_)));
			System.out.println(xmlGregorian.toString());
	    } catch (DatatypeConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	public void persist(){
		String json=new Gson().toJson(this);
		new Mongoi().doInsert(Mongoi.INVOICES, json);
	}
	
}
