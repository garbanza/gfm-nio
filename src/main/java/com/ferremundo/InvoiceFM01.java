package com.ferremundo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.ferremundo.InvoiceLog.LogKind;
import com.ferremundo.db.Mongoi;
import com.ferremundo.gth.SpanishInvoiceNumber;
import com.google.gson.Gson;
import com.ibm.icu.text.DecimalFormat;


public class InvoiceFM01 implements Invoice{
	
	private int version=3;
	
    private Long id;
    
    private String reference;
    
    private int invoiceType=Invoice.INVOICE_TYPE_ORDER;
    
    private long serial=8;
	
	private Client client;
	
	private Client agent;
	
	private Client facturedTo;
	
	private Client printedTo;
	
	private Seller seller;
	
	private Shopman shopman;
	
	private List<InvoiceItem> items=new LinkedList<InvoiceItem>();
	
	private InvoiceMetaData metaData;
	
	private Requester requester;
	
	private Destiny destiny;
	
	private long creationTime;
	
	private long updated;
	
	private List<InvoiceLog> logs;
	
	private float total;
	private float subTotal;
	private float taxes;
	private float totalValue;
	private float debt;
	
	private float agentPayment;
	
	private InvoiceElectronicVersion electronicVersion;
	
	private boolean hasElectronicVersion=false;
	
	@Override
	public float getAgentPayment() {
		return agentPayment;
	}
	@Override
	public void setAgentPayment(float agentPayment) {
		this.agentPayment = agentPayment;
	}
	public static final float TAXES_APPLY=1.16f;
	
	private InvoiceFM01() {
		// TODO Auto-generated constructor stub
	}
	
	/*public InvoiceFM01(JSONObject json){
		try {
			JSONObject clientJSON=json.getJSONObject("client");
			JSONObject sellerJSON=json.getJSONObject("seller");
			JSONObject shopmanJSON=json.getJSONObject("shopman");
			JSONObject requesterJSON=json.getJSONObject("requester");
			JSONObject destinyJSON=json.getJSONObject("destiny");
			JSONArray itemsJSON=json.getJSONArray("items");
			JSONObject metadataJSON=json.getJSONObject("metadata");
			
			client=ClientFactory.create(clientJSON);
			seller=new Seller(sellerJSON);
			shopman=new Shopman(shopmanJSON);
			requester=new Requester(requesterJSON);
			destiny=new Destiny(destinyJSON);
			metaData=new InvoiceMetaData(INVOICE_TYPE_TAXES_APLY, false);
			
			for (int i = 0; i < itemsJSON.length(); i++) {
				JSONObject itemJSON=itemsJSON.getJSONObject(i);
				float quantity=(float)itemJSON.getDouble("quantity");
				JSONObject productJSON=itemJSON.getJSONObject("product");
				Product product=new Product(productJSON);
				//System.out.println(product.getDescription()+" "+quantity);
				items.add(new InvoiceItem(quantity, product));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}*/
	
