package com.ferremundo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils {
	
	public static boolean isIntegerParseInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException nfe) {}
        return false;
    }

	public static boolean isInteger(String str)
	{
		if(str==null)return false;
		if(str.equals(""))return false;
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
	
	public static void main(String[] args) {
		System.out.println(isInteger(""));
		System.out.println(isInteger(null));
		System.out.println(isInteger("12n"));
		System.out.println(isInteger("n12"));
		System.out.println(isInteger("0112"));
		System.out.println(isInteger("12"));
		System.out.println(Integer.parseInt("0001"));
	}
	
	public static String getStringFromInputStream(InputStream is) {
		 
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
 
		String line;
		try {
 
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
 
		return sb.toString();
 
	}
}
