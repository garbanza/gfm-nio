package com.ferremundo;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;


public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
        // If you have any <init-param> in web.xml, then you could get them
        // here by config.getInitParameter("name") and assign it as field.
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        
        //System.out.println(new Gson().toJson(request));
        OnlineClients clients= OnlineClients.instance();
        OnlineClient onlineClient=null;
        String ref=request.getParameter("clientReference");
        System.out.println("loginfilter "+ref);
        boolean auth=false;
        if(Utils.isInteger(ref)){
        	int iref=Integer.parseInt(ref);
        	if(clients.has(iref)){
        		onlineClient=clients.get(iref);
        		if(onlineClient.isAuthenticated(request)){
        			if(onlineClient.isEaten())
        				auth=true;
        		}
        		else auth=true;
        	}
        	else auth=true;
        }
        else auth=true;
        if(auth){
        	String ipAddres=request.getRemoteAddr();
        	int clientReference=clients.add(ipAddres,request.getSession().getId());
        	onlineClient=clients.get(clientReference);
        	request.setAttribute("back",request.getRequestURI());
        	request.setAttribute("token",onlineClient.getToken());
        	request.setAttribute("clientReference",clientReference);
           	 // No logged-in user found, so redirect to login page.
            response.sendRedirect(request.getContextPath() + "/auth?back="+URLEncoder.encode(request.getRequestURI(),"UTF-8")+"&cr="+clientReference);
        } else {
        	req.setAttribute("token",onlineClient.getToken());
        	req.setAttribute("clientReference",onlineClient.getClientReference());
        	req.setAttribute("shopman","{ name:'"+onlineClient.getShopman().getName()+"',login:'"+onlineClient.getShopman().getLogin()+"'}");
        	request.getSession().setMaxInactiveInterval(60*60*8);
        	onlineClient.setEaten(true);
            chain.doFilter(request, response); // Logged-in user found, so just continue request.
        }
    }

    @Override
    public void destroy() {
        // If you have assigned any expensive resources as field of
        // this Filter class, then you could clean/close them here.
    }

}