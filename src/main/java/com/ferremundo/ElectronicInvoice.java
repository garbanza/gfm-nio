package com.ferremundo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.ferremundo.db.Mongoi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.DBObject;

public interface ElectronicInvoice {
	public PACResponse submit(boolean production);
	public PACResponse cancel(boolean production);
/* 
	private Rhino rhino= new Rhino(cerPath, keyPath, keyPass);
	
	private Rhino inflateRhino(Invoice invoice){
		
		rhino.setOpenSSL("/usr/bin/openssl");
		setGenerales(invoice);
		rhino.setEmisor(
				"FM",
                "RFC",
                
                rhino.generarDatosFiscales("MEXICO", "", "", "", "", "", "", ""),
                "INTERMEDIO");
		Client c=invoice.getClient();
		rhino.setReceptor(
				c.getConsummer(),
				c.getRfc(),
				//rhino.generarDatosUbicacion("MEXICO")
				rhino.generarDatosUbicacion(c.getCountry(), c.getState(), c.getCity(), c.getSuburb(), c.getAddress(), c.getExteriorNumber(), c.getInteriorNumber(), c.getCp(), c.getLocality())
				);
		
		List<InvoiceItem> items=invoice.getItems();
		for(InvoiceItem item : items){
			rhino.agregaConcepto(
					item.getDescription(),
					item.getUnit(),
					item.getQuantity()+"",
					item.getUnitPrice()+""
					);
			
		}
		return rhino;
	}
	
	private void setGenerales(Invoice invoice){
		//TODO HARDCODED HERE
		String serie="TEST-";
		String folio=invoice.getReference();
		Date fecha=new Date(invoice.getCreationTime());
		String formaDePago="PAGO EN UNA SOLA EXHIBICION";
		String metodoPago="INDEFINIDO";
		String tipoComprobante="ingreso";
		String lugarExpedicion="";
		String total=invoice.getTotal()+"";
		String subtotal=invoice.getSubtotal()+"";
		//String numCtaPago="";
		//String tipoCambio="0";
		//String moneda="MXN";
		//String descuento="0";
		//String motivoDescuento="";
		rhino.setGenerales(serie, folio, fecha, formaDePago, metodoPago, tipoComprobante, lugarExpedicion, total, subtotal);
	}
	
	public ElectronicInvoice(Invoice invoice){
		inflateRhino(invoice);
	}
	
	public String create(){
		String ret=rhino.timbrarPrueba("", "");
		return ret;
	}
	public static void main(String[] args) {
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		DBObject dbo=new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"reference\" : \"191517\"}");
		InvoiceFM01 fm01=new Gson().fromJson(dbo.toString(), InvoiceFM01.class);
		ElectronicInvoice test= new ElectronicInvoice(fm01);
		Rhino rhino=test.inflateRhino(fm01);
		
		String ret=rhino.timbrarPrueba("", "");
		System.out.println(gson.toJson(rhino));
		System.out.println(ret);
		
	}
*/

}

