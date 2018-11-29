
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
 *         &lt;element name="ConsultaPeticionesPendientesCFDIResult" type="{http://tempuri.org/}ArrayOfAnyType" minOccurs="0"/>
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
    "consultaPeticionesPendientesCFDIResult"
})
@XmlRootElement(name = "ConsultaPeticionesPendientesCFDIResponse")
public class ConsultaPeticionesPendientesCFDIResponse {

    @XmlElement(name = "ConsultaPeticionesPendientesCFDIResult")
    protected ArrayOfAnyType consultaPeticionesPendientesCFDIResult;

    /**
     * Gets the value of the consultaPeticionesPendientesCFDIResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getConsultaPeticionesPendientesCFDIResult() {
        return consultaPeticionesPendientesCFDIResult;
    }

    /**
     * Sets the value of the consultaPeticionesPendientesCFDIResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setConsultaPeticionesPendientesCFDIResult(ArrayOfAnyType value) {
        this.consultaPeticionesPendientesCFDIResult = value;
    }

}
