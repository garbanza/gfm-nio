package com.ferremundo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClientHasAccess extends HttpServlet{
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	throws ServletException, IOException {
		int clientReference=new Integer(req.getParameter("clientReference"));
		OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
		
		String perm=req.getParameter("permission");
		AccessPermission permission=AccessPermission.valueOf(perm);
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		if(!onlineClient.isAuthenticated(req)){
			resp.sendError(resp.SC_UNAUTHORIZED ,"no autorizado");return;
		}
		resp.getWriter().print("{ \"hasAccess\" : "+onlineClient.hasAccess(permission)+" }");	
	}

}
