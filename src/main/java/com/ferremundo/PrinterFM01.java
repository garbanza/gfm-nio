package com.ferremundo;

import java.io.File;
import java.io.IOException;

import com.ferremundo.stt.GSettings;

public class PrinterFM01 implements Printer {

	private File file;
	private String where;
	
	public PrinterFM01(File file,String where) {
		this.file=file;
		this.where=where;
	}
	
	@Override
	public boolean print(int copies) {
		Log log= new Log();
		log.object(file,where);
		boolean printing=new Boolean(GSettings.get("PRINTING"));
		Process p = null;
		ProcessBuilder pb;
		try {
			log.info("lpr -P "+where+ " -# "+copies+" "+file.getCanonicalPath());
		} catch (IOException e1) {
			log.trace("failed to print",e1);
		}
		if(!printing)return true;
		try {
			/*pb=new ProcessBuilder(new String[]{"bash","-c",
					"lpr -P "+where+" -# "+copies+" "+file.getCanonicalPath()});*/
			p=Runtime.getRuntime().exec(new String[]{"bash","-c",
					"lpr -P "+where+" -# "+copies+" "+file.getCanonicalPath()
			});
			log.info("printing exit code:"+p.waitFor());
		} catch (IOException e) {
			log.trace("failed to print",e);
		} catch (InterruptedException e) {
			log.trace("failed to print",e);
		}
		
		return true;
	}
	
}
