package com.ferremundo;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ferremundo.db.Mongoi;
import com.mongodb.DBObject;

public class Getthis extends HttpServlet{

	

	/*
	public List<Product> getProducts(int size, String matches){
		EntityManager em = EMF.get().createEntityManager();
		String query = "select from " + Product.class.getName()+" where description.like('%"+matches+"%')";
		List<Product> products = (List<Product>) em.newQuery(query).execute();
		em.close();
		return products;
	}
	*/
	
	 
	protected void doPost(HttpServletRequest req, HttpServletResponse resp){
		try{
			int clientReference=new Integer(req.getParameter("clientReference"));
			OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
			if(!(onlineClient.isAuthenticated(req)&&(
					onlineClient.hasAccess(AccessPermission.PRODUCT_READ)||
					onlineClient.hasAccess(AccessPermission.BASIC)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					))){
				resp.getWriter().print("[]");return;
			}
			
			
			String list=req.getParameter("list");
			String clientCode=req.getParameter("code");
			String ct=req.getParameter("consummerType");
			Integer consummerType=null;
			if(ct!=null)consummerType=new Integer(ct);
			System.out.println("list: "+URLDecoder.decode(list,"utf-8")+" code: "+clientCode);
			try{
				if(null!=list&&null!=clientCode){
					String decList=URLDecoder.decode(list,"utf-8");
					String decCode=URLDecoder.decode(clientCode,"utf-8");
					System.out.println(decList+"\n"+decCode);
					//Client client=ClientsStore.instance().getClientByCode(decCode);
					if(consummerType==null)consummerType=new Integer(new Mongoi().doFindOne(Mongoi.CLIENTS, "{ \"code\": \""+clientCode+"\"}").get("consummerType").toString());
					//int consummerType=client.getConsummerType();
					JSONArray jsa= new JSONArray(decList);
					int length=jsa.length();
					
					String json="[ ";
					for(int i=0;i<length;i++){
						JSONObject jso=jsa.getJSONObject(i);
						int productID=new Integer(jso.getString("id"));
						if(productID==-1){
							json+=jso.toString()+(i==length-1?"":",");
						}
						else {
							DBObject product=new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"id\" : "+productID+" }");
							//Product product=ProductsStore.getByKey(key);
							int productPriceKind=new Integer(product.get("productPriceKind").toString());
							float unitPrice=new Float(product.get("unitPrice").toString());
							if(consummerType==Client.TYPE_1){
								//unitPrice=product.getUnitPrice();
							}
							else if(consummerType==Client.TYPE_2){
								if(productPriceKind==Product.KIND_1)unitPrice*=Product.FACTOR_1;
								else if(productPriceKind==Product.KIND_2)unitPrice*=Product.FACTOR_2;
							}
							else if(consummerType==Client.TYPE_3){
								if(productPriceKind==Product.KIND_1)unitPrice*=Product.FACTOR_3;
								else if(productPriceKind==Product.KIND_2)unitPrice*=Product.FACTOR_4;
							}
							product.put("unitPrice", unitPrice);
							json+=product+(i==length-1?"":",");
						}
					}
					json+=" ]";
					resp.setCharacterEncoding("utf-8");
					resp.setContentType("application/json");
					System.out.println("getthis::: "+json);
					resp.getWriter().print(json);

				}
			}catch(JSONException e){e.printStackTrace();}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}

}

