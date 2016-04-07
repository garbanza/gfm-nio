package com.ferremundo;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.ferremundo.OnlineClient;
import com.ferremundo.db.Mongoi;
import com.mongodb.DBObject;

public class Port extends HttpServlet{

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		//String sIPAddress = req.getRemoteAddr();
		int clientReference=new Integer(req.getParameter("clientReference"));
		OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
		if(!(onlineClient.isAuthenticated(req)&&(
				onlineClient.hasAccess(AccessPermission.PRODUCT_READ)||
				onlineClient.hasAccess(AccessPermission.CONSUMMER_READ)||
				onlineClient.hasAccess(AccessPermission.BASIC)||
				onlineClient.hasAccess(AccessPermission.ADMIN)
				))){
			resp.getWriter().print("{}");return;
		}
		
		String search=req.getParameter("search");
		
		int consummerType=new Integer(req.getParameter("consummerType"));
		
		int requestNumber=new Integer(req.getParameter("requestNumber"));
		
		onlineClient.setRequestNumber(requestNumber);
		System.out.println("clientReference: "+clientReference+"\nsessionId: "+req.getSession().getId());
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		
		/**TODO delete private fields from products and clients resultsets like stored, requested, sending, etc.*/
		if(null!=search){
			String searchRequestID=req.getParameter("searchRequestID");
			String commandKind=req.getParameter("commandkind");
			System.out.println("commandKind="+commandKind);
			if(null!=commandKind){
				String decSearch=URLDecoder.decode(search,"utf-8").toUpperCase();
				
				List<String> matchList = new ArrayList<String>();
				Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");
				Matcher regexMatcher = regex.matcher(decSearch);
				while (regexMatcher.find()) {
				    if (regexMatcher.group(1) != null) {
				        // Add double-quoted string without the quotes
				        matchList.add(regexMatcher.group(1));
				    } else if (regexMatcher.group(2) != null) {
				        // Add single-quoted string without the quotes
				        matchList.add(regexMatcher.group(2));
				    } else {
				        // Add unquoted word
				        matchList.add(regexMatcher.group());
				    }
				}
				String[] patterns=matchList.toArray(new String[1]);
				if(commandKind.equals("product")||commandKind.equals("retrieve")){
					if(!(onlineClient.hasAccess(AccessPermission.PRODUCT_READ)||
							onlineClient.hasAccess(AccessPermission.BASIC))){
						resp.getWriter().print("{\"products\" : [ ],\"requestNumber\" : \""+requestNumber+"\"}");
						return;
					}
					
					
					System.out.println("searching for:::'"+decSearch+"'");
					//ProductsStore ps=ProductsStore.instance();
					
					//String encoded=URLEncoder.encode(ps.searchToXML(decSearch, 10,searchRequestID),"utf-8");
					//decSearch.split(" ");
					List<DBObject> list=Product.find(patterns,onlineClient,requestNumber);
					if(requestNumber<onlineClient.getRequestNumber()){
						resp.getWriter().print("{\"products\" : [ ],\"requestNumber\" : \""+requestNumber+"\"}");
						System.out.println("request no enviada -> "+requestNumber+"<"+onlineClient.getRequestNumber()+" :"+patterns);
						return;
					}
					String json="{ \"products\" : [ ";
					for(int i=0;i<list.size();i++){
						DBObject product=list.get(i);
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
						json+=product;
						if(i<list.size()-1)json+=" , ";
					}
					json+=" ], \"requestNumber\" : \""+requestNumber+"\"}";
					//String json=ps.searchToJson(decSearch.replaceAll("undefined", ""), consummertype,30);
					System.out.println("consummerType -> "+consummerType);
					System.out.println(json);
					resp.getWriter().print(json);//"{\"p\":[{\"code\":\"0\"}]}");
				}
				else if(commandKind.equals("client")||commandKind.equals("agent")||commandKind.equals("agentstatus")||commandKind.equals("clientstatus")){
					if(!(onlineClient.hasAccess(AccessPermission.CONSUMMER_READ)||
							onlineClient.hasAccess(AccessPermission.BASIC)
							||onlineClient.hasAccess(AccessPermission.ADMIN))){
						resp.getWriter().print("{\"clients\" : [ ],\"requestNumber\" : \""+requestNumber+"\"}");
						return;
					}
					String where=Mongoi.CLIENTS;
					if(commandKind.equals("agent")||commandKind.equals("agentstatus"))where=Mongoi.AGENTS;

					System.out.println("client searching for:::'"+decSearch+"'");
					//ClientsStore cs=ClientsStore.instance();
					resp.setCharacterEncoding("utf-8");
					resp.setContentType("application/json");
					//String encoded=URLEncoder.encode(ps.searchToXML(decSearch, 10,searchRequestID),"utf-8");
					//String json=cs.searchToJson(decSearch.replaceAll("undefined", ""), 20,searchRequestID);

					List<DBObject> list=Client.find(patterns,where);
					if(requestNumber<onlineClient.getRequestNumber()){
						resp.getWriter().print("{\"clients\" : [ ],\"requestNumber\" : \""+requestNumber+"\"}");
						System.out.println("request no enviada -> "+requestNumber+"<"+onlineClient.getRequestNumber()+" :"+patterns);
						return;
					}
					String json="{ \"clients\" : [ ";
					for(int i=0;i<list.size();i++){
						DBObject ob=list.get(i);
						//ob.put("code","");
						json+=ob;
						if(i<list.size()-1)json+=" , ";
					}
					json+="], \"requestNumber\" : \""+requestNumber+"\"}";
					System.out.println(json);
					resp.getWriter().print(json);
				}
			}
		}
		
	}
	
	private class IPRequest{
		int requestNumber=0;;
		String ipAddress;
		public IPRequest(String ipAddress) {
			this.ipAddress=ipAddress;
		}
		int increment(){
			requestNumber+=1;
			return requestNumber;
		}
		
		int getRequestNumber(){
			return requestNumber;
		}
		
		boolean isIP(String ip){
			return ipAddress.equals(ip);
		}
		
	}

}

