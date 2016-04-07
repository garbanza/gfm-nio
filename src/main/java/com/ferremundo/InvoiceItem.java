package com.ferremundo;

import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.icu.text.DecimalFormat;

public class InvoiceItem extends Product{
	
	private float quantity;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*public InvoiceItem(float quantity, Product product) {
		super(
				product.getCode(),
				product.getUnitPrice(),
				product.getUnit(),
				product.getMark(),
				product.getDescription(),
				product.getProductPriceKind()
		);
		this.quantity=quantity;
	}*/
	
	
	/*public InvoiceItem(float quantity, String code, float unitPrice, String unit, String mark,
			String description, int productPriceKind) {
		super(code, unitPrice, unit, mark, description, productPriceKind);
		this.quantity=quantity;
	}*/
	
	public InvoiceItem() {}
	/*
	public InvoiceItem(JSONObject json) {
		
		try {
			Long id;
			if(json.has("id"))id=json.getLong("id");
			else id=-1L;
			super.setId(id);
			super.setCode(json.getString("code").toUpperCase());
			super.setUnitPrice((float)json.getDouble("unitPrice"));
			super.setUnit(json.getString("unit").toUpperCase());
			super.setMark(json.getString("mark").toUpperCase());
			super.setDescription(json.getString("description").toUpperCase());
			super.setProductPriceKind((int)json.getLong("productPriceKind"));
			if(json.has("disabled")){
				Boolean disabled=json.getBoolean("disabled");
				super.setDisabled(disabled);
			}
			this.quantity=(float)json.getDouble("quantity");
		} catch (JSONException e) {
			System.out.println("com.ferremundo.InvoiceItem ( org.json.JSONObject json ) <-- org.json.JSONException");
			e.printStackTrace();
		}
	}
	*/
	public float getQuantity(){
		return roundTo6(quantity);
	}
	
	
	public float getTotal(){
		return roundTo6(super.getUnitPrice()*quantity);
	}
	
	public void setQuantity(float quantity){
		this.quantity=quantity;
	}
	/*public String toJson() {
		String json="{"+
		"\"quantity\":\""+this.quantity+"\","+
		"\"product\":"+super.toJsonL1()+"}";
		
		return json;
		
	}*/

	
	
}
