
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
 *         &lt;element name="ObtieneCFDIResult" type="{http://tempuri.org/}ArrayOfAnyType" minOccurs="0"/>
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
    "obtieneCFDIResult"
})
@XmlRootElement(name = "ObtieneCFDIResponse")
public class ObtieneCFDIResponse {

    @XmlElement(name = "ObtieneCFDIResult")
    protected ArrayOfAnyType obtieneCFDIResult;

    /**
     * Gets the value of the obtieneCFDIResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getObtieneCFDIResult() {
        return obtieneCFDIResult;
    }

    /**
     * Sets the value of the obtieneCFDIResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setObtieneCFDIResult(ArrayOfAnyType value) {
        this.obtieneCFDIResult = value;
    }

}
