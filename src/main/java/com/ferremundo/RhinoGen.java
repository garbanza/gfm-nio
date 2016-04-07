package com.ferremundo;

import java.util.Date;
import java.util.List;

import com.ferremundo.db.Mongoi;
import com.ferremundo.stt.GSettings;
import com.google.gson.Gson;
import com.mongodb.DBObject;

import mx.nafiux.Rhino;

public class RhinoGen {
	
	public static String gen(Invoice invoice){
		GSettings g=GSettings.instance();
		Rhino rhino= new Rhino(
				g.getKey("CERTIFICATE"),
				g.getKey("PRIVATE_KEY"),
				g.getKey("PRIVATE_KEY_PASS"));
		rhino.setOpenSSL(g.getKey("SSL"));
		
		//TODO HARDCODED HERE
		String serie=g.getKey("INVOICE_SERIAL");
		String folio=invoice.getReference();
		Date fecha=new Date(invoice.getCreationTime());
		String formaDePago="PAGO EN UNA SOLA EXHIBICION";
		String metodoPago="INDEFINIDO";
		String tipoComprobante="ingreso";
		String lugarExpedicion="MORELIA";
		String total=invoice.getTotal()+"";
		String subtotal=invoice.getSubtotal()+"";
		//String numCtaPago="";
		//String tipoCambio="0";
		//String moneda="MXN";
		//String descuento="0";
		//String motivoDescuento="";
		
		rhino.setGenerales(serie, folio, fecha, formaDePago, metodoPago, tipoComprobante, lugarExpedicion, total, subtotal);
		rhino.setEmisor(
				g.getKey("INVOICE_SENDER_NAME"),
				g.getKey("INVOICE_SENDER_TAX_CODE"),
                rhino.generarDatosFiscales(
                		g.getKey("INVOICE_SENDER_COUNTRY"),//MEXICO",
                		g.getKey("INVOICE_SENDER_STATE"),
                		g.getKey("INVOICE_SENDER_CITY"),
                		g.getKey("INVOICE_SENDER_SUBURB"),
                		g.getKey("INVOICE_SENDER_STREET"),
                		g.getKey("INVOICE_SENDER_EXTERIOR_NUMBER"),
                		g.getKey("INVOICE_SENDER_INTERIOR_NUMBER"),
                		g.getKey("INVOICE_SENDER_POSTAL_CODE")),//"58020"),
                g.getKey("INVOICE_SENDER_REGIME"));//"INTERMEDIO");
		Client c=invoice.getClient();
		rhino.setReceptor(
				c.getConsummer(),
				c.getRfc(),
				//rhino.generarDatosUbicacion("MEXICO")
				rhino.generarDatosUbicacion(c.getCountry(), c.getState(), c.getCity(), c.getSuburb(), c.getAddress(), c.getExteriorNumber(), c.getInteriorNumber(), c.getCp(), c.getLocality())
				);
		String taxesKind=g.getKey("TAXES_IVA_NAME");
		String taxesRate=g.getKey("TAXES_IVA_VALUE");
		rhino.setTraslado(taxesKind, taxesRate, invoice.getTaxes()+"");
		List<InvoiceItem> items=invoice.getItems();
		for(InvoiceItem item : items){
			rhino.agregaConcepto(
					item.getDescription(),
					item.getUnit(),
					item.getQuantity()+"",
					item.getUnitPrice()+""
					);
			
		}
		System.out.println("RHINO: "+new Gson().toJson(rhino));
		String ret=rhino.timbrar(
				g.getKey("INVOICE_CERTIFICATE_AUTHORITY_USER"),
				g.getKey("INVOICE_CERTIFICATE_AUTHORITY_PASS"));
		return ret;
	}

	
	public static void main(String[] args) {
		DBObject dbo=new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"reference\" : \"191518\"}");
		InvoiceFM01 fm01=new Gson().fromJson(dbo.toString(), InvoiceFM01.class);
		String resp=RhinoGen.gen(fm01);
		System.out.println(dbo);
		System.out.println(resp);
	}
}
