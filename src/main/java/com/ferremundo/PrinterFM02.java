package com.ferremundo;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

import com.ferremundo.stt.GSettings;

public class PrinterFM02 implements Printer {

	private File file;
	private String where;
	
	public PrinterFM02(File file,String where) {
		this.file=file;
		this.where=where;
	}
	
	@Override
	public boolean print(int copies) {
		Log log= new Log();
		log.object(file,where, copies);
		boolean printing=new Boolean(GSettings.get("PRINTING"));
		//Process p = null;
		//ProcessBuilder pb;
		/*try {
			log.info("lpr -P "+where+ " -# "+copies+" "+file.getCanonicalPath());
		} catch (IOException e1) {
			log.trace("failed to print",e1);
		}*/
		if (copies==0)return true;
		if(!printing)return true;
		try {
			PrintService printService=findPrintService(where);
			FileInputStream fis = new FileInputStream(file.getCanonicalPath());
			DocFlavor flavor = DocFlavor.INPUT_STREAM.PDF;
			Doc pdfDoc = new SimpleDoc(fis, flavor, null);
			DocPrintJob printJob = printService.createPrintJob();
			PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
			attributes.add(new Copies(copies));
			printJob.print(pdfDoc, attributes);
			fis.close();

			/*pb=new ProcessBuilder(new String[]{"bash","-c",
					"lpr -P "+where+" -# "+copies+" "+file.getCanonicalPath()});*/
			/*p=Runtime.getRuntime().exec(new String[]{"bash","-c",
					"lpr -P "+where+" -# "+copies+" "+file.getCanonicalPath()
			});*/
			log.info("printing exit code: 0");
		} catch (IOException e) {
			log.trace("failed to print",e);
		} catch (PrintException e) {
			log.trace("failed to print",e);
		}
		
		return true;
	}
	
	public static PrintService findPrintService(String printerName) {

        printerName = printerName.toLowerCase();

        PrintService service = null;

        // Get array of all print services
        PrintService[] services = PrinterJob.lookupPrintServices();

        // Retrieve a print service from the array
        for (int index = 0; service == null && index < services.length; index++) {

            if (services[index].getName().toLowerCase().indexOf(printerName) >= 0) {
                service = services[index];
            }
        }

        // Return the print service
        return service;
    }

	@Override
	public boolean print(String options) {
		
		Log log= new Log();
		log.object(file,where);
		boolean printing=new Boolean(GSettings.get("PRINTING"));
		Process p = null;
		ProcessBuilder pb;
		try {
			log.info("lpr "+options+" -P "+where+ " " + file.getCanonicalPath());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		if(!printing)return true;
		try {
			/*pb=new ProcessBuilder(new String[]{"bash","-c",
					"lpr -P "+where+" -# "+copies+" "+file.getCanonicalPath()});*/
			p=Runtime.getRuntime().exec(new String[]{"bash","-c",
					"lpr "+options+" -P "+where+ " " + file.getCanonicalPath()
			});
			p.waitFor();
			log.info("printing exit code:"+p.waitFor());
		} catch (IOException e) {
			log.trace("failed to print",e);
		} catch (InterruptedException e) {
			log.trace("failed to print",e);
		}
		
		return true;
	}
}

