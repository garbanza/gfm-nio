package com.ferremundo;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

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
import mx.bigdata.sat.security.KeyLoader;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;

public class Utils {
	
	public static boolean isIntegerParseInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {}
        return false;
    }

	public static boolean isInteger(String str)
	{
		if(str==null)return false;
		if(str.equals(""))return false;
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
	public static void main(String[] args) {
		System.out.println(isInteger(""));
		System.out.println(isInteger(null));
		System.out.println(isInteger("12n"));
		System.out.println(isInteger("n12"));
		System.out.println(isInteger("0112"));
		System.out.println(isInteger("12"));
		System.out.println(Integer.parseInt("0001"));
	}
	
	public static String getStringFromInputStream(InputStream is) {
		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
 
	}
	
	public static InputStream getInpuTreamFromString(String string){
		return new ByteArrayInputStream(string.getBytes(StandardCharsets.UTF_8));
	}

	/** Depends on xsltproc installed on the system
	 * @param xmlPath
	 * @param xsltPath
	 * @param outPath
	 */
	public static void XML_TO_HTML(String xmlPath, String xsltPath, String outPath, String additionalData){
		Log log=new Log();
		GSettings g=GSettings.instance();
		String 	//tmp=g.getPathTo("TMP_FOLDER"),
				xsltproc=g.getKey("XSLTPROC");
				//xslt=g.getPathTo("XSLT"),
				//xml=tmp+id+".xml",
				//html=tmp+id+".html";
		try {
			log.info("executing '"+xsltproc +" --nonet "+ xsltPath+" "+xmlPath+" > "+outPath+"'");
			Process p = Runtime.getRuntime().exec(new String[]{"bash","-c",
			xsltproc +" --nonet --stringparam INVOICE_SENDER_ADDITIONAL_DATA \""+additionalData+"\" "
			+ xsltPath+" "+xmlPath+" > "+outPath});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Depends on docker installed on the system
	 * @param htmlPath
	 * @param outPath
	 */
	public static void HTML_TO_PDF(String htmlPath, String outPath, String header, String footer){
		GSettings g=GSettings.instance();
		String home=g.getHome();
		String tmp=g.getPathTo("TMP_FOLDER");
		String htmlToPdf=g.getKey("DOCKER_HTMLTOPDF_IMAGE");
		Log log = new Log();
		log.object("executing","bash","-c","docker run --rm --volume="+
					home+":"+home+
					" "+ htmlToPdf+
					" "+htmlPath+" --header-center \""+header+"\""
					+" --header-font-size 7 --footer-center \""+footer+"\" --footer-font-size 7 "+
					outPath);
		try {
			Process p = Runtime.getRuntime().exec(new String[]{
					"bash","-c","docker run --rm --volume="+
					home+":"+home+
					" "+ htmlToPdf+
					" "+htmlPath+" --header-center \""+header+"\""
					+" --header-font-size 7 --footer-center \""+footer+"\" --footer-font-size 7 "+
					outPath});
			p.waitFor();
		} catch (IOException e) {
			log.trace(e);
		} catch (InterruptedException e) {
			log.trace(e);
		}
	}
	
	public static String GEN_CFD_STRING(Invoice invoice){
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
	    CFDv32 cfd=null;
		try {
			cfd = new CFDv32(comp);
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
	    //CFDv32 cfd2 = new CFDv32(sellado);
	    
	}
	
	public static void saveStringToFile(String str, String path){
		try {
			FileUtils.writeStringToFile(new File(path), str, "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
