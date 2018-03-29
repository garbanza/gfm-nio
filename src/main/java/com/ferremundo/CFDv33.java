package com.ferremundo;

/*** Eclipse Class Decompiler plugin, copyright (c) 2016 Chen Chao (cnfree2000@hotmail.com) ***/

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.util.JAXBSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import mx.bigdata.sat.cfdi.CFDI33;
import mx.bigdata.sat.cfdi.schema.TimbreFiscalDigital;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante;
import mx.bigdata.sat.cfdi.v33.schema.ObjectFactory;
import mx.bigdata.sat.cfdi.v33.schema.Comprobante.Complemento;
import mx.bigdata.sat.common.ComprobanteBase33;
import mx.bigdata.sat.common.NamespacePrefixMapperImpl;
import mx.bigdata.sat.common.URIResolverImpl;
import mx.bigdata.sat.common.implocal.schema.ImpuestosLocales;
import mx.bigdata.sat.common.nomina.v12.schema.Nomina;
import mx.bigdata.sat.common.pagos.schema.Pagos;
import mx.bigdata.sat.security.KeyLoaderEnumeration;
import mx.bigdata.sat.security.factory.KeyLoaderFactory;
import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ErrorHandler;

public class CFDv33 implements CFDI33 {
	private static final String XSLT = "/xslt/cadenaoriginal_3_3.xslt";
	private static final String[] XSD = new String[] { "/xsd/common/tdCFDI.xsd", "/xsd/common/catCFDI.xsd",
			"/xsd/common/catNomina.xsd", "/xsd/common/catComExt.xsd", "/xsd/v33/cfdv33.xsd",
			"/xsd/v33/TimbreFiscalDigitalv11.xsd", "/xsd/common/ecc/v10/ecc.xsd", "/xsd/common/ecc/v11/ecc11.xsd",
			"/xsd/common/donat/v11/donat11.xsd", "/xsd/common/divisas/divisas.xsd", "/xsd/common/implocal/implocal.xsd",
			"/xsd/common/leyendasFisc/leyendasFisc.xsd", "/xsd/common/pfic/pfic.xsd",
			"/xsd/common/TuristaPasajeroExtranjero/TuristaPasajeroExtranjero.xsd", "/xsd/common/spei/spei.xsd",
			"/xsd/common/detallista/detallista.xsd", "/xsd/common/cfdiregistrofiscal/cfdiregistrofiscal.xsd",
			"/xsd/common/nomina/v11/nomina11.xsd", "/xsd/common/nomina/v12/nomina12.xsd",
			"/xsd/common/pagoenespecie/pagoenespecie.xsd", "/xsd/common/valesdedespensa/valesdedespensa.xsd",
			"/xsd/common/consumodecombustibles/consumodecombustibles.xsd", "/xsd/common/aerolineas/aerolineas.xsd",
			"/xsd/common/notariospublicos/notariospublicos.xsd", "/xsd/common/vehiculousado/vehiculousado.xsd",
			"/xsd/common/servicioparcialconstruccion/servicioparcialconstruccion.xsd",
			"/xsd/common/renovacionysustitucionvehiculos/renovacionysustitucionvehiculos.xsd",
			"/xsd/common/certificadodedestruccion/certificadodedestruccion.xsd",
			"/xsd/common/obrasarteantiguedades/obrasarteantiguedades.xsd", "/xsd/common/ine/v11/INE11.xsd",
			"/xsd/common/ComercioExterior/v10/ComercioExterior10.xsd",
			"/xsd/common/ComercioExterior/v11/ComercioExterior11.xsd", "/xsd/common/catPagos.xsd",
			"/xsd/common/Pagos/Pagos10.xsd", "/xsd/common/iedu/iedu.xsd",
			"/xsd/common/ventavehiculos/v10/ventavehiculos.xsd", "/xsd/common/ventavehiculos/v11/ventavehiculos11.xsd",
			"/xsd/common/terceros/terceros11.xsd", "/xsd/common/AcreditamientoIEPS/AcreditamientoIEPS10.xsd",
			"/xsd/common/ecb/ecb.xsd", "/xsd/common/psgcfdsp/psgcfdsp.xsd", "/xsd/common/psgecfd/psgecfd.xsd" };
	private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	private static final String BASE_CONTEXT = "mx.bigdata.sat.cfdi.v33.schema";
	private static final Joiner JOINER = Joiner.on(':');
	private final JAXBContext context;
	public static final ImmutableMap<String, String> PREFIXES = ImmutableMap.of(
			"http://www.w3.org/2001/XMLSchema-instance", "xsi", "http://www.sat.gob.mx/cfd/3", "cfdi",
			"http://www.sat.gob.mx/TimbreFiscalDigital", "tfd");
	private final Map<String, String> localPrefixes;
	private TransformerFactory tf;
	final Comprobante document;

	public CFDv33(InputStream in, String... contexts) throws Exception {
		this.localPrefixes = Maps.newHashMap(PREFIXES);
		this.context = getContext(contexts);
		this.document = load(in, new String[0]);
	}

