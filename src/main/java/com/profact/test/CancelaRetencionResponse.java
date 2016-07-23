
package com.profact.test;

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
 *         &lt;element name="CancelaRetencionResult" type="{http://localhost/}ArrayOfAnyType" minOccurs="0"/>
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
    "cancelaRetencionResult"
})
@XmlRootElement(name = "CancelaRetencionResponse")
public class CancelaRetencionResponse {

    @XmlElement(name = "CancelaRetencionResult")
    protected ArrayOfAnyType cancelaRetencionResult;

    /**
     * Gets the value of the cancelaRetencionResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getCancelaRetencionResult() {
        return cancelaRetencionResult;
    }

    /**
     * Sets the value of the cancelaRetencionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setCancelaRetencionResult(ArrayOfAnyType value) {
        this.cancelaRetencionResult = value;
    }

}
