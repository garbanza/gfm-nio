
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
 *         &lt;element name="xmlComprobanteBase64" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="idComprobante" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "xmlComprobanteBase64",
    "idComprobante"
})
@XmlRootElement(name = "TimbraCFDI")
public class TimbraCFDI {

    protected String usuarioIntegrador;
    protected String xmlComprobanteBase64;
    protected String idComprobante;

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
     * Gets the value of the xmlComprobanteBase64 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getXmlComprobanteBase64() {
        return xmlComprobanteBase64;
    }

    /**
     * Sets the value of the xmlComprobanteBase64 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setXmlComprobanteBase64(String value) {
        this.xmlComprobanteBase64 = value;
    }

    /**
     * Gets the value of the idComprobante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdComprobante() {
        return idComprobante;
    }

    /**
     * Sets the value of the idComprobante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdComprobante(String value) {
        this.idComprobante = value;
    }

}
