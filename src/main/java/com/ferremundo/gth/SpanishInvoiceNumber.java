package com.ferremundo.gth;

public class SpanishInvoiceNumber {
	
	private String number="";
	
	public SpanishInvoiceNumber(double d) {
		double floored=Math.floor(d);
		int cents=(int)Math.round((100*(d-floored)));
		String zero="";
		if(cents<10)zero="0";
		SpanishNumber number= new SpanishNumber(floored);
		String inWords=number.toWords();
		this.number=inWords+"pesos "+zero+cents+"/100MN";
	}

	public String toWords(){
		return number;
	}
	
	public static void main(String[] args) {
		double d=123213.01;
		SpanishInvoiceNumber number= new SpanishInvoiceNumber(d);
		System.out.println(number.toWords());
	}
	
	public static String toWords(double d){
		return new SpanishInvoiceNumber(d).toWords();
	}
}
