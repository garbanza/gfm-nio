/**
 * TimbraCFDI.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package test.profact;

public class TimbraCFDI  implements java.io.Serializable {
    private java.lang.String usuarioIntegrador;

    private java.lang.String xmlComprobanteBase64;

    private java.lang.String idComprobante;

    public TimbraCFDI() {
    }

    public TimbraCFDI(
           java.lang.String usuarioIntegrador,
           java.lang.String xmlComprobanteBase64,
           java.lang.String idComprobante) {
           this.usuarioIntegrador = usuarioIntegrador;
           this.xmlComprobanteBase64 = xmlComprobanteBase64;
           this.idComprobante = idComprobante;
    }


    /**
     * Gets the usuarioIntegrador value for this TimbraCFDI.
     * 
     * @return usuarioIntegrador
     */
    public java.lang.String getUsuarioIntegrador() {
        return usuarioIntegrador;
    }


    /**
     * Sets the usuarioIntegrador value for this TimbraCFDI.
     * 
     * @param usuarioIntegrador
     */
    public void setUsuarioIntegrador(java.lang.String usuarioIntegrador) {
        this.usuarioIntegrador = usuarioIntegrador;
    }


    /**
     * Gets the xmlComprobanteBase64 value for this TimbraCFDI.
     * 
     * @return xmlComprobanteBase64
     */
    public java.lang.String getXmlComprobanteBase64() {
        return xmlComprobanteBase64;
    }


    /**
     * Sets the xmlComprobanteBase64 value for this TimbraCFDI.
     * 
     * @param xmlComprobanteBase64
     */
    public void setXmlComprobanteBase64(java.lang.String xmlComprobanteBase64) {
        this.xmlComprobanteBase64 = xmlComprobanteBase64;
    }


    /**
     * Gets the idComprobante value for this TimbraCFDI.
     * 
     * @return idComprobante
     */
    public java.lang.String getIdComprobante() {
        return idComprobante;
    }


    /**
     * Sets the idComprobante value for this TimbraCFDI.
     * 
     * @param idComprobante
     */
    public void setIdComprobante(java.lang.String idComprobante) {
        this.idComprobante = idComprobante;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TimbraCFDI)) return false;
        TimbraCFDI other = (TimbraCFDI) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.usuarioIntegrador==null && other.getUsuarioIntegrador()==null) || 
             (this.usuarioIntegrador!=null &&
              this.usuarioIntegrador.equals(other.getUsuarioIntegrador()))) &&
            ((this.xmlComprobanteBase64==null && other.getXmlComprobanteBase64()==null) || 
             (this.xmlComprobanteBase64!=null &&
              this.xmlComprobanteBase64.equals(other.getXmlComprobanteBase64()))) &&
            ((this.idComprobante==null && other.getIdComprobante()==null) || 
             (this.idComprobante!=null &&
              this.idComprobante.equals(other.getIdComprobante())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getUsuarioIntegrador() != null) {
            _hashCode += getUsuarioIntegrador().hashCode();
        }
        if (getXmlComprobanteBase64() != null) {
            _hashCode += getXmlComprobanteBase64().hashCode();
        }
        if (getIdComprobante() != null) {
            _hashCode += getIdComprobante().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TimbraCFDI.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://localhost/", ">TimbraCFDI"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usuarioIntegrador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "usuarioIntegrador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("xmlComprobanteBase64");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "xmlComprobanteBase64"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("idComprobante");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "idComprobante"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
