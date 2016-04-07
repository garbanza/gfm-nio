package com.ferremundo;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OnlineClient {

	private int clientReference;
	private String ipAddress;
	private int requestNumber=0;
	private Shopman shopman;
	private boolean logged=false;
	private boolean locked=true;
	private String token=MD5.get(new Random().nextDouble()+"");
	private String sessionId;
	private boolean isEaten=false;
	private String locale;
	
	public OnlineClient(int clientReference, String ipAddress, String sessionId) {
		this.clientReference = clientReference;
		this.ipAddress = ipAddress;
		this.sessionId=sessionId;
		//HARDCODED TODO fix this default
		this.locale="es";
	}

	public void clientLogged(boolean logged){
		this.logged=logged;
	}
	
	public boolean isAuthenticated(HttpServletRequest req){
		int clientReference=new Integer(req.getParameter("clientReference"));
		String ipAddress=req.getRemoteAddr();
		String token=req.getParameter("token");
		String sessionId=req.getSession().getId();
		if(logged&&clientReference==this.clientReference&&
				ipAddress.equals(this.ipAddress)&&
				token.equals(this.token)&&
				sessionId.equals(this.sessionId)&&
				!locked)return true;
		else return false;
	}
	
	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setClientReference(int clientReference) {
		this.clientReference = clientReference;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public synchronized int getRequestNumber() {
		return requestNumber;
	}
	
	public synchronized void setRequestNumber(int requestNumber) {
		this.requestNumber = requestNumber;
	}
	
	public int getClientReference() {
		return clientReference;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public String getAndUpdateToken(){
		String oldT=token;
		token=MD5.get(new Random().nextDouble()+"");
		return oldT;
	}
	
	public String getToken(){
		return token;
	}
	
	public Shopman getShopman(){
		return shopman;
	}
	
	public void setShopman(Shopman shopman){
		this.shopman=shopman;
	}
	
	public boolean hasAccess(List<AccessPermission> permissions){
		return ClientAuthenticate.hasAccess(this, permissions);
	}
	
	public boolean hasAccess(AccessPermission permission){
		return ClientAuthenticate.hasAccess(this, permission);
	}
	
	public boolean hasAccess(AccessPermission[] permissions){
		return ClientAuthenticate.hasAccess(this, permissions);
	}

	public boolean isEaten() {
		return isEaten;
	}

	public void setEaten(boolean isEaten) {
		this.isEaten = isEaten;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}
}