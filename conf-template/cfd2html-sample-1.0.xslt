<xsl:stylesheet version = '1.0'
    xmlns:xsl='http://www.w3.org/1999/XSL/Transform'
    xmlns:cfdi='http://www.sat.gob.mx/cfd/3'
	xmlns:tfd="http://www.sat.gob.mx/TimbreFiscalDigital">
 
<xsl:output method = "html" /> 

<xsl:template match="//cfdi:Comprobante">
<xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html&gt;</xsl:text>
   <html>
   <head>
   <link rel="STYLESHEET" media="screen" type="text/css" href="../invoice.css"/>

   <title>Factura Electronica <xsl:value-of select="@serie"/><xsl:value-of select="@folio"/></title>
   </head>
   <body>
	<table class="headTable">
	<!--tr><th class="emitData"></th><th class="emitData"></th></tr-->
	<tr>
		<td class="emitData">
		<h4>Cotizacion: <xsl:value-of select="@folio"/> </h4>
		<span class="bold">Lugar, fecha y hora de emisión</span>: <xsl:value-of select="//cfdi:DomicilioFiscal/@municipio"/>, <xsl:value-of select="//cfdi:DomicilioFiscal/@estado"/>, <xsl:value-of select="@fecha"/><br/>
		<!--span class="bold">Folio fiscal</span>:<xsl:value-of select="//tfd:TimbreFiscalDigital/@UUID"/><br/-->
		<!--span class="bold">Regimen fiscal</span>: <xsl:value-of select="//cfdi:RegimenFiscal/@Regimen"/><br/-->
		<span class="bold" id="emitter">Emisor</span>: RFC <xsl:value-of select="cfdi:Emisor/@rfc"/>, <xsl:value-of select="cfdi:Emisor/@nombre"/>,
		<xsl:apply-templates select="//cfdi:DomicilioFiscal"/><br/>
		<xsl:value-of select="$INVOICE_SENDER_ADDITIONAL_DATA"/><br/>
		<span class="bold" id="receiver">Receptor</span>: RFC <xsl:value-of select="cfdi:Receptor/@rfc"/>, <xsl:value-of select="cfdi:Receptor/@nombre"/>,
		<xsl:apply-templates select="//cfdi:Domicilio"/>
		</td>
		<td class="emitData">
		<table><tr><td><image src="../enterpricelogo.png" class="enterpricelogo"/></td></tr>
		<tr><td><span class="bold">Total</span>: $<xsl:value-of select="@total"/><br/>
			<!--span class="bold">Método de pago</span>: <xsl:value-of select="@metodoDePago"/><br/>
			<span class="bold">Forma de pago</span>: <xsl:value-of select="@formaDePago"/><br/>
			<span class="bold">Cuenta de pago</span>: <xsl:value-of select="@NumCtaPago"/--></td></tr>
		</table></td>
	</tr>
        </table>
	<table>
             <tr><th class="quantity bold" >Cantidad</th>
		<th class="unit">Unidad</th>
                 <th class="description">Descripción</th>
                 <th class="unitPrice">Precio $</th>
                 <th class="price">Importe $</th>
             </tr>
        </table>
	<xsl:apply-templates select="//cfdi:Concepto"/>
             <xsl:for-each select="Concepto">
        </xsl:for-each>
	<table class="tablenoborder">
		<tr>
		<td class="totalblank"></td>
		<td class="total">
			<table>
			<tr><td align="right"><span class="bold">Subtotal</span></td><td align="right"><span class="bold"><xsl:value-of select="@subTotal"/></span></td></tr>
			<tr><td align="right"><span class="bold">IVA (16%)</span></td><td align="right"><span class="bold"><xsl:value-of select="cfdi:Impuestos/@totalImpuestosTrasladados"/></span></td></tr>
			<tr><td align="right"><span class="bold">Total</span></td><td align="right"><span class="bold"><xsl:value-of select="@total"/></span></td></tr>
			</table>
		</td>
		</tr>
	</table>
	<xsl:variable name="folio" select="@folio"/>
	<!--table style="table-layout: fixed; font-size: 60%">
			<tr>
			<td class="cert">
				<span class="bold">Sello digital del CFDI</span>: <span><xsl:value-of select="//tfd:TimbreFiscalDigital/@selloCFD"/></span><br/>
				<span class="bold">Sello del SAT</span>: <span class="wrap"><xsl:value-of select="//tfd:TimbreFiscalDigital/@selloSAT"/><br/></span>
				<span class="bold">Cadena original del complemento de certificación del SAT</span>:<br/><span class="wrap">
				||<xsl:value-of select="//tfd:TimbreFiscalDigital/@version"/>|<xsl:value-of select="//tfd:TimbreFiscalDigital/@UUID"/>|<xsl:value-of select="//tfd:TimbreFiscalDigital/@FechaTimbrado"/>|<xsl:value-of select="//tfd:TimbreFiscalDigital/@selloCFD"/>|<xsl:value-of select="//tfd:TimbreFiscalDigital/@noCertificadoSAT"/>||
				</span><br/>
				<span class="bold">No de serie del certificado del SAT</span>:<br/><span class="wrap">
					<xsl:value-of select="//tfd:TimbreFiscalDigital/@noCertificadoSAT"/>
				</span><br/>
				<span class="bold">Fecha y hora de certificación</span>:<br/><span class="wrap">
					<xsl:value-of select="//tfd:TimbreFiscalDigital/@FechaTimbrado"/>
				</span>
			</td>
			<td class="qrcode"><image src="{$folio}.qrc.png" width="100%"/></td>
			</tr>
	</table-->
	<!--table class="due"><tr><td>
	<h4>PAGARÉ</h4>
	NOTA: 1) La garantía de nuestros productos la otorga únicamente el fabricante. 2) Cualquier devolución
