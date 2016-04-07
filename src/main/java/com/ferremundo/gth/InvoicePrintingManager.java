package com.ferremundo.gth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ferremundo.gth.Invoice;
import com.ferremundo.gth.InvoiceDataParser;
import com.ferremundo.gth.InvoiceDataParserCSV;
import com.ferremundo.gth.InvoiceForm;
import com.ferremundo.gth.InvoiceFormFM01;

public class InvoicePrintingManager {
	
	public void manage(){
		String base="cotizaciones";
		InvoiceDataParser dataParser= new InvoiceDataParserCSV("/home/dios/FERREMUNDO/BaseDeDatos/baseFerremundoPointer.csv");
		Invoice invoice=dataParser.getInvoice();
		InputStream stream = null;
		try {stream =new FileInputStream("/home/dios/workspace/Nepering/src/org/neper/core/form/formDescriptorInvoiceFM01.xml");}
		catch(FileNotFoundException fnfe){}
		
		InvoiceForm form= new InvoiceFormFM01(stream);
		Invoice[] invoices=invoice.subdivide(form.getRowsNumber());
		if(invoice.invoiceType()==Invoice.INVOICE_TYPE_ORDER)base="pedidos";
		else if(invoice.invoiceType()==Invoice.INVOICE_TYPE_TAXES_APLY)base="facturas";
		String baseDir="/home/dios/FERREMUNDO/"+base+"/";
		String fileOutName=invoice.getReference()+".pdf";//getFileOutName();
		String[] filePDFDest= new String[invoices.length];
		for(int i=0;i<invoices.length;i++){
			filePDFDest[i]=baseDir+fileOutName+"."+i;
			Printer printer= new Printer(form,invoices[i],Printer.PRINTER_PDF_FILE,new File(filePDFDest[i]));
			printer.print();
		}
		File file= new File("/home/dios/FERREMUNDO/BaseDeDatos/baseFerremundoPointer.csv");
		String fileOutDest="";
		if(invoice.invoiceType()==Invoice.INVOICE_TYPE_ORDER){
			fileOutDest="/home/dios/FERREMUNDO/pedidos/"+fileOutName+".csv";
		}
		else if(invoice.invoiceType()==Invoice.INVOICE_TYPE_TAXES_APLY){
			fileOutDest="/home/dios/FERREMUNDO/facturas/"+fileOutName+".csv";
		}
		file.renameTo(new File(fileOutDest));
		
		for(int i=0;i<invoices.length;i++){
			Process p = null;
			//System.out.println(">lpr -P ML320-1TURBO "+ filePDFDest[i]);
			try{p=Runtime.getRuntime().exec("lpr -P ML320-1TURBO "+ filePDFDest[i]);}
			catch(Exception e){}

			/*BufferedReader stdInput = new BufferedReader(new
					InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new
					InputStreamReader(p.getErrorStream()));
					*/
		}
		try{Process p=Runtime.getRuntime().exec("rm -f /home/dios/FERREMUNDO/BaseDeDatos/baseFerremundoPointer.csv");}
		catch(Exception e){}
		
		//for
		// read the output from the command
		/*
		System.out.println("Here is the standard output of the command:\n");
		while ((s = stdInput.readLine()) != null) {
		System.out.println(s);
		}

		// read any errors from the attempted command

		System.out.println("Here is the standard error of the command (if any):\n");
		while ((s = stdError.readLine()) != null) {
		System.out.println(s);
		}
		*/
		
	}
	
	private String getFileOutName(){
		return getDateTime()+".pdf";
	}

	public static void main(String[] args) {
		System.out.println(getDateTime());
	}
	
	private static String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
	
}
