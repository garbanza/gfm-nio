package com.ferremundo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.ferremundo.db.Mongoi;
import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Welcome extends HttpServlet{
	//EntityManager emis=EMF.get(EMF.UNIT_INVOICEFM01).createEntityManager();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException{
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		int clientReference=new Integer(req.getParameter("clientReference"));
		OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
		if(!(onlineClient.isAuthenticated(req)&&(
				onlineClient.hasAccess(AccessPermission.CONSUMMER_CREATE)||
				onlineClient.hasAccess(AccessPermission.BASIC)||
				onlineClient.hasAccess(AccessPermission.ADMIN)
				))){
			resp.sendError(resp.SC_UNAUTHORIZED,"acceso denegado");return;
		}
		
		String clientParam=req.getParameter("client");
		String clientJson="";
		try {
			clientJson = URLDecoder.decode(clientParam,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("persisting client "+clientJson);
		JSONObject jClient=null;
		try {
			jClient=new JSONObject(clientJson);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			jClient.put("address", ((String) jClient.get("address")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
			jClient.put("city", ((String) jClient.get("city")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
			//jClient.put("consummer", ((String) jClient.get("consummer")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
			jClient.put("country", ((String) jClient.get("country")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
			jClient.put("cp", ((String) jClient.get("cp")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
			jClient.put("exteriorNumber", ((String) jClient.get("exteriorNumber")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
			jClient.put("interiorNumber", ((String) jClient.get("interiorNumber")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
			jClient.put("locality", ((String) jClient.get("locality")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
			
			jClient.put("state", ((String) jClient.get("state")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
			jClient.put("suburb", ((String) jClient.get("suburb")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
			jClient.put("tel", ((String) jClient.get("tel")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
			jClient.put("email", ((String) jClient.get("email")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
			
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Client client=null;
		
		String clientDB=null;//Mongoi.CLIENTS;
		String where=req.getParameter("where");
		if(where!=null){
			if(where.equals("clients"))clientDB=Mongoi.CLIENTS;
			else if(where.equals("agents"))clientDB=Mongoi.AGENTS;
			else {
				resp.setStatus(
						HttpServletResponse.SC_UNAUTHORIZED);
				resp.getWriter().print("{ \"error\": \"'No de definio DB\" }");
				return;
			}
		}
		int consummerType;
		int payment;
		String ct="",py="";
		if(jClient.has("consummer")){
			try {

				ct=(String) jClient.get("consummer");
				if(!ct.equals("")){
					jClient.put("consummer", ((String) jClient.get("consummer")).replaceAll("^\\s+|\\s+$", "").toUpperCase());
				}
				else{
					resp.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					resp.getWriter().print("{ \"error\": \"nombre indefinido' }");
					
					return;
				}
				
			}catch (JSONException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}else {resp.setStatus(
			HttpServletResponse.SC_UNAUTHORIZED);
			resp.getWriter().print("{ \"error\": \"'NOMBRE' no definido, campo no recivido\" }");
		
			return;
		}
		if(jClient.has("consummerType")){
			try {
				ct=(String) jClient.get("consummerType");
				if(isInteger(ct)){
					consummerType=Integer.parseInt(ct);
					jClient.put("consummerType", consummerType);
				}
				else{
					resp.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					resp.getWriter().print("{ \"error\": \"tipo '"+ct+"' debe ser entero\" }");
					
					return;
				}
				
			} catch (NumberFormatException e) {
				
				// TODO Auto-generated catch block
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}else {resp.setStatus(
			HttpServletResponse.SC_UNAUTHORIZED);
			resp.getWriter().print("{ \"error\": \"'TIPO' no definido\" }");
		
			return;
		}
		if(jClient.has("payment")){
			try {
				py=(String) jClient.get("payment");
				if(isInteger(py)){
					payment=Integer.parseInt(py);
					jClient.put("payment", payment);
				}
				else{
					resp.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
					resp.getWriter().print("{ \"error\": \"credito '"+py+"' debe ser entero\" }");
					return;
				}
			} catch (NumberFormatException e) {
				
				// TODO Auto-generated catch block
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}else {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.getWriter().print("{ \"error\": \"'CREDITO' no definido\" }");
		
			return;
		}
		try {
			String rfc=((String) jClient.get("rfc")).replaceAll("-", "").replaceAll("^\\s+|\\s+$", "").toUpperCase();
			if(!testRFC(rfc)){
				resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				resp.getWriter().print("{ \"error\": \"rfc '"+rfc+"' incorrecto\" }");
				return;
			}
			jClient.put("rfc", rfc);
		} catch (JSONException e1) {
			resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			resp.getWriter().print("{ \"error\": \"'RFC' no definido\" }");
		
			return;
		}
		client=ClientFactory.create(jClient);
		int id=client.persist(clientDB);
		DBObject dbClient=new Mongoi().doFindOne(clientDB, "{ \"id\" : "+id+" }");
		
		System.out.println(id+" -> "+new Gson().toJson(dbClient));
		try {
			resp.getWriter().print(dbClient);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean testRFC(String rfc) {
		return rfc.matches("[A-Z,Ñ,&]{3,4}[0-9]{2}[0-1][0-9][0-3][0-9][A-Z,0-9]?[A-Z,0-9]?[0-9,A-Z]?");
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
		System.out.println("\""+"   asdsad asd   ".replaceAll("^\\s+|\\s+$", "")+"\"");
	}
}
