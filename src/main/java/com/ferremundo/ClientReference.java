package com.ferremundo;

public class ClientReference {

	private static final ThreadLocal<Integer> tl = new ThreadLocal<Integer>();
	
	public static void set(int clientReference) {
		tl.set(clientReference);
	}

	public static void unset() {
		tl.remove();
	}

	public static Integer get() {
		System.out.println("clientReference get");
		return tl.get();
	}
	
	public static OnlineClient getOnlineClient(){
		return OnlineClients.instance().get(get());
	}
	
	public static String getLogin(){
		return OnlineClients.instance().get(get()).getShopman().getLogin();
	}
	
}
