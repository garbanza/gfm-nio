/**
 * RegistraEmisor.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.profact;

public class RegistraEmisor  implements java.io.Serializable {
    private java.lang.String usuarioIntegrador;

    private java.lang.String rfcEmisor;

    private java.lang.String base64Cer;

    private java.lang.String base64Key;

    private java.lang.String contrasena;

    public RegistraEmisor() {
    }

    public RegistraEmisor(
           java.lang.String usuarioIntegrador,
           java.lang.String rfcEmisor,
           java.lang.String base64Cer,
           java.lang.String base64Key,
           java.lang.String contrasena) {
           this.usuarioIntegrador = usuarioIntegrador;
           this.rfcEmisor = rfcEmisor;
           this.base64Cer = base64Cer;
           this.base64Key = base64Key;
           this.contrasena = contrasena;
    }


    /**
     * Gets the usuarioIntegrador value for this RegistraEmisor.
     * 
     * @return usuarioIntegrador
     */
    public java.lang.String getUsuarioIntegrador() {
        return usuarioIntegrador;
    }


    /**
     * Sets the usuarioIntegrador value for this RegistraEmisor.
     * 
     * @param usuarioIntegrador
     */
    public void setUsuarioIntegrador(java.lang.String usuarioIntegrador) {
        this.usuarioIntegrador = usuarioIntegrador;
    }


    /**
     * Gets the rfcEmisor value for this RegistraEmisor.
     * 
     * @return rfcEmisor
     */
    public java.lang.String getRfcEmisor() {
        return rfcEmisor;
    }


    /**
     * Sets the rfcEmisor value for this RegistraEmisor.
     * 
     * @param rfcEmisor
     */
    public void setRfcEmisor(java.lang.String rfcEmisor) {
        this.rfcEmisor = rfcEmisor;
    }


    /**
     * Gets the base64Cer value for this RegistraEmisor.
     * 
     * @return base64Cer
     */
    public java.lang.String getBase64Cer() {
        return base64Cer;
    }


    /**
     * Sets the base64Cer value for this RegistraEmisor.
     * 
     * @param base64Cer
     */
    public void setBase64Cer(java.lang.String base64Cer) {
        this.base64Cer = base64Cer;
    }


    /**
     * Gets the base64Key value for this RegistraEmisor.
     * 
     * @return base64Key
     */
    public java.lang.String getBase64Key() {
        return base64Key;
    }


    /**
     * Sets the base64Key value for this RegistraEmisor.
     * 
     * @param base64Key
     */
    public void setBase64Key(java.lang.String base64Key) {
        this.base64Key = base64Key;
    }


    /**
     * Gets the contrasena value for this RegistraEmisor.
     * 
     * @return contrasena
     */
    public java.lang.String getContrasena() {
        return contrasena;
    }


    /**
     * Sets the contrasena value for this RegistraEmisor.
     * 
     * @param contrasena
     */
    public void setContrasena(java.lang.String contrasena) {
        this.contrasena = contrasena;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistraEmisor)) return false;
        RegistraEmisor other = (RegistraEmisor) obj;
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
            ((this.rfcEmisor==null && other.getRfcEmisor()==null) || 
             (this.rfcEmisor!=null &&
              this.rfcEmisor.equals(other.getRfcEmisor()))) &&
            ((this.base64Cer==null && other.getBase64Cer()==null) || 
             (this.base64Cer!=null &&
              this.base64Cer.equals(other.getBase64Cer()))) &&
            ((this.base64Key==null && other.getBase64Key()==null) || 
             (this.base64Key!=null &&
              this.base64Key.equals(other.getBase64Key()))) &&
            ((this.contrasena==null && other.getContrasena()==null) || 
             (this.contrasena!=null &&
              this.contrasena.equals(other.getContrasena())));
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
        if (getRfcEmisor() != null) {
            _hashCode += getRfcEmisor().hashCode();
        }
        if (getBase64Cer() != null) {
            _hashCode += getBase64Cer().hashCode();
        }
        if (getBase64Key() != null) {
            _hashCode += getBase64Key().hashCode();
        }
        if (getContrasena() != null) {
            _hashCode += getContrasena().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistraEmisor.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://localhost/", ">RegistraEmisor"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("usuarioIntegrador");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "usuarioIntegrador"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rfcEmisor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "rfcEmisor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("base64Cer");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "base64Cer"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("base64Key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "base64Key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contrasena");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "contrasena"));
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
