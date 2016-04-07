package com.ferremundo.gth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ferremundo.gth.InvoiceRow;
import com.ferremundo.gth.SpanishInvoiceNumber;
import com.ferremundo.gth.Printer;

public class Invoice {
	
	public static float TAXES_APPLY=1.16f;
	
	private String consummer="";
	private String consummerType="";
	private String address = "";
	private String city = "";
	private String country = "";
	private String cp = "";
	private String date = "";
	private String email = "";
	private String rfc = "";
	private InvoiceRow[] rows;
	private String state = "";
	private String tel = "";
	private float subtotal=0;
	private float taxes=0;
	private float total=0;
	private String totalInWords="cero pesos 00/100MN";
	private String reference = "";
	private String payment = "";
	
	public final static int INVOICE_TYPE_TAXES_APLY=0;
	public final static int INVOICE_TYPE_ORDER=1;
	public final static int INVOICE_TYPE_SAMPLE=2;
	public final static int INVOICE_TYPE_UNKNOWN=-1;
	
	private int invoiceType;
	
	public Invoice(String consummer,String consummerType, String address, String city,
			String country, String cp, String date,
			String email, String rfc, InvoiceRow[] rows, String state,
			String tel,int invoiceType, String reference,String payment) {
		this.consummer = consummer;
		this.consummerType=consummerType;
		this.address = address;
		this.city = city;
		this.country = country;
		this.cp = cp;
		this.date = date;
		this.email = email;
		this.rfc = rfc;
		/*if(invoiceType==INVOICE_TYPE_TAXES_APLY){
			for(int i=0;i<rows.length;i++){
				String unit=rows[i].getUnit();
				String code=rows[i].getCode();
				String description=rows[i].getDescription();
				float unitPrice=rows[i].getUnitPrice()/TAXES_APPLY;
				System.out.println("unitPrice="+unitPrice);
				float quantity=rows[i].getQuantity();
				float totalPrice=quantity*unitPrice;
				rows[i]= new InvoiceRow(quantity,unit,code,description,unitPrice,totalPrice);
			}
		}*/
		this.rows = rows;
		this.state = state;
		this.tel = tel;
		this.invoiceType= invoiceType;
		subtotal = getSubtotalFromRows(rows);
		if(invoiceType==INVOICE_TYPE_TAXES_APLY){
			total=Math.round(subtotal*TAXES_APPLY*100)/100f;
			taxes = total-subtotal;
		}
		else{
			total=Math.round(subtotal*100)/100f;
			taxes=0;
		}
		
		totalInWords=(new SpanishInvoiceNumber(total)).toWords();
		this.reference=reference;

		this.payment=payment;
	}
	public int invoiceType(){
		return invoiceType;
	}
	public String getConsummer() {
		return consummer;
	}
	public String getConsummerType() {
		return consummerType;
	}
	public String getAddress() {
		return address;
	}
	public String getCity() {
		return city;
	}
	public String getCountry() {
		return country;
	}
	public String getCp() {
		return cp;
	}
	public String getDate() {
		return date;
	}
	public String getEmail() {
		return email;
	}
	public String getRfc() {
		return rfc;
	}
	public InvoiceRow getRow(int index) {
		return rows[index];
	}
	public float getQuantity(int index)throws IndexOutOfBoundsException{
		if(index<0|index>=getRowsNumber())throw new IndexOutOfBoundsException(index+" index must be 0-"+(getRowsNumber()-1));
		return rows[index].getQuantity();
	}
	public String getCode(int index)throws IndexOutOfBoundsException{
		if(index<0|index>=getRowsNumber())throw new IndexOutOfBoundsException(index+" index must be 0-"+(getRowsNumber()-1));
		return rows[index].getCode();
	}
	public String getUnit(int index)throws IndexOutOfBoundsException{
		if(index<0|index>=getRowsNumber())throw new IndexOutOfBoundsException(index+" index must be 0-"+(getRowsNumber()-1));
		return rows[index].getUnit();
	}
	public String getDescription(int index)throws IndexOutOfBoundsException{
		if(index<0|index>=getRowsNumber())throw new IndexOutOfBoundsException(index+" index must be 0-"+(getRowsNumber()-1));
		return rows[index].getDescription();
	}
	public float getUnitPrice(int index)throws IndexOutOfBoundsException{
		if(index<0|index>=getRowsNumber())throw new IndexOutOfBoundsException(index+" index must be 0-"+(getRowsNumber()-1));
		return rows[index].getUnitPrice();
	}
	public float getTotalPrice(int index)throws IndexOutOfBoundsException{
		if(index<0|index>=getRowsNumber())throw new IndexOutOfBoundsException(index+" index must be 0-"+(getRowsNumber()-1));
		return rows[index].getTotalPrice();
	}
	public int getRowsNumber(){
		return rows.length;
	}
	public String getState() {
		return state;
	}
	public String getTel() {
		return tel;
	}
	public float getSubtotal() {
		return subtotal;
	}
	public float getTotal() {
		return total;
	}
	public String getTotalInWords() {
		return totalInWords;
	}
	public String getReference() {
		return reference;
	}
	public String getPayment() {
		return payment;
	}
	public float getTaxes() {
		return taxes;
	}

