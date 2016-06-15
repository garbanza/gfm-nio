/**
 * CancelaCFDIAckResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package test.profact;

public class CancelaCFDIAckResponse  implements java.io.Serializable {
    private java.lang.Object[] cancelaCFDIAckResult;

    public CancelaCFDIAckResponse() {
    }

    public CancelaCFDIAckResponse(
           java.lang.Object[] cancelaCFDIAckResult) {
           this.cancelaCFDIAckResult = cancelaCFDIAckResult;
    }


    /**
     * Gets the cancelaCFDIAckResult value for this CancelaCFDIAckResponse.
     * 
     * @return cancelaCFDIAckResult
     */
    public java.lang.Object[] getCancelaCFDIAckResult() {
        return cancelaCFDIAckResult;
    }


    /**
     * Sets the cancelaCFDIAckResult value for this CancelaCFDIAckResponse.
     * 
     * @param cancelaCFDIAckResult
     */
    public void setCancelaCFDIAckResult(java.lang.Object[] cancelaCFDIAckResult) {
        this.cancelaCFDIAckResult = cancelaCFDIAckResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CancelaCFDIAckResponse)) return false;
        CancelaCFDIAckResponse other = (CancelaCFDIAckResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cancelaCFDIAckResult==null && other.getCancelaCFDIAckResult()==null) || 
             (this.cancelaCFDIAckResult!=null &&
              java.util.Arrays.equals(this.cancelaCFDIAckResult, other.getCancelaCFDIAckResult())));
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
        if (getCancelaCFDIAckResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCancelaCFDIAckResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCancelaCFDIAckResult(), i);
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
        new org.apache.axis.description.TypeDesc(CancelaCFDIAckResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://localhost/", ">CancelaCFDIAckResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cancelaCFDIAckResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "CancelaCFDIAckResult"));
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
