package com.ferremundo;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.Vector;

import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ferremundo.gth.InvoiceRow;
import com.ibm.icu.text.DecimalFormat;
import com.lowagie.text.Rectangle;

public class tst {

	public static void main(String[] args) {
		double myDouble = 2.673;
		DecimalFormat myFormat = new DecimalFormat("0.000000");
		String myDoubleString = myFormat.format(myDouble);
		System.out.println("My number is: " + myDoubleString);
	}
	public static void main1(String[] args) throws ParserConfigurationException, SAXException, IOException {
		String cfdiResponse="<xml><codigo>0</codigo><str>&lt;merga&gt;</str></xml>";
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new ByteArrayInputStream(cfdiResponse.getBytes()));
		NodeList nlist=doc.getElementsByTagName("str");
		System.out.println(StringEscapeUtils.unescapeXml(nlist.item(0).getTextContent()));
	}
	private void meth(){
		File dir=new File("/home/dog/FERREMUNDO/pedidos/");
		String[] children = dir.list();
		if (children == null) {
		    // Either dir does not exist or is not a directory
		} else {
			boolean contin=true;
		    for (int i=0; i<children.length; i++) {
		        // Get filename of file or directory
		    	if(!contin&&children[i].endsWith(".csv")){System.out.println(children[i]);break;}
		    	if(children[i].equals("LKD.pdf.csv"))contin=false;
		    	if(children[i].endsWith(".csv")){
		    		String path=dir.getPath()+"/"+children[i];
		    		File file=new File(path);
		    		Date date= new Date(file.lastModified());
		    		String dates= ((DateFormat)(new SimpleDateFormat("dd/MM/yyyy"))).format(new Date((new Date(file.lastModified())).getTime() ));
		    		System.out.println(dir.getPath()+"/"+children[i]+" -> "+dates);
		    		
		    	}
		    }
		}
		//listStore=em.createNativeQuery("select * from Product",Product.class).getResultList();
		/*for(int i=0;i<20;i++){
			EntityManager emis=EMF.get(EMF.UNIT_INVOICEFM01).createEntityManager();
			emis.getTransaction().begin();
			InvoiceFM01 invoice = (InvoiceFM01)emis.createNativeQuery("select * from InvoiceFM01 x where x.reference like 'N5N'",InvoiceFM01.class).getResultList().get(0);
			emis.close();
			System.out.println("open?"+emis.isOpen());
			System.out.println(invoice.getJson());
			System.out.println(LevenshteinDistance.LD("ab","AB"));
			System.out.println("add".startsWith(""));
			
		}*/
		
	}

}