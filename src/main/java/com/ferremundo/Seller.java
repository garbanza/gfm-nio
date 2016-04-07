package com.ferremundo;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class Seller {

    public static final int SELLER_TYPE_SHOWER=0;
    public static final int SELLER_TYPE_AGENT=1;
    public static final int SELLER_TYPE_PROMOTER=2;
    public static final int SELLER_TYPE_ARQUITECT=3;
    public static final int SELLER_TYPE_1=4;
    public static final int SELLER_TYPE_2=5;
    public static final int SELLER_TYPE_3=6;
    public static final int SELLER_TYPE_4=7;
    public static final int SELLER_TYPE_5=8;
	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic private String code;
    
    @Basic private int sellerType;
    
    public Seller(JSONObject json){
    	try {
			id=json.getLong("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	try {
			code=json.getString("code");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public Seller(String code, int sellerType) {
		super();
		this.code = code;
		this.sellerType = sellerType;
	}
    
    public String getCode() {
		return code;
	}
    
    public Long getId() {
		return id;
	}
    
    public int getSellerType() {
		return sellerType;
	}
    
    public void setCode(String code) {
		this.code = code;
	}
    
    public void setSellerType(int sellerType) {
    	this.sellerType = sellerType;
	}

	public String toJson() {
		
		return "{\"id\":\""+id+"\",\"code\":\""+code+"\",\"sellertype\":\""+sellerType+"\"}";
	}
    
}
