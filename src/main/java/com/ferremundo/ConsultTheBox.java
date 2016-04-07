package com.ferremundo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class ConsultTheBox extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		int clientReference=new Integer(request.getParameter("clientReference"));
		OnlineClient onlineClient=OnlineClients.instance().get(clientReference);
		if(!onlineClient.isAuthenticated(request)&&!(
				onlineClient.hasAccess(AccessPermission.READ_THE_BOX)||
				onlineClient.hasAccess(AccessPermission.ADMIN)
				)){
			try {
				response.sendError(response.SC_UNAUTHORIZED,"acceso denegado");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}return;
		}
		System.out.println("THEBOX CASH IS $"+TheBox.instance().account());
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		try {
			response.getWriter().print(
					"{ \"cash\":"+TheBox.instance().account()+
					", \"logs\" :"+new Gson().toJson(TheBox.instance().getLogs())+"}"
					);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
