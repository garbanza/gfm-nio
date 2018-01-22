
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
 *         &lt;element name="TimbraCFDIResult" type="{http://tempuri.org/}ArrayOfAnyType" minOccurs="0"/>
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
    "timbraCFDIResult"
})
@XmlRootElement(name = "TimbraCFDIResponse")
public class TimbraCFDIResponse {

    @XmlElement(name = "TimbraCFDIResult")
    protected ArrayOfAnyType timbraCFDIResult;

    /**
     * Gets the value of the timbraCFDIResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getTimbraCFDIResult() {
        return timbraCFDIResult;
    }

    /**
     * Sets the value of the timbraCFDIResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setTimbraCFDIResult(ArrayOfAnyType value) {
        this.timbraCFDIResult = value;
    }

}
