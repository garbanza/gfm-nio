package com.ferremundo;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.json.JSONObject;

@Entity
public class ProductSample implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	@Basic
    private String code;
    
	@Basic
    private String mark;
    
	@Basic
    private float unitPrice;
    
	@Basic
    private String unit;
    
	@Basic
    private String description;
    
	@Basic
    private float stored;
    
	@Basic
    private float collecting;
    
	@Basic
    private float sending;
    
	@Basic
    private float requested;
    
	@Basic
    private float missed;
    
	@Basic
    private int productPriceKind;
    
	@Basic
    private int calls;
	
	public ProductSample(){}
	
	public ProductSample(String code, float unitPrice, String unit, String mark,String description, int productPriceKind) {
		super();
		
		this.code=code;
		this.unitPrice = unitPrice;
		this.unit = unit;
		this.mark=mark;
		this.description = description;
		this.productPriceKind=productPriceKind;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getStored() {
		return stored;
	}

	public void setStored(float stored) {
		this.stored = stored;
	}

	public float getCollecting() {
		return collecting;
	}

	public void setCollecting(float collecting) {
		this.collecting = collecting;
	}

	public float getSending() {
		return sending;
	}

	public void setSending(float sending) {
		this.sending = sending;
	}

	public float getRequested() {
		return requested;
	}

	public void setRequested(float requested) {
		this.requested = requested;
	}

	public float getMissed() {
		return missed;
	}

	public void setMissed(float missed) {
		this.missed = missed;
	}

	
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Long getKey() {
		return id;
	}
	
	public Long setKey(Long id) {
		return this.id=id;
	}

	public int getProductPriceKind() {
		return productPriceKind;
	}

	public void setProductPriceKind(int productPriceKind) {
		this.productPriceKind = productPriceKind;
	}
	
	public void addCall(int calls) {
		this.calls++;
	}
	
	public int getCalls() {
		return calls;
	}
    
	
	public String toJsonL1(){
		return "{ "+
		"\"key\" : \""+getKey()+"\", "+
		"\"code\" : \""+getCode()+"\", "+
		"\"unit\" : \""+getUnit()+"\", "+
		"\"mark\" : \""+getMark()+"\", "+
		"\"unitprice\" : \""+getUnitPrice()+"\", "+
		"\"description\" : \""+getDescription()+"\" "+
		"}";

	}
}
