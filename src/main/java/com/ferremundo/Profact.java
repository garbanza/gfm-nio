package com.ferremundo;

import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.ferremundo.stt.GSettings;
import com.profact.Timbrado;
import com.profact.ArrayOfAnyType;
import com.profact.CancelaCFDIAck;

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
		ArrayOfAnyType objects=null;
		
			try {
				objects = new Timbrado(new URL(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_WEB_SERVICE")))
						.getTimbradoSoap()
						.timbraCFDI(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_USER"), invoice64, invoice.getReference());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		List<Object> l= objects.getAnyType();
		if((int) l.get(1)==0){
			String xml=StringEscapeUtils.unescapeJava((String)l.get(3));
			String message=(String)l.get(2);
			String code=(int)l.get(1)+"";
			boolean success=true;
			return new PACResponse(success, message, xml, code);
		}
		String xml="<null></null>";
		String message=(String)l.get(2);
		String code=(int)l.get(1)+"";
		boolean success=false;
		return new PACResponse(success, message, xml, code);
	}
	
	public PACResponse submitTest1() {
	return new PACResponse(true, "OK",
			"<?xml version=\"1.0\" encoding=\"utf-8\"?><cfdi:Comprobante xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv32.xsd\" version=\"3.2\" fecha=\"2016-07-04T13:22:48\" sello=\"dy70goLThBqLo8miKXPUh3UeBouA8dsBqLve7WvHjyGrfK8s7En2SkK/Sbh77InttnJNxpDARo5Xut8GX/pLy8Zg8hgDew2WBdEe5iKhYXgYzmI4bC2vv+RFWwoD8wdb7Q4zB0YtxDw55603FoB75E4fob0em1NTLbK5VIorUco=\" formaDePago=\"Pago en una sola exhibición\" noCertificado=\"20001000000200001428\" certificado=\"MIIEYTCCA0mgAwIBAgIUMjAwMDEwMDAwMDAyMDAwMDE0MjgwDQYJKoZIhvcNAQEFBQAwggFcMRowGAYDVQQDDBFBLkMuIDIgZGUgcHJ1ZWJhczEvMC0GA1UECgwmU2VydmljaW8gZGUgQWRtaW5pc3RyYWNpw7NuIFRyaWJ1dGFyaWExODA2BgNVBAsML0FkbWluaXN0cmFjacOzbiBkZSBTZWd1cmlkYWQgZGUgbGEgSW5mb3JtYWNpw7NuMSkwJwYJKoZIhvcNAQkBFhphc2lzbmV0QHBydWViYXMuc2F0LmdvYi5teDEmMCQGA1UECQwdQXYuIEhpZGFsZ28gNzcsIENvbC4gR3VlcnJlcm8xDjAMBgNVBBEMBTA2MzAwMQswCQYDVQQGEwJNWDEZMBcGA1UECAwQRGlzdHJpdG8gRmVkZXJhbDESMBAGA1UEBwwJQ295b2Fjw6FuMTQwMgYJKoZIhvcNAQkCDCVSZXNwb25zYWJsZTogQXJhY2VsaSBHYW5kYXJhIEJhdXRpc3RhMB4XDTEzMDUwNzE2MDEyOVoXDTE3MDUwNzE2MDEyOVowgdsxKTAnBgNVBAMTIEFDQ0VNIFNFUlZJQ0lPUyBFTVBSRVNBUklBTEVTIFNDMSkwJwYDVQQpEyBBQ0NFTSBTRVJWSUNJT1MgRU1QUkVTQVJJQUxFUyBTQzEpMCcGA1UEChMgQUNDRU0gU0VSVklDSU9TIEVNUFJFU0FSSUFMRVMgU0MxJTAjBgNVBC0THEFBQTAxMDEwMUFBQSAvIEhFR1Q3NjEwMDM0UzIxHjAcBgNVBAUTFSAvIEhFR1Q3NjEwMDNNREZOU1IwODERMA8GA1UECxMIcHJvZHVjdG8wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAKS/beUVy6E3aODaNuLd2S3PXaQre0tGxmYTeUxa55x2t/7919ttgOpKF6hPF5KvlYh4ztqQqP4yEV+HjH7yy/2d/+e7t+J61jTrbdLqT3WD0+s5fCL6JOrF4hqy//EGdfvYftdGRNrZH+dAjWWml2S/hrN9aUxraS5qqO1b7btlAgMBAAGjHTAbMAwGA1UdEwEB/wQCMAAwCwYDVR0PBAQDAgbAMA0GCSqGSIb3DQEBBQUAA4IBAQACPXAWZX2DuKiZVv35RS1WFKgT2ubUO9C+byfZapV6ZzYNOiA4KmpkqHU/bkZHqKjR+R59hoYhVdn+ClUIliZf2ChHh8s0a0vBRNJ3IHfA1akWdzocYZLXjz3m0Er31BY+uS3qWUtPsONGVDyZL6IUBBUlFoecQhP9AO39er8zIbeU2b0MMBJxCt4vbDKFvT9i3V0Puoo+kmmkf15D2rBGR+drd8H8Yg8TDGFKf2zKmRsgT7nIeou6WpfYp570WIvLJQY+fsMp334D05Up5ykYSAxUGa30RdUzA4rxN5hT+W9whWVGD88TD33Nw55uNRUcRO3ZUVHmdWRG+GjhlfsD\" subTotal=\"94\" total=\"109\" tipoDeComprobante=\"ingreso\" metodoDePago=\"No identificado\" LugarExpedicion=\"MORELIA\" xmlns:cfdi=\"http://www.sat.gob.mx/cfd/3\">  <cfdi:Emisor rfc=\"AAA010101AAA\" nombre=\"TEST\">    <cfdi:DomicilioFiscal calle=\"LAGO DE PATZCUARO\" noInterior=\"A\" colonia=\"VENTURA PUENTE\" municipio=\"MORELIA\" estado=\"MICHOACAN\" pais=\"MEXICO\" codigoPostal=\"58020\" />    <cfdi:ExpedidoEn calle=\"LAGO DE PATZCUARO\" noInterior=\"A\" colonia=\"VENTURA PUENTE\" municipio=\"MORELIA\" estado=\"MICHOACAN\" pais=\"MEXICO\" codigoPostal=\"58020\" />    <cfdi:RegimenFiscal Regimen=\"INTERMEDIO\" />  </cfdi:Emisor>  <cfdi:Receptor rfc=\"XAXX010101000\" nombre=\"TEST\">    <cfdi:Domicilio calle=\"NA\" noExterior=\"1\" noInterior=\"1\" colonia=\"NA\" municipio=\"MORELIA\" estado=\"MICH\" pais=\"MEXICO\" codigoPostal=\"58150\" />  </cfdi:Receptor>  <cfdi:Conceptos>    <cfdi:Concepto cantidad=\"1\" unidad=\"PZA\" descripcion=\"PRODUCTO DUMMIE 0112\" valorUnitario=\"94\" importe=\"94\" />  </cfdi:Conceptos>  <cfdi:Impuestos totalImpuestosTrasladados=\"15\">    <cfdi:Traslados>      <cfdi:Traslado impuesto=\"IVA\" tasa=\"16.0\" importe=\"15\" />    </cfdi:Traslados>  </cfdi:Impuestos>  <cfdi:Complemento>    <tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/TimbreFiscalDigital/TimbreFiscalDigital.xsd\" version=\"1.0\" UUID=\"EA4B7C77-6BE0-466F-817D-73BF88B5E0A2\" FechaTimbrado=\"2016-07-04T13:25:12\" noCertificadoSAT=\"20001000000100005761\" selloCFD=\"dy70goLThBqLo8miKXPUh3UeBouA8dsBqLve7WvHjyGrfK8s7En2SkK/Sbh77InttnJNxpDARo5Xut8GX/pLy8Zg8hgDew2WBdEe5iKhYXgYzmI4bC2vv+RFWwoD8wdb7Q4zB0YtxDw55603FoB75E4fob0em1NTLbK5VIorUco=\" selloSAT=\"s9ymlOT4Uj/8fwBEWZ55OMxdBCt3WP8QKxlbnrE62kOO6axwN0XxIiAsl61txH4vsFDe98AP6+JeVa41od/x01sCNyqYihzDU4B3UMccUIiqjyXbGOgMJxrAcpcnnjqcW/m3Cp8NV7RkKFG1MF+LMKp7ezU/cSXF+3eaObgsGmo=\" />  </cfdi:Complemento></cfdi:Comprobante>"
			,"0");
	}
	public PACResponse submitTest() {
		GSettings g = GSettings.instance();
		com.profact.test.ArrayOfAnyType objects=null;
		try {
			objects = new com.profact.test.Timbrado(new URL(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_WEB_SERVICE")))
					.getTimbradoSoap()
					.timbraCFDI(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_USER"), invoice64, invoice.getReference());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List l = objects.getAnyType();
		if((int) l.get(1)==0){
			String xml=StringEscapeUtils.unescapeJava((String)l.get(3));
			String message=(String)l.get(2);
			String code=(int)l.get(1)+"";
			boolean success=true;
			return new PACResponse(success, message, xml, code);
		}
		String xml="<null></null>";
		String message=(String)l.get(2);
		String code=(int)l.get(1)+"";
		boolean success=false;
		return new PACResponse(success, message, xml, code);
	}
	
	public PACResponse cancel(boolean production){
		if(production)return cancelProduction();
		return cancelTest();
	}

	private PACResponse cancelTest() {
		Log log = new Log();
		log.entry();
		String xmlEscaped=invoice.getElectronicVersion().getXml();
		String xml=StringEscapeUtils.unescapeXml(xmlEscaped);
		log.object("xml", xml);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	    DocumentBuilder builder=null;
	    Document document=null;
		try  
	    {  
	        builder = factory.newDocumentBuilder();  
	        document = builder.parse(
	        		new InputSource( 
	        				new StringReader( xml ) ) );  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }
		log.object("document", document);
		Node node=document.getElementsByTagName("tfd:TimbreFiscalDigital").item(0);
	    Element e=(Element)node;
	    String uuid=e.getAttribute("UUID");
	    GSettings g = GSettings.instance();
		com.profact.test.ArrayOfAnyType objects=null;
		try {
			objects = new com.profact.test.Timbrado(new URL(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_WEB_SERVICE")))
							.getTimbradoSoap()
							.cancelaCFDIAck(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_USER"), g.getKey("INVOICE_SENDER_TAX_CODE"), uuid);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		List<Object> l= objects.getAnyType();
		if((int) l.get(1)==0){
			String content=StringEscapeUtils.unescapeJava((String)l.get(3));
			String message=(String)l.get(2);
			String code=(int)l.get(1)+"";
			boolean success=true;
			return new PACResponse(success, message, content, code);
		}
		String content="<null></null>";
		String message=(String)l.get(2);
		String code=(int)l.get(1)+"";
		boolean success=false;
		return new PACResponse(success, message, content, code);
	}

	private PACResponse cancelProduction() {
		Log log =new Log();
		log.entry();
		log.object("cancelling invoice",invoice);
		String xml=invoice.getElectronicVersion().getXml();
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
	    DocumentBuilder builder=null;
	    Document document=null;
		try  
	    {  
	        builder = factory.newDocumentBuilder();  
	        document = builder.parse(
	        		new InputSource( 
	        				new StringReader( xml ) ) );  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }
		Node node=document.getElementsByTagName("tfd:TimbreFiscalDigital").item(0);
	    Element e=(Element)node;
	    String uuid=e.getAttribute("UUID");
	    GSettings g = GSettings.instance();
		ArrayOfAnyType objects=null;
		try {
			objects = new Timbrado(new URL(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_WEB_SERVICE")))
							.getTimbradoSoap()
							.cancelaCFDIAck(g.getKey("INVOICE_CERTIFICATE_AUTHORITY_USER"), g.getKey("INVOICE_SENDER_TAX_CODE"), uuid);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		List<Object> l= objects.getAnyType();
		if((int) l.get(1)==0){
			String content=StringEscapeUtils.unescapeJava((String)l.get(3));
			String message=(String)l.get(2);
			String code=(int)l.get(1)+"";
			boolean success=true;
			return new PACResponse(success, message, content, code);
		}
		String content="<null></null>";
		String message=(String)l.get(2);
		String code=(int)l.get(1)+"";
		boolean success=false;
		PACResponse pr=new PACResponse(success, message, content, code);
		log.exit("packpesponse",pr);
		return pr;
	}

}
