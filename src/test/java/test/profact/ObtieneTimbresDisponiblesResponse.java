/**
 * ObtieneTimbresDisponiblesResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package test.profact;

public class ObtieneTimbresDisponiblesResponse  implements java.io.Serializable {
    private java.lang.Object[] obtieneTimbresDisponiblesResult;

    public ObtieneTimbresDisponiblesResponse() {
    }

    public ObtieneTimbresDisponiblesResponse(
           java.lang.Object[] obtieneTimbresDisponiblesResult) {
           this.obtieneTimbresDisponiblesResult = obtieneTimbresDisponiblesResult;
    }


    /**
     * Gets the obtieneTimbresDisponiblesResult value for this ObtieneTimbresDisponiblesResponse.
     * 
     * @return obtieneTimbresDisponiblesResult
     */
    public java.lang.Object[] getObtieneTimbresDisponiblesResult() {
        return obtieneTimbresDisponiblesResult;
    }


    /**
     * Sets the obtieneTimbresDisponiblesResult value for this ObtieneTimbresDisponiblesResponse.
     * 
     * @param obtieneTimbresDisponiblesResult
     */
    public void setObtieneTimbresDisponiblesResult(java.lang.Object[] obtieneTimbresDisponiblesResult) {
        this.obtieneTimbresDisponiblesResult = obtieneTimbresDisponiblesResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ObtieneTimbresDisponiblesResponse)) return false;
        ObtieneTimbresDisponiblesResponse other = (ObtieneTimbresDisponiblesResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.obtieneTimbresDisponiblesResult==null && other.getObtieneTimbresDisponiblesResult()==null) || 
             (this.obtieneTimbresDisponiblesResult!=null &&
              java.util.Arrays.equals(this.obtieneTimbresDisponiblesResult, other.getObtieneTimbresDisponiblesResult())));
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
        if (getObtieneTimbresDisponiblesResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getObtieneTimbresDisponiblesResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getObtieneTimbresDisponiblesResult(), i);
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
        new org.apache.axis.description.TypeDesc(ObtieneTimbresDisponiblesResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://localhost/", ">ObtieneTimbresDisponiblesResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("obtieneTimbresDisponiblesResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "ObtieneTimbresDisponiblesResult"));
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
