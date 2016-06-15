package com.ferremundo;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

public class ElectronicInvoiceFactory {

	
	public static String genCFDBase64Binary(Invoice invoice){
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
	    
	    CFDv32 cfd=null;
		try {
			cfd = new CFDv32(comp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Crea el CFD a partir de un archivo
		//cfd.
		PrivateKey key=null;
		try {
			key = KeyLoaderFactory.createInstance(
			        KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
			        new FileInputStream(g.getPathTo("PRIVATE_KEY")),
			        g.getKey("PRIVATE_KEY_PASS")
			).getKey();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		X509Certificate cert=null;
		try {
			cert = KeyLoaderFactory.createInstance(
			        KeyLoaderEnumeration.PUBLIC_KEY_LOADER,
			        new FileInputStream(g.getPathTo("CERTIFICATE"))
			).getKey();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			cfd.sellarComprobante(key, cert);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //System.out.println(sellado.getSello());
		ByteArrayOutputStream out= new ByteArrayOutputStream();
		InputStream in=null;
		try {
			cfd.validar();
			cfd.verificar();
		    cfd.guardar(out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return DatatypeConverter.printBase64Binary(out.toString().getBytes());
	    //CFDv32 cfd2 = new CFDv32(sellado);
	    
	}
	
	public static void genQRCode(String emisor, String receptor, float total, String uuid){
		String re="?re="+emisor.toUpperCase();
		String rr="&rr="+receptor.toUpperCase();
		DecimalFormat formatter = new DecimalFormat("0000000000.000000");
		String tt="&tt="+formatter.format(total);
		String id="&id="+uuid;
		String toqrc=re+rr+tt+id;
		String path=GSettings.getPathTo("TMP_FOLDER")+id+".qrc.png";
		try {
			QRCode.from(toqrc)
			.to(ImageType.PNG).withSize(512, 512)
			.writeTo(new FileOutputStream(new File(path)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void genQRCode(String id){
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder=null;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		String path=GSettings.getPathTo("TMP_FOLDER")+id+".xml";
		Document doc=null;
		try {
			doc = dBuilder.parse(new FileInputStream(new File(path)));
		} catch (SAXException | IOException e) {
			e.printStackTrace();
		}
		Element t=((Element)(doc.getElementsByTagName("cfdi:Comprobante").item(0)));
		float total=new Float(t.getAttribute("total"));
		Element u=((Element)(doc.getElementsByTagName("tfd:TimbreFiscalDigital").item(0)));
		String uuid=u.getAttribute("UUID");
		Element e=((Element)(doc.getElementsByTagName("cfdi:Emisor").item(0)));
		String emisor=e.getAttribute("rfc");
		Element r=((Element)(doc.getElementsByTagName("cfdi:Receptor").item(0)));
		String receptor=r.getAttribute("rfc");
		genQRCode(emisor, receptor, total, uuid);
	}
	
	public static void saveCFDI(String xml, String id){
		GSettings g=GSettings.instance();
		String tmp=g.getPathTo("TMP_FOLDER");
		String path=tmp+id+".xml";
		try {
			FileUtils.writeStringToFile(new File(path), xml, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void genHTML(String id){
		GSettings g=GSettings.instance();
		String 	tmp=g.getPathTo("TMP_FOLDER"),
				xsltproc=g.getKey("XSLTPROC"),
				xslt=g.getPathTo("XSLT"),
				xml=tmp+id+".xml",
				html=tmp+id+".html";
		try {
			Process p = Runtime.getRuntime().exec(new String[]{"bash","-c",
			xsltproc +" --nonet "+ xslt+" "+xml+" > "+html});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void genPDF(String id){
		GSettings g=GSettings.instance();
		String home=g.getHome();
		String tmp=g.getPathTo("TMP_FOLDER");
		String htmlToPdf=g.getKey("DOCKER_HTMLTOPDF_IMAGE");
		try {
			Process p = Runtime.getRuntime().exec(new String[]{
					"bash","-c","docker run --rm --volume="+
					home+":"+home+
					" "+ htmlToPdf+
					" "+tmp+id+".html --header-center \"Factura CFDI-"+id
					+" - página [page]/[topage]\" --header-font-size 7 --footer-center \"Este documento es una representacion impresa de un CFDI\" --footer-font-size 7 "+
					tmp+id+".pdf"});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
