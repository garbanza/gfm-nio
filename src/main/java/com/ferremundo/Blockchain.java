package com.ferremundo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;

import com.ferremundo.stt.GSettings;
import com.google.gson.Gson;

public class Blockchain {

	private String
			rpcUser,
			rpcPass,
			rpcPort,
			streamName;
	private static Blockchain blockchain = null;
	
	private Blockchain(){
		if(blockchain==null){
			this.rpcUser = GSettings.get("BLOCKCHAIN_RPC_USER");
			this.rpcPass = GSettings.get("BLOCKCHAIN_RPC_PASSWORD");
			this.rpcPort = GSettings.get("BLOCKCHAIN_RPC_PORT");
			this.streamName = GSettings.get("BLOCKCHAIN_STREAM_NAME");
		}
	}
	
	public static Blockchain instance(){
		if(blockchain == null){
			blockchain = new Blockchain();
		}
		return blockchain;
	}
	
	public String publish(Array[] keys, JSONObject json, String id){
		String keys_ = new Gson().toJson(keys);
		String json_ = json.toString();
		String query =
				"{\"method\" : \"publish\","
				+"\"params\" : [\""+streamName+"\", "+keys_+", {\"json\" : "+json_+"}], \"id\" : \""+id+"\"}";
		return query(query);
	}
	
	public String query(String JSONRPCQuery){
		Log log = new Log();
		log.info("JSONRPCQuery: '"+JSONRPCQuery+"'");
		URL url = null;
		try {
			url = new URL("http://localhost:" + rpcPort);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
		} catch (IOException e1) {
			log.trace(e1);
		}
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestProperty("Authorization","Basic " + String.valueOf(Base64Coder.encode((rpcUser+":"+rpcPass).getBytes(Charset.forName("ISO8859-1")))));

		byte[] requestData = JSONRPCQuery.getBytes(Charset.forName("UTF-8")); //"{\"method\" : \"publish\","+" \"params\" : [\"str\", [\"key3\"], {\"json\" : {\"val1\" : \"the val 1\"}}], \"id\" : \"id\"}"

		OutputStream out = null;
		try {
			connection.connect();
			out = connection.getOutputStream();
			out.write(requestData);
			out.flush();
			int statusCode = connection.getResponseCode();
			if (statusCode !=HttpURLConnection.HTTP_OK){
				throw new Exception("unexpected status code returned : " + statusCode); } }
		catch (IOException e) { log.trace(e);}
		catch (Exception e) {log.trace(e);}

		try { out.close(); }
		catch (IOException e) { log.trace(e); } 
		InputStream in = null;
		try { in = connection.getInputStream(); }
		catch (IOException e) { e.printStackTrace(); }
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		StringBuilder result = new StringBuilder();
		String line;
		try { while((line = reader.readLine()) != null) {
			byte[] lineb = line.getBytes("UTF8");
			result.append(new String(lineb,"UTF8"));
		} }
		catch(IOException e){ e.printStackTrace(); }
		String result_ = result.toString();
		log.info(result_);
		return result_;
	}
}
