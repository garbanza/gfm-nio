package com.ferremundo;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ferremundo.db.Mongoi;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


public class GetInvoice extends HttpServlet{
	//EntityManager emis=EMF.get(EMF.UNIT_INVOICEFM01).createEntityManager();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int clientReference=new Integer(req.getParameter("clientReference"));
		OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
		if(!(onlineClient.isAuthenticated(req)&&(
				onlineClient.hasAccess(AccessPermission.INVOICE_READ)||
				onlineClient.hasAccess(AccessPermission.BASIC)||
				onlineClient.hasAccess(AccessPermission.ADMIN)
				))){
			resp.sendError(resp.SC_UNAUTHORIZED,"acceso denegado");return;
			
		}
		String command=req.getParameter("command");
		String ref=req.getParameter("reference").replace(".","").toUpperCase();
		System.out.println("retrieving reference = "+ref);
			//	emis.getTransaction().begin();
		//listStore=em.createNativeQuery("select * from Product",Product.class).getResultList();
		DBObject dbObject= new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"reference\" : \""+ref+"\" }");
		//InvoiceFM01 invoice = (InvoiceFM01)emis.createNativeQuery("select * from InvoiceFM01 x where x.reference like '"+ref+"'",InvoiceFM01.class).getResultList().get(0);
		int invoiceType=new Integer(dbObject.get("invoiceType").toString());
		System.out.println(ref+"-> "+dbObject);
		//emis.close();
		BasicDBList dbList= (BasicDBList)dbObject.get("items");
		BasicDBList listRet=new BasicDBList();
		for(int i=dbList.size()-1;i>=0;i--){
			//System.out.println("["+i+"] "+((DBObject)dbList.get(i)).toString());
			DBObject ob=(DBObject)dbList.get(i);
			/*if(invoiceType==InvoiceFM01.INVOICE_TYPE_TAXES_APLY){
				float fixedPrice=(new Float(ob.get("unitPrice").toString()))*InvoiceFM01.TAXES_APPLY;
				ob.put("unitPrice", Math.round(fixedPrice*100)/100f);
			}*/
			DBObject obFromCode=new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"code\" : \""+ob.get("code").toString()+"\" }");
			//System.out.println(obFromCode);
			
			if(obFromCode!=null){
				
				ob.put("mark", obFromCode.get("mark").toString());
				String hash=Product.dbObjectToHash(ob);
				DBObject fromDB=new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"hash\" : \""+hash+"\" }");
				//System.out.println((new Integer(ob.get("id").toString())==-1)+"fromDB "+fromDB);
				if(ob.containsField("id")){
					if(new Integer(ob.get("id").toString())==-1)System.out.println("id equals '-1'");
					else{
						if(fromDB!=null){
							ob.put("id", new Long(fromDB.get("id").toString()));
						}
					}
				}
				else {
					ob.put("id", -1);
				}
			}
			listRet.add(ob);
			//System.out.println("listRet "+listRet);
		}
		DBObject objRet=new BasicDBObject();
		objRet.put("client", (DBObject)dbObject.get("client"));
		objRet.put("items", listRet);
		System.out.println("retrieving "+objRet);
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		//resp.getWriter().print(invoice.getJson());
		if(command.equals("@r")){
			resp.getWriter().print(objRet);
		}
		else if(command.equals("@rl")){
			resp.getWriter().write("{ \"invoice\" : "+dbObject.toString()+" }");
		}
	}
}
