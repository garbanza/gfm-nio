
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
 *         &lt;element name="ConsultaEstatusSatExternoResult" type="{http://tempuri.org/}ArrayOfAnyType" minOccurs="0"/>
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
    "consultaEstatusSatExternoResult"
})
@XmlRootElement(name = "ConsultaEstatusSatExternoResponse")
public class ConsultaEstatusSatExternoResponse {

    @XmlElement(name = "ConsultaEstatusSatExternoResult")
    protected ArrayOfAnyType consultaEstatusSatExternoResult;

    /**
     * Gets the value of the consultaEstatusSatExternoResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getConsultaEstatusSatExternoResult() {
        return consultaEstatusSatExternoResult;
    }

    /**
     * Sets the value of the consultaEstatusSatExternoResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setConsultaEstatusSatExternoResult(ArrayOfAnyType value) {
        this.consultaEstatusSatExternoResult = value;
    }

}
