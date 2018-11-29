
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
 *         &lt;element name="ObtieneTimbresDisponiblesResult" type="{http://tempuri.org/}ArrayOfAnyType" minOccurs="0"/>
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
    "obtieneTimbresDisponiblesResult"
})
@XmlRootElement(name = "ObtieneTimbresDisponiblesResponse")
public class ObtieneTimbresDisponiblesResponse {

    @XmlElement(name = "ObtieneTimbresDisponiblesResult")
    protected ArrayOfAnyType obtieneTimbresDisponiblesResult;

    /**
     * Gets the value of the obtieneTimbresDisponiblesResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getObtieneTimbresDisponiblesResult() {
        return obtieneTimbresDisponiblesResult;
    }

    /**
     * Sets the value of the obtieneTimbresDisponiblesResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setObtieneTimbresDisponiblesResult(ArrayOfAnyType value) {
        this.obtieneTimbresDisponiblesResult = value;
    }

}
