package com.ferremundo;

public class InvoiceElectronicVersion {
	
	private String xml;
	private boolean active;
	
	private String xmlPermaLink;
	private String xmlTempLink;
	
	private String pdfPermaLink;
	private String pdfTempLink;
	
	private String cancelXml;
	
	public InvoiceElectronicVersion() {}

	public InvoiceElectronicVersion(String xml) {
		this.xml = xml;
		active=true;
	}

	public InvoiceElectronicVersion(String xml, String xmlPermaLink,
			String xmlTempLink, String pdfPermaLink, String pdfTempLink) {
		this.xml = xml;
		this.xmlPermaLink = xmlPermaLink;
		this.xmlTempLink = xmlTempLink;
		this.pdfPermaLink = pdfPermaLink;
		this.pdfTempLink = pdfTempLink;
		active=true;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getXmlPermaLink() {
		return xmlPermaLink;
	}

	public void setXmlPermaLink(String xmlPermaLink) {
		this.xmlPermaLink = xmlPermaLink;
	}

	public String getXmlTempLink() {
		return xmlTempLink;
	}

	public void setXmlTempLink(String xmlTempLink) {
		this.xmlTempLink = xmlTempLink;
	}

	public String getPdfPermaLink() {
		return pdfPermaLink;
	}

	public void setPdfPermaLink(String pdfPermaLink) {
		this.pdfPermaLink = pdfPermaLink;
	}

	public String getPdfTempLink() {
		return pdfTempLink;
	}

	public void setPdfTempLink(String pdfTempLink) {
		this.pdfTempLink = pdfTempLink;
	}

	public String getCancelXml() {
		return cancelXml;
	}

	public void setCancelXml(String cancelXml) {
		this.cancelXml = cancelXml;
	}
	
	

}
