package com.ferremundo;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

@RemoteProxy
public class OnlineClients {
	
	private int clientsCounter=0;
	
	private static List<OnlineClient> onlineClients= new LinkedList<OnlineClient>();
	
	private static OnlineClients oc=null;
	
	private OnlineClients(){
	}
	
	@RemoteMethod
	public static OnlineClients instance(){
		if(oc==null)oc=new OnlineClients();
		return oc;
	}
	
	public int getClientRequestNumber(int clientReference) throws RuntimeException{
		for(OnlineClient client: onlineClients){
			if(client.getClientReference()==clientReference)return client.getRequestNumber();
		}
		throw new RuntimeException("no se encontró el cliente con referencia "+clientReference);
	}
	
	public boolean has(int clientReference){
		for(OnlineClient client: onlineClients){
			if(client.getClientReference()==clientReference)return true;
		}
		return false;
	}
	
	public OnlineClient get(int clientReference){
		for(OnlineClient client: onlineClients){
			if(client.getClientReference()==clientReference)return client;
		}
		return null;
	}
	
	public boolean has(OnlineClient onlineClient){
		return has(onlineClient.getClientReference());
	}
	
	private synchronized int next(){
		return clientsCounter++;
	}
	
	private static synchronized int nextClientReference() {
		return instance().next();
	}
	
	public int add(String ipAddress,String sessionId){
		OnlineClient client=new OnlineClient(nextClientReference(), ipAddress,sessionId); 
		onlineClients.add(client);
		return client.getClientReference();
	}
	


}
