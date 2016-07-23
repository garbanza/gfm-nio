package com.ferremundo;

import java.io.File;
import java.io.IOException;

import com.ferremundo.stt.GSettings;

public class PrinterFM01 implements Printer {

	//public static final String PRINTER_ONE="printer";
	public static final String PRINTER_ONE="ML320-1TURBO";
	public static final String PRINTER_TWO="Deskjet-1000-J110-series";
	
	private File file;
	private String where;
	
	//*TODO unhardcode this shit
	private static final String directoryToScan="/home/god/.printing-directory";
	
	public PrinterFM01(File file,String where) {
		this.file=file;
		this.where=where;
	}
	
	@Override
	public boolean print() {
		Log log= new Log();
		log.object(file,where);
		boolean test=new Boolean(GSettings.get("TEST"));
		Process p = null;
		try {
			log.info("lpr -P "+where+ " "+file.getCanonicalPath());
		} catch (IOException e1) {
			log.trace("failed to print",e1);
		}
		if(test){
			return true;
		}
		try {
			p=Runtime.getRuntime().exec("lpr -P "+where+ " "+file.getCanonicalPath());
		} catch (IOException e) {
			log.trace("failed to print",e);
		}
		return true;
	}
	
}
