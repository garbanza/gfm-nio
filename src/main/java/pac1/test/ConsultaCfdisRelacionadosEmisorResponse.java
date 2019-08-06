
package pac1.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected         content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ConsultaCfdisRelacionadosEmisorResult" type="{http://tempuri.org/}ArrayOfAnyType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "consultaCfdisRelacionadosEmisorResult"
})
@XmlRootElement(name = "ConsultaCfdisRelacionadosEmisorResponse")
public class ConsultaCfdisRelacionadosEmisorResponse {

    @XmlElement(name = "ConsultaCfdisRelacionadosEmisorResult")
    protected ArrayOfAnyType consultaCfdisRelacionadosEmisorResult;

    /**
     * Gets the value of the consultaCfdisRelacionadosEmisorResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public ArrayOfAnyType getConsultaCfdisRelacionadosEmisorResult() {
        return consultaCfdisRelacionadosEmisorResult;
    }

    /**
     * Sets the value of the consultaCfdisRelacionadosEmisorResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAnyType }
     *     
     */
    public void setConsultaCfdisRelacionadosEmisorResult(ArrayOfAnyType value) {
        this.consultaCfdisRelacionadosEmisorResult = value;
    }

}
