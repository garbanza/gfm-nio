/**
 * CancelaRetencionResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.profact;

public class CancelaRetencionResponse  implements java.io.Serializable {
    private java.lang.Object[] cancelaRetencionResult;

    public CancelaRetencionResponse() {
    }

    public CancelaRetencionResponse(
           java.lang.Object[] cancelaRetencionResult) {
           this.cancelaRetencionResult = cancelaRetencionResult;
    }


    /**
     * Gets the cancelaRetencionResult value for this CancelaRetencionResponse.
     * 
     * @return cancelaRetencionResult
     */
    public java.lang.Object[] getCancelaRetencionResult() {
        return cancelaRetencionResult;
    }


    /**
     * Sets the cancelaRetencionResult value for this CancelaRetencionResponse.
     * 
     * @param cancelaRetencionResult
     */
    public void setCancelaRetencionResult(java.lang.Object[] cancelaRetencionResult) {
        this.cancelaRetencionResult = cancelaRetencionResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CancelaRetencionResponse)) return false;
        CancelaRetencionResponse other = (CancelaRetencionResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.cancelaRetencionResult==null && other.getCancelaRetencionResult()==null) || 
             (this.cancelaRetencionResult!=null &&
              java.util.Arrays.equals(this.cancelaRetencionResult, other.getCancelaRetencionResult())));
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
        if (getCancelaRetencionResult() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCancelaRetencionResult());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCancelaRetencionResult(), i);
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
        new org.apache.axis.description.TypeDesc(CancelaRetencionResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://localhost/", ">CancelaRetencionResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cancelaRetencionResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://localhost/", "CancelaRetencionResult"));
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
