package com.ferremundo;

import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

public class Destiny {
	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic private String address="mostrador";
    
	public Destiny(String address) {
		super();
		this.address = address;
	}
    
    public Destiny(JSONObject json) {
    	try {
			id=json.getLong("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			address=json.getString("address");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getAddress() {
		return address;
	}
    
    public String toJson(){
    	return "{\"id\":\""+id+"\",\"address\":\""+address+"\"}";
    }

}
