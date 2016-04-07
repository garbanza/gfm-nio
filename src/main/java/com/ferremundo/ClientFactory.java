package com.ferremundo;

import org.json.JSONException;
import org.json.JSONObject;

public class ClientFactory{
	
	private ClientFactory(){}
	
	public static Client create(JSONObject json){
		System.out.println("creating client for "+json.toString());
		Client client=null;
		try{
			String code="";
			if(json.has("code")){
				code=json.getString("code").toUpperCase();
			}
			String consummer="";
			if(json.has("consummer")){
				consummer=json.getString("consummer").toUpperCase();
			}
			int consummerType=1;
			if(json.has("consummerType")){
				consummerType=new Integer(json.getString("consummerType"));
			}
			String address="";
			if(json.has("address")){
				address=json.getString("address").toUpperCase();
			}
			String interiorNumber="";
			if(json.has("interiorNumber")){
				interiorNumber=json.getString("interiorNumber").toUpperCase();
			}
			String exteriorNumber="";
			if(json.has("exteriorNumber")){
				exteriorNumber=json.getString("exteriorNumber").toUpperCase();
			}
			String suburb="";
			if(json.has("suburb")){
				suburb=json.getString("suburb").toUpperCase();
			}
			String locality="";
			if(json.has("locality")){
				locality=json.getString("locality").toUpperCase();
			}
			String city="";
			if(json.has("city")){
				city=json.getString("city").toUpperCase();
			}
			String country="";
			if(json.has("country")){
				country=json.getString("country").toUpperCase();
			}
			String state="";
			if(json.has("state")){
				state=json.getString("state").toUpperCase();
			}
			String email="";
			if(json.has("email")){
				email=json.getString("email").toUpperCase();
			}
			String cp="";
			if(json.has("cp")){
				cp=json.getString("cp").toUpperCase();
			}
			String rfc="";
			if(json.has("rfc")){
				rfc=json.getString("rfc").toUpperCase();
			}
			String tel="";
			if(json.has("tel")){
				tel=json.getString("tel").toUpperCase();
			}
			int payment=0;
			if(json.has("payment")){
				payment=new Integer(json.getString("payment"));
			}
			String reference="";
			if(json.has("reference")){
				reference=json.getString("reference").toUpperCase();
			}
			String aditionalReference="";
			if(json.has("aditionalReference")){
				aditionalReference=json.getString("aditionalReference").toUpperCase();
			}
			client=new Client(code, consummer, consummerType, address, interiorNumber, exteriorNumber, suburb, locality, city, country, state, email, cp, rfc, tel, payment, reference, aditionalReference);
			client.setCode(client.getHash());
		}catch(JSONException j){System.out.println("json exception");j.printStackTrace();}
		System.out.println("client succesfuly created");
		return client;
		
	}

}
