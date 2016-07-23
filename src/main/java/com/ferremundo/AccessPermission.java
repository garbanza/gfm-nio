package com.ferremundo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum AccessPermission {
	ADMIN,
	ROOT,
	BASIC,
	AGENT,
	
	CONSUMMER_READ,
	CONSUMMER_CREATE,
	CONSUMMER_BLOCK,
	
	AGENT_REGISTER,
	AGENT_READ,
	AGENT_LOG_SEARCH,
	AGENT_INCREMENT_EARNINGS,
	
	PRODUCT_READ,
	PRODUCT_CREATE,
	PRODUCT_UPDATE,
	
	GLOBAL_SEARCH,
	
	SHOPMAN_UPDATE,
	SHOPMAN_CREATE,
	
	INVOICE_READ,
	INVOICE_SAMPLE,
	INVOICE_ORDER,
	INVOICE_FACTURE,	
	INVOICE_ORDER_PAY_ON_CREDIT,
	INVOICE_FACTURE_PAY_ON_CREDIT,
	INVOICE_ORDER_PAY_ON_PAST_DUE,
	INVOICE_FACTURE_PAY_ON_PAST_DUE,
	INVOICE_CANCEL,
	READ_THE_BOX,
	
	RESET_PRODUCT_INVENTORY,
	REQUEST_SHOPMAN,
	
	READ_PROVIDERS,
	CREATE_PROVIDERS;
	
	public static List<AccessPermission> admin(){
		return new ArrayList<AccessPermission>(Arrays.asList(
				ADMIN
		));
	}
	
	public static List<AccessPermission> basic(){
		return new ArrayList<AccessPermission>(Arrays.asList(
				BASIC,
				PRODUCT_READ,
				CONSUMMER_READ,
				CONSUMMER_CREATE,
				INVOICE_SAMPLE,
				INVOICE_ORDER,
				INVOICE_FACTURE,
				GLOBAL_SEARCH,
				AGENT_READ,
				INVOICE_READ
		));
	}
	
	public static List<AccessPermission> agent(){
		return new ArrayList<AccessPermission>(Arrays.asList(
				AGENT,
				PRODUCT_READ,
				CONSUMMER_READ,
				CONSUMMER_CREATE,
				INVOICE_SAMPLE,
				INVOICE_ORDER,
				AGENT_LOG_SEARCH
		));
	}

}

