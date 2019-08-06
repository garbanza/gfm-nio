
package pac1.test;

import java.math.BigDecimal;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.3.0-SNAPSHOT
 * Generated source version: 2.2
 * 
 */
@WebService(name = "TimbradoSoap", targetNamespace = "http://tempuri.org/")
@XmlSeeAlso({
    ObjectFactory.class
})
public interface TimbradoSoap {


    /**
     * Servicio de aceptación-rechazo de peticiones de cancelación
     * 
     * @param rfcReceptor
     * @param accion
     * @param usuarioIntegrador
     * @param folioUUID
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "AceptaRechazaCFDI", action = "http://tempuri.org/AceptaRechazaCFDI")
    @WebResult(name = "AceptaRechazaCFDIResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "AceptaRechazaCFDI", targetNamespace = "http://tempuri.org/", className = "pac1.test.AceptaRechazaCFDI")
    @ResponseWrapper(localName = "AceptaRechazaCFDIResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.AceptaRechazaCFDIResponse")
    public ArrayOfAnyType aceptaRechazaCFDI(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "rfcReceptor", targetNamespace = "http://tempuri.org/")
        String rfcReceptor,
        @WebParam(name = "folioUUID", targetNamespace = "http://tempuri.org/")
        String folioUUID,
        @WebParam(name = "accion", targetNamespace = "http://tempuri.org/")
        String accion);

    /**
     * Servicio de cancelación CFDI con acuse
     * 
     * @param rfcReceptor
     * @param usuarioIntegrador
     * @param total
     * @param folioUUID
     * @param rfcEmisor
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "CancelaCFDIAckExterno", action = "http://tempuri.org/CancelaCFDIAckExterno")
    @WebResult(name = "CancelaCFDIAckExternoResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "CancelaCFDIAckExterno", targetNamespace = "http://tempuri.org/", className = "pac1.test.CancelaCFDIAckExterno")
    @ResponseWrapper(localName = "CancelaCFDIAckExternoResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.CancelaCFDIAckExternoResponse")
    public ArrayOfAnyType cancelaCFDIAckExterno(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "rfcEmisor", targetNamespace = "http://tempuri.org/")
        String rfcEmisor,
        @WebParam(name = "folioUUID", targetNamespace = "http://tempuri.org/")
        String folioUUID,
        @WebParam(name = "rfcReceptor", targetNamespace = "http://tempuri.org/")
        String rfcReceptor,
        @WebParam(name = "total", targetNamespace = "http://tempuri.org/")
        BigDecimal total);

    /**
     * Servicio de aceptación-rechazo de peticiones de cancelación
     * 
     * @param rfcReceptor
     * @param usuarioIntegrador
     * @param folioUUID
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "ConsultaCfdisRelacionados", action = "http://tempuri.org/ConsultaCfdisRelacionados")
    @WebResult(name = "ConsultaCfdisRelacionadosResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "ConsultaCfdisRelacionados", targetNamespace = "http://tempuri.org/", className = "pac1.test.ConsultaCfdisRelacionados")
    @ResponseWrapper(localName = "ConsultaCfdisRelacionadosResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.ConsultaCfdisRelacionadosResponse")
    public ArrayOfAnyType consultaCfdisRelacionados(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "folioUUID", targetNamespace = "http://tempuri.org/")
        String folioUUID,
        @WebParam(name = "rfcReceptor", targetNamespace = "http://tempuri.org/")
        String rfcReceptor);

    /**
     * Servicio de aceptación-rechazo de peticiones de cancelación
     * 
     * @param usuarioIntegrador
     * @param folioUUID
     * @param rfcEmisor
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "ConsultaCfdisRelacionadosEmisor", action = "http://tempuri.org/ConsultaCfdisRelacionadosEmisor")
    @WebResult(name = "ConsultaCfdisRelacionadosEmisorResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "ConsultaCfdisRelacionadosEmisor", targetNamespace = "http://tempuri.org/", className = "pac1.test.ConsultaCfdisRelacionadosEmisor")
    @ResponseWrapper(localName = "ConsultaCfdisRelacionadosEmisorResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.ConsultaCfdisRelacionadosEmisorResponse")
    public ArrayOfAnyType consultaCfdisRelacionadosEmisor(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "folioUUID", targetNamespace = "http://tempuri.org/")
        String folioUUID,
        @WebParam(name = "rfcEmisor", targetNamespace = "http://tempuri.org/")
        String rfcEmisor);

    /**
     * Servicio de consulta de estatus de cfdi en el Sat.
     * 
     * @param usuarioIntegrador
     * @param folioUUID
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "ConsultaEstatusSat", action = "http://tempuri.org/ConsultaEstatusSat")
    @WebResult(name = "ConsultaEstatusSatResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "ConsultaEstatusSat", targetNamespace = "http://tempuri.org/", className = "pac1.test.ConsultaEstatusSat")
    @ResponseWrapper(localName = "ConsultaEstatusSatResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.ConsultaEstatusSatResponse")
    public ArrayOfAnyType consultaEstatusSat(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "folioUUID", targetNamespace = "http://tempuri.org/")
        String folioUUID);

    /**
     * Servicio de consulta de estatus de cfdi.
     * 
     * @param rfcReceptor
     * @param usuarioIntegrador
     * @param total
     * @param folioUUID
     * @param rfcEmisor
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "ConsultaEstatusSatExterno", action = "http://tempuri.org/ConsultaEstatusSatExterno")
    @WebResult(name = "ConsultaEstatusSatExternoResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "ConsultaEstatusSatExterno", targetNamespace = "http://tempuri.org/", className = "pac1.test.ConsultaEstatusSatExterno")
    @ResponseWrapper(localName = "ConsultaEstatusSatExternoResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.ConsultaEstatusSatExternoResponse")
    public ArrayOfAnyType consultaEstatusSatExterno(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "folioUUID", targetNamespace = "http://tempuri.org/")
        String folioUUID,
        @WebParam(name = "rfcEmisor", targetNamespace = "http://tempuri.org/")
        String rfcEmisor,
        @WebParam(name = "rfcReceptor", targetNamespace = "http://tempuri.org/")
        String rfcReceptor,
        @WebParam(name = "total", targetNamespace = "http://tempuri.org/")
        BigDecimal total);

    /**
     * Servicio de consulta de peticiones pendientes de cancelación
     * 
     * @param rfcReceptor
     * @param usuarioIntegrador
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "ConsultaPeticionesPendientesCFDI", action = "http://tempuri.org/ConsultaPeticionesPendientesCFDI")
    @WebResult(name = "ConsultaPeticionesPendientesCFDIResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "ConsultaPeticionesPendientesCFDI", targetNamespace = "http://tempuri.org/", className = "pac1.test.ConsultaPeticionesPendientesCFDI")
    @ResponseWrapper(localName = "ConsultaPeticionesPendientesCFDIResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.ConsultaPeticionesPendientesCFDIResponse")
    public ArrayOfAnyType consultaPeticionesPendientesCFDI(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "rfcReceptor", targetNamespace = "http://tempuri.org/")
        String rfcReceptor);

    /**
     * Servicio de asignación de timbres para Integradores
     * 
     * @param usuarioIntegrador
     * @param noTimbres
     * @param rfcEmisor
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "AsignaTimbresEmisor", action = "http://tempuri.org/AsignaTimbresEmisor")
    @WebResult(name = "AsignaTimbresEmisorResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "AsignaTimbresEmisor", targetNamespace = "http://tempuri.org/", className = "pac1.test.AsignaTimbresEmisor")
    @ResponseWrapper(localName = "AsignaTimbresEmisorResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.AsignaTimbresEmisorResponse")
    public ArrayOfAnyType asignaTimbresEmisor(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "rfcEmisor", targetNamespace = "http://tempuri.org/")
        String rfcEmisor,
        @WebParam(name = "noTimbres", targetNamespace = "http://tempuri.org/")
        int noTimbres);

    /**
     * Servicio de cancelación CFDI
     * 
     * @param usuarioIntegrador
     * @param folioUUID
     * @param rfcEmisor
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "CancelaCFDI", action = "http://tempuri.org/CancelaCFDI")
    @WebResult(name = "CancelaCFDIResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "CancelaCFDI", targetNamespace = "http://tempuri.org/", className = "pac1.test.CancelaCFDI")
    @ResponseWrapper(localName = "CancelaCFDIResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.CancelaCFDIResponse")
    public ArrayOfAnyType cancelaCFDI(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "rfcEmisor", targetNamespace = "http://tempuri.org/")
        String rfcEmisor,
        @WebParam(name = "folioUUID", targetNamespace = "http://tempuri.org/")
        String folioUUID);

    /**
     * Servicio de cancelación CFDI con acuse
     * 
     * @param usuarioIntegrador
     * @param folioUUID
     * @param rfcEmisor
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "CancelaCFDIAck", action = "http://tempuri.org/CancelaCFDIAck")
    @WebResult(name = "CancelaCFDIAckResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "CancelaCFDIAck", targetNamespace = "http://tempuri.org/", className = "pac1.test.CancelaCFDIAck")
    @ResponseWrapper(localName = "CancelaCFDIAckResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.CancelaCFDIAckResponse")
    public ArrayOfAnyType cancelaCFDIAck(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "rfcEmisor", targetNamespace = "http://tempuri.org/")
        String rfcEmisor,
        @WebParam(name = "folioUUID", targetNamespace = "http://tempuri.org/")
        String folioUUID);

    /**
     * Servicio de cancelación de retenciones y pagos
     * 
     * @param usuarioIntegrador
     * @param folioUUID
     * @param rfcEmisor
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "CancelaRetencion", action = "http://tempuri.org/CancelaRetencion")
    @WebResult(name = "CancelaRetencionResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "CancelaRetencion", targetNamespace = "http://tempuri.org/", className = "pac1.test.CancelaRetencion")
    @ResponseWrapper(localName = "CancelaRetencionResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.CancelaRetencionResponse")
    public ArrayOfAnyType cancelaRetencion(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "rfcEmisor", targetNamespace = "http://tempuri.org/")
        String rfcEmisor,
        @WebParam(name = "folioUUID", targetNamespace = "http://tempuri.org/")
        String folioUUID);

    /**
     * Servicio de consulta CFDI
     * 
     * @param usuarioIntegrador
     * @param folioUUID
     * @param rfcEmisor
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "ObtieneCFDI", action = "http://tempuri.org/ObtieneCFDI")
    @WebResult(name = "ObtieneCFDIResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "ObtieneCFDI", targetNamespace = "http://tempuri.org/", className = "pac1.test.ObtieneCFDI")
    @ResponseWrapper(localName = "ObtieneCFDIResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.ObtieneCFDIResponse")
    public ArrayOfAnyType obtieneCFDI(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "rfcEmisor", targetNamespace = "http://tempuri.org/")
        String rfcEmisor,
        @WebParam(name = "folioUUID", targetNamespace = "http://tempuri.org/")
        String folioUUID);

    /**
     * Servicio de consulta retenciones y pagos
     * 
     * @param usuarioIntegrador
     * @param folioUUID
     * @param rfcEmisor
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "ObtieneRetencion", action = "http://tempuri.org/ObtieneRetencion")
    @WebResult(name = "ObtieneRetencionResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "ObtieneRetencion", targetNamespace = "http://tempuri.org/", className = "pac1.test.ObtieneRetencion")
    @ResponseWrapper(localName = "ObtieneRetencionResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.ObtieneRetencionResponse")
    public ArrayOfAnyType obtieneRetencion(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "rfcEmisor", targetNamespace = "http://tempuri.org/")
        String rfcEmisor,
        @WebParam(name = "folioUUID", targetNamespace = "http://tempuri.org/")
        String folioUUID);

    /**
     * Servicio de consulta de timbres para Integradores
     * 
     * @param usuarioIntegrador
     * @param rfcEmisor
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "ObtieneTimbresDisponibles", action = "http://tempuri.org/ObtieneTimbresDisponibles")
    @WebResult(name = "ObtieneTimbresDisponiblesResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "ObtieneTimbresDisponibles", targetNamespace = "http://tempuri.org/", className = "pac1.test.ObtieneTimbresDisponibles")
    @ResponseWrapper(localName = "ObtieneTimbresDisponiblesResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.ObtieneTimbresDisponiblesResponse")
    public ArrayOfAnyType obtieneTimbresDisponibles(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "rfcEmisor", targetNamespace = "http://tempuri.org/")
        String rfcEmisor);

    /**
     * Servicio de registro para emisores
     * 
     * @param usuarioIntegrador
     * @param contrasena
     * @param base64Key
     * @param base64Cer
     * @param rfcEmisor
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "RegistraEmisor", action = "http://tempuri.org/RegistraEmisor")
    @WebResult(name = "RegistraEmisorResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "RegistraEmisor", targetNamespace = "http://tempuri.org/", className = "pac1.test.RegistraEmisor")
    @ResponseWrapper(localName = "RegistraEmisorResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.RegistraEmisorResponse")
    public ArrayOfAnyType registraEmisor(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "rfcEmisor", targetNamespace = "http://tempuri.org/")
        String rfcEmisor,
        @WebParam(name = "base64Cer", targetNamespace = "http://tempuri.org/")
        String base64Cer,
        @WebParam(name = "base64Key", targetNamespace = "http://tempuri.org/")
        String base64Key,
        @WebParam(name = "contrasena", targetNamespace = "http://tempuri.org/")
        String contrasena);

    /**
     * Servicio de timbrado para Integradores
     * 
     * @param usuarioIntegrador
     * @param xmlComprobanteBase64
     * @param idComprobante
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "TimbraCFDI", action = "http://tempuri.org/TimbraCFDI")
    @WebResult(name = "TimbraCFDIResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "TimbraCFDI", targetNamespace = "http://tempuri.org/", className = "pac1.test.TimbraCFDI")
    @ResponseWrapper(localName = "TimbraCFDIResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.TimbraCFDIResponse")
    public ArrayOfAnyType timbraCFDI(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "xmlComprobanteBase64", targetNamespace = "http://tempuri.org/")
        String xmlComprobanteBase64,
        @WebParam(name = "idComprobante", targetNamespace = "http://tempuri.org/")
        String idComprobante);

    /**
     * Servicio de timbrado de retenciones y pagos
     * 
     * @param usuarioIntegrador
     * @param xmlComprobanteBase64
     * @param idComprobante
     * @return
     *     returns pac1.test.ArrayOfAnyType
     */
    @WebMethod(operationName = "TimbraRetencion", action = "http://tempuri.org/TimbraRetencion")
    @WebResult(name = "TimbraRetencionResult", targetNamespace = "http://tempuri.org/")
    @RequestWrapper(localName = "TimbraRetencion", targetNamespace = "http://tempuri.org/", className = "pac1.test.TimbraRetencion")
    @ResponseWrapper(localName = "TimbraRetencionResponse", targetNamespace = "http://tempuri.org/", className = "pac1.test.TimbraRetencionResponse")
    public ArrayOfAnyType timbraRetencion(
        @WebParam(name = "usuarioIntegrador", targetNamespace = "http://tempuri.org/")
        String usuarioIntegrador,
        @WebParam(name = "xmlComprobanteBase64", targetNamespace = "http://tempuri.org/")
        String xmlComprobanteBase64,
        @WebParam(name = "idComprobante", targetNamespace = "http://tempuri.org/")
        String idComprobante);

}
