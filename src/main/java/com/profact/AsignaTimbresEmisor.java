/**
 * AsignaTimbresEmisor.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.profact;

public class AsignaTimbresEmisor  implements java.io.Serializable {
    private java.lang.String usuarioIntegrador;

    private java.lang.String rfcEmisor;

    private int noTimbres;

    public AsignaTimbresEmisor() {
    }

    public AsignaTimbresEmisor(
           java.lang.String usuarioIntegrador,
           java.lang.String rfcEmisor,
           int noTimbres) {
           this.usuarioIntegrador = usuarioIntegrador;
           this.rfcEmisor = rfcEmisor;
           this.noTimbres = noTimbres;
    }


    /**
     * Gets the usuarioIntegrador value for this AsignaTimbresEmisor.
     * 
     * @return usuarioIntegrador
     */
    public java.lang.String getUsuarioIntegrador() {
        return usuarioIntegrador;
    }


    /**
     * Sets the usuarioIntegrador value for this AsignaTimbresEmisor.
     * 
     * @param usuarioIntegrador
     */
    public void setUsuarioIntegrador(java.lang.String usuarioIntegrador) {
        this.usuarioIntegrador = usuarioIntegrador;
    }


    /**
     * Gets the rfcEmisor value for this AsignaTimbresEmisor.
     * 
     * @return rfcEmisor
     */
    public java.lang.String getRfcEmisor() {
        return rfcEmisor;
    }


    /**
     * Sets the rfcEmisor value for this AsignaTimbresEmisor.
     * 
     * @param rfcEmisor
     */
    public void setRfcEmisor(java.lang.String rfcEmisor) {
        this.rfcEmisor = rfcEmisor;
    }


    /**
     * Gets the noTimbres value for this AsignaTimbresEmisor.
     * 
     * @return noTimbres
     */
    public int getNoTimbres() {
        return noTimbres;
    }


    /**
     * Sets the noTimbres value for this AsignaTimbresEmisor.
     * 
     * @param noTimbres
     */
    public void setNoTimbres(int noTimbres) {
        this.noTimbres = noTimbres;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AsignaTimbresEmisor)) return false;
        AsignaTimbresEmisor other = (AsignaTimbresEmisor) obj;
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
            this.noTimbres == other.getNoTimbres();
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
        _hashCode += getNoTimbres();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AsignaTimbresEmisor.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://localhost/", ">AsignaTimbresEmisor"));
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
        elemField.setFieldName("noTimbres");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "noTimbres"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
