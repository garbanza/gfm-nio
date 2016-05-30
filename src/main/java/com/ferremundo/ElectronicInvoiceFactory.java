package com.ferremundo;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

import com.ferremundo.stt.GSettings;

import mx.bigdata.sat.cfdi.CFDv32;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante;
import mx.bigdata.sat.cfdi.v32.schema.ObjectFactory;
import mx.bigdata.sat.cfdi.v32.schema.TUbicacion;
import mx.bigdata.sat.cfdi.v32.schema.TUbicacionFiscal;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Conceptos;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Emisor;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Receptor;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Conceptos.Concepto;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Emisor.RegimenFiscal;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos.Traslados;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante.Impuestos.Traslados.Traslado;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;

public class ElectronicInvoiceFactory {

	
	public static String gen(Invoice invoice){
		GSettings g=GSettings.instance();
		
		ObjectFactory of = new ObjectFactory();
	    Comprobante comp = of.createComprobante();
	    comp.setVersion("3.2");
	    comp.setFecha(new Date());
	    comp.setMetodoDePago("INDEFINIDO");
	    comp.setFormaDePago("PAGO EN UNA SOLA EXHIBICION");
	    
	    Conceptos cps = of.createComprobanteConceptos();
	    List<Concepto> list = cps.getConcepto();
	    float iva=new Float(GSettings.get("TAXES_IVA_VALUE"));
	    float subTotal=0;
	    for(InvoiceItem invoiceItems : invoice.getItems()){
	    	float unitPrice=Math.round(100*invoiceItems.getUnitPrice()/(1+iva/100))/100;
	    	float quantity=invoiceItems.getQuantity();
	    	float total=unitPrice*quantity;
	    	subTotal+=total;
	    	String unit=invoiceItems.getUnit();
	    	String description=invoiceItems.getDescription();
	    	Concepto c1 = of.createComprobanteConceptosConcepto();	
	    	c1.setUnidad(unit);
		    c1.setImporte(new BigDecimal(total));
		    c1.setCantidad(new BigDecimal(quantity));
		    c1.setDescripcion(description);
		    c1.setValorUnitario(new BigDecimal(unitPrice));
		    list.add(c1);	
	    }
	    comp.setSubTotal(new BigDecimal(subTotal));
	    float total=Math.round(100*subTotal*(1+iva/100))/100;
	    comp.setTotal(new BigDecimal(total));
	    comp.setTipoDeComprobante("INGRESO");
	    
	    Emisor emisor = of.createComprobanteEmisor();
	    emisor.setNombre(g.getKey("INVOICE_SENDER_NAME"));
	    emisor.setRfc(g.getKey("INVOICE_SENDER_TAX_CODE"));
	    TUbicacionFiscal uf = of.createTUbicacionFiscal();
	    uf.setCalle(g.getKey("INVOICE_SENDER_STREET"));
	    uf.setCodigoPostal(g.getKey("INVOICE_SENDER_POSTAL_CODE"));
	    uf.setColonia(g.getKey("INVOICE_SENDER_SUBURB")); 
	    uf.setEstado(g.getKey("INVOICE_SENDER_STATE")); 
	    uf.setMunicipio(g.getKey("INVOICE_SENDER_CITY")); 
	    uf.setNoExterior(g.getKey("INVOICE_SENDER_EXTERIOR_NUMBER"));
	    uf.setNoInterior(g.getKey("INVOICE_SENDER_INTERIOR_NUMBER"));
	    uf.setPais(g.getKey("INVOICE_SENDER_COUNTRY")); 
	    emisor.setDomicilioFiscal(uf);
	    TUbicacion u = of.createTUbicacion();
	    u.setCalle(g.getKey("INVOICE_SENDER_STREET"));
	    u.setCodigoPostal(g.getKey("INVOICE_SENDER_POSTAL_CODE"));
	    u.setColonia(g.getKey("INVOICE_SENDER_SUBURB")); 
	    u.setEstado(g.getKey("INVOICE_SENDER_STATE")); 
	    u.setMunicipio(g.getKey("INVOICE_SENDER_CITY")); 
	    u.setNoExterior(g.getKey("INVOICE_SENDER_EXTERIOR_NUMBER"));
	    u.setNoInterior(g.getKey("INVOICE_SENDER_INTERIOR_NUMBER"));
	    u.setPais(g.getKey("INVOICE_SENDER_COUNTRY")); 
	    emisor.setExpedidoEn(u); 
	    RegimenFiscal rf = of.createComprobanteEmisorRegimenFiscal();
	    rf.setRegimen(g.getKey("INVOICE_SENDER_REGIME"));
	    emisor.getRegimenFiscal().add(rf);
	    
	    
	    Client c=invoice.getClient();
	    Receptor receptor = of.createComprobanteReceptor();
	    receptor.setNombre(c.getConsummer());
	    receptor.setRfc(c.getRfc());
	    TUbicacion uf2 = of.createTUbicacion();
	    uf2.setCalle(c.getAddress());
	    uf2.setCodigoPostal(c.getCp());
	    uf2.setColonia(c.getSuburb()); 
	    uf2.setEstado(c.getState()); 
	    uf2.setMunicipio(c.getCity()); 
	    uf2.setNoExterior(c.getExteriorNumber()); 
	    uf2.setNoInterior(c.getInteriorNumber()); 
	    uf2.setPais(c.getCountry()); 
	    receptor.setDomicilio(uf2);
	    
	    Impuestos imps = of.createComprobanteImpuestos();
	    Traslados trs = of.createComprobanteImpuestosTraslados();
	    List<Traslado> list2 = trs.getTraslado(); 
	    Traslado t2 = of.createComprobanteImpuestosTrasladosTraslado();
	    t2.setImporte(new BigDecimal(total-subTotal));
	    t2.setImpuesto(g.getKey("TAXES_IVA_NAME"));
	    t2.setTasa(new BigDecimal(g.getKey("TAXES_IVA_VALUE")));
	    list2.add(t2);
	    imps.setTraslados(trs);
	    imps.setTotalImpuestosTrasladados(new BigDecimal(total-subTotal));
	    
	    comp.setLugarExpedicion(g.getKey("INVOICE_SENDER_CITY"));
	    
	    CFDv32 cfd = new CFDv32(comp); // Crea el CFD a partir de un archivo
		//cfd.
		PrivateKey key = KeyLoaderFactory.createInstance(
	            KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
	            new FileInputStream("/opt/fm/auth/COSB760826KV8_1301311654S-parasello.key"),
	            "Lachirgaquetepari0"
	    ).getKey();
		X509Certificate cert = KeyLoaderFactory.createInstance(
	            KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
	            new FileInputStream("/opt/fm/auth/COSB760826KV8_1301311654S-SELLO.cer")
	    ).getKey();
	    Comprobante sellado = cfd.sellarComprobante(key, cert);
	    //System.out.println(sellado.getSello());
	    cfd.validar();
	    cfd.verificar();
	    cfd.guardar(System.out);
	    CFDv32 cfd2 = new CFDv32(sellado);
	    
	}
}
