package com.ferremundo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.lowagie.text.Rectangle;

public class InvoiceFormFM01 implements InvoiceForm {

	private int measure;
	
	private Rectangle invoiceArea;
	private Rectangle consummerArea;
	private Rectangle addressArea;
	private Rectangle cityArea;
	private Rectangle countryArea;
	private Rectangle stateArea;
	private Rectangle dateArea;
	private Rectangle emailArea;
	private Rectangle cpArea;
	private Rectangle rfcArea;
	private Rectangle[] quantityArea;
	private Rectangle[] unitArea;
	private Rectangle[] codeArea;
	private Rectangle[] descriptionArea;
	private Rectangle[] unitPriceArea;
	private Rectangle[] totalPriceArea;
	private Rectangle subtotalArea;
	private Rectangle telArea;
	private Rectangle taxesArea;
	private Rectangle totalArea;
	private Rectangle totalInWordsArea;
	private Rectangle reference;
	private Rectangle payment;
	private Rectangle headarea;
	public static int ROWS_NUMBER=17;
	
	public InvoiceFormFM01(InputStream stream) {
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
		
		//get a nodelist 
		NodeList nl = docEle.getElementsByTagName("measureunit");
		Element element = (Element)nl.item(0);
		String measure=element.getAttribute("measure");
		if (measure.equals("millimeter"))this.measure=InvoiceForm.MILLIMETER_MEASUREMENT_UNIT;
		else if (measure.equals("centimeter"))this.measure=InvoiceForm.CENTIMETER_MEASUREMENT_UNIT;
		else if (measure.equals("meter"))this.measure=InvoiceForm.METER_MEASUREMENT_UNIT;
		
		nl = docEle.getElementsByTagName("invoicearea");
		element = (Element)nl.item(0);
		invoiceArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("consummerarea");
		element = (Element)nl.item(0);
		consummerArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("addressarea");
		element = (Element)nl.item(0);
		addressArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("cityarea");
		element = (Element)nl.item(0);
		cityArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("statearea");
		element = (Element)nl.item(0);
		stateArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("datearea");
		element = (Element)nl.item(0);
		dateArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("cparea");
		element = (Element)nl.item(0);
		cpArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("rfcarea");
		element = (Element)nl.item(0);
		rfcArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("quantityarea");
		element = (Element)nl.item(0);
		NodeList nl2 = element.getElementsByTagName("qa");
		quantityArea = new Rectangle[nl2.getLength()];
		for(int i=0;i<nl2.getLength();i++){
			Element e=(Element)nl2.item(i);
			quantityArea[i]=getRectangleFromElement(e);
		}
		
		nl = docEle.getElementsByTagName("unitarea");
		element = (Element)nl.item(0);
		nl2 = element.getElementsByTagName("ua");
		unitArea = new Rectangle[nl2.getLength()];
		for(int i=0;i<nl2.getLength();i++){
			Element e=(Element)nl2.item(i);
			unitArea[i]=getRectangleFromElement(e);
		}
		
		nl = docEle.getElementsByTagName("codearea");
		element = (Element)nl.item(0);
		nl2 = element.getElementsByTagName("ca");
		codeArea = new Rectangle[nl2.getLength()];
		for(int i=0;i<nl2.getLength();i++){
			Element e=(Element)nl2.item(i);
			codeArea[i]=getRectangleFromElement(e);
		}
		
		nl = docEle.getElementsByTagName("descriptionarea");
		element = (Element)nl.item(0);
		nl2 = element.getElementsByTagName("da");
		descriptionArea = new Rectangle[nl2.getLength()];
		for(int i=0;i<nl2.getLength();i++){
			Element e=(Element)nl2.item(i);
			descriptionArea[i]=getRectangleFromElement(e);
		}
		
		nl = docEle.getElementsByTagName("unitpricearea");
		element = (Element)nl.item(0);
		nl2 = element.getElementsByTagName("upa");
		unitPriceArea = new Rectangle[nl2.getLength()];
		for(int i=0;i<nl2.getLength();i++){
			Element e=(Element)nl2.item(i);
			unitPriceArea[i]=getRectangleFromElement(e);
		}
		
		nl = docEle.getElementsByTagName("totalpricearea");
		element = (Element)nl.item(0);
		nl2 = element.getElementsByTagName("tpa");
		totalPriceArea = new Rectangle[nl2.getLength()];
		for(int i=0;i<nl2.getLength();i++){
			Element e=(Element)nl2.item(i);
			totalPriceArea[i]=getRectangleFromElement(e);
		}
		
		nl = docEle.getElementsByTagName("subtotalarea");
		element = (Element)nl.item(0);
		subtotalArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("telarea");
		element = (Element)nl.item(0);
		telArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("taxesarea");
		element = (Element)nl.item(0);
		taxesArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("totalarea");
		element = (Element)nl.item(0);
		totalArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("totalinwordsarea");
		element = (Element)nl.item(0);
		totalInWordsArea= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("referencearea");
		element = (Element)nl.item(0);
		reference= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("paymentarea");
		element = (Element)nl.item(0);
		payment= getRectangleFromElement(element);
		
		nl = docEle.getElementsByTagName("headarea");
		element = (Element)nl.item(0);
		headarea= getRectangleFromElement(element);
		
	}
	
