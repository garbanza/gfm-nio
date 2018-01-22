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

   <title>Factura Electronica <xsl:value-of select="@Serie"/><xsl:value-of select="@Folio"/></title>
   </head>
   <body>
	<table class="headTable">
	<!--tr><th class="emitData"></th><th class="emitData"></th></tr-->
	<tr>
		<td class="emitData">
		<h4>Factura Electronica: <xsl:value-of select="@Serie"/>-<xsl:value-of select="@Folio"/> </h4>
		<span class="bold">Lugar, fecha y hora de emisión</span>: c.p. <xsl:value-of select="@LugarExpedicion"/>, <xsl:value-of select="@Fecha"/><br/>
		<span class="bold">Folio fiscal</span>:<xsl:value-of select="//tfd:TimbreFiscalDigital/@UUID"/><br/>
		<span class="bold">Regimen fiscal</span>: <xsl:value-of select="cfdi:Emisor/@RegimenFiscal"/><br/>
		<span class="bold">Emisor</span>: <xsl:value-of select="cfdi:Emisor/@Nombre"/>, RFC <xsl:value-of select="cfdi:Emisor/@Rfc"/>, <xsl:value-of select="$INVOICE_SENDER_ADDITIONAL_DATA"/><br/>
		<!--xsl:apply-templates select="//cfdi:DomicilioFiscal"/><br/-->
		<span class="bold">Receptor</span>: <xsl:value-of select="cfdi:Receptor/@Nombre"/>, RFC <xsl:value-of select="cfdi:Receptor/@Rfc"/>,
		
		</td>
		<td class="emitData">
		<table><tr><td><image src="../enterpricelogo.png" class="enterpricelogo"/></td></tr>
		<tr><td><span class="bold">Total</span>: <xsl:value-of select="@Total"/>&#160;<xsl:value-of select="@Moneda"/><br/>

		</td></tr>
		</table></td>
	</tr>
        </table>
	<table>
             <tr><th class="quantity bold" >Cantidad</th>
		<th class="unit">Unidad</th>
                 <th class="description">Descripción</th>
                 <th class="unitPrice">Precio&#160;<xsl:value-of select="@Moneda"/></th>
                 <th class="price">Importe &#160;<xsl:value-of select="@Moneda"/></th>
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
			<tr><td align="right"><span class="bold">Subtotal</span></td><td align="right"><span class="bold"><xsl:value-of select="@SubTotal"/></span></td></tr>
			<tr><td align="right"><span class="bold">IVA (16%)</span></td><td align="right"><span class="bold"><xsl:value-of select="cfdi:Impuestos/@TotalImpuestosTrasladados"/></span></td></tr>
			<tr><td align="right"><span class="bold">Total</span></td><td align="right"><span class="bold"><xsl:value-of select="@Total"/></span></td></tr>
			</table>
		</td>
		</tr>
	</table>
	<xsl:variable name="Folio" select="@Folio"/>
	<!--table style="table-layout: fixed; font-size: 60%">
			<tr>
			<td class="cert">
				<span class="bold">Sello digital del CFDI</span>: <span><xsl:value-of select="//tfd:TimbreFiscalDigital/@SelloCFD"/></span><br/>
				<span class="bold">Sello del SAT</span>: <span class="wrap"><xsl:value-of select="//tfd:TimbreFiscalDigital/@SelloSAT"/><br/></span>
				<span class="bold">Cadena original del complemento de certificación del SAT</span>:<br/><span class="wrap">
				||<xsl:value-of select="//tfd:TimbreFiscalDigital/@Version"/>|<xsl:value-of select="//tfd:TimbreFiscalDigital/@UUID"/>|<xsl:value-of select="//tfd:TimbreFiscalDigital/@FechaTimbrado"/>|<xsl:value-of select="//tfd:TimbreFiscalDigital/@SelloCFD"/>|<xsl:value-of select="//tfd:TimbreFiscalDigital/@NoCertificadoSAT"/>||
				</span><br/>
				<span class="bold">No de serie del certificado del SAT</span>:<br/><span class="wrap">
					<xsl:value-of select="//tfd:TimbreFiscalDigital/@NoCertificadoSAT"/>
				</span><br/>
				<span class="bold">Fecha y hora de certificación</span>:<br/><span class="wrap">
					<xsl:value-of select="//tfd:TimbreFiscalDigital/@FechaTimbrado"/>
				</span>
				<br/>
				<span class="bold">RFC PAC</span>:<br/><span class="wrap">
					<xsl:value-of select="//tfd:TimbreFiscalDigital/@RfcProvCertif"/>
				</span>
			</td>
			<td class="qrcode"><image src="{$Folio}.qrc.png" width="100%"/></td>
			</tr>
	</table>
	<table class="due"><tr><td>
	<h4>PAGARÉ</h4>
	NOTA: 1) La garantía de nuestros productos la otorga únicamente el fabricante. 2) Cualquier devolución
injustificada de mercancia causará un 20% de cargo. 3) Todo cheque devuelto causará 20% de cargo.<br/>
Por este pagaré debo(emos) y pagaré(mos) incondicionalmente a la orden de <xsl:value-of select="cfdi:Emisor/@Nombre"/> la cantidad de $<xsl:value-of select="@Total"/>
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
	<td align="center" class="quantity"><xsl:value-of select="@Cantidad"/></td>
	<td align="center" class="unit"><xsl:value-of select="@Unidad"/>&#160; <xsl:value-of select="@ClaveUnidad"/></td>
        <td class="description"><xsl:value-of select="@Descripcion"/>&#160; <xsl:value-of select="@ClaveProdServ"/></td>
        <td align="right" class="unitPrice"><xsl:value-of select="@ValorUnitario"/></td>
        <td align="right" class="price"><xsl:value-of select="@Importe"/></td>
    </tr>
	</table>
</xsl:template>
 
<xsl:template match="//cfdi:Traslado">
    <tr><td colspan="2" align="right">$ <xsl:value-of select="@Impuesto"/></td>
        <td align="right"><xsl:value-of select="@Importe"/></td>
        <td><xsl:value-of select="@TasaOCuota"/>%</td>
    </tr>
</xsl:template>
 
<xsl:template match="//tfd:TimbreFiscalDigital">
	<span class="bold">Folio Fiscal</span>: <xsl:value-of select="@UUID"/><br/>
	<span class="bold">Certificado del SAT</span>: <xsl:value-of select="@NoCertificadoSAT"/>
</xsl:template>

</xsl:stylesheet>
