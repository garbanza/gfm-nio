package com.ferremundo;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.EntityManager;

public class ClientsStore {
	
	private static List<Client> listStore;
	
	private static ClientsStore store=null;

	private ClientsStore() {
		EntityManager em=EMF.get(EMF.UNIT_CLIENT).createEntityManager();
		listStore=em.createNativeQuery("select * from Client",Client.class).getResultList();

	}
	
	public static ClientsStore instance(){
		if(null==store){
			store=new ClientsStore();
			System.out.println("inflating store "+listStore.size()+" Clients");
		}
		else{System.out.println("store was inflated "+listStore.size()+" Clients");}
		return store;
	}
	
	public List<Client> search(String arg,int size){
		String[] argUP=arg.toUpperCase().split(" ");
		List<Client> ret=new LinkedList<Client>();
		for(int i=0;i<listStore.size()&&ret.size()<size;i++){
			String code =listStore.get(i).getCode();
			String consummer=listStore.get(i).getConsummer();
			String rfc=listStore.get(i).getRfc();
			String address=listStore.get(i).getAddress();
			for(int j=0,counter=0;j<argUP.length;j++){
				if(code.contains(argUP[j])||consummer.contains(argUP[j])||rfc.contains(argUP[j])||address.contains(argUP[j])){
					counter++;
					if(counter==argUP.length){
						ret.add(listStore.get(i));
					}
				}
				else j=argUP.length;
			}
		}
		return ret;
	}
	
	public Client getClientByRFC(String rfc){
		for(int i=0;i<listStore.size();i++){
			if(listStore.get(i).getRfc().equals(rfc))return listStore.get(i);
		}
		return null;
	}
	
	public Client getClientByCode(String code){
		for(int i=0;i<listStore.size();i++){
			if(listStore.get(i).getCode().equals(code))return listStore.get(i);
		}
		return null;
	}
	
	/*
	public String searchToXML(String arg, int size,String searchRequestID){
		List<Product> products=search(arg,size);
		String ret="<?xml version=\"1.0\" encoding=\"utf-8\" ?>\n<prs>\n";
		for(int i=0;i<products.size();i++){
			Product product=products.get(i);
			ret+="<pr "+
				"key=\""+product.getKey()+"\" "+
				"code=\""+product.getCode()+"\" "+
				"unit=\""+product.getUnit()+"\" "+
				"mark=\""+product.getMark()+"\" "+
				"unitprice=\""+product.getUnitPrice()+"\""+
				">"+product.getDescription()+"</pr>\n";
		}
		ret+="<req val=\""+searchRequestID+"\"></req></prs>";
		System.out.println(ret);
		return ret;
	}
	*/
	public String searchToJson(String arg, int size,String searchRequestID){
		List<Client> clients=search(arg,size);
		String ret="{ \"clients\" : [ ";
		for(int i=0;i<clients.size();i++){
			Client client=clients.get(i);
			ret+="{ "+
				"\"key\" : \""+client.getId()+"\", "+
				"\"code\" : \""+client.getCode()+"\", "+
				"\"consummer\" : \""+client.getConsummer()+"\", "+
				"\"consummertype\" : \""+client.getConsummerType()+"\", "+
				"\"address\" : \""+client.getAddress()+"\", "+
				"\"city\" : \""+client.getCity()+"\", "+
				"\"country\" : \""+client.getCountry()+"\", "+
				"\"state\" : \""+client.getState()+"\", "+
				"\"email\" : \""+client.getEmail()+"\", "+
				"\"cp\" : \""+client.getCp()+"\", "+
				"\"rfc\" : \""+client.getRfc()+"\", "+
				"\"tel\" : \""+client.getTel()+"\", "+
				"\"payment\" : \""+client.getPayment()+"\" }"+(i==(clients.size()-1)?" ":", ");
		}
		ret+="], \"searchrequestid\" : \""+searchRequestID+"\"}";
		System.out.println(ret);
		return ret;
	}
	
	public static void refresh(){
		store=null;
		ClientsStore.instance();
	}

}
