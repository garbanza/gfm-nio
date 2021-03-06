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
import java.security.Permissions;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringEscapeUtils;

import com.ferremundo.db.Mongoi;
import com.ferremundo.stt.GSettings;
import com.ferremundo.util.Util;
import com.google.gson.Gson;
import com.ibm.icu.text.DecimalFormat;
import com.mongodb.DBObject;

import mx.bigdata.sat.cfdi.CFDv33;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import mx.bigdata.sat.cfdi.v33.schema.ObjectFactory;
import mx.bigdata.sat.common.pagos.schema.CMonedaPago;
import mx.bigdata.sat.common.pagos.schema.Pagos;
import mx.bigdata.sat.common.pagos.schema.Pagos.Pago;
import mx.bigdata.sat.common.pagos.schema.Pagos.Pago.DoctoRelacionado;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Complemento;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Emisor;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Receptor;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.Impuestos;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.Impuestos.Traslados;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Conceptos.Concepto.Impuestos.Traslados.Traslado;
import mx.bigdata.sat.cfdi.v33.schema.CMetodoPago;
import mx.bigdata.sat.cfdi.v33.schema.CMoneda;
import mx.bigdata.sat.cfdi.v33.schema.CPais;
import mx.bigdata.sat.cfdi.v33.schema.CTipoDeComprobante;
import mx.bigdata.sat.cfdi.v33.schema.CTipoFactor;
import mx.bigdata.sat.cfdi.v33.schema.CUsoCFDI;
import mx.bigdata.sat.security.KeyLoader;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;

