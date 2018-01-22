
package mx.buzoncfdi.cfdi33_pruebas;

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
 *         &lt;element name="RegistraEmisorResult" type="{http://tempuri.org/}ArrayOfAnyType" minOccurs="0"/>
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
    "registraEmisorResult"
})
@XmlRootElement(name = "RegistraEmisorResponse")
public class RegistraEmisorResponse {

    @XmlElement(name = "RegistraEmisorResult")
    protected ArrayOfAnyType registraEmisorResult;

    /**
     * Gets the value of the registraEmisorResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getRegistraEmisorResult() {
        return registraEmisorResult;
    }

    /**
     * Sets the value of the registraEmisorResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setRegistraEmisorResult(ArrayOfAnyType value) {
        this.registraEmisorResult = value;
    }

}
