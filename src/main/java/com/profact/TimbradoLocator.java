/**
 * TimbradoLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.profact;

public class TimbradoLocator extends org.apache.axis.client.Service implements com.profact.Timbrado {

    public TimbradoLocator() {
    }


    public TimbradoLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public TimbradoLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for TimbradoSoap
    private java.lang.String TimbradoSoap_address = "http://www.timbracfdi.mx/serviciointegracion/Timbrado.asmx";

    public java.lang.String getTimbradoSoapAddress() {
        return TimbradoSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String TimbradoSoapWSDDServiceName = "TimbradoSoap";

    public java.lang.String getTimbradoSoapWSDDServiceName() {
        return TimbradoSoapWSDDServiceName;
    }

    public void setTimbradoSoapWSDDServiceName(java.lang.String name) {
        TimbradoSoapWSDDServiceName = name;
    }

    public com.profact.TimbradoSoap getTimbradoSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(TimbradoSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getTimbradoSoap(endpoint);
    }

    public com.profact.TimbradoSoap getTimbradoSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.profact.TimbradoSoapStub _stub = new com.profact.TimbradoSoapStub(portAddress, this);
            _stub.setPortName(getTimbradoSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setTimbradoSoapEndpointAddress(java.lang.String address) {
        TimbradoSoap_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.profact.TimbradoSoap.class.isAssignableFrom(serviceEndpointInterface)) {
                com.profact.TimbradoSoapStub _stub = new com.profact.TimbradoSoapStub(new java.net.URL(TimbradoSoap_address), this);
                _stub.setPortName(getTimbradoSoapWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("TimbradoSoap".equals(inputPortName)) {
            return getTimbradoSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost/", "Timbrado");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost/", "TimbradoSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("TimbradoSoap".equals(portName)) {
            setTimbradoSoapEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
