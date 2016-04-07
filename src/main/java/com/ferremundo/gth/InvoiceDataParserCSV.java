package com.ferremundo.gth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import com.ferremundo.gth.Printer;

public class InvoiceDataParserCSV implements InvoiceDataParser{
	
	private InputStream stream;
	
	private int invoiceType;
	
	public InvoiceDataParserCSV(InputStream stream) {
		this.stream=stream;
	}
	
	public InvoiceDataParserCSV(File file){
		try{stream=new FileInputStream(file);}
		catch(FileNotFoundException fnfe){}
	}
	
	public InvoiceDataParserCSV(String file){
		try{stream=new FileInputStream(file);}
		catch(FileNotFoundException fnfe){}		
	}
	
	public Invoice getInvoice() {
		LinkedList<String> stringFile = new LinkedList<String>();
		int ch=0;
		String fileRow="";
		do{
			try{ch=stream.read();}
			catch(IOException e){}
			if(ch=='\n'){
				stringFile.add(fileRow);
				//System.out.println(fileRow.replace("'", ""));
				fileRow="";
			}
			else if(ch==-1);
			else{
				fileRow=fileRow+(char)ch;
			}
		}while(ch!=-1);
		
		String consummer=stringFile.get(0).replace("'", "").toUpperCase();
		String address=stringFile.get(1).replace("'", "").toUpperCase();
		String city=stringFile.get(2).replace("'", "").toUpperCase();
		String cp=stringFile.get(3).replace("'", "").toUpperCase();
		String rfc=stringFile.get(4).replace("'", "").toUpperCase();
		String tel=stringFile.get(5).replace("'", "").toUpperCase();
		String date=stringFile.get(6).replace("'", "").toUpperCase();
		invoiceType=(int)Math.round(new Float(stringFile.get(7).replace("'", "")));
		String reference=getCount();//((DateFormat)(new SimpleDateFormat("yyyyMMddHHmmss"))).format(new Date());
		int daystoPay=(int)Math.round(new Float(stringFile.get(8).replace("'", "")));//.intValue();
		String payment=((DateFormat)(new SimpleDateFormat("dd/MM/yyyy"))).format(new Date((new Date()).getTime() + (1L+daystoPay)*24L*3600000));
		//stringFile.get(8).replace("'", "").toUpperCase();
		LinkedList<InvoiceRow> rows = new LinkedList<InvoiceRow>();
		BufferedReader codeItemBase = null;
		for(int i=9;i<68;i++){
			String[] row = stringFile.get(i).replace("'", "").toUpperCase().split("\t");
			if(row.length==6){
				if("".equals(row[0])){}
				else{
					Float quantity = new Float(row[0]);
					String code=row[1].toUpperCase();
					String unit=row[2];
					try{codeItemBase= new BufferedReader(new InputStreamReader(new FileInputStream("/home/dios/FERREMUNDO/BaseDeDatos/codeItems.base"),"UTF8"));}
					catch(FileNotFoundException fnfe){}
					catch(UnsupportedEncodingException uee){}
					String fileRowCode="";
					String desc="";
					do{
						try{fileRowCode=codeItemBase.readLine();}
						catch(IOException e){}
						String[] st=null;
						if(fileRowCode!=null){
							st=fileRowCode.split("\t");
							if(code.equals(st[0])){
								desc=st[1];
							}
							else fileRowCode="";
						}
					}while(fileRowCode!=null);
					String description="";
					if(desc.equals(""))description=row[3];
					else description=desc;
					Float unitPrice=null;
					Float totalPrice=null;//new Float(row[5]);
					if(invoiceType==Invoice.INVOICE_TYPE_TAXES_APLY){
						unitPrice=Math.round(100f*(new Float(row[4]))/Invoice.TAXES_APPLY)/100f;
						totalPrice=Math.round(quantity*unitPrice*100f)/100f;
					}
					else{
						unitPrice=Math.round((new Float(row[4]))*100f)/100f;
						totalPrice=Math.round(quantity*unitPrice*100f)/100f;
					}
					
					InvoiceRow inviceRow=new InvoiceRow(quantity,unit,code,description,unitPrice,totalPrice);
					rows.add(inviceRow);
				}
			}
		}
		InvoiceRow[] iRows = new InvoiceRow[rows.size()];
		for(int i=0;i<rows.size();i++){
			iRows[i]=rows.get(i);
		}
		Invoice invoice= new Invoice(consummer,"",address,city,"",cp,date,"",rfc,iRows,"",tel,invoiceType,reference,payment);
		return invoice;
	}
	
	public static void main(String[] args) {
		/*
		InvoiceDataParser dataParser= new InvoiceDataParserCSV("/home/dios/FERREMUNDO/BaseDeDatos/baseFerremundoPointer.csv");
		Invoice invoice=dataParser.getInvoice();
		InputStream stream= null;
		try {stream =new FileInputStream("/home/dios/workspace/Nepering/src/org/neper/core/form/formDescriptorInvoiceFM01.xml");}
		catch(FileNotFoundException fnfe){}
		
		InvoiceForm form= new InvoiceFormFM01(stream);
		Printer printer= new Printer(form,invoice,Printer.PRINTER_PDF_FILE,new File("/home/dios/tmp/test01.pdf"));
		printer.print();
		*/
		/*String reference=((DateFormat)(new SimpleDateFormat("yyyyMMddHHmmss"))).format(new Date());
		Date now = new Date();
		String after15days=((DateFormat)(new SimpleDateFormat("dd/MM/yyyy"))).format(new Date(now.getTime() + 15L*24L*Timer.ONE_HOUR));
		Date in8Hours = new Date(now.getTime() + 15L*24L*Timer.ONE_HOUR);
		System.out.println(reference+" ->"+after15days);
		*/
		System.out.println("diez="+Integer.parseInt("10"));
	}

    public String getCount(){
    	
        FileReader reader = null;
        String count="";
        try{
            reader=new FileReader(new File("/home/dios/FERREMUNDO/BaseDeDatos/.counter"));
            BufferedReader br = new BufferedReader(reader);
            count=br.readLine();
            reader.close();
        }
        catch(IOException exception){}
        int newCount=1+Integer.parseInt(count);
        String newCountStr=""+newCount;
        char buffer[] = new char[newCountStr.length()];
        newCountStr.getChars(0, newCountStr.length(), buffer, 0);
        FileWriter fWriter = null;
        try{
            fWriter=new FileWriter("/home/dios/FERREMUNDO/BaseDeDatos/.counter");
            for (int i=0; i < buffer.length; i++) {
                fWriter.write(buffer[i]);
            }
            fWriter.close();
        }catch(IOException exception){}
        String counter535=Integer.toString(newCount,Character.MAX_RADIX);
        //if(counter535.length()==1)counter535="0000"+counter535;
        //else if(counter535.length()==2)counter535="000"+counter535;
        //else if(counter535.length()==3)counter535="00"+counter535;
        //else if(counter535.length()==4)counter535="0"+counter535;
        return counter535.toUpperCase();
        
      
    }
    
}
