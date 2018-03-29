package com.ferremundo;

import java.util.LinkedList;
import java.util.List;

public class PaymentInvoice {

private int version=4;
	
    private Long id;
    
    private String reference;
    
    private String series;
    
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

	private String paymentMethod;

	private String paymentWay;

	private String documentType;

	private String coin;
	
}
