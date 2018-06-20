package com.ferremundo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.jsoup.Jsoup;

public class InvoiceTypePayment {

private int version=4;
	
    private Long id;
    
    private String reference;
    
    private String[] referred;
    
    private String series;
    
    private int invoiceType=Invoice.INVOICE_TYPE_PAYMENT;
    
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

	private String paymentMethod;

	private String paymentWay;

	private String documentType;

	private String coin;
	
	
	public InvoiceTypePayment(Client client, Shopman shopman,
			List<InvoiceItem> items, String reference, String[] referred,
			String series,
			Client agent,
			String paymentMethod, String paymentWay,
			String coin) {
		/**TODO implement commented fields*/
		System.out.println("start of InvoiceFM01");
		Log log=new Log();
		System.out.println("after log");
		log.entry(shopman);
		
		System.out.println("creating invoice");
		this.client = client;
		//this.seller = seller;
		this.shopman = shopman;
		this.shopman.setPassword("");
		this.items = items;
		//this.metaData = metaData;
		//this.requester = requester;
		this.agent=agent;
		this.reference=reference;//metaData.getReference();
		this.referred = referred;
		this.series = series;
		//json=toJson();
		//invoiceType=metaData.getInvoiceType();
		this.paymentMethod=paymentMethod;
		this.paymentWay=paymentWay;
		this.documentType="P";
		this.coin=coin;
		//serial=metaData.getSerial();
		
		InvoiceLog created=new InvoiceLog(InvoiceLog.LogKind.CREATED,true,shopman.getLogin());
		//InvoiceLog created=new InvoiceLog(InvoiceLog.LogKind.CREATED,true);
		this.creationTime=(Long)created.getDate();
		//metaData.setDate(creationTime+"");
		
		logs=new ArrayList<InvoiceLog>(Arrays.asList(
				new InvoiceLog(InvoiceLog.LogKind.CREATED,true,shopman.getLogin())
		));
		//** TODO make this more efficient please, setting total, subtotal & taxex*/
		/*Profact electronicInvoice=new Profact(this);
		String xml=electronicInvoice.xml;
		org.jsoup.nodes.Document document=null;
		document = Jsoup.parse(xml);
		String total=document.select("cfdi|Comprobante").get(0).attr("Total");
		String subTotal=document.select("cfdi|Comprobante").get(0).attr("SubTotal");
		String taxes=document.select("cfdi|Comprobante > cfdi|Impuestos").get(0).attr("TotalImpuestosTrasladados");
		*/

		this.total=new Float(total);
		this.subTotal=new Float(subTotal);
		this.taxes=new Float(taxes);
		
	}


	public int getVersion() {
		return version;
	}


	public void setVersion(int version) {
		this.version = version;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getReference() {
		return reference;
	}


	public void setReference(String reference) {
		this.reference = reference;
	}


	public String[] getReferred() {
		return referred;
	}


	public void setReferred(String[] referred) {
		this.referred = referred;
	}


	public String getSeries() {
		return series;
	}


	public void setSeries(String series) {
		this.series = series;
	}


	public int getInvoiceType() {
		return invoiceType;
	}


	public void setInvoiceType(int invoiceType) {
		this.invoiceType = invoiceType;
	}


	public long getSerial() {
		return serial;
	}


	public void setSerial(long serial) {
		this.serial = serial;
	}


	public Client getClient() {
		return client;
	}


	public void setClient(Client client) {
		this.client = client;
	}


	public Client getAgent() {
		return agent;
	}


	public void setAgent(Client agent) {
		this.agent = agent;
	}


	public Client getFacturedTo() {
		return facturedTo;
	}


	public void setFacturedTo(Client facturedTo) {
		this.facturedTo = facturedTo;
	}


	public Client getPrintedTo() {
		return printedTo;
	}


	public void setPrintedTo(Client printedTo) {
		this.printedTo = printedTo;
	}


	public Seller getSeller() {
		return seller;
	}


	public void setSeller(Seller seller) {
		this.seller = seller;
	}


	public Shopman getShopman() {
		return shopman;
	}


	public void setShopman(Shopman shopman) {
		this.shopman = shopman;
	}


	public List<InvoiceItem> getItems() {
		return items;
	}


	public void setItems(List<InvoiceItem> items) {
		this.items = items;
	}


	public InvoiceMetaData getMetaData() {
		return metaData;
	}


	public void setMetaData(InvoiceMetaData metaData) {
		this.metaData = metaData;
	}


	public Requester getRequester() {
		return requester;
	}


	public void setRequester(Requester requester) {
		this.requester = requester;
	}


	public Destiny getDestiny() {
		return destiny;
	}


	public void setDestiny(Destiny destiny) {
		this.destiny = destiny;
	}


	public long getCreationTime() {
		return creationTime;
	}


	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}


	public long getUpdated() {
		return updated;
	}


	public void setUpdated(long updated) {
		this.updated = updated;
	}


	public List<InvoiceLog> getLogs() {
		return logs;
	}


	public void setLogs(List<InvoiceLog> logs) {
		this.logs = logs;
	}


	public float getTotal() {
		return total;
	}


	public void setTotal(float total) {
		this.total = total;
	}


	public float getSubTotal() {
		return subTotal;
	}


	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}


	public float getTaxes() {
		return taxes;
	}


	public void setTaxes(float taxes) {
		this.taxes = taxes;
	}


	public float getTotalValue() {
		return totalValue;
	}


	public void setTotalValue(float totalValue) {
		this.totalValue = totalValue;
	}


	public float getDebt() {
		return debt;
	}


	public void setDebt(float debt) {
		this.debt = debt;
	}


	public float getAgentPayment() {
		return agentPayment;
	}


	public void setAgentPayment(float agentPayment) {
		this.agentPayment = agentPayment;
	}


	public InvoiceElectronicVersion getElectronicVersion() {
		return electronicVersion;
	}


	public void setElectronicVersion(InvoiceElectronicVersion electronicVersion) {
		this.electronicVersion = electronicVersion;
	}


	public boolean isHasElectronicVersion() {
		return hasElectronicVersion;
	}


	public void setHasElectronicVersion(boolean hasElectronicVersion) {
		this.hasElectronicVersion = hasElectronicVersion;
	}


	public String getPaymentMethod() {
		return paymentMethod;
	}


	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}


	public String getPaymentWay() {
		return paymentWay;
	}


	public void setPaymentWay(String paymentWay) {
		this.paymentWay = paymentWay;
	}


	public String getDocumentType() {
		return documentType;
	}


	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}


	public String getCoin() {
		return coin;
	}


	public void setCoin(String coin) {
		this.coin = coin;
	}
	
	
	
	
}