	public CFDv33(Comprobante comprobante, String... contexts) throws Exception {
		this.localPrefixes = Maps.newHashMap(PREFIXES);
		this.context = getContext(contexts);
		this.document = this.copy(comprobante);
	}

	public void addNamespace(String uri, String prefix) {
		this.localPrefixes.put(uri, prefix);
	}

	public void setTransformerFactory(TransformerFactory tf) {
		this.tf = tf;
		tf.setURIResolver(new URIResolverImpl());
	}

	public void sellar(PrivateKey key, X509Certificate cert) throws Exception {
		String nc = new String(cert.getSerialNumber().toByteArray());
		cert.checkValidity();
		byte[] bytes = cert.getEncoded();
		Base64 b64 = new Base64(-1);
		String certStr = b64.encodeToString(bytes);
		this.document.setCertificado(certStr);
		this.document.setNoCertificado(nc);
		String signature = this.getSignature(key);
		this.document.setSello(signature);
	}

	public Comprobante sellarComprobante(PrivateKey key, X509Certificate cert) throws Exception {
		this.sellar(key, cert);
		return this.doGetComprobante();
	}

	public void validar() throws Exception {
		this.validar((ErrorHandler) null);
	}

	public void validar(ErrorHandler handler) throws Exception {
		SchemaFactory sf = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
		Source[] schemas = new Source[XSD.length];

		for (int schema = 0; schema < XSD.length; ++schema) {
			schemas[schema] = new StreamSource(this.getClass().getResourceAsStream(XSD[schema]));
		}

		Schema arg5 = sf.newSchema(schemas);
		Validator validator = arg5.newValidator();
		if (handler != null) {
			validator.setErrorHandler(handler);
		}

		validator.validate(new JAXBSource(this.context, this.document));
	}

	public void verificar() throws Exception {
		String certStr = this.document.getCertificado();
		Base64 b64 = new Base64();
		byte[] cbs = b64.decode(certStr);
		X509Certificate cert = (X509Certificate) KeyLoaderFactory
				.createInstance(KeyLoaderEnumeration.PUBLIC_KEY_LOADER, new ByteArrayInputStream(cbs), new String[0])
				.getKey();
		String sigStr = this.document.getSello();
		byte[] signature = b64.decode(sigStr);
		byte[] bytes = this.getOriginalBytes();
		Signature sig = Signature.getInstance("SHA256withRSA");
		sig.initVerify(cert);
		sig.update(bytes);
		boolean bool = sig.verify(signature);
		if (!bool) {
			throw new Exception("Invalid signature");
		}
	}

	public void verificar(InputStream in) throws Exception {
		String certStr = this.document.getCertificado();
		Base64 b64 = new Base64();
		byte[] cbs = b64.decode(certStr);
		X509Certificate cert = (X509Certificate) KeyLoaderFactory
				.createInstance(KeyLoaderEnumeration.PUBLIC_KEY_LOADER, new ByteArrayInputStream(cbs), new String[0])
				.getKey();
		String sigStr = this.document.getSello();
		byte[] signature = b64.decode(sigStr);
		byte[] bytes = this.getOriginalBytes(in);
		Signature sig = Signature.getInstance("SHA256withRSA");
		sig.initVerify(cert);
		sig.update(bytes);
		boolean bool = sig.verify(signature);
		if (!bool) {
			throw new Exception("Invalid signature.");
		}
	}