	public InvoiceFM01(Client client, Seller seller, Shopman shopman,
			List<InvoiceItem> items, InvoiceMetaData metaData,
			Requester requester, Destiny destiny, Client agent,Float payment) {
		/**TODO implement commented fields*/
		System.out.println("creating invoice");
		this.client = client;
		//this.seller = seller;
		this.shopman = shopman;
		this.shopman.setPassword("");
		this.items = items;
		this.metaData = metaData;
		//this.requester = requester;
		//this.destiny = destiny;
		this.agent=agent;
		reference=metaData.getReference();
		//json=toJson();
		invoiceType=metaData.getInvoiceType();
		serial=metaData.getSerial();
		
		InvoiceLog created=new InvoiceLog(InvoiceLog.LogKind.CREATED,true,shopman.getLogin());
		//InvoiceLog created=new InvoiceLog(InvoiceLog.LogKind.CREATED,true);
		creationTime=(Long)created.getDate();
		metaData.setDate(creationTime+"");
		
		logs=new ArrayList<InvoiceLog>(Arrays.asList(
				new InvoiceLog(InvoiceLog.LogKind.CREATED,true,shopman.getLogin())
		));
		System.out.println("LOGS:"+new Gson().toJson(logs));
		total=this.getTotal();
		System.out.println("TOTAL:"+total);
		subTotal=this.getSubtotal();
		System.out.println("SUBTOTAL:"+subTotal);
		taxes=this.getTaxes();
		System.out.println("TAXES:"+taxes);
		float vipActive=0;
		float taxInactive=0;
		for(int i=0;i<items.size();i++){
			float quantity=items.get(i).getQuantity();
			Float unitPrice=items.get(i).getUnitPrice();
			// If not edited
			if(items.get(i).getId()!=-1){
				// If not disabled.
				if(!items.get(i).isDisabled()){
					unitPrice=items.get(i).getUnitPrice(agent.getConsummerType());
					vipActive+=unitPrice*quantity;
				}
				else{
					taxInactive+=unitPrice*quantity*(1-1/TAXES_APPLY);
				}
			}
			else{
				// If not disabled.
				if(!items.get(i).isDisabled()){
					vipActive+=unitPrice*quantity;
				}
				else{
					taxInactive+=unitPrice*quantity*(1-1/TAXES_APPLY);
				}
			}
		}
		totalValue=vipActive+taxInactive;//totalEnabled+(totalDisabled-totalDisabled/TAXES_APPLY);
		agentPayment=total-totalValue;//(totalEnabled-vipEnabled)/TAXES_APPLY+(total-totalValue);
		
		System.out.println("AGENTPAYMENT:"+agentPayment+
				"\n TOTALVALUE="+totalValue);

		if(payment==0)debt=total;
		else if(payment>=total)debt=0;
		else {
			debt=total-payment;
		}
		
		System.out.println("creating invoice : SUCCES");
	}
	
	public long getCreationTime() {
		return creationTime;
	}

	public float getTotalValue(){
		return roundTo6(totalValue);
	}
	/*public InvoiceFM01(Client client, Seller seller, Shopman shopman,
			List<InvoiceItem> items, InvoiceMetaData metaData,
			Requester requester, Destiny destiny, Client agent) {
		///TODO implement commented fields
		this.client = client;
		//this.seller = seller;
		this.shopman = shopman;
		this.items = items;
		this.metaData = metaData;
		//this.requester = requester;
		//this.destiny = destiny;
		this.agent=agent;
		reference=metaData.getReference();
		//json=toJson();
		invoiceType=metaData.getInvoiceType();
		serial=metaData.getSerial();
		
		InvoiceLog created=new InvoiceLog(InvoiceLog.LogKind.CREATED,true,shopman.getLogin());
		//InvoiceLog created=new InvoiceLog(InvoiceLog.LogKind.CREATED,true);
		creationTime=new Long(metaData.getDate());
		
	}*/
	
