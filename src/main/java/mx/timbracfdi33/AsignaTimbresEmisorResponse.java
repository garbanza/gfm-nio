
package mx.timbracfdi33;

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
 *         &lt;element name="AsignaTimbresEmisorResult" type="{http://tempuri.org/}ArrayOfAnyType" minOccurs="0"/>
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
    "asignaTimbresEmisorResult"
})
@XmlRootElement(name = "AsignaTimbresEmisorResponse")
public class AsignaTimbresEmisorResponse {

    @XmlElement(name = "AsignaTimbresEmisorResult")
    protected ArrayOfAnyType asignaTimbresEmisorResult;

    /**
     * Gets the value of the asignaTimbresEmisorResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getAsignaTimbresEmisorResult() {
        return asignaTimbresEmisorResult;
    }

    /**
     * Sets the value of the asignaTimbresEmisorResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setAsignaTimbresEmisorResult(ArrayOfAnyType value) {
        this.asignaTimbresEmisorResult = value;
    }

}