	private InvoiceRow[] getRowsFrom(int i,int j){
		LinkedList<InvoiceRow> list= new LinkedList<InvoiceRow>();
		for(int k=i;k<j;k++){
			list.add(getRow(k));
		}
		InvoiceRow[] rows= new InvoiceRow[list.size()];
		for(int k=0;k<rows.length;k++){
			rows[k]=list.get(k);
		}
		return rows;
	}
	
	public Invoice[] subdivide(int newRowsNumber){
		int rowsNumber=getRowsNumber();
		int invoicesNumber=(int)Math.ceil((rowsNumber*1.0)/newRowsNumber);
		Integer indexes[][]=new Integer[invoicesNumber][2];
		boolean isAnyIncompleteInvoice=false;
		if(invoicesNumber*newRowsNumber>rowsNumber)isAnyIncompleteInvoice=true;
		if(isAnyIncompleteInvoice){
			for(int i=0;i<invoicesNumber-1;i++){
				indexes[i][0]=i*newRowsNumber;
				indexes[i][1]=(i+1)*newRowsNumber;
			}
			indexes[invoicesNumber-1][0]=newRowsNumber*(invoicesNumber-1);
			indexes[invoicesNumber-1][1]=rowsNumber;
		}
		else{
			for(int i=0;i<invoicesNumber;i++){
				indexes[i][0]=i*newRowsNumber;
				indexes[i][1]=(i+1)*newRowsNumber;
			}
		}
		Invoice[] invoices= new Invoice[invoicesNumber];
		InvoiceRow[][] invoiceRows= new InvoiceRow[invoicesNumber][];
		for(int i=0;i<invoicesNumber;i++){
			invoiceRows[i]= getRowsFrom(indexes[i][0], indexes[i][1]);
		}

		Float subtotals[]=new Float[invoicesNumber];
		Float taxess[]=new Float[invoicesNumber];
		Float totals[]=new Float[invoicesNumber];
		String totalInWordss[]=new String[invoicesNumber];
		
		for(int i=0;i<invoicesNumber;i++){
			
			subtotals[i]=getSubtotalFromRows(invoiceRows[i]);
			totals[i]=subtotals[i]*TAXES_APPLY;
			taxess[i]=totals[i]-subtotals[i];
			totalInWordss[i]=(new SpanishInvoiceNumber(totals[i])).toWords();
			invoices[i]=new Invoice(consummer,consummerType, address,city,
					country, cp, date,
					email, rfc,  invoiceRows[i], state,
					tel,invoiceType,reference,payment);
		}
		return invoices;
	}
	
	private float getSubtotalFromRows(InvoiceRow[] rows){
		float subtotal=0;
		for(int i=0;i<rows.length;i++){
			subtotal+=rows[i].getTotalPrice();
		}
		return subtotal;
	}
	
	public void printToConsole(){
		System.out.println(getAddress());
		System.out.println(getCity());
		System.out.println(getConsummer());
		System.out.println(getCountry());
		System.out.println(getCp());
		System.out.println(getDate());
		System.out.println(getEmail());
		System.out.println(getRfc());
		System.out.println("rowsNumber = "+getRowsNumber());
		System.out.println(getState());
		System.out.println(getSubtotal());
		System.out.println(getTaxes());
		System.out.println(getTel());
		System.out.println(getTotal());
		System.out.println(getTotalInWords());
		System.out.println(getReference());
		for(int i=0;i<rows.length;i++){
			System.out.println(getRow(i).toString());
		}
		
	}
	
	public static void main(String[] args) {
		InvoiceRow[] invoiceRows = new InvoiceRow[20];
		for(int i=0;i<20;i++){
			invoiceRows[i]= new InvoiceRow(1,"unit","code-"+i,"desc",i*10,i*10);
		}
		Invoice invoice= new Invoice("raymundo","1", "cerrada del Fundillo 36","morelia",
				"méxico", "58120", "today",
				"raynmune@hotmail.com", "rycs701983",  invoiceRows, "michoacán",
				"3132205",INVOICE_TYPE_ORDER,"AFDSFA345","09/12/3");
		//invoice.printToConsole();
		Invoice[] invoices= invoice.subdivide(InvoiceFormFM01.ROWS_NUMBER);
		for(int i=0;i<invoices.length;i++){
			invoices[i].printToConsole();
		}
		
		InputStream stream= null;
		try {stream =new FileInputStream("/home/dios/workspace/Nepering/src/org/neper/core/form/formDescriptorInvoiceFM01.xml");}
		catch(FileNotFoundException fnfe){}
		
		InvoiceForm form= new InvoiceFormFM01(stream);
		
		Printer printer= new Printer(form,invoices[0],Printer.PRINTER_PDF_FILE,new File("/home/dios/tmp/test01.pdf"));
		printer.print();
	}
}