	@Override
	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}

	@Override
	public String getRequesterName(){
		return requester.getName();
	}
	
	@Override
	public String getDestinyAddress(){
		return destiny.getAddress();
	}
	
	@Override
	public Invoice contruct(){
		return null;
	}
	
	@Override
	public long getSerial() {
		// TODO Auto-generated method stub
		return serial;
	}

	public Long getId() {
		return id;
	}



	public void setReference(String reference) {
		this.reference = reference;
	}



	public int getInvoiceType() {
		return invoiceType;
	}

	public InvoiceMetaData getMetaData() {
		return metaData;
	}

	@Override
	public Client getClient() {
		// TODO Auto-generated method stub
		return client;
	}

	@Override
	public InvoiceMetaData getInvoiceMetaData() {
		// TODO Auto-generated method stub
		return metaData;
	}

	@Override
	public List<InvoiceItem> getItems() {
		// TODO Auto-generated method stub
		return items;
	}

	@Override
	public Invoice[] subdivide(int size) {
		int S=items.size();
		int s=size;
		int[][] indexes=getIndexes(S, s);

		Invoice[] invoices=new InvoiceFM01[indexes.length];
		
		for(int i=0;i<indexes.length;i++){
			int 
				from=indexes[i][0],
				to=indexes[i][1];//(i==indexes.length-1 ? S : indexes[i-1]);
			System.out.println("from: "+from+" to: "+to);
			List<InvoiceItem> items= this.items.subList(from,to+1);
			InvoiceMetaData metaData=this.metaData;
			//if(persist)metaData=new InvoiceMetaData(this.invoiceType, true);
			invoices[i]=new InvoiceFM01(client, seller, shopman, items, metaData, requester, destiny,agent,0f);
			invoices[i].setPrintedTo(printedTo);
		}
		
		return invoices;
	}
	
	private static int[][] getIndexes(int S, int s){
		int rest=S%s; // rest
		boolean rests=rest!=0; // rests ?
		int n=S/s+(rests?1:0); // invoices count
		int[] ret= new int[n];ret[0]=0;
		for(int i=1;i<n;i++){
			if(S%n>0&&i<=S%n)ret[i]=S/n+ret[i-1]+1;
			else ret[i]=S/n+ret[i-1];
		}
		int[][] indexes=new int[n][2];
		for(int i=0;i<ret.length-1;i++){
			indexes[i][0]=ret[i];
			indexes[i][1]=ret[i+1]-1;
		}
		indexes[indexes.length-1][0]=ret[ret.length-1];
		indexes[indexes.length-1][1]=S-1;
		//System.out.println(ret[ret.length-1]+"-"+(S-1));
		return indexes;
	}
	
	public static void main(String[] args) {
		/*List<InvoiceItem> items=new LinkedList<InvoiceItem>();
		items.add(new InvoiceItem(2, new Product("code", 3.1416f, "unit", "mark", "description", 1)));
		Invoice invoice=new InvoiceFM01(new Client("code", "consummer", 1, "address", "city", "country", "state", "email", "cp", "rfc", "tel", 0)
				, new Seller("code", 1)
				, new Shopman("login")
				, items
				, new InvoiceMetaData(INVOICE_TYPE_SAMPLE, true)
				, new Requester("name")
				, new Destiny("address"));
		System.out.println(invoice.toJson());
		*/
		/*EntityManager em=EMF.get(EMF.UNIT_INVOICEFM01).createEntityManager();
		em.getTransaction().begin();
		List<Invoice> list=em.createNativeQuery("select * from InvoiceFM01 where reference = '6H'",InvoiceFM01.class).getResultList();
		Invoice invoice=list.get(0);
		System.out.println(invoice.getJson());
		em.close();
		*/
		/*int S=100,s=17;
		int[][] indexes=getIndexes(S, s);
		for(int i=0;i<indexes.length;i++){
			System.out.println(indexes[i][0]+"-"+indexes[i][1]+" items:"+(indexes[i][1]-indexes[i][0]+1));
		}*/
		
		Invoice invoice=new Gson().fromJson(
				new Mongoi().doFindOne(Mongoi.INVOICES, "{ \"invoiceType\" : 0 }").toString(),
				InvoiceFM01.class);
		System.out.println(new Gson().toJson(invoice));
		System.out.println(invoice.getTotal()+" "+invoice.getSubtotal()+" "+invoice.getTaxes());
		
	}
	
	@Override
	public int invoiceType(){
		return metaData.getInvoiceType();
	}

	@Override
	public String getConsummer() {
		return client.getConsummer();
	}

	@Override
	public int getConsummerType() {
		return client.getConsummerType();
	}

	@Override
	public String getAddress() {
		return client.getAddress();
	}

	@Override
	public String getCity() {
		return client.getCity();
	}

	@Override
	public String getCountry() {
		return client.getCountry();
	}

	@Override
	public String getCp() {
		return client.getCp();
	}

	@Override
	public String getCreationDate() {
		return new SimpleDateFormat("dd/MM/yyyy").format(new Date(getCreationTime()));
	}

	@Override
	public String getEmail() {
		return client.getEmail();
	}

	@Override
	public String getRfc() {
		return client.getRfc();
	}

	@Override
	public InvoiceItem getItem(int index) {
		return items.get(index);
	}

	@Override
	public float getQuantity(int index)throws IndexOutOfBoundsException{
		if(index<0|index>=size())throw new IndexOutOfBoundsException(index+" index must be 0-"+(size()-1));
		return items.get(index).getQuantity();
	}

	@Override
	public String getCode(int index)throws IndexOutOfBoundsException{
		if(index<0|index>=size())throw new IndexOutOfBoundsException(index+" index must be 0-"+(size()-1));
		return items.get(index).getCode();
	}

	@Override
	public String getUnit(int index)throws IndexOutOfBoundsException{
		if(index<0|index>=size())throw new IndexOutOfBoundsException(index+" index must be 0-"+(size()-1));
		return items.get(index).getUnit();
	}

	@Override
	public String getDescription(int index)throws IndexOutOfBoundsException{
		if(index<0|index>=size())throw new IndexOutOfBoundsException(index+" index must be 0-"+(size()-1));
		return items.get(index).getDescription();
	}

	@Override
	public float getUnitPrice(int index)throws IndexOutOfBoundsException{
		if(index<0|index>=size())throw new IndexOutOfBoundsException(index+" index must be 0-"+(size()-1));
		float unit$=items.get(index).getUnitPrice();
		if(metaData.getInvoiceType()==Invoice.INVOICE_TYPE_TAXES_APLY)unit$=items.get(index).getUnitPrice()/TAXES_APPLY;
		return roundTo6(unit$);
	}

	@Override
	public float getTotalPrice(int index)throws IndexOutOfBoundsException{
		if(index<0|index>=size())throw new IndexOutOfBoundsException(index+" index must be 0-"+(size()-1));
		return roundTo6(items.get(index).getQuantity()*getUnitPrice(index));
	}
	
	public float roundTo6(float f){
		BigDecimal big = new BigDecimal(f);
		big = big.setScale(6, RoundingMode.HALF_UP);
		return big.floatValue();
	}

	@Override
	public int size(){
		return items.size();
	}

	@Override
	public String getState() {
		return client.getState();
	}

	@Override
	public String getTel() {
		return client.getTel();
	}

	@Override
	public float getSubtotal() {
		float subt=0;
		for(int i=0;i<items.size();i++){
			subt+=getTotalPrice(i);
		}
		return roundTo6(subt);
	}

	@Override
	public float getTotal() {
		
		return roundTo6(getSubtotal()+getTaxes());//(float)(getSubtotal()+getTaxes());
	}
	
	public float getTotalForConsummerType(int consummerType){
		/**TODO implement this*/
		return 0.0f;
	}
	
	@Override
	public String getTotalInWords() {
		return SpanishInvoiceNumber.toWords(getTotal());
	}

	public String getReference() {
		return reference;
	}


	@Override
	public int getPayment() {
		return client.getPayment();
	}

	@Override
	public float getTaxes() {
		float subt=getSubtotal();
		if(metaData.getInvoiceType()==Invoice.INVOICE_TYPE_TAXES_APLY){
			return roundTo6(subt*TAXES_APPLY-subt);//subt*TAXES_APPLY-subt;
		}
		else return 0;
	}
	@Override
	public float getTaxesApply() {
		return TAXES_APPLY;
	}
	
	@Override
	public String getSellerCode() {
		return seller.getCode();
		
	}
	
	@Override
	public void setSeller(Seller seller) {
		this.seller=seller;
	}
	
	/*@Override
	public String toJson() {
		
		String json="{"+
		"\"client\":"+client!=null?new Gson().toJson(client):null+","+
		"\"seller\":"+seller!=null?new Gson().toJson(seller):null+","+
		"\"shopman\":"+shopman!=null?new Gson().toJson(shopman):null+","+
		"\"requester\":"+requester!=null?new Gson().toJson(requester):null+","+
		"\"destiny\":"+destiny!=null?new Gson().toJson(destiny):null+","+
		"\"items\":[";
		for (int i = 0; i < items.size(); i++) {
			json+=items.get(i).toJson()+(i!=(items.size()-1)?",":"");
		}
		json+="],\"metadata\":"+metaData!=null?metaData.toJson():null+"}";
		return json;
	}*/

	@Override
	public void setSerial() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller getSeller() {
		// TODO Auto-generated method stub
		return seller;
	}
	
	@Override
	public Shopman getShopman() {
		return shopman;
	}
	
	@Override
	public String getShopmanLogin() {
		return shopman.getLogin();
	}
	
	@Override
	public void setShopman(Shopman shopman) {
		this.shopman = shopman;
	}
	
	@Override
	public void persist(){
		String json=new Gson().toJson(this);
		new Mongoi().doInsert(Mongoi.INVOICES, json);
	}

	@Override
	public Client getAgent() {
		// TODO Auto-generated method stub
		return agent;
	}

	@Override
	public void setAgent(Client agent) {
		this.agent=agent;
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<InvoiceLog> getLogs() {
		return logs;
	}

	@Override
	public InvoiceLogAllowed attemptToLog(LogKind logKind) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MMM.dd HH:mm:ss");
		if(hasLog(LogKind.CLOSE)){
			String date=dateFormat.format(
					new Date(
							getLog(LogKind.CLOSE).getDate()
							)
					);
			return  new InvoiceLogAllowed(false, "el documento ya fue: 'CERRADO' el dia: "+date);
		}
		else if(hasLog(LogKind.CANCEL)){
			String date=dateFormat.format(
					new Date(
							getLog(LogKind.CANCEL).getDate()
							)
					);
			return new InvoiceLogAllowed(false, "el documento ya fue: 'CANCELADO' el dia: "+date);
		}
		else if(logKind.equals(LogKind.CLOSE)){
			if(!hasLog(LogKind.FACTURE)){
				return new InvoiceLogAllowed(false, "el documento no se puede cerrar por que no ha sido facturado");
			}
			if(!hasLog(LogKind.AGENT_PAYMENT)){
				return new InvoiceLogAllowed(false, "el documento no se puede cerrar por que no ha sido liquidado el agente: "+getAgent().getConsummer());
			}
			if(debt>0){
				return new InvoiceLogAllowed(false, "el documento no se puede cerrar por que existe deuda : $"+debt);
			}
			
		}
		else if(logKind.equals(LogKind.AGENT_PAYMENT)){
			if(debt>0){
				return new InvoiceLogAllowed(false, "no se puede pagar por que existe deuda : $"+debt);
			}
			else if(hasLog(LogKind.AGENT_PAYMENT)){
				InvoiceLog agentLog=getLog(LogKind.AGENT_PAYMENT);
				String date=dateFormat.format(
						new Date(
								agentLog.getDate()
								)
						);
				return new InvoiceLogAllowed(false, "operación no permitida, el agente fue liquidado en la fecha "+date+" por el operario "+agentLog.getLogin().toUpperCase());
			}
		}
		else if(logKind.equals(LogKind.FACTURE)){
			if(hasLog(LogKind.FACTURE)){
				String date=dateFormat.format(
						new Date(
								getLog(LogKind.FACTURE).getDate()
								)
						);
				return new InvoiceLogAllowed(false, "el documento ya fue: 'FACTURADO' el dia: "+date);
			}
		}
		else if(logKind.equals(LogKind.PAYMENT)){
			if(debt==0){
				String date=dateFormat.format(
						new Date(
								getLog(LogKind.PAYMENT).getDate()
								)
						);
				return new InvoiceLogAllowed(false, "el documento ya fue: 'PAGADO' el dia: "+date);
			}
		}
		return new InvoiceLogAllowed(true, "");
	}
	
	public InvoiceLog getLog(LogKind logKind){
		for(InvoiceLog log : logs){
			if(log.kind().equals(logKind))return log;
		}
		return null;
	}

	public boolean hasLog(LogKind logKind){
		for(InvoiceLog log : logs){
			if(logKind.equals(log.kind()))return true;
		}
		return false;
	}

	@Override
	public float getDebt() {
		// TODO Auto-generated method stub
		return debt;
	}
	@Override
	public void setUpdated(long updated) {
		this.updated = updated;
	}
	@Override
	public long getUpdated() {
		return updated;
	}
	@Override
	public void setFacturedTo(Client facturedTo) {
		this.facturedTo = facturedTo;
	}
	@Override
	public Client getFacturedTo() {
		return facturedTo;
	}
	@Override
	public void setPrintedTo(Client printedTo) {
		this.printedTo = printedTo;
	}
	@Override
	public Client getPrintedTo() {
		return printedTo;
	}
	@Override
	public InvoiceElectronicVersion getElectronicVersion() {
		return electronicVersion;
	}
	@Override
	public void setElectronicVersion(InvoiceElectronicVersion electronicVersion) {
		this.electronicVersion = electronicVersion;
	}
	@Override
	public boolean hasElectronicVersion() {
		return hasElectronicVersion;
	}
	@Override
	public void setHasElectronicVersion(boolean hasElectronicVersion) {
		this.hasElectronicVersion = hasElectronicVersion;
	}
	
	
	/*
	public static void main(String[] args) {
		int M=15,m=4;
		int cycles=M/m+(M%m==0?0:1);
		for(int i=0;i<M/m;i++)System.out.println((i*m)+"-"+((i+1)*m-1));
		if(M%m>0)System.out.println((m*(M/m))+"-"+(M-1));
	}
	*/
	
}
