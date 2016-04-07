package com.ferremundo.gth;

import java.io.File;

import com.ferremundo.gth.InvoicePrintingManager;

public class StreamDetectorCSV {
	
	public static void main(String[] args) {
		String hardCodedURLFile="/home/dios/FERREMUNDO/BaseDeDatos/baseFerremundoPointer.csv";
		File file= null;
		file=new File(hardCodedURLFile);
		do {
			try{Thread.sleep(100);}
			catch(InterruptedException ie){}
			if(file.exists()){
				InvoicePrintingManager manager= new InvoicePrintingManager();
				manager.manage();
			}
		} while (true);
	}

}
