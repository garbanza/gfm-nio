package com.ferremundo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

import com.ferremundo.stt.GSettings;
import com.lowagie.text.Rectangle;

public class Sampling implements InvoiceHandler {
	
	InvoiceForm form=null;
	
	public Sampling(Invoice invoice) throws FileNotFoundException{
		form=new InvoiceFormFM01(new FileInputStream(GSettings.get("INVOICE_FORM_DESCRIPTOR")));
		File pdf=new PDF(invoice, form, GSettings.get("TMP_FOLDER")+invoice.getReference()+".pdf").make();
	}
	
	public void start(){
		
	}
	
	private static void update(ClientSample clientSample){
		
	}
	
	private static void update(ProductSample productSample){
		
	}
	
	public static void main(String[] args) {
		URL url=Thread.currentThread().getContextClassLoader().getResource(".");
		System.out.println(url.getPath());
	}
	
	public static String getPath() throws URISyntaxException{
		URL u = Sampling.class.getProtectionDomain().getCodeSource().getLocation();
		//String path = Sampling.class.getRealPath("WEB-INF");
		return u.toURI().toString();
	}

}
