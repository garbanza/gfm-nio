package com.ferremundo.gth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.ferremundo.gth.Invoice;
import com.ferremundo.gth.InvoiceForm;
import com.ferremundo.gth.PtsConverter;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class Printer {

	public static int PRINTER_PDF_FILE = 1;
	public static int PRINTER_PHYSICAL = 0;
	public static int PRINTER_STANDARD_OUTPUT = 3;
	public static int PRINTER_TEXT_FILE = 2;

	private static int PRINTERS_NUMBER = 4;
	private int printerSelected = 1;

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
	
	//TODO remove hardCode here please mother fucker
	public Printer(InvoiceForm form, Invoice invoice, int printer, File out) {
		this.form = form;
		this.invoice = invoice;
		this.out = out;
		if(form==null)throw new NullPointerException("null InvoiceForm");
		if(invoice==null)throw new NullPointerException("null Invoice");
		if(out==null)throw new NullPointerException("null File out");
		if(!out.exists()){
			try{throw new FileNotFoundException("file not found "+out.getName());}
			catch(FileNotFoundException fnfe){}
		}
		if (printer < 0 | printer >= PRINTERS_NUMBER)
			throw new IndexOutOfBoundsException(printer + " out of 0-"
					+ (PRINTERS_NUMBER - 1));
		printerSelected = printer;

	}

	private void insertAddressToPDF() {
		insertTextToPDF(form.getAddressArea(), invoice.getAddress(),FONT_SIZE_INVOICE_LEGIBLE_NUMBER,ALIGNMENT_LEFT);
	}

	private void insertCityToPDF() {
		insertTextToPDF(form.getCityArea(), invoice.getCity(),FONT_SIZE_INVOICE_LEGIBLE_NUMBER,ALIGNMENT_LEFT);
	}

	private void insertConsummerToPDF() {
		insertTextToPDF(form.getConsummerArea(), invoice.getConsummer(),FONT_SIZE_INVOICE_LEGIBLE_NUMBER,ALIGNMENT_LEFT);
	}

	private void insertCountryToPDF() {
		insertTextToPDF(form.getCountryArea(), invoice.getCountry(),FONT_SIZE_INVOICE_HEAD,ALIGNMENT_LEFT);
	}

	private void insertCpToPDF() {
		insertTextToPDF(form.getCpArea(), invoice.getCp(),FONT_SIZE_INVOICE_LEGIBLE_NUMBER,ALIGNMENT_LEFT);
	}

	private void insertDateToPDF() {
		insertTextToPDF(form.getDateArea(), invoice.getDate(),FONT_SIZE_INVOICE_HEAD,ALIGNMENT_CENTER);
	}

	private void insertEmailToPDF() {
		insertTextToPDF(form.getEmailArea(), invoice.getEmail(),FONT_SIZE_INVOICE,ALIGNMENT_LEFT);
	}

	private void insertRfcToPDF() {
		insertTextToPDF(form.getRfcArea(), invoice.getRfc(),FONT_SIZE_INVOICE_HEAD,ALIGNMENT_LEFT);
	}

	private void insertRowsToPDF() {
		for (int i = 0; i < invoice.getRowsNumber(); i++) {
			insertTextToPDF(form.getQuantityArea(i), ""
					+ formatter.format(invoice.getQuantity(i)),FONT_SIZE_INVOICE_LEGIBLE_NUMBER,ALIGNMENT_RIGTH);
			insertTextToPDF(form.getCodeArea(i), "" + invoice.getCode(i),FONT_SIZE_LITTLE_DATA,ALIGNMENT_LEFT);
			insertTextToPDF(form.getUnitArea(i), "" + invoice.getUnit(i),FONT_SIZE_INVOICE,ALIGNMENT_CENTER);
			insertTextToPDF(form.getDescriptionArea(i), ""
					+ invoice.getDescription(i),FONT_SIZE_INVOICE,ALIGNMENT_LEFT);
			insertTextToPDF(form.getUnitPriceArea(i), ""
					+ formatter.format(invoice.getUnitPrice(i)),FONT_SIZE_INVOICE_LEGIBLE_NUMBER,ALIGNMENT_RIGTH);
			insertTextToPDF(form.getTotalPriceArea(i), ""
					+ formatter.format(invoice.getTotalPrice(i)),FONT_SIZE_INVOICE_LEGIBLE_NUMBER,ALIGNMENT_RIGTH);
		}

	}

	private void insertStateToPDF() {
		insertTextToPDF(form.getStateArea(), invoice.getState(),FONT_SIZE_INVOICE_HEAD,ALIGNMENT_LEFT);
	}

	private void insertSubtotalToPDF() {
		insertTextToPDF(form.getSubtotalArea(), "" + formatter.format(invoice.getSubtotal()),FONT_SIZE_NORMAL,ALIGNMENT_RIGTH);
	}

	private void insertTaxesToPDF() {
		insertTextToPDF(form.getTaxesArea(), "" + formatter.format(invoice.getTaxes()),FONT_SIZE_NORMAL,ALIGNMENT_RIGTH);
	}

	private void insertTelToPDF() {
		insertTextToPDF(form.getTelArea(), invoice.getTel(),FONT_SIZE_INVOICE_HEAD,ALIGNMENT_LEFT);
	}

	private void insertTextToPDF(Rectangle area, String text, float fontSize,int alignment) {
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

	private void insertTotalInwordsToPDF() {
		insertTextToPDF(form.getTotalInWordsArea(), invoice.getTotalInWords(),FONT_SIZE_INVOICE,ALIGNMENT_CENTER);
	}
	private void insertReferenceToPDF() {
		insertTextToPDF(form.getReferenceArea(), invoice.getReference(),FONT_SIZE_NORMAL,ALIGNMENT_CENTER);
	}
	private void insertPaymentToPDF() {
		insertTextToPDF(form.getPaymentArea(), invoice.getPayment(),FONT_SIZE_NORMAL,ALIGNMENT_CENTER);
	
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
		} catch (DocumentException de) {
		} catch (FileNotFoundException fnfe) {
		}
		
		document.open();
		if(writer==null)throw new NullPointerException("null pointer exception in writer");
		contentByte = writer.getDirectContent();
		try {
			baseFont = BaseFont.createFont(font, BaseFont.CP1252,
					BaseFont.NOT_EMBEDDED);
		} catch (DocumentException de) {
		} catch (IOException ioe) {
		}
	}
	
	private void closePDF(){
		document.close();
	}

	public void print() {
		if (printerSelected == PRINTER_PHYSICAL)
			printToPhysical();
		else if (printerSelected == PRINTER_PDF_FILE)
			printToPDF();
		else if (printerSelected == PRINTER_TEXT_FILE)
			printToTextFile();
		else if (printerSelected == PRINTER_STANDARD_OUTPUT)
			printToStandardOutput();

	}

	private void printToPDF() {
		openPDF();
		insertConsummerToPDF();
		insertAddressToPDF();
		insertCityToPDF();
		//insertCountryToPDF();
		insertCpToPDF();
		insertDateToPDF();
		//insertEmailToPDF();
		insertRfcToPDF();
		insertRowsToPDF();
		insertStateToPDF();
		insertSubtotalToPDF();
		insertTaxesToPDF();
		insertTelToPDF();
		insertTotalInwordsToPDF();
		insertReferenceToPDF();
		insertTotalToPDF();
		insertPaymentToPDF();
		closePDF();
	}

	private void printToPhysical() {
	}

	private void printToStandardOutput() {
	}

	private void printToTextFile() {
	}

}
