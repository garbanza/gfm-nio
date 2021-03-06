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
import java.math.MathContext;
import java.math.RoundingMode;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Formatter.BigDecimalLayoutForm;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
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

import mx.bigdata.sat.cfdi.CFDv33;
import mx.bigdata.sat.cfdi.v33.schema.CMetodoPago;
import mx.bigdata.sat.cfdi.v33.schema.CMoneda;
import mx.bigdata.sat.cfdi.v33.schema.CPais;
import mx.bigdata.sat.cfdi.v33.schema.CTipoDeComprobante;
import mx.bigdata.sat.cfdi.v33.schema.CTipoFactor;
import mx.bigdata.sat.cfdi.v33.schema.CUsoCFDI;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import mx.bigdata.sat.cfdi.v33.schema.ObjectFactory;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Emisor;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Receptor;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.Impuestos;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.Impuestos.Traslados;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado;
import mx.bigdata.sat.security.KeyLoader;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

public class ElectronicInvoiceFactory33 {

	
	public static String genCFDBase64Binary(Invoice invoice){
		int clientReference=ClientReference.get();
		
		OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
		Log log=new Log(onlineClient);
		log.entry(invoice);

		GSettings g=GSettings.instance();
		
		ObjectFactory of = new ObjectFactory();
	    Comprobante comp = of.createComprobante();
	    log.object("comp is:",comp);
	    comp.setVersion("3.3");
	    GregorianCalendar gregorian = new GregorianCalendar();
	    gregorian.setTime(new Date());
	    XMLGregorianCalendar xmlGregorian=null;
		try {
			xmlGregorian = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorian);
		} catch (DatatypeConfigurationException e1) {
			e1.printStackTrace();
		}
	    comp.setFecha(xmlGregorian);
	    comp.setMetodoPago(CMetodoPago.fromValue(invoice.getPaymentMethod()));
	    comp.setFormaPago(invoice.getPaymentWay());
	    comp.setSerie(g.getKey("INVOICE_SERIAL"));
	    comp.setFolio(invoice.getReference());
	    Conceptos cps = of.createComprobanteConceptos();
	    List<Concepto> list = cps.getConcepto();
	    float iva=new Float(GSettings.get("TAXES_VALUE"))*100;
	    MathContext mt=new MathContext(6, RoundingMode.HALF_UP);
	    MathContext mt2=new MathContext(2, RoundingMode.HALF_UP);
	    BigDecimal subTotal=new BigDecimal(0,mt);
	    log.info(subTotal.toPlainString());

	    for(InvoiceItem invoiceItems : invoice.getItems()){
	    	BigDecimal unitPrice= new BigDecimal(invoiceItems.getUnitPrice()/(1+iva*1f/100),mt);
	    	BigDecimal quantity=new BigDecimal(invoiceItems.getQuantity()).multiply(new BigDecimal(1),mt);
	    	BigDecimal total=quantity.multiply(unitPrice,mt);
	    	subTotal=subTotal.add(total,mt);
	    	String unit=invoiceItems.getUnit();
	    	String description=invoiceItems.getDescription();
	    	String claveProdServ=invoiceItems.getProdservCode();
	    	String claveUnidad=invoiceItems.getUnitCode();
	    	Impuestos impuestos= new Impuestos();
	    	Traslados traslados = new Traslados();
	    	Traslado traslado = new Traslado();
	    	traslado.setBase(new BigDecimal("0.16"));
	    	traslado.setImpuesto("002");
	    	traslado.setTipoFactor(CTipoFactor.TASA);
	        traslado.setTasaOCuota(new BigDecimal("0.160000"));
	        BigDecimal taxes = total.multiply(new BigDecimal(1+iva*1f/100),mt).subtract(total,mt);
	        traslado.setImporte(taxes);
	    	traslados.getTraslado().add(traslado);
	    	impuestos.setTraslados(traslados);
	    	Concepto c1 = of.createComprobanteConceptosConcepto();	
	    	c1.setUnidad(unit);
		    c1.setImporte(total);
		    log.info("product total :"+total.toPlainString());
		    c1.setCantidad(quantity);
		    log.info("product quantity :"+quantity.toPlainString());
		    c1.setDescripcion(description);
		    c1.setValorUnitario(unitPrice);
		    c1.setClaveProdServ(claveProdServ);
		    c1.setClaveUnidad(claveUnidad);
		    c1.setImpuestos(impuestos);
		    log.info("product unitPrice :"+unitPrice.toPlainString());
		    list.add(c1);
	    }
	    comp.setConceptos(cps);
	    comp.setSubTotal(subTotal.multiply(new BigDecimal(1),mt2));
	    log.info("subtotal :"+subTotal.toPlainString());
	    BigDecimal total=subTotal.multiply(new BigDecimal(1+iva*1f/100),mt2);
	    log.info("total :"+total.toPlainString());
	    comp.setTotal(total);
	    comp.setTipoDeComprobante(CTipoDeComprobante.fromValue(invoice.getDocumentType()));
	    
