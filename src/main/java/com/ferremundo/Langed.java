package com.ferremundo;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Langed {

	private static Map<String,JSONObject> json=new HashMap<String, JSONObject>();
	
	private Langed(Class<Object> class_){
		
	}
	
	public static String get(String locale, String key, String... args){
		//int index=Thread.currentThread().getStackTrace().length-1;
		StackTraceElement traceElement=Thread.currentThread().getStackTrace()[2];//index];
		/*for(int i=0;i< Thread.currentThread().getStackTrace().length;i++){
			StackTraceElement te =Thread.currentThread().getStackTrace()[i];
			System.out.println();
			System.out.println(te.getClassName());
			System.out.println(te.getFileName());
		}*/
		String name=traceElement.getClassName();
		try {
			return fromLang_(name, locale, key, args);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	private static String fromLang_(String name, String locale, String key, String... args) throws JSONException{
		if(json.containsKey(name)){
			JSONObject jsono=json.get(name);
			if(jsono.has(key)){
				JSONObject theKey=(JSONObject)jsono.get(key);
				if(theKey.has(locale)){
					return getf((String)theKey.get(locale),args);
				}
				else {
					return getf((String)theKey.get("en"),args);
				}
			}
			else {
				throw new JSONException(key +" not found");
			}
		}
		else {
			String resource="/"+name.replaceAll("\\.", "/")+".lang.json";
			Class<?> clazz=null;
			try {
				clazz = Class.forName(name);
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//System.out.println(name+" "+locale+" "+key);
			InputStream is= clazz.getResourceAsStream(resource);
			String jsonStr=Utils.getStringFromInputStream(is);
			JSONObject jsonObject=null;
			try {
				jsonObject = new JSONObject(jsonStr);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			json.put(name, jsonObject);
			return fromLang_(name, locale, key, args);
		}
	}
	
	private static String getf(String str,String... args){
		String ret=str.toString();
		for(int i=0;i<args.length;i++){
			ret=ret.replaceAll("\\$\\{"+i+"\\}", args[i]);
		}
		return ret;
	}
}
