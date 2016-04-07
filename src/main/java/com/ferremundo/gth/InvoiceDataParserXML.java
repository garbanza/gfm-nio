package com.ferremundo.gth;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class InvoiceDataParserXML implements InvoiceDataParser {

	private InputStream stream=null;
	
	private int size;
	private String consummer;
	private String consummerType;
	private String address;
	private String city;
	private String country;
	private String state;
	private String date;
	private String email;
	private String cp;
	private String rfc;
	private float[] quantity;
	private String[] unit;
	private String[] code;
	private String[] mark;
	private String[] description;
	private float[] unitPrice;
	private float[] totalPrice;
	private float subtotal;
	private String tel;
	private float taxes;
	private float total;
	private String totalInWords;
	private String reference;
	private String payment;//pay before date
	private int invoiceType;
	private InvoiceRow[] invoiceRows;
	
	public InvoiceDataParserXML(InputStream stream) {
		this.stream=stream;
	}
	
	@Override
	public Invoice getInvoice() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom=null;
		try {
			//Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			//parse using builder to get DOM representation of the XML file
			dom = db.parse(stream);
		}catch(ParserConfigurationException pce) {
			pce.printStackTrace();
		}catch(SAXException se) {
			se.printStackTrace();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		Element docEle = dom.getDocumentElement();
		Element element = null;
		NodeList nl = null;
		nl = docEle.getElementsByTagName("consummer");
		element = (Element)nl.item(0);
		consummer= element.getTextContent();
		
		nl = docEle.getElementsByTagName("consummertype");
		element = (Element)nl.item(0);
		consummerType= element.getTextContent();
		
		nl = docEle.getElementsByTagName("address");
		element = (Element)nl.item(0);
		address= element.getTextContent();
		
		nl = docEle.getElementsByTagName("city");
		element = (Element)nl.item(0);
		city= element.getTextContent();
		
		nl = docEle.getElementsByTagName("country");
		element = (Element)nl.item(0);
		country= element.getTextContent();
		
		nl = docEle.getElementsByTagName("state");
		element = (Element)nl.item(0);
		state= element.getTextContent();
		
		nl = docEle.getElementsByTagName("email");
		element = (Element)nl.item(0);
		email= element.getTextContent();
		
		nl = docEle.getElementsByTagName("state");
		element = (Element)nl.item(0);
		state= element.getTextContent();
		
		nl = docEle.getElementsByTagName("date");
		element = (Element)nl.item(0);
		date=element.getTextContent();
		
		nl = docEle.getElementsByTagName("cp");
		element = (Element)nl.item(0);
		cp= element.getTextContent();
		
		nl = docEle.getElementsByTagName("rfc");
		element = (Element)nl.item(0);
		rfc= element.getTextContent();
		
		nl = docEle.getElementsByTagName("prs");
		element = (Element)nl.item(0);
		NodeList nl2 = element.getElementsByTagName("pr");
		size=nl2.getLength();
		invoiceRows=new InvoiceRow[size];
		quantity=new float[size];
		unit=new String[size];
		code=new String[size];
		mark=new String[size];
		description=new String[size];
		unitPrice=new float[size];
		totalPrice=new float[size];
		//<pr key="41" code="M4901" unit="PZA" mark="METALFLU" unitprice="1392.5" _quantities="1">MONOMANDO P/LAVABO OMEGA BF</pr>
		for(int i=0;i<size;i++){
			Element e=(Element)nl2.item(i);
			quantity[i]=new Float(e.getAttribute("_quantities"));
			unitPrice[i]=new Float(e.getAttribute("unitprice"));
			code[i]=e.getAttribute("code");
			unit[i]=e.getAttribute("unit");
			mark[i]=e.getAttribute("mark");
			description[i]=e.getTextContent();
			totalPrice[i]=Math.round(quantity[i]*unitPrice[i]*100f)/100f;
			invoiceRows[i]=new InvoiceRow(Math.round(quantity[i]*100f)/100f,unit[i],code[i],mark[i],description[i],unitPrice[i],totalPrice[i]);
		}

		nl = docEle.getElementsByTagName("tel");
		element = (Element)nl.item(0);
		tel= element.getTextContent();
		
		nl = docEle.getElementsByTagName("reference");
		element = (Element)nl.item(0);
		reference= element.getTextContent();
		
		nl = docEle.getElementsByTagName("payment");
		element = (Element)nl.item(0);
		payment= element.getTextContent();
		
		nl = docEle.getElementsByTagName("invoicetype");
		element = (Element)nl.item(0);
		if(element.getTextContent()!=null){
			if(element.getTextContent().equals("invoice")){
				invoiceType=Invoice.INVOICE_TYPE_TAXES_APLY;
				for(int i=0;i<invoiceRows.length;i++){
					subtotal+=invoiceRows[i].getQuantity()*invoiceRows[i].getUnitPrice();
				}
				total=Invoice.TAXES_APPLY*subtotal;
				taxes=total-subtotal;
			}
			else if(element.getTextContent().equals("order")){
				invoiceType=Invoice.INVOICE_TYPE_ORDER;
				for(int i=0;i<invoiceRows.length;i++){
					subtotal+=invoiceRows[i].getQuantity()*invoiceRows[i].getUnitPrice();
				}
				total=subtotal;
				taxes=0;
			}
			else {
				invoiceType=Invoice.INVOICE_TYPE_UNKNOWN;
				total=0;
				subtotal=0;
				taxes=0;
			}
		}
		else {
			invoiceType=Invoice.INVOICE_TYPE_UNKNOWN;
			total=0;
			subtotal=0;
			taxes=0;
		}
		
		
		totalInWords=SpanishInvoiceNumber.toWords(total);
		Invoice invoice=new Invoice(consummer, consummerType, address, city, country, cp, date, email, rfc, invoiceRows, state, tel, invoiceType, reference, payment);

		return invoice;
	}

	public int getSize() {
		return size;
	}

	public String getConsummer() {
		return consummer;
	}

	public String getConsummerType() {
		return consummerType;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getState() {
		return state;
	}

	public String getDate() {
		return date;
	}

	public String getEmail() {
		return email;
	}

	public String getCp() {
		return cp;
	}

	public String getRfc() {
		return rfc;
	}

	public float[] getQuantity() {
		return quantity;
	}

	public String[] getUnit() {
		return unit;
	}

	public String[] getCode() {
		return code;
	}

	public String[] getMark() {
		return mark;
	}

	public String[] getDescription() {
		return description;
	}

	public float[] getUnitPrice() {
		return unitPrice;
	}

	public float[] getTotalPrice() {
		return totalPrice;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public String getTel() {
		return tel;
	}

	public float getTaxes() {
		return taxes;
	}

	public float getTotal() {
		return total;
	}

	public String getTotalInWords() {
		return totalInWords;
	}

	public String getReference() {
		return reference;
	}

	public String getPayment() {
		return payment;
	}

	public int getInvoiceType() {
		return invoiceType;
	}

	public InvoiceRow[] getInvoiceRows() {
		return invoiceRows;
	}
	
}
