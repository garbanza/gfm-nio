
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
 *         &lt;element name="ConsultaEstatusSatResult" type="{http://tempuri.org/}ArrayOfAnyType" minOccurs="0"/>
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
    "consultaEstatusSatResult"
})
@XmlRootElement(name = "ConsultaEstatusSatResponse")
public class ConsultaEstatusSatResponse {

    @XmlElement(name = "ConsultaEstatusSatResult")
    protected ArrayOfAnyType consultaEstatusSatResult;

    /**
     * Gets the value of the consultaEstatusSatResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getConsultaEstatusSatResult() {
        return consultaEstatusSatResult;
    }

    /**
     * Sets the value of the consultaEstatusSatResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setConsultaEstatusSatResult(ArrayOfAnyType value) {
        this.consultaEstatusSatResult = value;
    }

}
