package com.ferremundo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import javax.net.ssl.HttpsURLConnection;
import javax.persistence.EntityManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ferremundo.gth.InvoiceRow;
import com.google.gson.Gson;
import com.ibm.icu.text.DecimalFormat;
import com.lowagie.text.Rectangle;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.ConnectionConfigurator;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;

import wf.bitcoin.javabitcoindrpcclient.BitcoinJSONRPCClient;
import wf.bitcoin.javabitcoindrpcclient.BitcoinRPCException;
import wf.bitcoin.javabitcoindrpcclient.BitcoinRawTxBuilder;
import wf.bitcoin.javabitcoindrpcclient.BitcoinRpcException;
import wf.bitcoin.javabitcoindrpcclient.BitcoinUtil;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient.PeerInfoResult;
import wf.bitcoin.javabitcoindrpcclient.BitcoindRpcClient.TxInput;
import wf.bitcoin.krotjson.Base64Coder;

public class tst {

	public static void main2(String[] args) {
		double myDouble = 2.673;
		DecimalFormat myFormat = new DecimalFormat("0.000000");
		String myDoubleString = myFormat.format(myDouble);
		System.out.println("My number is: " + myDoubleString);
	}

	public static void main(String[] args) {
		// String escapeUtils =
		// StringEscapeUtils.escapeJava("\u002758cc15e4-4c2a-4f34-8923-2a5990360e0b\u0027");
		// System.out.println("-"+escapeUtils+"-");
		//blockchain();
		String[] emails = "1".split(" ");
		String[] emails1 = "1".split(" ");
		String[] emails2 = "1".split(" ");
		for(int j = 0; j < emails.length; j++){
			for(int k = 0; k < emails1.length; k++){
				if(! emails[j].equals(emails1[k])) emails = (String[])ArrayUtils.add(emails,emails1[k]);
			}
			for(int k = 0; k < emails2.length; k++){
				if(! emails[j].equals(emails2[k])) emails = (String[])ArrayUtils.add(emails,emails2[k]);
			}
		}
	}

