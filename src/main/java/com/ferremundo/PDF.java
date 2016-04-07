package com.ferremundo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ferremundo.Invoice;
import com.ferremundo.InvoiceForm;
import com.ferremundo.db.Mongoi;
import com.ferremundo.gth.PtsConverter;
import com.ferremundo.stt.GSettings;
import com.google.gson.Gson;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.ColumnText;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class PDF {
	
	private BaseFont baseFont;
	private String font=BaseFont.HELVETICA; 
	private PdfContentByte contentByte;
	private Document document;
	private float fontSize=8;
	private InvoiceForm form;
	private Invoice invoice;
	private File out;
	private PdfWriter writer;
	
	public static float FONT_SIZE_BIG=15;
	public static float FONT_SIZE_NORMAL=12;
	public static float FONT_SIZE_INVOICE_HEAD=11;
	public static float FONT_SIZE_INVOICE_LEGIBLE_NUMBER=10;
	public static float FONT_SIZE_INVOICE_ACCEPTABLE=9;
	public static float FONT_SIZE_INVOICE=8;
	public static float FONT_SIZE_LITTLE_DATA=6;
	public static int ALIGNMENT_LEFT=-1;
	public static int ALIGNMENT_CENTER=0;
	public static int ALIGNMENT_RIGTH=1;
	
	private NumberFormat formatter= new DecimalFormat("#0.00");
	
	public PDF(Invoice invoice, InvoiceForm form, String pathname) {
		this.invoice=invoice;
		this.form=form;
		out=new File(pathname);
	}
	
	public PDF(Invoice invoice,String out){
		InputStream stream=null;
		try{
			stream=new FileInputStream(new File(GSettings.get("INVOICE_FORM_DESCRIPTOR")));
		}catch(FileNotFoundException e){e.printStackTrace();}
		form=new InvoiceFormFM01(stream);
		this.invoice=invoice;
		this.out=new File(out);
	}
	
	public File make() {
		openPDF();
		try {
			insertHeadToPDF();
		}
		catch (DocumentException e) {
			e.printStackTrace();
		}
		
		//insertConsummerToPDF();
		//insertAddressToPDF();
		//insertCityToPDF();
		//insertCountryToPDF();
		//insertCpToPDF();
		insertDateToPDF();
		//insertEmailToPDF();
		//insertRfcToPDF();
		insertRowsToPDF();
		//insertStateToPDF();
		insertSubtotalToPDF();
		insertTaxesToPDF();
		//insertTelToPDF();
		insertTotalInwordsToPDF();
		insertReferenceToPDF();
		insertTotalToPDF();
		insertPaymentToPDF();
		
		closePDF();

		return out;
	}
	
	public static void main(String[] args) {
		InvoiceFM01 fm01=new Gson().fromJson(new Mongoi().doFindOne(Mongoi.INVOICES, "{\"reference\":\"414V\"}").toString(), InvoiceFM01.class);
		
		fm01.setPrintedTo(fm01.getClient());
		System.out.println(new Gson().toJson(fm01));
		PDF pdf= new PDF(fm01, "/opt/tmp/tmp.pdf");
		pdf.make();
	}
	
	private void insertTextToPDF(Rectangle area, String text, float fontSize,int alignment) {
		System.out.println("inserting : "+text);
		float px = area.getLeft();
		float py = area.getBottom();
		float width = area.getWidth();
		float height = area.getHeight();
		contentByte.saveState();
		contentByte.beginText();
		contentByte.setFontAndSize(baseFont, fontSize);
		if(alignment==ALIGNMENT_CENTER){
			float textLenght=PtsConverter.pt2mm(contentByte.getEffectiveStringWidth(text, false));
			px=px+(width-textLenght)/2;
		}
		else if(alignment==ALIGNMENT_RIGTH){
			float textLenght=PtsConverter.pt2mm(contentByte.getEffectiveStringWidth(text, false));
			px=px+width-textLenght;
		}
		contentByte.moveText(PtsConverter.mm2Pt(px), PtsConverter.mm2Pt2(py));
		contentByte.showText(text);
		contentByte.endText();
		contentByte.restoreState();
	}
	
	/**TODO this is HARDCODED, handle it
	 * @throws DocumentException */
	private void insertHeadToPDF() throws DocumentException{
		float w=PtsConverter.mm2Pt(form.getHeadArea().getWidth());
		float h=PtsConverter.mm2Pt2(form.getHeadArea().getHeight());
		PdfTemplate template = contentByte.createTemplate(w,h);

        // put our text into a phrase
        
        Client pt=invoice.getPrintedTo();
        
        String line="";
        System.out.println(line);
        System.out.println(new Gson().toJson(pt));
        System.out.println(pt.getConsummer()+". "+pt.getAddress()+". ");
        System.out.println(pt.getConsummer()+". "+pt.getAddress()+". "+pt.getCity()+". "+pt.getState()+". "+"CP. "+(pt.getCp()!=null?pt.getCp():"N/A")+". "+"RFC "+(pt.getRfc()!=null?pt.getRfc():"N/A")+". "+pt.getAditionalReference()+".");
        String CITY=null;
        if(pt.getLocality()!=null){
        	if(!pt.getLocality().equals("")){
        		CITY=pt.getLocality();
        		if(pt.getCity()!=null){
        			if(!pt.getCity().equals("")){
        				if(!pt.getLocality().equals(pt.getCity()))CITY=pt.getLocality()+". "+pt.getCity();
        			}
        		}
        	}
        }
        System.out.println("CITY:"+CITY);
        line+=
        	(pt.getConsummer()!=null?(!pt.getConsummer().equals("")?(pt.getConsummer()):("")):(""))+
        	(pt.getAddress()!=null?(!pt.getAddress().equals("")?(". "+pt.getAddress()):("")):(""))+
        	(pt.getExteriorNumber()!=null?(!pt.getExteriorNumber().equals("")?(" #"+pt.getExteriorNumber()):("")):(""))+
        	(pt.getInteriorNumber()!=null?(!pt.getInteriorNumber().equals("")?("-"+pt.getInteriorNumber()):("")):(""))+
        	(pt.getSuburb()!=null?(!pt.getSuburb().equals("")?(". "+pt.getSuburb()):("")):(""))+
        	(CITY!=null?(!CITY.equals("")?(". "+CITY):("")):(""))+
        	(pt.getState()!=null?(!pt.getState().equals("")?(". "+pt.getState()):("")):(""))+
        	//(pt.getCountry()!=null?(!pt.getCountry().equals("")?(". "+pt.getCountry()):("")):(""))+
        	(pt.getCp()!=null?(!pt.getCp().equals("")?(". CP: "+pt.getCp()):("")):(""))+
        	(pt.getRfc()!=null?(!pt.getRfc().equals("")?(". RFC: "+pt.getRfc()):("")):(""))+
        	//(pt.get!=null?(!pt.get.equals("")?(". "+pt.get):("")):(""))+
        	(pt.getAditionalReference()!=null?(!pt.getAditionalReference().equals("")?(". REF: "+pt.getAditionalReference()):("")):(""));
        System.out.println("line:"+line);
        //insertTextToPDF(form.getHeadArea(),line,FONT_SIZE_INVOICE,ALIGNMENT_LEFT);
        Font        font    = new Font(baseFont, FONT_SIZE_INVOICE_HEAD, Font.NORMAL);
        Phrase      p       = new Phrase(line, font);

        // define our column text using the template as the Content Byte
        ColumnText  ct      = new ColumnText(template);
        ct.setSimpleColumn(p,
        		0f,
        		0f,
        		w,h,10,p.ALIGN_LEFT);
        ct.go();

        // place our text box in the very center of the page
        System.out.println("form.getHeadArea().getTop():"+form.getHeadArea().getTop());
        System.out.println("PtsConverter.mm2Pt2(form.getHeadArea().getTop()):"+PtsConverter.mm2Pt2(form.getHeadArea().getTop()));
        float   x   = PtsConverter.mm2Pt(form.getHeadArea().getLeft());
        float   y   = PtsConverter.mm2Pt(form.getHeadArea().getBottom()-form.getHeadArea().getHeight())+6;
        contentByte.addTemplate(template, x, y);
        /*contentByte.setFontAndSize(baseFont, fontSize);
	
		contentByte.moveText(PtsConverter.mm2Pt(px), PtsConverter.mm2Pt2(py));
		contentByte.showText(text);
		contentByte.endText();
		contentByte.restoreState();
		*/
        // and in the lower left corner of the page just for fun
        //cb.addTemplate(template, 0, 0);
	}
	
	private void insertAddressToPDF() {
		insertTextToPDF(form.getAddressArea(), invoice.getPrintedTo().getAddress(),FONT_SIZE_INVOICE,ALIGNMENT_LEFT);
	}

	private void insertCityToPDF() {
		insertTextToPDF(form.getCityArea(), invoice.getPrintedTo().getCity(),FONT_SIZE_INVOICE,ALIGNMENT_LEFT);
	}

	private void insertConsummerToPDF() {
		System.out.println(form.getConsummerArea().getLeft());
		System.out.println(invoice.getPrintedTo().getConsummer());
		insertTextToPDF(form.getConsummerArea(), invoice.getPrintedTo().getConsummer(),FONT_SIZE_INVOICE,ALIGNMENT_LEFT);
	}

	private void insertCountryToPDF() {
		insertTextToPDF(form.getCountryArea(), invoice.getPrintedTo().getCountry(),FONT_SIZE_INVOICE_HEAD,ALIGNMENT_LEFT);
	}

	private void insertCpToPDF() {
		insertTextToPDF(form.getCpArea(), invoice.getPrintedTo().getCp(),FONT_SIZE_INVOICE,ALIGNMENT_LEFT);
	}

	private void insertDateToPDF() {
		insertTextToPDF(form.getDateArea(),
				new SimpleDateFormat("dd/MM/yyyy").format(new Date(invoice.getCreationTime())),
				FONT_SIZE_INVOICE_HEAD,ALIGNMENT_CENTER);
	}

	private void insertEmailToPDF() {
		insertTextToPDF(form.getEmailArea(), invoice.getPrintedTo().getEmail(),FONT_SIZE_INVOICE,ALIGNMENT_LEFT);
	}

	private void insertRfcToPDF() {
		insertTextToPDF(form.getRfcArea(), invoice.getPrintedTo().getRfc(),FONT_SIZE_INVOICE,ALIGNMENT_LEFT);
	}

	private void insertRowsToPDF() {
		for (int i = 0; i < invoice.size(); i++) {
			insertTextToPDF(form.getQuantityArea(i), ""
					+ formatter.format(invoice.getItems().get(i).getQuantity()),FONT_SIZE_INVOICE_LEGIBLE_NUMBER,ALIGNMENT_RIGTH);
			insertTextToPDF(form.getCodeArea(i), "" + invoice.getItems().get(i).getCode(),FONT_SIZE_LITTLE_DATA,ALIGNMENT_LEFT);
			insertTextToPDF(form.getUnitArea(i), "" + invoice.getItems().get(i).getUnit(),FONT_SIZE_INVOICE,ALIGNMENT_CENTER);
			insertTextToPDF(form.getDescriptionArea(i), ""
					+ invoice.getItems().get(i).getDescription(),FONT_SIZE_INVOICE,ALIGNMENT_LEFT);
			insertTextToPDF(form.getUnitPriceArea(i), ""
					+ formatter.format(invoice.getItems().get(i).getUnitPrice()),FONT_SIZE_INVOICE_LEGIBLE_NUMBER,ALIGNMENT_RIGTH);
			insertTextToPDF(form.getTotalPriceArea(i), ""
					+ formatter.format(invoice.getItems().get(i).getTotal()),FONT_SIZE_INVOICE_LEGIBLE_NUMBER,ALIGNMENT_RIGTH);
		}

	}

	private void insertStateToPDF() {
		insertTextToPDF(form.getStateArea(), invoice.getPrintedTo().getState(),FONT_SIZE_INVOICE,ALIGNMENT_LEFT);
	}

	private void insertSubtotalToPDF() {
		insertTextToPDF(form.getSubtotalArea(), "" + formatter.format(invoice.getSubtotal()),FONT_SIZE_NORMAL,ALIGNMENT_RIGTH);
	}

	private void insertTaxesToPDF() {
		insertTextToPDF(form.getTaxesArea(), "" + formatter.format(invoice.getTaxes()),FONT_SIZE_NORMAL,ALIGNMENT_RIGTH);
	}

	private void insertTelToPDF() {
		insertTextToPDF(form.getTelArea(), invoice.getPrintedTo().getTel(),FONT_SIZE_INVOICE_HEAD,ALIGNMENT_LEFT);
	}

	private void insertTotalInwordsToPDF() {
		insertTextToPDF(form.getTotalInWordsArea(), invoice.getTotalInWords(),FONT_SIZE_INVOICE,ALIGNMENT_CENTER);
	}
	private void insertReferenceToPDF() {
		insertTextToPDF(form.getReferenceArea(), invoice.getReference(),FONT_SIZE_NORMAL,ALIGNMENT_CENTER);
	}
	private void insertPaymentToPDF() {
		String payment=((DateFormat)(new SimpleDateFormat("dd/MM/yyyy"))).format(new Date((new Date()).getTime() + (1L+invoice.getPayment())*24L*3600000));
		insertTextToPDF(form.getPaymentArea(), payment,FONT_SIZE_NORMAL,ALIGNMENT_CENTER);
	
	}
	private void insertTotalToPDF() {
		insertTextToPDF(form.getTotalArea(), "" + formatter.format(invoice.getTotal()),FONT_SIZE_NORMAL,ALIGNMENT_RIGTH);
	}
	
	private void openPDF(){
		Rectangle r0=form.getInvoiceArea();
		Rectangle r= new Rectangle(0,0,PtsConverter.mm2Pt(r0.getWidth()),PtsConverter.mm2Pt2(r0.getHeight()));
		document = new Document(r, 0, 0, 0, 0);
		try{System.out.println(out.getCanonicalPath());}
		catch(Exception e){}
		try {
			writer = PdfWriter.getInstance(document, new FileOutputStream(out));
		} catch (DocumentException de) {de.printStackTrace();
		} catch (FileNotFoundException fnfe) {fnfe.printStackTrace();
		}

		document.open();
		if(writer==null)throw new NullPointerException("null pointer exception in writer");
		contentByte = writer.getDirectContent();
		try {
			baseFont = BaseFont.createFont(font, BaseFont.CP1252,
					BaseFont.NOT_EMBEDDED);
		} catch (DocumentException de) {de.printStackTrace();
		} catch (IOException ioe) {ioe.printStackTrace();
		}
	}
	
	private void closePDF(){
		document.close();
	}
	
	



}
