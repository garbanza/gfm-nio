package com.ferremundo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ferremundo.db.Mongoi;
import com.google.gson.Gson;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ClientHistory extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response){
		try{	
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/json");
			int clientReference=new Integer(request.getParameter("clientReference"));
			String where=request.getParameter("where");
			OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
			if(!onlineClient.isAuthenticated(request)&&!(
					(where.equals(Mongoi.AGENTS)&&onlineClient.hasAccess(AccessPermission.AGENT_READ))||
					(where.equals(Mongoi.CLIENTS)&&onlineClient.hasAccess(AccessPermission.CONSUMMER_READ))||
					onlineClient.hasAccess(AccessPermission.BASIC)||
					onlineClient.hasAccess(AccessPermission.ADMIN)
					)){
				response.sendError(response.SC_UNAUTHORIZED,"acceso denegado");return;
			}
			String hash=request.getParameter("hash");
			String[] fields=null;
			List<DBObject> list=new LinkedList<DBObject>();
			System.out.println("find history for:"+hash+" in '"+where);
			if(where.equals(Mongoi.AGENTS.toString())){
				
				list=new Mongoi().doFindAgentHistory(hash, 50,0);
			}
			else if(where.equals(Mongoi.CLIENTS)){
				list=new Mongoi().doFindClientHistory(hash, 50,0);
				
			}
			System.out.println(new Gson().toJson(list));
			response.getWriter().write("{ \"invoices\" : "+new Gson().toJson(list)+" }");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
}