	public static void main1(String[] args) throws ParserConfigurationException, SAXException, IOException {
		String cfdiResponse = "<xml><codigo>0</codigo><str>&lt;merga&gt;</str></xml>";
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new ByteArrayInputStream(cfdiResponse.getBytes()));
		NodeList nlist = doc.getElementsByTagName("str");
		System.out.println(StringEscapeUtils.unescapeXml(nlist.item(0).getTextContent()));
	}

	private void meth() {
		File dir = new File("/home/dog/FERREMUNDO/pedidos/");
		String[] children = dir.list();
		if (children == null) {
			// Either dir does not exist or is not a directory
		} else {
			boolean contin = true;
			for (int i = 0; i < children.length; i++) {
				// Get filename of file or directory
				if (!contin && children[i].endsWith(".csv")) {
					System.out.println(children[i]);
					break;
				}
				if (children[i].equals("LKD.pdf.csv"))
					contin = false;
				if (children[i].endsWith(".csv")) {
					String path = dir.getPath() + "/" + children[i];
					File file = new File(path);
					Date date = new Date(file.lastModified());
					String dates = ((DateFormat) (new SimpleDateFormat("dd/MM/yyyy")))
							.format(new Date((new Date(file.lastModified())).getTime()));
					System.out.println(dir.getPath() + "/" + children[i] + " -> " + dates);

				}
			}
		}
		// listStore=em.createNativeQuery("select * from
		// Product",Product.class).getResultList();
		/*
		 * for(int i=0;i<20;i++){ EntityManager
		 * emis=EMF.get(EMF.UNIT_INVOICEFM01).createEntityManager();
		 * emis.getTransaction().begin(); InvoiceFM01 invoice =
		 * (InvoiceFM01)emis.
		 * createNativeQuery("select * from InvoiceFM01 x where x.reference like 'N5N'"
		 * ,InvoiceFM01.class).getResultList().get(0); emis.close();
		 * System.out.println("open?"+emis.isOpen());
		 * System.out.println(invoice.getJson());
		 * System.out.println(LevenshteinDistance.LD("ab","AB"));
		 * System.out.println("add".startsWith(""));
		 * 
		 * }
		 */

	}

	/*
	 * public Object query(String method, Object... o) throws
	 * BitcoinRpcException { HttpURLConnection conn; try { conn =
	 * (HttpURLConnection) noAuthURL.openConnection();
	 * 
	 * conn.setDoOutput(true); conn.setDoInput(true);
	 * 
	 * if (conn instanceof HttpsURLConnection) { if (hostnameVerifier != null)
	 * ((HttpsURLConnection) conn).setHostnameVerifier(hostnameVerifier); if
	 * (sslSocketFactory != null) ((HttpsURLConnection)
	 * conn).setSSLSocketFactory(sslSocketFactory); }
	 * 
	 * // conn.connect(); ((HttpURLConnection)
	 * conn).setRequestProperty("Authorization", "Basic " + authStr); byte[] r =
	 * prepareRequest(method, o); logger.log(Level.FINE,
	 * "Bitcoin JSON-RPC request:\n{0}", new String(r, QUERY_CHARSET));
	 * conn.getOutputStream().write(r); conn.getOutputStream().close(); int
	 * responseCode = conn.getResponseCode(); if (responseCode != 200) throw new
	 * BitcoinRPCException(method, Arrays.deepToString(o), responseCode,
	 * conn.getResponseMessage(), new String(loadStream(conn.getErrorStream(),
	 * true))); return loadResponse(conn.getInputStream(), "1", true); } catch
	 * (IOException ex) { throw new BitcoinRPCException(method,
	 * Arrays.deepToString(o), ex); } }
	 */
	private static void blockchain() {
		String user = "multichainrpc",
				pass = "47MgcHZuvShr3ch72rRCdnQA8MX88MUjqdYCGYBHcuP5",
				port = "6002",
				method = "getstreamkeysummary",
				params = "[\"str\", \"SPC-BYS\", \"jsonobjectmerge,ignoreother\"]";
		Object thequery = JSONTools.JSONStringToListMaps(params);
		/*
		System.out.println(new Gson().toJson(thequery));
		URL url = null;
		try {
			url = new URL("http://localhost:" + port);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		JSONRPC2Session session = new JSONRPC2Session(url);
		
		session.setConnectionConfigurator(new ConnectionConfigurator() {
			public void configure(HttpURLConnection connection) {
				connection.addRequestProperty("Authorization", "Basic " + String
						.valueOf(Base64Coder.encode((user + ":" + pass).getBytes(Charset.forName("ISO8859-1")))));
			}
		});
		session.getOptions().setRequestContentType("application/json+rpc");
		
		JSONRPC2Request request = new JSONRPC2Request("publish", (List)thequery,"id");
		JSONRPC2Response response = null;
		System.out.println(new Gson().toJson(request));
		try {
			System.out.println("SEND");
			response = session.send(request);
			System.out.println("/SEND");
			
		} catch (JSONRPC2SessionException e) {
			System.err.println(e.getMessage());
		}
		if (response!=null){//response.indicatesSuccess())
			System.out.println("result");
			System.out.println(response.getResult());
		}
		else
			System.out.println("null");//response.getError().getMessage());
		*/
		
		
		URL url = null;
		try {
			url = new URL("http://localhost:" + port);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		HttpURLConnection connection = null;

		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestProperty("Authorization","Basic " + String.valueOf(Base64Coder.encode((user+":"+pass).getBytes(Charset.forName("ISO8859-1")))));
		

		//String requestData =  "{\"method\" : \"publish\","+" \"params\" : [\"str\", [\"key3\"], {\"json\" : {\"val1\" : \"the val 1\"}}], \"id\" : \"id\"}";
		//String requestData =  "{\"method\" : \"publish\","+" \"params\" : [\"str\", [\"key3\"], {\"json\" : {\"val1\" : \"the val 1º\"}}], \"id\" : \"id\"}";
		String requestData = "{\"method\" :\"getstreamkeysummary\","+"\"params\" : [\"str\", \"key3\", \"jsonobjectmerge,ignoreother\"], \"id\" : \"id\"}";
		OutputStream out = null;
		try {
			connection.setRequestMethod("POST");
			connection.connect();
			out = connection.getOutputStream();
			out.write(requestData.getBytes(Charset.forName("UTF-8")));//Charset.forName("ISO8859-1")));
			out.flush(); out.close();
			int statusCode = connection.getResponseCode();
			if (statusCode !=HttpURLConnection.HTTP_OK){
				throw new Exception("unexpected status code returned : " + statusCode); } }
		catch (IOException e) { e.printStackTrace(); }
		catch (Exception e) {
			e.printStackTrace();
		}

		if (out != null) {
			try { out.close(); }
			catch (IOException e) { e.printStackTrace(); } 
			InputStream in = null;
			try { in = connection.getInputStream(); }
			catch (IOException e) { e.printStackTrace(); }
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder result = new StringBuilder(); String line;
			try { while((line = reader.readLine()) != null) {
				byte[] lineb = line.getBytes(StandardCharsets.UTF_8);
				result.append(new String(lineb,StandardCharsets.UTF_8));
			} }
			catch(IOException e){ e.printStackTrace(); }
			String result_ = result.toString();
			System.out.println(result_.toString());
		}
		
		 
		
		//JsonParser parser = new JsonParser(); JsonObject resp = (JsonObject)
		//parser.parse(new StringReader(bos.toString()));
		 
		/* 
		BitcoinJSONRPCClient rpcClient = null;
		try {
			rpcClient = new BitcoinJSONRPCClient("http://"+user+":"+pass+"@localhost:"+port); }
		catch (MalformedURLException e) { e.printStackTrace(); }
		//Object r = rpcClient.query("publish",
		//"\"str\", [\"key4\"] , {\"json\" : {\"val1\" : \"the val 1\"}}, \"id\" : \"id\"");
		Object r = rpcClient.query(method,params);
		
		System.out.println(new Gson().toJson(r)); //System.out.println(rpcClient.decodeScript(r);
		*/
		//List<PeerInfoResult> peers = rpcClient.getPeerInfo();
		//for(PeerInfoResult peer : peers){ System.out.println(peer.getAddr());
		//}
		
		// BitcoinRawTxBuilder builder = new BitcoinRawTxBuilder(rpcClient);
		// builder.
		// rpcClient.
	}
	
}