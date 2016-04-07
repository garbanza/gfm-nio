package com.ferremundo.gth;

import com.lowagie.text.Rectangle;

public interface InvoiceForm {

	static int MILLIMETER_MEASUREMENT_UNIT=0;
	static int CENTIMETER_MEASUREMENT_UNIT=1;
	static int METER_MEASUREMENT_UNIT=2;
	
	public Rectangle getInvoiceArea();
	public Rectangle getConsummerArea();
	public Rectangle getAddressArea();
	public Rectangle getCityArea();
	public Rectangle getCountryArea();
	public Rectangle getStateArea();
	public Rectangle getDateArea();
	public Rectangle getCpArea();
	public Rectangle getEmailArea();
	public Rectangle getRfcArea();
	public Rectangle getQuantityArea(int index)throws IndexOutOfBoundsException;
	public Rectangle getUnitArea(int index)throws IndexOutOfBoundsException;
	public Rectangle getCodeArea(int index)throws IndexOutOfBoundsException;
	public Rectangle getDescriptionArea(int index)throws IndexOutOfBoundsException;
	public Rectangle getUnitPriceArea(int index)throws IndexOutOfBoundsException;
	public Rectangle getTotalPriceArea(int index)throws IndexOutOfBoundsException;
	public Rectangle getTelArea();
	public Rectangle getSubtotalArea();
	public Rectangle getTaxesArea();
	public Rectangle getTotalArea();
	public Rectangle getTotalInWordsArea();
	public Rectangle getReferenceArea();
	public Rectangle getPaymentArea();
	public int getMeasurementUnit();
	public int getRowsNumber();
}
