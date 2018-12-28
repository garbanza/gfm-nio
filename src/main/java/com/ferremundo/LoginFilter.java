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

import com.ferremundo.db.Mongoi;
import com.ferremundo.stt.GSettings;
import com.google.gson.Gson;
import com.mongodb.DBObject;


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
        Log log=new Log();
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
        	GSettings g= GSettings.instance();
        	boolean test=new Boolean(g.getKey("TEST"));
        	// bypass setting credencials if test
        	if(test){
        		String testUser=g.getKey("TEST_USER");
        		String testUserPassword=g.getKey("TEST_USER_PASSWORD");
        		if(testUser!=null){
        			
        			DBObject dbshopman=new Mongoi().doFindOne(Mongoi.SHOPMANS, "{ \"login\" : \""+testUser+"\" }");
        			//log.object(dbshopman,password);
        			if(dbshopman!=null&&testUserPassword!=null){
        				if(dbshopman.get("password").toString().equals(MD5.get(testUserPassword))&&
        						dbshopman.get("login").toString().equals(testUser)){
        					onlineClient=OnlineClients.instance().get(clientReference);
        					//System.out.println("pass&log passes");
        					/*if(!token.equals(onlineClient.getToken())){
        						resp.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
        						resp.getWriter().write("No autorizado");
        						System.out.println("token dismatch");
        						return;
        					}*/
        					//System.out.println("token passes");
        					Shopman shopman=(Shopman)new Gson().fromJson(dbshopman.toString(), Shopman.class);
        					onlineClient.setShopman(shopman);
        					String shortId = OnlineClient.shortId(shopman.getLogin().toUpperCase());
        					onlineClient.setShortId(shortId);
        					request.setAttribute("cacheSession",shortId);
        					onlineClient.setLogged(true);
        					onlineClient.setLocked(false);
        					onlineClient.setEaten(true);
        					
        					request.setAttribute("token",onlineClient.getToken());
        					request.setAttribute("clientReference",onlineClient.getClientReference());
        					request.setAttribute("shopman","{ name:'"+onlineClient.getShopman().getName()+"',login:'"+onlineClient.getShopman().getLogin()+"'}");
        		        	request.getSession().setMaxInactiveInterval(60*60*8);
        					log.info("logged");
        		            chain.doFilter(request, response); // Logged-in user found, so just continue request.
        		            
        					/*if(onlineClient.isAuthenticated(req)){
        						//System.out.println("client authenticated passes");
        						resp.getWriter().print("{ \"authenticated\": "+true+" , \"shopman\" : { \"name\" : \""+shopman.getName()+"\" , \"login\" : \""+shopman.getLogin()+"\" } }");
        						return;
        					}
        					else{
        						resp.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
        						resp.getWriter().write("No autorizado pss!=");
        						System.out.println("is not authenticated");
        						return;
        					}*/
        				}
        				else{
        					response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
        					response.getWriter().write("pass dismatch, Definir correctamente el usuario y password en las variables TEST_USER y TEST_USER_PASSWORD en el archivo de configuracion");
        					System.out.println("pass dismatch");
        					return;
        				}
        			}
        			else {
        				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
        				response.getWriter().write("No autorizado. Definir correctamente el usuario y password en las variables TEST_USER y TEST_USER_PASSWORD en el archivo de configuracion");
        				System.out.println("login/password dismatch");
        				return;
        			}
        		}
        		else {
    				response.setStatus( HttpServletResponse.SC_UNAUTHORIZED);
    				response.getWriter().write("No autorizado, usuario indefinido. Definir correctamente el usuario y password en las variables TEST_USER y TEST_USER_PASSWORD en el archivo de configuracion");
    				System.out.println("login/password dismatch");
    				return;
    			}
        	}
        	else{
           	 // No logged-in user found, so redirect to login page.
        		response.sendRedirect(request.getContextPath() + "/auth?back="+URLEncoder.encode(request.getRequestURI(),"UTF-8")+"&cr="+clientReference);
        	}
        } else {
        	req.setAttribute("token",onlineClient.getToken());
        	req.setAttribute("clientReference",onlineClient.getClientReference());
        	req.setAttribute("shopman","{ name:'"+onlineClient.getShopman().getName()+"',login:'"+onlineClient.getShopman().getLogin()+"'}");
        	String shortId = OnlineClient.shortId(onlineClient.getShopman().getLogin().toUpperCase());
			onlineClient.setShortId(shortId);
			req.setAttribute("cacheSession",shortId);
        	
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