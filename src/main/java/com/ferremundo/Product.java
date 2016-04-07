package com.ferremundo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.json.JSONException;
import org.json.JSONObject;

import com.ferremundo.OnlineClient;
import com.ferremundo.db.Mongoi;
import com.google.common.collect.Iterators;
import com.ibm.icu.text.DecimalFormat;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

@Entity
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Basic
    private String hash;
    
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

	private boolean disabled=false;
	private boolean edited=false;
	
	private boolean firstTimeInventored;
	
	public static final int KIND_1=1;
	public static final int KIND_2=2;
	public static final float FACTOR_1=.9f;
	public static final float FACTOR_2=.95f;
	public static final float FACTOR_3=.864f;
	public static final float FACTOR_4=.945f;
	
	public Product(){}
	public Product(JSONObject json) throws JSONException{
		this.code=json.getString("code");
		this.unitPrice = (float)json.getDouble("unitprice");
		this.unit =json.getString("unit");
		this.mark=json.getString("mark");
		this.description = json.getString("description");
		this.productPriceKind=(int)json.getDouble("productpricekind");
	}
	
	public Product(String code, float unitPrice, String unit, String mark,String description, int productPriceKind) {
		this.code=code;
		this.unitPrice = unitPrice;
		this.unit = unit;
		this.mark=mark;
		this.description = description;
		this.productPriceKind=productPriceKind;
	    stored=0;
	    collecting=0;
	    sending=0;
	    requested=0;
	    missed=0;
	    calls=0;
	}
	
	public Product cloneL1(){
		Product product= new Product(code, unitPrice, unit, mark, description, productPriceKind);
		product.id=id;
		return product;
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public float getUnitPrice() {
		return roundTo6(unitPrice);
	}
	public float roundTo6(float f){
		BigDecimal big = new BigDecimal(f);
		big = big.setScale(6, RoundingMode.HALF_UP);
		return big.floatValue();
	}
	
	public Float getUnitPrice(int consummerType) {
		DBObject product=new Mongoi().doFindOne(Mongoi.PRODUCTS, "{ \"code\" : \""+code+"\" }");
		//String hash=this.getHash();
		//String oHash=product.get("hash").toString();

		if(product==null)return null;
		//Product product=ProductsStore.getByKey(key);
		//int productPriceKind=new Integer(product.get("productPriceKind").toString());
		float unitPrice=new Float(product.get("unitPrice").toString());
		if(consummerType==Client.TYPE_1){
			//unitPrice=product.getUnitPrice();
		}
		else if(consummerType==Client.TYPE_2){
			if(productPriceKind==Product.KIND_1)unitPrice*=Product.FACTOR_1;
			else if(productPriceKind==Product.KIND_2)unitPrice*=Product.FACTOR_2;
		}
		else if(consummerType==Client.TYPE_3){
			if(productPriceKind==Product.KIND_1)unitPrice*=Product.FACTOR_3;
			else if(productPriceKind==Product.KIND_2)unitPrice*=Product.FACTOR_4;
		}
		//if()
		return roundTo6(unitPrice);
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
    
	
	public Long getId() {
		return id;
	}

	public void setCalls(int calls) {
		this.calls = calls;
	}
	

	public String getHash() {
		String str=code+" "+unit+" "+mark+" "+description;
		return MD5.get(str);
	}
	public void setHash(String hash) {
		this.hash = hash;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String toJsonL1(){
		return "{ "+
		"\"id\" : \""+getId()+"\", "+
		"\"code\" : \""+getCode()+"\", "+
		"\"unit\" : \""+getUnit()+"\", "+
		"\"mark\" : \""+getMark()+"\", "+
		"\"unitprice\" : \""+getUnitPrice()+"\", "+
		"\"productpricekind\" : \""+getProductPriceKind()+"\", "+
		"\"description\" : \""+getDescription()+"\" "+
		"}";

	}
	
	private static synchronized boolean isLastSearch(OnlineClient onlineClient, int requestNumber){
		if(requestNumber<onlineClient.getRequestNumber()){
			return false;
		}
		return true;
	}
	
	public static List<DBObject> find(String[] patterns, OnlineClient onlineClient, int requestNumber){
		DBCursor c1=new Mongoi().doFindThisThen(Mongoi.MATCHES,Mongoi.PRODUCTS, new String[]{"code"}, new String[]{"description","mark"},patterns);
		if(!isLastSearch(onlineClient, requestNumber))return null;
		DBCursor c2=new Mongoi().doFindThisThen(Mongoi.STARTS,Mongoi.PRODUCTS, new String[]{"code"}, new String[]{"description","mark"},patterns);
		if(!isLastSearch(onlineClient, requestNumber))return null;
		DBCursor c3=new Mongoi().doFindThisThen(Mongoi.CONTAINS,Mongoi.PRODUCTS, new String[]{"code"}, new String[]{"description","mark"},patterns);
		if(!isLastSearch(onlineClient, requestNumber))return null;
		DBCursor c4=new Mongoi().doFindThisThen(Mongoi.CONTAINS,Mongoi.PRODUCTS, null, new String[]{"code","description","mark"},patterns);
		if(!isLastSearch(onlineClient, requestNumber))return null;
		Iterator<DBObject> it=Iterators.concat(c1,c2,c3,c4);
		List<DBObject> lst=new LinkedList<DBObject>();
		while(it.hasNext())lst.add(it.next());
		if(!isLastSearch(onlineClient, requestNumber))return null;
		for(int i=0;i<lst.size();i++){
			DBObject ob1=lst.get(i);
			for(int j=i-1;j>=0;j--){
				DBObject ob2=lst.get(j);
				if(ob1.get("code").equals(ob2.get("code"))){lst.remove(i);i--;}
			}
		}
		if(!isLastSearch(onlineClient, requestNumber))return null;
		return lst;
	}
	
	public static String dbObjectToHash(DBObject dbObject){
		String pstr=dbObject.get("code")+" "+dbObject.get("unit")+" "+dbObject.get("mark")+" "+dbObject.get("description");
		String hash=MD5.get(pstr);
		return hash;
	}
	public void setDisabled(boolean disabled) {
		this.disabled=disabled;
	}
	
	public boolean isDisabled(){
		return disabled;
	}
	public boolean isEdited() {
		return edited;
	}
	public void setEdited(boolean edited) {
		this.edited = edited;
	}
	public boolean isFirstTimeInventored() {
		return firstTimeInventored;
	}
	public void setFirstTimeInventored(boolean firstTimeInventored) {
		this.firstTimeInventored = firstTimeInventored;
	}
	
}
