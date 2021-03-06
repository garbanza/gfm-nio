
package pac1.test;

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
 *         &lt;element name="CancelaCFDIAckExternoResult" type="{http://tempuri.org/}ArrayOfAnyType" minOccurs="0"/>
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
    "cancelaCFDIAckExternoResult"
})
@XmlRootElement(name = "CancelaCFDIAckExternoResponse")
public class CancelaCFDIAckExternoResponse {

    @XmlElement(name = "CancelaCFDIAckExternoResult")
    protected ArrayOfAnyType cancelaCFDIAckExternoResult;

    /**
     * Gets the value of the cancelaCFDIAckExternoResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getCancelaCFDIAckExternoResult() {
        return cancelaCFDIAckExternoResult;
    }

    /**
     * Sets the value of the cancelaCFDIAckExternoResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setCancelaCFDIAckExternoResult(ArrayOfAnyType value) {
        this.cancelaCFDIAckExternoResult = value;
    }

}
