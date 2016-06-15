package test.profact;

public class TimbradoSoapProxy implements test.profact.TimbradoSoap {
  private String _endpoint = null;
  private test.profact.TimbradoSoap timbradoSoap = null;
  
  public TimbradoSoapProxy() {
    _initTimbradoSoapProxy();
  }
  
  public TimbradoSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initTimbradoSoapProxy();
  }
  
  private void _initTimbradoSoapProxy() {
    try {
      timbradoSoap = (new test.profact.TimbradoLocator()).getTimbradoSoap();
      if (timbradoSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)timbradoSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)timbradoSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (timbradoSoap != null)
      ((javax.xml.rpc.Stub)timbradoSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public test.profact.TimbradoSoap getTimbradoSoap() {
    if (timbradoSoap == null)
      _initTimbradoSoapProxy();
    return timbradoSoap;
  }
  
  public java.lang.Object[] registraEmisor(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, java.lang.String base64Cer, java.lang.String base64Key, java.lang.String contrasena) throws java.rmi.RemoteException{
    if (timbradoSoap == null)
      _initTimbradoSoapProxy();
    return timbradoSoap.registraEmisor(usuarioIntegrador, rfcEmisor, base64Cer, base64Key, contrasena);
  }
  
  public java.lang.Object[] timbraRetencion(java.lang.String usuarioIntegrador, java.lang.String xmlComprobanteBase64, java.lang.String idComprobante) throws java.rmi.RemoteException{
    if (timbradoSoap == null)
      _initTimbradoSoapProxy();
    return timbradoSoap.timbraRetencion(usuarioIntegrador, xmlComprobanteBase64, idComprobante);
  }
  
  public java.lang.Object[] asignaTimbresEmisor(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, int noTimbres) throws java.rmi.RemoteException{
    if (timbradoSoap == null)
      _initTimbradoSoapProxy();
    return timbradoSoap.asignaTimbresEmisor(usuarioIntegrador, rfcEmisor, noTimbres);
  }
  
  public java.lang.Object[] obtieneTimbresDisponibles(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor) throws java.rmi.RemoteException{
    if (timbradoSoap == null)
      _initTimbradoSoapProxy();
    return timbradoSoap.obtieneTimbresDisponibles(usuarioIntegrador, rfcEmisor);
  }
  
  public java.lang.Object[] timbraCFDI(java.lang.String usuarioIntegrador, java.lang.String xmlComprobanteBase64, java.lang.String idComprobante) throws java.rmi.RemoteException{
    if (timbradoSoap == null)
      _initTimbradoSoapProxy();
    return timbradoSoap.timbraCFDI(usuarioIntegrador, xmlComprobanteBase64, idComprobante);
  }
  
  public java.lang.Object[] cancelaCFDI(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, java.lang.String folioUUID) throws java.rmi.RemoteException{
    if (timbradoSoap == null)
      _initTimbradoSoapProxy();
    return timbradoSoap.cancelaCFDI(usuarioIntegrador, rfcEmisor, folioUUID);
  }
  
  public java.lang.Object[] cancelaCFDIAck(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, java.lang.String folioUUID) throws java.rmi.RemoteException{
    if (timbradoSoap == null)
      _initTimbradoSoapProxy();
    return timbradoSoap.cancelaCFDIAck(usuarioIntegrador, rfcEmisor, folioUUID);
  }
  
  public java.lang.Object[] cancelaRetencion(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, java.lang.String folioUUID) throws java.rmi.RemoteException{
    if (timbradoSoap == null)
      _initTimbradoSoapProxy();
    return timbradoSoap.cancelaRetencion(usuarioIntegrador, rfcEmisor, folioUUID);
  }
  
  public java.lang.Object[] obtieneCFDI(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, java.lang.String folioUUID) throws java.rmi.RemoteException{
    if (timbradoSoap == null)
      _initTimbradoSoapProxy();
    return timbradoSoap.obtieneCFDI(usuarioIntegrador, rfcEmisor, folioUUID);
  }
  
  public java.lang.Object[] obtieneRetencion(java.lang.String usuarioIntegrador, java.lang.String rfcEmisor, java.lang.String folioUUID) throws java.rmi.RemoteException{
    if (timbradoSoap == null)
      _initTimbradoSoapProxy();
    return timbradoSoap.obtieneRetencion(usuarioIntegrador, rfcEmisor, folioUUID);
  }
  
  
}