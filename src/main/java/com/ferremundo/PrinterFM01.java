package com.ferremundo;

import java.io.File;

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
		//if(where.equals(PRINTER_ONE)){
			Process p = null;
			
			try{
				System.out.println("lpr -P "+where+ " "+file.getCanonicalPath());
				p=Runtime.getRuntime().exec("lpr -P "+where+ " "+file.getCanonicalPath());}
			catch(Exception e){}
		//s}
		return false;
	}
	
	public static void main(String[] args) {
		boolean computerIsAlive=true;
		while(computerIsAlive){
			
		}
	}
	
}
