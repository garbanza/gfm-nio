package com.ferremundo.gth;

public class InvoiceRow {
	
	private float quantity;
	private String code="";
	private String unit="";
	private String description="";
	private float unitPrice;
	private float totalPrice;
	private String mark="";
	
	public InvoiceRow(float quantity, String unit,String code, String mark, String description,
			float unitPrice, float totalPrice) {
		this.quantity = quantity;
		this.unit=unit;
		this.code = code;
		this.description = description;
		this.unitPrice = unitPrice;
		this.totalPrice=totalPrice;
		this.mark=mark;
	}
	
	public InvoiceRow(float quantity, String unit,String code, String description,
			float unitPrice, float totalPrice) {
		this.quantity = quantity;
		this.unit=unit;
		this.code = code;
		this.description = description;
		this.unitPrice = unitPrice;
		this.totalPrice=totalPrice;
	}
	
	public InvoiceRow(){}

	public float getQuantity() {
		return quantity;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	
	public float getTotalPrice() {
		return totalPrice;
	}

	public String getUnit() {
		return unit;
	}
	
	
	public String getMark() {
		return mark;
	}

	public String toString(){
		String resultSet="";
		resultSet=resultSet+"\t"+getCode();
		resultSet=resultSet+"\t"+getDescription();
		resultSet=resultSet+"\t"+getQuantity();
		resultSet=resultSet+"\t"+getUnit();
		resultSet=resultSet+"\t"+getUnitPrice();
		resultSet=resultSet+"\t"+getTotalPrice();
		return resultSet;
	}
	
	
	
}
