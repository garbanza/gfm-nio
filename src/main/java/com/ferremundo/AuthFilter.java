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
//import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;


public class AuthFilter implements Filter {

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
        System.out.println("authfilter");
        //System.out.println(new Gson().toJson(request));
        OnlineClients clients= OnlineClients.instance();
        OnlineClient onlineClient=null;
        String ref=request.getParameter("cr");
        int clientReference;
        if(Utils.isInteger(ref)){
        	clientReference=Integer.parseInt(ref);
        }
        else {
        	res.getWriter().print("malformed");
        	return;
        }
       	//String ipAddres=request.getRemoteAddr();
       	onlineClient=clients.get(clientReference);
       	req.setAttribute("back",request.getParameter("back"));
       	req.setAttribute("token",onlineClient.getToken());
       	req.setAttribute("clientReference",clientReference);
        //esponse.sendRedirect(request.getContextPath() + "/auth?ret="+URLEncoder.encode(request.getRequestURI(),"UTF-8"));
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
        // If you have assigned any expensive resources as field of
        // this Filter class, then you could clean/close them here.
    }

}