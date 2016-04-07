package com.ferremundo;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;



import com.ferremundo.db.Mongoi;
import com.google.common.collect.Iterators;
import com.google.gson.Gson;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class Client implements Serializable {
	private static final long serialVersionUID = 1L;
	
    private Long id;
    
	private String code;
	private String consummer;
	private int consummerType=1;
	private String address;
	private String interiorNumber;
	private String exteriorNumber;
	private String suburb;
	private String locality;
	private String city;
	private String country;
	private String state;
	private String email;
	private String cp;
	private String rfc;
	private String tel;
	private int payment;//credit days
	private String reference;
	private String aditionalReference="";
	
	public static final int TYPE_1=1;
	public static final int TYPE_2=2;
	public static final int TYPE_3=3;
	public static final int TYPE_4=4;
	
	public Client(){}
	
	
	public Client(String code, String consummer, int consummerType,
			String address, String interiorNumber, String exteriorNumber,
			String suburb, String locality, String city, String country,
			String state, String email, String cp, String rfc, String tel,
			int payment, String reference, String aditionalReference) {
		this.code = code;
		this.consummer = consummer;
		this.consummerType = consummerType;
		this.address = address;
		this.interiorNumber = interiorNumber;
		this.exteriorNumber = exteriorNumber;
		this.suburb = suburb;
		this.locality = locality;
		this.city = city;
		this.country = country;
		this.state = state;
		this.email = email;
		this.cp = cp;
		this.rfc = rfc;
		this.tel = tel;
		this.payment = payment;
		this.reference = reference;
		this.aditionalReference = aditionalReference;
	}


	/*public Client(String code, String consummer, int consummerType, String address,
			String city, String country, String state, String email, String cp,
			String rfc, String tel, int payment) {
		this.code=code;
		this.consummer = consummer;
		this.consummerType = consummerType;
		this.address = address;
		this.city = city;
		this.country = country;
		this.state = state;
		this.email = email;
		this.cp = cp;
		this.rfc = rfc;
		this.tel = tel;
		this.payment = payment;
	}*/
	
	public boolean isFacturable(){
		if(consummer!=null)if(consummer!="");else return false;
		if(address!=null)if(address!="");else return false;
		if(city!=null)if(city!="");else return false;
		if(state!=null)if(state!="");else return false;
		if(cp!=null)if(cp!="");else return false;
		if(rfc!=null)if(rfc!="");else return false;
		return true;
	}
	
	/*public static Client fromDBObject(DBObject dbObject){
		Client client= new Client(
				dbObject.get("code").toString(), 
				dbObject.get("consummer").toString(), 
				new Integer(dbObject.get("consummerType").toString()), 
				dbObject.get("address").toString(), 
				dbObject.get("city").toString(), 
				dbObject.get("country").toString(), 
				dbObject.get("state").toString(), 
				dbObject.get("email").toString(), 
				dbObject.get("cp").toString(), 
				dbObject.get("rfc").toString(), 
				dbObject.get("tel").toString(), 
				new Integer(dbObject.get("payment").toString())
				);
		if(dbObject.containsField("id")){
			if(!dbObject.get("id").toString().equals("")){
				client.setId(new Long(dbObject.get("id").toString()));
			}
		}
		//client.setId(new Integer(dbObject.get("id").toString()));
		return client;
	}*/
	
	/*public Client cloneL1(){
		Client client= new Client(code, consummer, consummerType, address, city, country, state, email, cp, rfc, tel, payment);
		client.id=id;
		return client;
	}*/
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getConsummer() {
		return consummer;
	}
	public void setConsummer(String consummer) {
		this.consummer = consummer;
	}
	public int getConsummerType() {
		return consummerType;
	}
	public void setConsummerType(int consummerType) {
		this.consummerType = consummerType;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCp() {
		return cp;
	}
	public void setCp(String cp) {
		this.cp = cp;
	}
	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public int getPayment() {
		return payment;
	}
	public void setPayment(int payment) {
		this.payment = payment;
	}
	public Long getId() {
		return id;
	}
	
	public void setId(long id){
		this.id=id;
	}
	
	public String toJson(){
		String json="{"+
		"\"id\":"+this.id+","+
		"\"code\":\""+this.code+"\","+
		"\"consummer\":\""+this.consummer+"\","+
		"\"consummerType\":"+this.consummerType+","+
		"\"address\":\""+this.address+"\","+
		"\"city\":\""+this.city+"\","+
		"\"country\":\""+this.country+"\","+
		"\"state\":\""+this.state+"\","+
		"\"email\":\""+this.email+"\","+
		"\"cp\":\""+this.cp+"\","+
		"\"rfc\":\""+this.rfc+"\","+
		"\"tel\":\""+this.tel+"\","+
		"\"payment\":"+this.payment+
		"}";
		return json;
	}
	
	public static List<DBObject> find(String[] patterns,String where){
		DBCursor c1=new Mongoi().doFindThisThen(Mongoi.MATCHES,where, new String[]{"consummer"}, new String[]{"address","rfc"},patterns);
		DBCursor c2=new Mongoi().doFindThisThen(Mongoi.STARTS,where, new String[]{"consummer"}, new String[]{"address","rfc"},patterns);
		DBCursor c3=new Mongoi().doFindThisThen(Mongoi.CONTAINS,where, new String[]{"consummer"}, new String[]{"address","rfc"},patterns);
		DBCursor c4=new Mongoi().doFindThisThen(Mongoi.CONTAINS,where, null, new String[]{"consummer","address","rfc"},patterns);
		Iterator<DBObject> it=Iterators.concat(c1,c2,c3,c4);
		List<DBObject> lst=new LinkedList<DBObject>();
		while(it.hasNext())lst.add(it.next());
		for(int i=0;i<lst.size();i++){
			DBObject ob1=lst.get(i);
			for(int j=i-1;j>=0;j--){
				DBObject ob2=lst.get(j);
				if(ob1.get("code").equals(ob2.get("code"))){lst.remove(i);i--;}
			}
		}
		for(int i=0;i<lst.size();i++){
			DBObject ob=lst.get(i);
			if(ob.containsField("active")){
				Boolean active=new Boolean(ob.get("active").toString());
				if(!active){lst.remove(i);i--;}
			}
		}
		return lst;
	}
	
	public String getHash(){
		String str=consummer+" "+address+" "+cp+" "+rfc+" "+city+" "+state+" "+consummerType+" "+payment;
		return MD5.get(str);
	}
	
	
	
	public String getInteriorNumber() {
		return interiorNumber;
	}


	public String getExteriorNumber() {
		return exteriorNumber;
	}


	public String getSuburb() {
		return suburb;
	}


	public String getLocality() {
		return locality;
	}


	public String getReference() {
		return reference;
	}


	public int persist(String where){
		
		String hash=this.getHash();
		this.setCode(hash);
		String counterDB=Mongoi.CLIENTS_COUNTER;
		if(where==Mongoi.AGENTS)counterDB=Mongoi.AGENTS_COUNTER;
		DBObject dbObject=new Mongoi().doFindOne(where, "{ \"code\" : \""+hash+"\" }");

		if(dbObject!=null){
			new Mongoi().doUpdate(where, "{ \"code\" : \""+hash+"\" }", "{ \"active\" : "+true+" }");
			System.out.println("coincidence -> "+new Gson().toJson(dbObject));
			return new Integer(dbObject.get("id").toString());
		}
		else {
			int id=new Mongoi().doIncrement(counterDB,"{ \"unique\" : \"unique\" }", "id");
			this.setId(id);
			String json=new Gson().toJson(this);
			System.out.println("inserting -> "+json);
			new Mongoi().doInsert(where, json);
			return id;
		}
	}
	
	public static void main(String[] args) {
		//Client client= new Client("code", "consummer", 0, "address", "city", "country", "state", "email", "cp", "rfc", "tel", 0);
		//System.out.println(client.getConsummer());
	}
	public void setAditionalReference(String aditionalReference) {
		this.aditionalReference=aditionalReference;
	}
	
	public String getAditionalReference() {
		return aditionalReference;
	}

}
