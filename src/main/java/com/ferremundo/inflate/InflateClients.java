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

public class InflateClients extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
    	FileInputStream fstream = null;
        try{
        	fstream=new FileInputStream("/opt/workspace/com.ferremundo/db/clients");
        }catch(FileNotFoundException e){e.printStackTrace();}
        DataInputStream in = new DataInputStream(fstream);
         	BufferedReader br = new BufferedReader(new InputStreamReader(in));
         	String strLine;
         	EntityManager em=EMF.get(EMF.UNIT_CLIENT).createEntityManager();
         	em.getTransaction().begin();
         	try{
         		while ((strLine = br.readLine()) != null)   {
         			String[] fields=strLine.split("\t");
         			String code = 		fields[0];
         			String consummer =	fields[1];
         			int consummerType = new Integer(fields[6]);
         			String address =	fields[2];
         			String city = 		fields[8];
         			String country = 	fields[10];
         			String state = 		fields[9];
         			String email =		fields[11];
         			String cp =			fields[3];
        			String rfc = 		fields[4];
        			String tel = 		fields[7];
        			int payment = 		new Integer(fields[5]);
        			//Client client = new Client(code, consummer, consummerType, address, city, country, state, email, cp, rfc, tel, payment);
        			//System.out.println(client.toJson());
        			//em.persist(client);
         		}
         	}catch(IOException e){e.printStackTrace();}
         	try{in.close();}
         	catch(IOException e){e.printStackTrace();}
         	em.getTransaction().commit();
	}
	
	public void add(Client client){
		EntityManager em=EMF.get(EMF.UNIT_CLIENT).createEntityManager();
     	em.getTransaction().begin();
     	em.persist(client);
     	em.getTransaction().commit();
     	em.close();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String clientParam=req.getParameter("client");
		
	}

}
