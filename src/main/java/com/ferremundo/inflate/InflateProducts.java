package com.ferremundo.inflate;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ferremundo.Client;
import com.ferremundo.EMF;
import com.ferremundo.Product;

public class InflateProducts extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
    	FileInputStream fstream = null;
        try{
        	fstream=new FileInputStream("/opt/workspace/com.ferremundo/db/products");
        }catch(FileNotFoundException e){e.printStackTrace();}
        DataInputStream in = new DataInputStream(fstream);
         	BufferedReader br = new BufferedReader(new InputStreamReader(in));
         	String strLine;
         	EntityManager em=EMF.get(EMF.UNIT_PRODUCT).createEntityManager();
         	em.getTransaction().begin();
         	try{
         		while ((strLine = br.readLine()) != null)   {
         			String[] fields=strLine.split("\t");
         			String mark=fields[0];
         			String code=fields[1];
         			String unit=fields[2];
         			String description=fields[3];
         			float unitPrice=Float.parseFloat(fields[4].replace(",","."));
        			int productPriceKind=Integer.parseInt(fields[5]);
        			Product product=new Product( code,  unitPrice,  unit,  mark, description,  productPriceKind);
        			em.persist(product);
         		}
         	}catch(IOException e){e.printStackTrace();}
         	try{in.close();}
         	catch(IOException e){e.printStackTrace();}
         	em.getTransaction().commit();
	}
	
	public void add(Product product){
		EntityManager em=EMF.get(EMF.UNIT_PRODUCT).createEntityManager();
     	em.getTransaction().begin();
     	em.persist(product);
     	em.getTransaction().commit();
     	em.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
	}
	
	

}
