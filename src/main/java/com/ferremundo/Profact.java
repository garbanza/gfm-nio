package com.ferremundo;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.apache.axis.AxisFault;
import org.apache.commons.lang.StringEscapeUtils;

import com.ferremundo.stt.GSettings;
import com.profact.TimbradoSoapStub;

public class Profact implements ElectronicInvoice {

	String invoice64;
	Invoice invoice;
	public Profact(Invoice invoice) {
		invoice64=ElectronicInvoiceFactory.genCFDBase64Binary(invoice);
		this.invoice=invoice;
	}
	
	@Override
	public PACResponse submit(boolean production) {
		if(production)return submitProduction();
		return submitTest();
	}
	public PACResponse submitProduction() {
		GSettings g = GSettings.instance();
		Object[] objects=null;
		try {
			objects = new TimbradoSoapStub(new URL(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_CFDI_INPUT")),null)
					.timbraCFDI(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_USER"), invoice64, invoice.getReference());
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if((int) objects[1]==0){
			String xml=StringEscapeUtils.unescapeJava((String)objects[3]);
			String message=(String)objects[2];
			String code=(int)objects[1]+"";
			boolean success=true;
			return new PACResponse(success, message, xml, code);
		}
		String xml="<null></null>";
		String message=(String)objects[2];
		String code=(int)objects[1]+"";
		boolean success=false;
		return new PACResponse(success, message, xml, code);
	}
	public PACResponse submitTest() {
		GSettings g = GSettings.instance();
		Object[] objects=null;
		try {
			objects = new test.profact.TimbradoSoapStub(new URL(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_CFDI_INPUT")),null)
					.timbraCFDI(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_USER"), invoice64, invoice.getReference());
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if((int) objects[1]==0){
			String xml=StringEscapeUtils.unescapeJava((String)objects[3]);
			String message=(String)objects[2];
			String code=(int)objects[1]+"";
			boolean success=true;
			return new PACResponse(success, message, xml, code);
		}
		String xml="<null></null>";
		String message=(String)objects[2];
		String code=(int)objects[1]+"";
		boolean success=false;
		return new PACResponse(success, message, xml, code);
	}

}
