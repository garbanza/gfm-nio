
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
 *         &lt;element name="ObtieneRetencionResult" type="{http://localhost/}ArrayOfAnyType" minOccurs="0"/>
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
    "obtieneRetencionResult"
})
@XmlRootElement(name = "ObtieneRetencionResponse")
public class ObtieneRetencionResponse {

    @XmlElement(name = "ObtieneRetencionResult")
    protected ArrayOfAnyType obtieneRetencionResult;

    /**
     * Gets the value of the obtieneRetencionResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getObtieneRetencionResult() {
        return obtieneRetencionResult;
    }

    /**
     * Sets the value of the obtieneRetencionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setObtieneRetencionResult(ArrayOfAnyType value) {
        this.obtieneRetencionResult = value;
    }

}
