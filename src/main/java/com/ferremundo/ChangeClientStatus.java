package com.ferremundo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ferremundo.db.Mongoi;

public class ChangeClientStatus extends HttpServlet{
	//EntityManager emis=EMF.get(EMF.UNIT_INVOICEFM01).createEntityManager();

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int clientReference=new Integer(req.getParameter("clientReference"));
		OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
		if(!(onlineClient.isAuthenticated(req)&&(
				onlineClient.hasAccess(AccessPermission.CONSUMMER_BLOCK)||
				onlineClient.hasAccess(AccessPermission.BASIC)
				))){
			resp.sendError(resp.SC_UNAUTHORIZED,"acceso denegado");return;
			
		}
		String active=req.getParameter("active");
		Boolean bactive=null;
		if(active!=null)bactive=new Boolean(active);
		String id=req.getParameter("id");
		if(id==null||bactive==null){throw new ServletException("id null");}
		new Mongoi().doUpdate(Mongoi.CLIENTS, "{ \"id\" : "+id+" }", "{ \"active\" : "+bactive+" }");
	}

}
