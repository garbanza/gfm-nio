package com.ferremundo.ei;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;


import mx.bigdata.sat.cfdi.CFDv32;
import mx.bigdata.sat.cfdi.v32.schema.Comprobante;
import mx.bigdata.sat.security.KeyLoader;

public class CFDITest {
/*
	public static void main(String[] args) {
		CFDv32 cfd = null;
		PrivateKey key=null;
		X509Certificate cert=null;
		Comprobante sellado=null;
		try{
			cfd=new CFDv32(new FileInputStream("/home/lucifer/tst/ejemplo1 cfdv3.xml"));
			key=KeyLoader.loadPKCS8PrivateKey(new FileInputStream("/opt/thesat/COSB760826KV8_1301311654S-parasello.key"),  "pass");
			cert=KeyLoader.loadX509Certificate(new FileInputStream("/opt/thesat/COSB760826KV8_1301311654S-SELLO.cer"));
			sellado=cfd.sellarComprobante(key, cert);
			cfd.validar(); // Valida el XML, que todos los elementos estén presentes
		    cfd.verificar(); // Verifica un CFD ya firmado
		    cfd.guardar(System.out); // Serializa el CFD a un OutputStream
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	}*/
}