injustificada de mercancia causará un 20% de cargo. 3) Todo cheque devuelto causará 20% de cargo.<br/>
Por este pagaré debo(emos) y pagaré(mos) incondicionalmente a la orden de <xsl:value-of select="cfdi:Emisor/@nombre"/> la cantidad de $<xsl:value-of select="@total"/>
en Morelia, Michoacan o en el lugar que se me(nos) requiera. La falta de pago de dicha cantidad causará
intereses moratorios a partir de 30 días cumplidos despues de la fecha <xsl:value-of select="//tfd:TimbreFiscalDigital/@FechaTimbrado"/> a razón del _____%
mensual sobre su importe (total o parcial) en su caso, mismos que pagaré(mos) sin excusa ni pretexto,
así como los gastos judiciales y/o extrajudiciales que lo ameriten. La firma puesta en cualquier lugar se
considera como aceptación de dichas condiciones.	
        </td></tr>
	<tr><td>
		<table><tr><td><span style="display: block; height: 100px">Acepto(amos) (nombre y firma)</span></td><td><span style="display: block; height: 100px">Acepto(amos) (nombre y firma)</span></td><td><span style="display: block; height: 100px">Acepto(amos) (nombre y firma)</span></td></tr></table>
	</td></tr>
        </table-->
        <!--center>
        <Este documento es una representación impresa de un CFDI>
        </center-->
    </body>
    </html>
</xsl:template>
 
 
<xsl:template match="//cfdi:DomicilioFiscal">
    Domicilio: 
    <xsl:value-of select="@calle"/> # <xsl:value-of select="@noExterior"/> - <xsl:value-of select="@noInterior"/>,
    <xsl:value-of select="@colonia"/>,
    <xsl:value-of select="@municipio"/>,
     <xsl:value-of select="@estado"/>,
     <xsl:value-of select="@pais"/>.
    <xsl:value-of select="@codigoPostal"/>,
     <xsl:value-of select="@referencia"/>.
</xsl:template>
 
<xsl:template match="//cfdi:Domicilio">
    Domicilio: 
    <xsl:value-of select="@calle"/> # <xsl:value-of select="@noExterior"/> - <xsl:value-of select="@noInterior"/>,
    <xsl:value-of select="@colonia"/>,
    <xsl:value-of select="@municipio"/>,
     <xsl:value-of select="@estado"/>,
     <xsl:value-of select="@pais"/>,
    <xsl:value-of select="@codigoPostal"/><br/>
     Referencia: <xsl:value-of select="@referencia"/>.
</xsl:template>
 
<xsl:template match="//cfdi:Concepto">
	<table>    
	<tr>
	<td align="center" class="quantity"><xsl:value-of select="@cantidad"/></td>
	<td align="center" class="unit"><xsl:value-of select="@unidad"/></td>
        <td class="description"><xsl:value-of select="@descripcion"/></td>
        <td align="right" class="unitPrice"><xsl:value-of select="@valorUnitario"/></td>
        <td align="right" class="price"><xsl:value-of select="@importe"/></td>
    </tr>
	</table>
</xsl:template>
 
<xsl:template match="//cfdi:Traslado">
    <tr><td colspan="2" align="right">$ <xsl:value-of select="@impuesto"/></td>
        <td align="right"><xsl:value-of select="@importe"/></td>
        <td><xsl:value-of select="@tasa"/>%</td>
    </tr>
</xsl:template>
 
<xsl:template match="//tfd:TimbreFiscalDigital">
	<span class="bold">Folio Fiscal</span>: <xsl:value-of select="@UUID"/><br/>
	<span class="bold">Certificado del SAT</span>: <xsl:value-of select="@noCertificadoSAT"/>
</xsl:template>

</xsl:stylesheet>
