package com.ferremundo;

import javax.persistence.Basic;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.json.JSONException;
import org.json.JSONObject;

public class Requester {
	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic private String name="publico";
    
    @Basic private String consummer="publico";
    
    @Transient private Client client=new Client();
    
	public Requester(String name) {
		super();
		this.name = name;
	}

	public Requester(String name,Client client) {
		super();
		this.name = name;
		consummer=client.getConsummer();
	}
    
    public Requester(JSONObject json) {
    	try {
			id=json.getLong("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			name=json.getString("name");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			consummer=json.getString("consummer");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}
    
    public String getConsummer(){
    	return consummer;
    }
    
    public Long getId() {
		return id;
	}
    
    public String toJson(){
    	return "{\"id\":\""+id+"\",\"name\":\""+name+"\",\"consummer\":\""+consummer+"\"}";
    }

}
