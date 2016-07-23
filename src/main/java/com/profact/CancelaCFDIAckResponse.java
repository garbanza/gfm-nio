
package com.profact;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CancelaCFDIAckResult" type="{http://localhost/}ArrayOfAnyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "cancelaCFDIAckResult"
})
@XmlRootElement(name = "CancelaCFDIAckResponse")
public class CancelaCFDIAckResponse {

    @XmlElement(name = "CancelaCFDIAckResult")
    protected ArrayOfAnyType cancelaCFDIAckResult;

    /**
     * Gets the value of the cancelaCFDIAckResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getCancelaCFDIAckResult() {
        return cancelaCFDIAckResult;
    }

    /**
     * Sets the value of the cancelaCFDIAckResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setCancelaCFDIAckResult(ArrayOfAnyType value) {
        this.cancelaCFDIAckResult = value;
    }

}