	public void guardar(OutputStream out) throws Exception {
		Marshaller m = this.context.createMarshaller();
		m.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapperImpl(this.localPrefixes));
		m.setProperty("jaxb.fragment", Boolean.TRUE);
		m.setProperty("jaxb.formatted.output", Boolean.TRUE);
		m.setProperty("jaxb.schemaLocation", this.getSchemaLocation());
		byte[] xmlHeaderBytes = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>".getBytes("UTF8");
		out.write(xmlHeaderBytes);
		m.marshal(this.document, out);
	}

	private String getSchemaLocation() throws Exception {
		ArrayList contexts = new ArrayList();
		String schema = "http://www.sat.gob.mx/cfd/3 http://www.sat.gob.mx/sitio_internet/cfd/3/cfdv33.xsd";
		if (this.document != null && this.document.getComplemento() != null
				&& this.document.getComplemento().size() > 0) {
			Iterator arg2 = this.document.getComplemento().iterator();

			while (arg2.hasNext()) {
				Complemento o = (Complemento) arg2.next();
				Iterator arg4 = o.getAny().iterator();

				while (arg4.hasNext()) {
					Object c = arg4.next();
					if (c instanceof TimbreFiscalDigital) {
						schema = schema
								+ " http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd";
						this.addNamespace("http://www.sat.gob.mx/TimbreFiscalDigital", "tfd");
					} else if (c instanceof Nomina) {
						schema = schema
								+ " http://www.sat.gob.mx/nomina12 http://www.sat.gob.mx/sitio_internet/cfd/nomina/nomina12.xsd";
						this.addNamespace("http://www.sat.gob.mx/nomina12", "nomina12");
					} else if (c instanceof ImpuestosLocales) {
						schema = schema
								+ " http://www.sat.gob.mx/implocal http://www.sat.gob.mx/sitio_internet/cfd/implocal/implocal.xsd";
						this.addNamespace("http://www.sat.gob.mx/implocal", "implocal");
					} else if (c instanceof Pagos) {
						schema = schema
								+ " http://www.sat.gob.mx/Pagos http://www.sat.gob.mx/sitio_internet/cfd/Pagos/Pagos10.xsd";
						this.addNamespace("http://www.sat.gob.mx/Pagos", "pago10");
					} else {
						System.out.println("El complemento " + c + " aún no ha sido declarado.");
					}
				}
			}

			if (!contexts.isEmpty()) {
				getContext((String[]) contexts.toArray(new String[contexts.size()]));
			}
		}

		return schema;
	}

	public String getCadenaOriginal() throws Exception {
		byte[] bytes = this.getOriginalBytes();
		return new String(bytes, "UTF8");
	}

	public static Comprobante newComprobante(InputStream in) throws Exception {
		return load(in, new String[0]);
	}

	byte[] getOriginalBytes() throws Exception {
		JAXBSource in = new JAXBSource(this.context, this.document);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		StreamResult out = new StreamResult(baos);
		TransformerFactory factory = this.tf;
		if (factory == null) {
			factory = TransformerFactory.newInstance();
			factory.setURIResolver(new URIResolverImpl());
		}

		Transformer transformer = factory
				.newTransformer(new StreamSource(this.getClass().getResourceAsStream("/xslt/cadenaoriginal_3_3.xslt")));
		transformer.transform(in, out);
		return baos.toByteArray();
	}

	byte[] getOriginalBytes(InputStream in) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		StreamSource source = new StreamSource(in);
		StreamResult out = new StreamResult(baos);
		TransformerFactory factory = this.tf;
		if (factory == null) {
			factory = TransformerFactory.newInstance();
			factory.setURIResolver(new URIResolverImpl());
		}

		Transformer transformer = factory
				.newTransformer(new StreamSource(this.getClass().getResourceAsStream("/xslt/cadenaoriginal_3_3.xslt")));
		transformer.transform(source, out);
		in.close();
		return baos.toByteArray();
	}

	String getSignature(PrivateKey key) throws Exception {
		byte[] bytes = this.getOriginalBytes();
		Signature sig = Signature.getInstance("SHA256withRSA");
		sig.initSign(key);
		sig.update(bytes);
		byte[] signed = sig.sign();
		Base64 b64 = new Base64(-1);
		return b64.encodeToString(signed);
	}

	public ComprobanteBase33 getComprobante() throws Exception {
		return new CFDv33.CFDv33ComprobanteBase(this.doGetComprobante());
	}

	Comprobante doGetComprobante() throws Exception {
		return this.copy(this.document);
	}

	private Comprobante copy(Comprobante comprobante) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.newDocument();
		Marshaller m = this.context.createMarshaller();
		m.marshal(comprobante, doc);
		Unmarshaller u = this.context.createUnmarshaller();
		return (Comprobante) u.unmarshal(doc);
	}

	private static JAXBContext getContext(String[] contexts) throws Exception {
		List ctx = Lists.asList("mx.bigdata.sat.cfdi.v33.schema", contexts);
		return JAXBContext.newInstance(JOINER.join(ctx));
	}

	private static Comprobante load(InputStream source, String... contexts) throws Exception {
		JAXBContext context = getContext(contexts);

		Comprobante arg3;
		try {
			Unmarshaller u = context.createUnmarshaller();
			arg3 = (Comprobante) u.unmarshal(source);
		} finally {
			source.close();
		}

		return arg3;
	}

	static void dump(String title, byte[] bytes, PrintStream out) {
		out.printf("%s: ", new Object[] { title });
		byte[] arg2 = bytes;
		int arg3 = bytes.length;

		for (int arg4 = 0; arg4 < arg3; ++arg4) {
			byte b = arg2[arg4];
			out.printf("%02x ", new Object[] { Integer.valueOf(b & 255) });
		}

		out.println();
	}

	public static final class CFDv33ComprobanteBase implements ComprobanteBase33 {
		private final Comprobante document;

		public CFDv33ComprobanteBase(Comprobante document) {
			this.document = document;
		}

		public boolean hasComplemento() {
			return this.document.getComplemento() != null;
		}

		public List<Complemento> getComplementoGetAny() {
			return this.document.getComplemento();
		}

		public String getSello() {
			return this.document.getSello();
		}

		public void setComplemento(Element element) {
			ObjectFactory of = new ObjectFactory();
			Complemento comp = of.createComprobanteComplemento();
			List list = comp.getAny();
			list.add(element);
			this.document.getComplemento().add(comp);
		}

		public Object getComprobante() {
			return this.document;
		}
	}
}
