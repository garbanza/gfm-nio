package com.ferremundo.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.ibm.icu.text.DecimalFormat;

public class Util {

	public static float round2(float f){
		BigDecimal big = new BigDecimal(f);
		big = big.setScale(2, RoundingMode.HALF_UP);
		return big.floatValue();
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	public static double round6(double value){
		return round(value,6);
	}
	public static double round2(double value){
		return round(value,2);
	}
	public static BigDecimal bigRound2(double value){
		double r = round(value,2);
		return new BigDecimal(r,new MathContext(String.valueOf((int)r).length()+2));
	}
	public static BigDecimal bigRound6(double value){
		double r = round(value,6);
		return new BigDecimal(r,new MathContext(String.valueOf((int)r).length()+6));
	}
	
	private static DecimalFormat format6 = new DecimalFormat("0.000000");
	private static DecimalFormat format2 = new DecimalFormat("0.00");
	public static String roundf6(double value){
		return format6.format(value);
	}

	public static BigDecimal round2(BigDecimal value){
		String b6 = format2.format(value.doubleValue());
		return new BigDecimal(b6);
	}
	public static BigDecimal round6(BigDecimal value){
		String b6 = format6.format(value.doubleValue());
		return new BigDecimal(b6);
	}
	public static String roundf2(double value){
		return format2.format(value);
	}
	public static void main(String[] args) {
		double x=100000000.1234567;
		System.out.println(roundf6(x)+" "+round6(x));
		System.out.println(roundf2(x)+" "+round2(x));
	}
}
