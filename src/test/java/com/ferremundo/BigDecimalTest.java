package com.ferremundo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class BigDecimalTest {

	public static void main(String[] args) {
		MathContext mt=new MathContext(5, RoundingMode.HALF_UP);
	    BigDecimal subTotal=new BigDecimal(0,mt);
	    float dec=.1f;
	    float iva=16f;
	    BigDecimal unitPrice= new BigDecimal(dec/(1+iva*1f/100),mt);
	    
	    BigDecimal subtotal =unitPrice.multiply(new BigDecimal(20,mt), mt);
	    BigDecimal total= subTotal.multiply(new BigDecimal(1+iva*1f/100,mt),mt);
	    System.out.println(unitPrice.toPlainString());
	    System.out.println(subtotal.toPlainString());
	    System.out.println(total.toPlainString());
	}
}
