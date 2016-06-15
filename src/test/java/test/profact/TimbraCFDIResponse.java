/**
 * TimbraCFDIResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package test.profact;

public class TimbraCFDIResponse  implements java.io.Serializable {
    private java.lang.Object[] timbraCFDIResult;

    public TimbraCFDIResponse() {
    }

    public TimbraCFDIResponse(
           java.lang.Object[] timbraCFDIResult) {
           this.timbraCFDIResult = timbraCFDIResult;
    }


    /**
     * Gets the timbraCFDIResult value for this TimbraCFDIResponse.
     * 
     * @return timbraCFDIResult
     */
    public java.lang.Object[] getTimbraCFDIResult() {
        return timbraCFDIResult;
    }


    /**
     * Sets the timbraCFDIResult value for this TimbraCFDIResponse.
     * 
     * @param timbraCFDIResult
     */
    public void setTimbraCFDIResult(java.lang.Object[] timbraCFDIResult) {
        this.timbraCFDIResult = timbraCFDIResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TimbraCFDIResponse)) return false;
        TimbraCFDIResponse other = (TimbraCFDIResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.timbraCFDIResult==null && other.getTimbraCFDIResult()==null) || 
             (this.timbraCFDIResult!=null &&
              java.util.Arrays.equals(this.timbraCFDIResult, other.getTimbraCFDIResult())));
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
        if (getTimbraCFDIResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTimbraCFDIResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTimbraCFDIResult(), i);
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
        new org.apache.axis.description.TypeDesc(TimbraCFDIResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://localhost/", ">TimbraCFDIResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timbraCFDIResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "TimbraCFDIResult"));
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
