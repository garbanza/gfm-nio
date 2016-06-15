/**
 * TimbradoSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.profact;

public interface TimbradoSoap extends java.rmi.Remote {

    /**
     * Servicio de registro para emisores
     */
    public java.lang.Object[] registraEmisor(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, java.lang.String base64Cer, java.lang.String base64Key, java.lang.String contrasena) throws java.rmi.RemoteException;

    /**
     * Servicio de timbrado de retenciones y pagos
     */
    public java.lang.Object[] timbraRetencion(java.lang.String usuarioIntegrador, java.lang.String xmlComprobanteBase64, java.lang.String idComprobante) throws java.rmi.RemoteException;

    /**
     * Servicio de asignación de timbres para Integradores
     */
    public java.lang.Object[] asignaTimbresEmisor(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, int noTimbres) throws java.rmi.RemoteException;

    /**
     * Servicio de consulta de timbres para Integradores
     */
    public java.lang.Object[] obtieneTimbresDisponibles(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor) throws java.rmi.RemoteException;

    /**
     * Servicio de timbrado para Integradores
     */
    public java.lang.Object[] timbraCFDI(java.lang.String usuarioIntegrador, java.lang.String xmlComprobanteBase64, java.lang.String idComprobante) throws java.rmi.RemoteException;

    /**
     * Servicio de cancelación CFDI
     */
    public java.lang.Object[] cancelaCFDI(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, java.lang.String folioUUID) throws java.rmi.RemoteException;

    /**
     * Servicio de cancelación CFDI con acuse
     */
    public java.lang.Object[] cancelaCFDIAck(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, java.lang.String folioUUID) throws java.rmi.RemoteException;

    /**
     * Servicio de cancelación de retenciones y pagos
     */
    public java.lang.Object[] cancelaRetencion(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, java.lang.String folioUUID) throws java.rmi.RemoteException;

    /**
     * Servicio de consulta CFDI
     */
    public java.lang.Object[] obtieneCFDI(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, java.lang.String folioUUID) throws java.rmi.RemoteException;

    /**
     * Servicio de consulta retenciones y pagos
     */
    public java.lang.Object[] obtieneRetencion(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, java.lang.String folioUUID) throws java.rmi.RemoteException;
}
