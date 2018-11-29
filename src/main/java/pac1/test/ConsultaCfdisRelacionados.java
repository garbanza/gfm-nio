
package pac1.test;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="usuarioIntegrador" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="folioUUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rfcReceptor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "usuarioIntegrador",
    "folioUUID",
    "rfcReceptor"
})
@XmlRootElement(name = "ConsultaCfdisRelacionados")
public class ConsultaCfdisRelacionados {

    protected String usuarioIntegrador;
    protected String folioUUID;
    protected String rfcReceptor;

    /**
     * Gets the value of the usuarioIntegrador property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsuarioIntegrador() {
        return usuarioIntegrador;
    }

    /**
     * Sets the value of the usuarioIntegrador property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsuarioIntegrador(String value) {
        this.usuarioIntegrador = value;
    }

    /**
     * Gets the value of the folioUUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFolioUUID() {
        return folioUUID;
    }

    /**
     * Sets the value of the folioUUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFolioUUID(String value) {
        this.folioUUID = value;
    }

    /**
     * Gets the value of the rfcReceptor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcReceptor() {
        return rfcReceptor;
    }

    /**
     * Sets the value of the rfcReceptor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcReceptor(String value) {
        this.rfcReceptor = value;
    }

}
