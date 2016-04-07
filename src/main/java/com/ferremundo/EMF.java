package com.ferremundo;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
	public static final String UNIT_CLIENT = "client";
	public static final String UNIT_CLIENT_SAMPLE = "clientsample";
	public static final String UNIT_PRODUCT = "product";
	public static final String UNIT_PRODUCT_SAMPLE = "productsample";
	public static final String UNIT_REF = "ref";
	public static final String UNIT_INVOICEFM01 = "invoicefm01";
	public static final String UNIT_INVOICE_SERIAL = "invoiceserial";
	public static final String UNIT_SELLER = "seller";
	public static final String UNIT_SHOPMAN = "shopman";
	public static final String UNIT_DESTINY = "destiny";
	public static final String UNIT_REQUESTER = "requester";
	
    private static final EntityManagerFactory EMF_UNIT_CLIENT=Persistence.createEntityManagerFactory(UNIT_CLIENT);
    private static final EntityManagerFactory EMF_UNIT_CLIENT_SAMPLE=Persistence.createEntityManagerFactory(UNIT_CLIENT_SAMPLE);
    private static final EntityManagerFactory EMF_UNIT_PRODUCT=Persistence.createEntityManagerFactory(UNIT_PRODUCT);
    private static final EntityManagerFactory EMF_UNIT_PRODUCT_SAMPLE=Persistence.createEntityManagerFactory(UNIT_PRODUCT_SAMPLE);
    private static final EntityManagerFactory EMF_UNIT_REF=Persistence.createEntityManagerFactory(UNIT_REF);
    private static final EntityManagerFactory EMF_UNIT_INVOICEFM01=Persistence.createEntityManagerFactory(UNIT_INVOICEFM01);
    private static final EntityManagerFactory EMF_UNIT_INVOICE_SERIAL=Persistence.createEntityManagerFactory(UNIT_INVOICE_SERIAL);
    private static final EntityManagerFactory EMF_UNIT_SELLER=Persistence.createEntityManagerFactory(UNIT_SELLER);
    private static final EntityManagerFactory EMF_UNIT_SHOPMAN=Persistence.createEntityManagerFactory(UNIT_SHOPMAN);
    private static final EntityManagerFactory EMF_UNIT_DESTINY=Persistence.createEntityManagerFactory(UNIT_DESTINY);
    private static final EntityManagerFactory EMF_UNIT_REQUESTER=Persistence.createEntityManagerFactory(UNIT_REQUESTER);
    private EMF() {}

    public static EntityManagerFactory get(String unit) {
    	if(unit.equals(UNIT_CLIENT))return EMF_UNIT_CLIENT;
    	else if(unit.equals(UNIT_CLIENT_SAMPLE))return EMF_UNIT_CLIENT_SAMPLE;
    	else if(unit.equals(UNIT_PRODUCT))return EMF_UNIT_PRODUCT;
    	else if(unit.equals(UNIT_PRODUCT_SAMPLE))return EMF_UNIT_PRODUCT_SAMPLE;
    	else if(unit.equals(UNIT_REF))return EMF_UNIT_REF;
    	else if(unit.equals(UNIT_INVOICEFM01))return EMF_UNIT_INVOICEFM01;
    	else if(unit.equals(UNIT_INVOICE_SERIAL))return EMF_UNIT_INVOICE_SERIAL;
    	else if(unit.equals(UNIT_SELLER))return EMF_UNIT_SELLER;
    	else if(unit.equals(UNIT_SELLER))return EMF_UNIT_SHOPMAN;
    	else if(unit.equals(UNIT_SELLER))return EMF_UNIT_DESTINY;
    	else if(unit.equals(UNIT_SELLER))return EMF_UNIT_REQUESTER;
    	else return null;
    }
}