	    Emisor emisor = of.createComprobanteEmisor();
	    emisor.setNombre(g.getKey("INVOICE_SENDER_NAME"));
	    emisor.setRfc(g.getKey("INVOICE_SENDER_TAX_CODE"));
	    emisor.setRegimenFiscal(g.getKey("INVOICE_SENDER_REGIME"));
	    comp.setEmisor(emisor);
	    Client c=invoice.getClient();
	    Receptor receptor = of.createComprobanteReceptor();
	    receptor.setNombre(c.getConsummer());
	    receptor.setRfc(c.getRfc());
	    // TODO fix residencia fiscal
	    if(c.getCountry().equals("MEXICO")||c.getCountry()==null)receptor.setResidenciaFiscal(CPais.MEX);
	    else receptor.setResidenciaFiscal(CPais.fromValue(c.getCountry()));
	    receptor.setUsoCFDI(CUsoCFDI.fromValue(c.getCfdiUse()));
	    comp.setReceptor(receptor);
	    mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos.Traslados.Traslado t2 = of.createComprobanteImpuestosTrasladosTraslado();
	    t2.setImporte(total.subtract(subTotal,mt2));
	    t2.setImpuesto(g.getKey("TAXES_NAME"));
	    t2.setTasaOCuota(new BigDecimal(g.getKey("TAXES_VALUE"),mt));
	    t2.setTipoFactor(CTipoFactor.fromValue(g.getKey("TAXES_FACTOR_TYPE")));
	    mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos.Traslados tr2 = new mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos.Traslados();
	    tr2.getTraslado().add(t2);
	    mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos imps = new mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos(); 
	    imps.setTraslados(tr2);
	    imps.setTotalImpuestosTrasladados(total.subtract(subTotal,mt2));
	    comp.setImpuestos(imps);
	    comp.setLugarExpedicion(g.getKey("INVOICE_SENDER_POSTAL_CODE"));
	    comp.setMoneda(CMoneda.MXN);
	    log.object("comp is:",comp);
	    CFDv33 cfd=null;
		try {
			cfd = new CFDv33(comp);
			log.object("cfd is:",comp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Crea el CFD a partir de un archivo
		//cfd.
		PrivateKey key=null;
		try {
			KeyLoader kl=
			KeyLoaderFactory.createInstance(
			        KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
			        new FileInputStream(g.getPathTo("PRIVATE_KEY")),
			        g.getKey("PRIVATE_KEY_PASS")
			);
			key=kl.getKey();
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
			log.info("validating");
			//cfd.validar();
			log.info("verifying");
			cfd.verificar();
			log.info("saving "+out);
		    cfd.guardar(out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] cfd64=out.toString().getBytes();
		log.exit("cfd is:",out.toString());
	    return DatatypeConverter.printBase64Binary(cfd64);
	    
	}
	
	public static void genQRCode(String emisor, String receptor, float total, String uuid, String ref){
		String re="?re="+emisor.toUpperCase();
		String rr="&rr="+receptor.toUpperCase();
		DecimalFormat formatter = new DecimalFormat("0000000000.000000");
		String tt="&tt="+formatter.format(total);
		String id="&id="+uuid;
		String toqrc=re+rr+tt+id;
		String path=GSettings.getPathTo("TMP_FOLDER")+ref+".qrc.png";
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
		genQRCode(emisor, receptor, total, uuid, id);
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
		Log log=new Log();
		GSettings g=GSettings.instance();
		String 	tmp=g.getPathTo("TMP_FOLDER"),
				xsltproc=g.getKey("XSLTPROC"),
				xslt=g.getPathTo("XSLT"),
				xml=tmp+id+".xml",
				html=tmp+id+".html",
				additionalData=g.getKey("INVOICE_SENDER_ADDITIONAL_DATA"),
				email=g.getKey("EMAIL_NOTIFICATIONS");
		
		try {
			log.info("executing '"+xsltproc +" --nonet "+ xslt+" "+xml+" > "+html+"'");
			Process p = Runtime.getRuntime().exec(new String[]{"bash","-c",
			xsltproc +" --nonet --stringparam INVOICE_SENDER_ADDITIONAL_DATA \""+additionalData+"\" "
			+ xslt+" "+xml+" > "+html});
			p.waitFor();
		} catch (IOException e) {
			log.trace(e);
		} catch (InterruptedException e) {
			log.trace(e);
		}
	}
	
	public static void genPDF(String id){
		Log log=new Log();
		GSettings g=GSettings.instance();
		String home=g.getHome();
		String tmp=g.getPathTo("TMP_FOLDER");
		String htmlToPdf=g.getKey("DOCKER_HTMLTOPDF_IMAGE");
		try {
			log.object("executing","bash","-c","docker run --rm --volume="+
					home+":"+home+
					" "+ htmlToPdf+
					" "+tmp+id+".html --header-center \"Factura CFDI-"+id
					+" - pagina [page]/[topage]\" --header-font-size 7 --footer-center \"Este documento es una representacion impresa de un CFDI\" --footer-font-size 7 "+
					tmp+id+".pdf");
			Process p = Runtime.getRuntime().exec(new String[]{
					"bash","-c","docker run --rm --volume="+
					home+":"+home+
					" "+ htmlToPdf+
					" "+tmp+id+".html --header-center \"Factura CFDI-"+id
					+" - pagina [page]/[topage]\" --header-font-size 7 --footer-center \"Este documento es una representacion impresa de un CFDI\" --footer-font-size 7 "+
					tmp+id+".pdf"});
			p.waitFor();
		} catch (IOException e) {
			log.trace(e);
		} catch (InterruptedException e) {
			log.trace(e);
		}
	}

	/*public static String genCFD(Invoice invoice){
		int clientReference=ClientReference.get();
		
		OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
		Log log=new Log(onlineClient);
		log.entry(invoice);
	
		GSettings g=GSettings.instance();
		
		ObjectFactory of = new ObjectFactory();
	    Comprobante comp = of.createComprobante();
	    log.object("comp is:",comp);
	    comp.setVersion("3.2");
	    comp.setFecha(new Date());
	    comp.setMetodoDePago(invoice.getPaymentMethod());
	    comp.setFormaDePago(invoice.getPaymentWay());
	    comp.setSerie(g.getKey("INVOICE_SERIAL"));
	    comp.setFolio(invoice.getReference());
	    if(invoice.getAccountPaymentNumber()!=null)comp.setNumCtaPago(invoice.getAccountPaymentNumber());
	    Conceptos cps = of.createComprobanteConceptos();
	    List<Concepto> list = cps.getConcepto();
	    float iva=new Float(GSettings.get("TAXES_IVA_VALUE"));
	    MathContext mt=new MathContext(5, RoundingMode.HALF_UP);
	    BigDecimal subTotal=new BigDecimal(0,mt);
	    
	    log.info(subTotal.toPlainString());
	    //float subTotal=0;
	    for(InvoiceItem invoiceItems : invoice.getItems()){
	    	BigDecimal unitPrice= new BigDecimal(invoiceItems.getUnitPrice()/(1+iva*1f/100),mt);
	    	//float unitPrice=Math.round(100*invoiceItems.getUnitPrice()/(1+iva/100))*1f/100;
	    	BigDecimal quantity=new BigDecimal(invoiceItems.getQuantity()).multiply(new BigDecimal(1),mt);
	    	//quantity.setScale(6, BigDecimal.ROUND_HALF_UP);
	    	//float quantity=invoiceItems.getQuantity();
	    	BigDecimal total=quantity.multiply(unitPrice,mt);
	    	//total.setScale(6, BigDecimal.ROUND_HALF_UP);
	    	//float total=unitPrice*quantity;
	    	subTotal=subTotal.add(total,mt);
	    	String unit=invoiceItems.getUnit();
	    	String description=invoiceItems.getDescription();
	    	Concepto c1 = of.createComprobanteConceptosConcepto();	
	    	c1.setUnidad(unit);
	    	//BigDecimal bTotal=new BigDecimal(total);
	    	//bTotal.setScale(6, BigDecimal.ROUND_HALF_UP);
		    c1.setImporte(total);
		    log.info("product total :"+total.toPlainString());
		    c1.setCantidad(quantity);
		    log.info("product quantity :"+quantity.toPlainString());
		    c1.setDescripcion(description);
		    c1.setValorUnitario(unitPrice);
		    log.info("product unitPrice :"+unitPrice.toPlainString());
		    list.add(c1);
	    }
	    comp.setConceptos(cps);
	    comp.setSubTotal(subTotal);
	    log.info("subtotal :"+subTotal.toPlainString());
	    BigDecimal total=subTotal.multiply(new BigDecimal(1+iva*1f/100),mt);
	    //float total=subTotal*(1+iva*1f/100);
	    log.info("total :"+total.toPlainString());
	    //total.setScale(6, BigDecimal.ROUND_HALF_UP);
	    comp.setTotal(total);
	    comp.setTipoDeComprobante(invoice.getDocumentType());
	    
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
	    uf.setReferencia(g.getKey("INVOICE_SENDER_REFERENCE"));
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
	    comp.setEmisor(emisor);
	    
	    Client c=invoice.getClient();
	    Receptor receptor = of.createComprobanteReceptor();
	    receptor.setNombre(c.getConsummer());
	    receptor.setRfc(c.getRfc());
	    TUbicacion uf2 = new TUbicacion();
	    log.object("ubicacion after is:",uf2);
	    if(!(c.getAddress().equals("")))uf2.setCalle(c.getAddress());
	    if(!(c.getCp().equals("")))uf2.setCodigoPostal(c.getCp());
	    if(!(c.getSuburb().equals("")))uf2.setColonia(c.getSuburb()); 
	    if(!(c.getState().equals("")))uf2.setEstado(c.getState());
	    if(!(c.getCity().equals("")))uf2.setMunicipio(c.getCity());
	    if(!(c.getExteriorNumber().equals("")))uf2.setNoExterior(c.getExteriorNumber());
	    if(!(c.getInteriorNumber().equals("")))uf2.setNoInterior(c.getInteriorNumber());
	    if(!(c.getCountry().equals("")))uf2.setPais(c.getCountry());
	    if(!(invoice.getDestiny().getAddress().equals("")))uf2.setReferencia(invoice.getDestiny().getAddress());
	    log.object("ubicacion before is:",uf2);
	    receptor.setDomicilio(uf2);
	    log.object("comp before setreceptor is:",comp);
	    comp.setReceptor(receptor);
	    log.object("comp after setreceptor is:",comp);
	    Impuestos imps = of.createComprobanteImpuestos();
	    Traslados trs = of.createComprobanteImpuestosTraslados();
	    List<Traslado> list2 = trs.getTraslado(); 
	    Traslado t2 = of.createComprobanteImpuestosTrasladosTraslado();
	    t2.setImporte(total.subtract(subTotal));
	    t2.setImpuesto(g.getKey("TAXES_IVA_NAME"));
	    t2.setTasa(new BigDecimal(g.getKey("TAXES_IVA_VALUE")));
	    list2.add(t2);
	    imps.setTraslados(trs);
	    imps.setTotalImpuestosTrasladados(total.subtract(subTotal));
	    comp.setImpuestos(imps);
	    
	    comp.setLugarExpedicion(g.getKey("INVOICE_SENDER_CITY"));
	    log.object("comp is:",comp);
	    CFDv33 cfd=null;
		try {
			cfd = new CFDv33(comp);
			log.object("cfd is:",comp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // Crea el CFD a partir de un archivo
		//cfd.
		PrivateKey key=null;
		try {
			KeyLoader kl=
			KeyLoaderFactory.createInstance(
			        KeyLoaderEnumeration.PRIVATE_KEY_LOADER,
			        new FileInputStream(g.getPathTo("PRIVATE_KEY")),
			        g.getKey("PRIVATE_KEY_PASS")
			);
			key=kl.getKey();
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
			//log.info("validating");
			//cfd.validar();
			//log.info("verifying");
			//cfd.verificar();
			log.info("saving "+out);
		    cfd.guardar(out);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.exit("cfd is:",out.toString());
	    return out.toString();
	    //CFDv33 cfd2 = new CFDv33(sellado);
	    
	}*/
	
	
}