import static com.ferremundo.AccessPermission.ADMIN;
import static com.ferremundo.AccessPermission.BASIC;
import static com.ferremundo.AccessPermission.EMIT_ORDER;
import static com.ferremundo.util.Util.*;

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
	
	public static void main1(String[] args) {
		MathContext mt=new MathContext(5, RoundingMode.HALF_UP);
	    DecimalFormat format = new DecimalFormat("0.000000");
	    float total=0;
	    for(int i = 0; i < 10; i++){
	    	double quantity = Util.round2(Math.random()*2);
	    	double unitPrice = Util.round2(Math.random()*2);
	    	double unitPrice2 = Util.round2(unitPrice/1.16);
	    	double total2 = Util.round2(quantity*unitPrice/1.16);
	    	double taxes = Util.round2(total2*.16);
	    	System.out.println(quantity+" * "+ unitPrice2 +" = "+ total2 +" : tax "+taxes);
	    	//System.out.println(new BigDecimal(quantity,mt)+" * "+ new BigDecimal(unitPrice2,mt) +" = "+ new BigDecimal(total2,mt) +" : tax "+new BigDecimal(taxes,mt));
	    	System.out.println(
	    			Util.bigRound6(quantity)+":"+(String.valueOf((int)quantity).length()+2)
	    			+" * "+ Util.bigRound6(unitPrice2)+":"+(String.valueOf((int)unitPrice2).length()+2)
	    			+" = "+ Util.bigRound6(total2)+":"+(String.valueOf((int)total2).length()+2)
	    			+" : tax "+ Util.bigRound6(taxes)+":"+(String.valueOf((int)taxes).length()+2));
	    	System.out.println(
	    			format.format(new BigDecimal(quantity,new MathContext(String.valueOf((int)quantity).length()+2)))+":"+(String.valueOf((int)quantity).length()+2)
	    			+" * "+ format.format(new BigDecimal(unitPrice2,new MathContext(String.valueOf((int)unitPrice2).length()+2)))+":"+(String.valueOf((int)unitPrice2).length()+2)
	    			+" = "+ format.format(new BigDecimal(total2, new MathContext(String.valueOf((int)total2).length()+2)))+":"+(String.valueOf((int)total2).length()+2)
	    			+" : tax "+format.format(new BigDecimal(taxes, new MathContext(String.valueOf((int)taxes).length()+2)))+":"+(String.valueOf((int)taxes).length()+2));
	    	total += total2;
	    }
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
	public static void XML_TO_HTML(String xmlPath, String xsltPath, String outPath, String additionalData, String recieverAditionalData){
		Log log=new Log();
		GSettings g=GSettings.instance();
		String 	//tmp=g.getPathTo("TMP_FOLDER"),
				xsltproc=g.getKey("XSLTPROC");
				//xslt=g.getPathTo("XSLT"),
				//xml=tmp+id+".xml",
				//html=tmp+id+".html";
		try {
			log.info("executing '"+
			xsltproc + " --nonet --stringparam INVOICE_SENDER_ADDITIONAL_DATA \""+additionalData+"\" "
					+ "--stringparam INVOICE_RECIEVER_ADDITIONAL_DATA \" "+recieverAditionalData+"\" "
			 		+ xsltPath+" "+xmlPath+" > "+outPath +"; chmod 444 "+outPath);
			Process p = Runtime.getRuntime().exec(new String[]{"bash","-c",
			xsltproc + " --nonet --stringparam INVOICE_SENDER_ADDITIONAL_DATA \""+additionalData+"\" "
					 + "--stringparam INVOICE_RECIEVER_ADDITIONAL_DATA \""+recieverAditionalData+"\" "
					 + xsltPath+" "+xmlPath+" > "+outPath +"; chmod 444 "+outPath});
			p.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
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
					" --margin-top 30mm --margin-bottom 30mm"+
					" "+htmlPath+" --header-center \""+header+"\""
					+" --header-font-size 7 --footer-center \""+footer+"\" --footer-font-size 7 "+
					outPath+"; chmod 444 "+outPath);
		try {
			Process p = Runtime.getRuntime().exec(new String[]{
					"bash","-c","docker run --rm --volume="+
					home+":"+home+
					" "+ htmlToPdf+
					" --margin-top 30mm --margin-bottom 30mm"+
					" "+htmlPath+" --header-center \""+header+"\""
					+" --header-font-size 7 --footer-center \""+footer+"\" --footer-font-size 7 "+
					outPath+"; chmod 444 "+outPath});
			p.waitFor();
		} catch (IOException e) {
			log.trace(e);
		} catch (InterruptedException e) {
			log.trace(e);
		}
	}
	
	public static void HTML_TO_PDF_TICKET(String htmlPath, String outPath){
		GSettings g=GSettings.instance();
		String home=g.getHome();
		String tmp=g.getPathTo("TMP_FOLDER");
		String htmlToPdf=g.getKey("DOCKER_HTMLTOPDF_IMAGE");
		Log log = new Log();
		log.object("executing","bash","-c","docker run --rm --volume="+
				home+":"+home+
				" "+ htmlToPdf
				+" --page-width 80mm --page-height 240mm --margin-bottom 0mm --margin-left 0mm --margin-right 0mm --margin-top 0mm "
				+htmlPath+ " "
				+outPath
				+"; chmod 444 "+outPath);
		try {
			Process p = Runtime.getRuntime().exec(new String[]{
					"bash","-c","docker run --rm --volume="+
					home+":"+home+
					" "+ htmlToPdf
					+" --page-width 80mm --page-height 240mm --margin-bottom 0mm --margin-left 0mm --margin-right 0mm --margin-top 0mm "
					+htmlPath+ " "
					+outPath
					+"; chmod 444 "+outPath});
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
	    comp.setVersion("3.3");
	    GregorianCalendar gregorian = new GregorianCalendar();
	    gregorian.setTime(new Date());
	    XMLGregorianCalendar xmlGregorian=null;
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			xmlGregorian = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(new Date()));
		} catch (DatatypeConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    comp.setFecha(xmlGregorian);
	    if(invoice.getPaymentMethod()!=null&&invoice.getPaymentMethod()!="")comp.setMetodoPago(CMetodoPago.fromValue(invoice.getPaymentMethod()));
	    if(invoice.getPaymentWay()!=null&&invoice.getPaymentWay()!="")comp.setFormaPago(invoice.getPaymentWay());
	    comp.setSerie(invoice.getSeries());
	    comp.setFolio(invoice.getReference());
	    Conceptos cps = of.createComprobanteConceptos();
	    List<Concepto> list = cps.getConcepto();
	    float taxValue=new Float(GSettings.get("TAXES_VALUE"))*100;
	    MathContext mt=new MathContext(20, RoundingMode.HALF_UP);
	    MathContext mt2=new MathContext(6, RoundingMode.HALF_UP);
	    DecimalFormat format6 = new DecimalFormat("0.000000");
		DecimalFormat format2 = new DecimalFormat("0.00");
	    //BigDecimal F = new BigDecimal(1+taxValue*1f/100f, mt);
	    double F = 1+taxValue*1f/100f;
	    //BigDecimal f = new BigDecimal(taxValue*1f/100f,mt);
	    double f = taxValue*1f/100f;
	    double subTotal = 0;//new BigDecimal(0,mt);
	    //double total = 0;//new BigDecimal(0,mt);
	    double totalTaxes = 0;
	    for(InvoiceItem invoiceItems : invoice.getItems()){
	    	//BigDecimal unitPricePlusTaxes = new BigDecimal(invoiceItems.getUnitPrice(),mt);
	    	double unitPricePlusTaxes = invoiceItems.getUnitPrice();
	    	//BigDecimal unitPrice = unitPricePlusTaxes.divide(F, mt);
	    	double unitPrice = Util.round2(unitPricePlusTaxes/F);
	    	//BigDecimal quantity = new BigDecimal(invoiceItems.getQuantity(), mt);
	    	double quantity = invoiceItems.getQuantity();
	    	//BigDecimal itemTotalPlusTaxes = quantity.multiply(unitPricePlusTaxes, mt);
	    	double itemTotalPlusTaxes = quantity*unitPricePlusTaxes;
	    	//BigDecimal itemTotal = quantity.multiply(unitPrice,mt);
	    	double itemTotal = Util.round2(quantity*unitPrice);
	    	//total = total.add(Util.round2(itemTotalPlusTaxes), mt);
	    	//total = total + itemTotalPlusTaxes;
	    	subTotal += itemTotal;
	    	String unit=invoiceItems.getUnit();
	    	String description=invoiceItems.getDescription();
	    	String claveProdServ=invoiceItems.getProdservCode();
	    	String claveUnidad=invoiceItems.getUnitCode();
	    	Impuestos impuestos= new Impuestos();
	    	Traslados traslados = new Traslados();
	    	Traslado traslado = new Traslado();
	    	traslado.setBase(Util.bigRound2(itemTotal));
	    	traslado.setImpuesto(g.get("TAXES_NAME"));
	    	traslado.setTipoFactor(CTipoFactor.fromValue(g.get("TAXES_FACTOR_TYPE")));
	        traslado.setTasaOCuota(new BigDecimal(g.get("TAXES_VALUE"),mt));
	        //BigDecimal taxes = itemTotalPlusTaxes.subtract(itemTotal,mt);
	        double taxes = Util.round2(itemTotal*f);
	        totalTaxes += taxes;
	        traslado.setImporte(Util.bigRound2(taxes));
	    	traslados.getTraslado().add(traslado);
	    	impuestos.setTraslados(traslados);
	    	Concepto c1 = of.createComprobanteConceptosConcepto();	
	    	c1.setUnidad(unit);
		    c1.setImporte(Util.bigRound2(itemTotal));
		    log.info("product total :"+itemTotal);
		    c1.setCantidad(Util.bigRound6(quantity));
		    log.info("product quantity :"+quantity);
		    c1.setDescripcion(description);
		    c1.setValorUnitario(Util.bigRound2(unitPrice));
		    c1.setClaveProdServ(claveProdServ);
		    c1.setClaveUnidad(claveUnidad);
		    c1.setImpuestos(impuestos);
		    log.info("product unitPrice :"+unitPrice);
		    list.add(c1);
	    }
	    comp.setConceptos(cps);
	    //subTotal = total.divide(F, mt);
	    //subTotal = Util.round2(total/F);
	    //BigDecimal taxes = total.subtract(subTotal, mt); //subTotal2.multiply(new BigDecimal(1+taxValue*1f/100),mt).subtract(subTotal2,mt2);
	    double total = totalTaxes + subTotal;
	    comp.setSubTotal(Util.bigRound2(subTotal));
	    log.info("subtotal :"+subTotal);
	    
	    log.info("total :"+total);
	    comp.setTotal(Util.bigRound2(total));
	    if(!(invoice.getDocumentType().equals("I")||invoice.getDocumentType().equals("E")||invoice.getDocumentType().equals("T")))
	    	comp.setTipoDeComprobante(CTipoDeComprobante.fromValue("I"));
	    else comp.setTipoDeComprobante(CTipoDeComprobante.fromValue(invoice.getDocumentType()));
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
	    /* if(c.getCountry().equals("MEXICO")||c.getCountry()==null)receptor.setResidenciaFiscal(CPais.MEX);
	    else receptor.setResidenciaFiscal(CPais.fromValue(c.getCountry()));
	    */
	    CUsoCFDI cCfdiUse = null;
	    System.out.println("cfdiUse='"+c.getCfdiUse()+"'");
	    if(c.getCfdiUse()!=""||c.getCfdiUse()!=null) cCfdiUse = CUsoCFDI.fromValue(c.getCfdiUse());
	    receptor.setUsoCFDI(cCfdiUse);
	    comp.setReceptor(receptor);
	    mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos.Traslados.Traslado t2 = of.createComprobanteImpuestosTrasladosTraslado();
	    t2.setImporte(Util.bigRound2(totalTaxes));
	    t2.setImpuesto(g.getKey("TAXES_NAME"));
	    t2.setTasaOCuota(new BigDecimal(g.getKey("TAXES_VALUE"),mt));
	    t2.setTipoFactor(CTipoFactor.fromValue(g.getKey("TAXES_FACTOR_TYPE")));
	    mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos.Traslados tr2 = new mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos.Traslados();
	    tr2.getTraslado().add(t2);
	    mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos imps = new mx.bigdata.sat.cfdi.v33.schema.Comprobante.Impuestos(); 
	    imps.setTraslados(tr2);
	    imps.setTotalImpuestosTrasladados(Util.bigRound2(totalTaxes));
	    comp.setImpuestos(imps);
	    comp.setLugarExpedicion(g.getKey("INVOICE_SENDER_POSTAL_CODE"));
	    comp.setMoneda(CMoneda.MXN);
	    log.object("comp is:",comp);
	    CFDv33 cfd=null;
	    
		try {
			cfd = new CFDv33(comp);
			log.object("comp is:",comp);
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
		try {
			log.object(cfd.getComprobante());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    //System.out.println(sellado.getSello());
		ByteArrayOutputStream out= new ByteArrayOutputStream();
		InputStream in=null;
	    
		try {
			//log.info("validating");
			//cfd.validar();
			//log.info("verifying");
			//cfd.verificar();
			log.object("saving cfd with original chain",cfd.getCadenaOriginal());
		    cfd.guardar(out);
		    log.info("saving "+StringEscapeUtils.unescapeJava(out.toString()));
		} catch (Exception e) {
			log.trace(e);
		}
		log.exit("cfd is:",out.toString());
	    return out.toString();
	}
	
	public static void saveStringToFile(String str, String path){
		try {
			FileUtils.writeStringToFile(new File(path), str, "utf-8");
			Process p = Runtime.getRuntime().exec(new String[]{"bash","-c","chmod 444 "+path});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getDateString(String format){
		return ((DateFormat)(new SimpleDateFormat(format))).format(new Date());
	}
	public static String getDateString(Date date,String format){
		return ((DateFormat)(new SimpleDateFormat(format))).format(date);
	}
	
	/*public static void main(String[] args) {
		System.out.println("start");
		
		Invoice invoice = new InvoiceFM01(
				new Client(
						"code",
						"consummer",
						1,
						"address",
						"interiorNumber",
						"exteriorNumber",
						"suburb",
						"locality",
						"city",
						"MEXICO",
						"state",
						"email",
						"cp",
						"rfc",
						"tel",
						0,
						"reference",
						"aditionalReference",
						"cfdiUse"),
				new Shopman("name", "login", "password", new ArrayList<AccessPermission>(Arrays.asList(
						AccessPermission.BASIC))),
				new ArrayList<InvoiceItem>(Arrays.asList(
						new InvoiceItem(
								1,
								"code",
								1f,
								"unit",
								"mark",
								"description",
								1f,
								1f,
								0f,
								"prodservCode",
								"unitCode"))),
				"reference",
				"series",
				new Destiny("address"),
				new Client(
						"code",
						"consummer",
						1,
						"address",
						"interiorNumber",
						"exteriorNumber",
						"suburb",
						"locality",
						"city",
						"MEXICO",
						"state",
						"email",
						"cp",
						"rfc",
						"tel",
						0,
						"reference",
						"aditionalReference",
						"cfdiUse"),
				"paymentMethod",
				"paymentWay",
				"documentType",
				"coin", "PUE");
		ObjectFactory of = new ObjectFactory();
	    Comprobante comp = of.createComprobante();
	    
	    comp.setVersion("3.3");
	    Gson gson = new Gson();
	    System.out.println(gson.toJson(comp));
	    
	}*/

	public static String GEN_CFD_TYPE_PAYMENT_STRING(InvoiceTypePayment invoice){
		int clientReference=ClientReference.get();
		
		OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
		Log log=new Log(onlineClient);
		log.entry(invoice);
	
		GSettings g=GSettings.instance();
		
		ObjectFactory of = new ObjectFactory();
	    Comprobante comp = of.createComprobante();
	    log.object("comp is:",comp);
	    GregorianCalendar gregorian = new GregorianCalendar();
	    gregorian.setTime(new Date());
	    XMLGregorianCalendar xmlGregorian=null;
	    DateFormat format = null;
		try {
			format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			xmlGregorian = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(new Date()));
		} catch (DatatypeConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		comp.setVersion("3.3");
	    comp.setFecha(xmlGregorian);
	    comp.setSerie(invoice.series);
	    comp.setFolio(invoice.reference);
	    comp.setSubTotal(Util.bigRound2(0));
	    comp.setTotal(Util.bigRound2(0));
	    comp.setTipoDeComprobante(CTipoDeComprobante.fromValue("P"));
	    comp.setLugarExpedicion(g.getKey("INVOICE_SENDER_POSTAL_CODE"));
	    comp.setMoneda(CMoneda.XXX);
	    
	    Conceptos cps = of.createComprobanteConceptos();
	    List<Concepto> list = cps.getConcepto();
	    Concepto c1 = of.createComprobanteConceptosConcepto();
	    InvoiceItem item = invoice.items.get(0);
	    c1.setImporte(Util.bigRound2(item.getTotal()));
	    c1.setCantidad(Util.bigRound6(item.getQuantity()));
	    //c1.setCantidad(Util.bigRound2(item.getQuantity()));
	    c1.setDescripcion(item.getDescription());
	    c1.setValorUnitario(Util.bigRound2(item.getUnitPrice()));
	    c1.setClaveProdServ(item.getProdservCode());
	    c1.setClaveUnidad(item.getUnitCode());
	    list.add(c1);
	    comp.setConceptos(cps);

	    Emisor emiter = of.createComprobanteEmisor();
	    emiter.setNombre(g.getKey("INVOICE_SENDER_NAME"));
	    emiter.setRfc(g.getKey("INVOICE_SENDER_TAX_CODE"));
	    emiter.setRegimenFiscal(g.getKey("INVOICE_SENDER_REGIME"));
	    comp.setEmisor(emiter);
	    
	    Client c=invoice.client;
	    Receptor receiver = of.createComprobanteReceptor();
	    receiver.setNombre(c.getConsummer());
	    receiver.setRfc(c.getRfc());
	    CUsoCFDI cCfdiUse = CUsoCFDI.fromValue("P01");
	    receiver.setUsoCFDI(cCfdiUse);
	    comp.setReceptor(receiver);

	    List<Complemento> complements = comp.getComplemento();
	    Complemento complement = new Complemento();
	    Pago payment = new Pagos.Pago();
	    try { xmlGregorian = DatatypeFactory.newInstance().newXMLGregorianCalendar(format.format(new Date(invoice.dateTimePayment))); }
	    catch (DatatypeConfigurationException e2) { e2.printStackTrace(); }
	    payment.setFechaPago(xmlGregorian);
	    payment.setMonto(Util.bigRound2(invoice.amount));
    	payment.setMonedaP(CMonedaPago.MXN);
    	if(invoice.operationNumber!=null && !invoice.operationNumber.equals(""))
    		payment.setNumOperacion(invoice.operationNumber);
    	payment.setFormaDePagoP(invoice.paymentWay);
    	List<DoctoRelacionado> relatedDocs = payment.getDoctoRelacionado();
    	Mongoi mongoi = new Mongoi();
    	for(int i = 0; i < invoice.relatedDocs.length; i++){
    		relatedDocs.add(invoice.relatedDocs[i]);
    	}
    	
	    Pagos payments = new Pagos();
	    
	    payments.setVersion("1.0");
	    payments.getPago().add(payment);
	    complement.getAny().add(payments);
	    complements.add(complement);
	    log.object("comp is:",comp);
	    CFDv33 cfd=null;
	    
		try {
			cfd = new CFDv33(comp,"mx.bigdata.sat.common.pagos.schema");
			//cfd = new CFDv33(comp);
			log.object("comp is:",comp);
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
		try {
			log.object(cfd.getComprobante());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    //System.out.println(sellado.getSello());
		ByteArrayOutputStream out= new ByteArrayOutputStream();
		InputStream in=null;
	    
		try {
			//log.info("validating");
			//cfd.validar();
			//log.info("verifying");
			//cfd.verificar();
			log.object("saving cfd with original chain",cfd.getCadenaOriginal());
		    cfd.guardar(out);
		    log.info("saving "+StringEscapeUtils.unescapeJava(out.toString()));
		} catch (Exception e) {
			log.trace(e);
		}
		log.exit("cfd is:",out.toString());
	    return out.toString();
	}
}
