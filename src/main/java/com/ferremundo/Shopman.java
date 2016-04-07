package com.ferremundo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

import com.ferremundo.db.Mongoi;
import com.google.gson.Gson;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Entity
public class Shopman {
	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Basic private String login;
    
    @Basic private String password=null;
    
    private List<AccessPermission> permissions=new LinkedList<AccessPermission>();

    public List<AccessPermission> getPermissions(){
    	return permissions;
    }
    
    public Shopman(String name, String login,String password, List<AccessPermission> permissions) {
    	this.login=login;
    	this.permissions=permissions;
    	this.password=MD5.get(password);
    	this.name=name;
	}
    
    public DBObject persist(){
    	DBObject dbObject= new Mongoi().doFindOne(Mongoi.SHOPMANS, "{ \"login\" : \""+login+"\" }");
    	if(dbObject!=null)return null;
    	else{
    		this.id=(long)new Mongoi().doIncrement(Mongoi.SHOPMANS_COUNTER, "{ \"unique\" : \"unique\" }", "id");
    		new Mongoi().doInsert(Mongoi.SHOPMANS, new Gson().toJson(this));
    		return new Mongoi().doFindOne(Mongoi.SHOPMANS, "{ \"login\" : \""+login+"\" }");
    	}
    }
    
    public Shopman(JSONObject json) {
    	try {
			id=json.getLong("id");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		finally{id=-1l;}
    	try {
			login=json.getString("login");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		finally{login="noauth";}
	}

	public String getLogin() {
		return login;
	}
	
	public String getName() {
		return name;
	}
	
	public String getPassword() {
		return password;
	}
    
    public void setPassword(String password) {
    	if(password!=null)
    		this.password = MD5.get(password);
    	else this.password=null;
	}
    
    public boolean isPassword(String password){
    	if(MD5.get(password).equals(this.password))return true;
    	else return false;
    }
    
    
    
    public static DBObject getShopman(String login, String password){
    	DBObject dbObject= new Mongoi().doFindOne(Mongoi.SHOPMANS, "{ \"login\" : \""+login+"\" }");
    	if(dbObject!=null){
    		String pass=dbObject.get("password").toString();
    		if(pass.equals(MD5.get(password)))return dbObject;
    		else return null;
    	}
    	else return null;
    }

    public static void main(String[] args) {
		/*Shopman shopman= new Shopman("root","root", "rand",new ArrayList<AccessPermission>(Arrays.asList(
				AccessPermission.ADMIN
		)));
		*/
    	//shopman.persist();
    	//new Mongoi().doUpdate(Mongoi.SHOPMANS, "{\"login\":\"root\"}", "{\"name\":\"ADMIN\"}");
	}
    
}
