package com.ferremundo;

import java.util.List;

import com.ferremundo.InvoiceLog.LogKind;

public interface Invoice {
	
	public final static int INVOICE_TYPE_TAXES_APLY=0;
	public final static int INVOICE_TYPE_ORDER=1;
	public final static int INVOICE_TYPE_SAMPLE=2;
	public final static int INVOICE_TYPE_UNKNOWN=-1;
	
	public Client getClient();

	public InvoiceMetaData getInvoiceMetaData();

	public List<InvoiceItem> getItems();

	public Invoice[] subdivide(int size);

	public int invoiceType();

	@Deprecated
	public String getConsummer();
	@Deprecated
	public int getConsummerType();
	@Deprecated
	public String getAddress();
	@Deprecated
	public String getCity();
	@Deprecated
	public String getCountry();
	@Deprecated
	public String getCp();
	
	public String getCreationDate();
	public long getCreationTime();
	@Deprecated
	public String getEmail();
	@Deprecated
	public String getRfc();
	@Deprecated
	public InvoiceItem getItem(int index);
	@Deprecated
	public float getQuantity(int index)
			throws IndexOutOfBoundsException;
	@Deprecated
	public String getCode(int index) throws IndexOutOfBoundsException;
	@Deprecated
	public String getUnit(int index) throws IndexOutOfBoundsException;
	@Deprecated
	public String getDescription(int index)
			throws IndexOutOfBoundsException;
	@Deprecated
	public float getUnitPrice(int index)
			throws IndexOutOfBoundsException;
	@Deprecated
	public float getTotalPrice(int index)
			throws IndexOutOfBoundsException;

	public int size();
	@Deprecated
	public String getState();
	@Deprecated
	public String getTel();

	public float getSubtotal();

	public float getTotal();
	
	public float getTotalValue();
	
	public float getTotalForConsummerType(int consummerType);

	public String getTotalInWords();

	public String getReference();

	public int getPayment();

	public float getTaxes();
	
	public float getTaxesApply();
	
	public long getSerial();
	
	public void setSerial();
	
	public void setSeller(Seller seller);
	
	public Seller getSeller();

	public String getSellerCode();

	public Shopman getShopman();

	public String getShopmanLogin();

	public void setShopman(Shopman shopman);

	public Invoice contruct();
	@Deprecated
	public String getRequesterName();
	@Deprecated
	public String getDestinyAddress();

	public void setInvoiceType(int invoiceType);

	public void persist();

	public InvoiceMetaData getMetaData();
	
	public void setAgent(Client agent);
	
	public Client getAgent();

	public List<InvoiceLog> getLogs();

	public InvoiceLogAllowed attemptToLog(InvoiceLog.LogKind logKind);

	public InvoiceLog getLog(LogKind logKind);

	public boolean hasLog(LogKind logKind);

	public float getDebt();

	public void setUpdated(long updated);

	public long getUpdated();

	public void setFacturedTo(Client facturedTo);

	public Client getFacturedTo();

	public void setPrintedTo(Client printedTo);

	public Client getPrintedTo();

	public float getAgentPayment();

	public void setAgentPayment(float agentPayment);

	public InvoiceElectronicVersion getElectronicVersion();

	public void setElectronicVersion(InvoiceElectronicVersion electronicVersion);

	public boolean hasElectronicVersion();

	public void setHasElectronicVersion(boolean hasElectronicVersion);

}
