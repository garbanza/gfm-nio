package com.ferremundo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

public class QRGen {
	public static void main(String[] args) {
		String re="?re=AAA010101AAA";
		String rr="&rr=PEPJ8001019Q8";
		float total=123412.1232f;
		DecimalFormat formatter = new DecimalFormat("0000000000.000000");
		String tt="&tt="+formatter.format(total);
		String id="&id=ad662d33-6934-459c-a128-BDf0393f0f44";
		String toqrc=re+rr+tt+id;
		System.out.println(toqrc+" "+toqrc.length());
		
		try {
			QRCode.from(toqrc)
			.to(ImageType.PNG).withSize(512, 512)
			.writeTo(new FileOutputStream(new File("/home/rayn/Documents/tmp/qrcode.png")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void gen(String emit, String recieve, float total, String uuid, String pngOut){
		String re="?re="+emit.toUpperCase();
		String rr="&rr="+recieve.toUpperCase();
		DecimalFormat formatter = new DecimalFormat("0000000000.000000");
		String tt="&tt="+formatter.format(total);
		String id="&id="+uuid;
		String toqrc=re+rr+tt+id;
		try {
			QRCode.from(toqrc)
			.to(ImageType.PNG).withSize(512, 512)
			.writeTo(new FileOutputStream(new File(pngOut)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
