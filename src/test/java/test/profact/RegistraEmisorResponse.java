/**
 * RegistraEmisorResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package test.profact;

public class RegistraEmisorResponse  implements java.io.Serializable {
    private java.lang.Object[] registraEmisorResult;

    public RegistraEmisorResponse() {
    }

    public RegistraEmisorResponse(
           java.lang.Object[] registraEmisorResult) {
           this.registraEmisorResult = registraEmisorResult;
    }


    /**
     * Gets the registraEmisorResult value for this RegistraEmisorResponse.
     * 
     * @return registraEmisorResult
     */
    public java.lang.Object[] getRegistraEmisorResult() {
        return registraEmisorResult;
    }


    /**
     * Sets the registraEmisorResult value for this RegistraEmisorResponse.
     * 
     * @param registraEmisorResult
     */
    public void setRegistraEmisorResult(java.lang.Object[] registraEmisorResult) {
        this.registraEmisorResult = registraEmisorResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RegistraEmisorResponse)) return false;
        RegistraEmisorResponse other = (RegistraEmisorResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.registraEmisorResult==null && other.getRegistraEmisorResult()==null) || 
             (this.registraEmisorResult!=null &&
              java.util.Arrays.equals(this.registraEmisorResult, other.getRegistraEmisorResult())));
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
        if (getRegistraEmisorResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRegistraEmisorResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRegistraEmisorResult(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(RegistraEmisorResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://localhost/", ">RegistraEmisorResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("registraEmisorResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "RegistraEmisorResult"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://localhost/", "anyType"));
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