	private Rectangle getRectangleFromElement(Element element){
		Float px=new Float(element.getAttribute("px"));
		Float py=new Float(element.getAttribute("py"));
		Float width=new Float(element.getAttribute("width"));
		Float height=new Float(element.getAttribute("height"));
		//System.out.println(element.getTagName()+" has "+px+" "+py+" "+width+" "+height);
		Rectangle rectangle=new Rectangle(px,py,px+width,py+height);
		//System.out.println("retangle has "+rectangle.getLeft()+" "+rectangle.getBottom()+" "+rectangle.getWidth()+" "+rectangle.getHeight());
		return rectangle;
	}
	
	public Rectangle getAddressArea() {
		return addressArea;
	}

	public Rectangle getCityArea() {
		return cityArea;
	}
	
	public Rectangle getCountryArea() {
		return countryArea;
	}

	public Rectangle getCodeArea(int index) throws IndexOutOfBoundsException {
		if(index<0|index>=ROWS_NUMBER)throw new IndexOutOfBoundsException(index+" index must be 0-"+(ROWS_NUMBER-1)); 
		return codeArea[index];
	}

	public Rectangle getConsummerArea() {
		return consummerArea;
	}

	public Rectangle getCpArea() {
		return cpArea;
	}

	public Rectangle getDateArea() {
		return dateArea;
	}
	
	public Rectangle getEmailArea() {
		return emailArea;
	}

	public Rectangle getDescriptionArea(int index)
			throws IndexOutOfBoundsException {
		if(index<0|index>=ROWS_NUMBER)throw new IndexOutOfBoundsException(index+" index must be 0-"+(ROWS_NUMBER-1));
		return descriptionArea[index];
	}

	public Rectangle getInvoiceArea() {
		return invoiceArea;
	}

	public Rectangle getQuantityArea(int index)
			throws IndexOutOfBoundsException {
		if(index<0|index>=ROWS_NUMBER)throw new IndexOutOfBoundsException(index+" index must be 0-"+(ROWS_NUMBER-1));
		return quantityArea[index];
	}

	public Rectangle getRfcArea() {
		return rfcArea;
	}

	public int getRowsNumber() {
		return ROWS_NUMBER;
	}

	public Rectangle getStateArea() {
		return stateArea;
	}
	
	public Rectangle getTelArea() {
		return telArea;
	}

	public Rectangle getSubtotalArea() {
		return subtotalArea;
	}

	public Rectangle getTaxesArea() {
		return taxesArea;
	}

	public Rectangle getTotalArea() {
		return totalArea;
	}

	public Rectangle getTotalInWordsArea() {
		return totalInWordsArea;
	}
	
	public Rectangle getReferenceArea() {
		return reference;
	}

	public Rectangle getPaymentArea() {
		return payment;
	}
	
	public Rectangle getTotalPriceArea(int index)
			throws IndexOutOfBoundsException {
		if(index<0|index>=ROWS_NUMBER)throw new IndexOutOfBoundsException(index+" index must be 0-"+(ROWS_NUMBER-1));
		return totalPriceArea[index];
	}

	public Rectangle getUnitArea(int index) throws IndexOutOfBoundsException {
		if(index<0|index>=ROWS_NUMBER)throw new IndexOutOfBoundsException(index+" index must be 0-"+(ROWS_NUMBER-1));
		return unitArea[index];
	}

	public Rectangle getUnitPriceArea(int index)
			throws IndexOutOfBoundsException {
		if(index<0|index>=ROWS_NUMBER)throw new IndexOutOfBoundsException(index+" index must be 0-"+(ROWS_NUMBER-1));
		return unitPriceArea[index];
	}
	
	public int getMeasurementUnit(){
		return measure;
	}
	
	public static void main(String[] args) {
		try{
			InvoiceForm form= new InvoiceFormFM01(new FileInputStream(new File("fm01.xml")));
		}
		catch(Exception e){}
	}

	@Override
	public Rectangle getHeadArea() {
		return headarea;
	}

}
