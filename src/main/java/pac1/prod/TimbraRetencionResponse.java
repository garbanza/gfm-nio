
package pac1.prod;

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
 *         &lt;element name="TimbraRetencionResult" type="{http://tempuri.org/}ArrayOfAnyType" minOccurs="0"/>
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
    "timbraRetencionResult"
})
@XmlRootElement(name = "TimbraRetencionResponse")
public class TimbraRetencionResponse {

    @XmlElement(name = "TimbraRetencionResult")
    protected ArrayOfAnyType timbraRetencionResult;

    /**
     * Gets the value of the timbraRetencionResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getTimbraRetencionResult() {
        return timbraRetencionResult;
    }

    /**
     * Sets the value of the timbraRetencionResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setTimbraRetencionResult(ArrayOfAnyType value) {
        this.timbraRetencionResult = value;
    }

}
