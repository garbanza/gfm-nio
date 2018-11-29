package com.ferremundo;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;

import com.ferremundo.stt.GSettings;
import com.google.gson.Gson;

public class Log {
	
	private static LoggerContext loggerContext = null;
	static final Level ENTRY = Level.forName("ENTRY", 600);
	static final Level EXIT = Level.forName("EXIT", 600);
	static final Level OBJECT = Level.forName("OBJECT", 500);
	
	Logger logger;
	String login;
	String className;
	String methodName;
	int lineNumber;
	int reference;
	public Log(OnlineClient oc){
		login = oc.getShopman().getLogin();
		reference = oc.getClientReference();
		StackTraceElement traceElement=Thread.currentThread().getStackTrace()[2];//index];
		className=traceElement.getClassName();
		methodName=traceElement.getMethodName();
		lineNumber=traceElement.getLineNumber();
		initLogger();
		logger = LogManager.getLogger(className);
	}
	
	public Log(){
		Integer clientReference=ClientReference.get();
		if(clientReference!=null){
			OnlineClient ol=OnlineClients.instance().get(clientReference);
			if(ol!=null&&ol.getShopman()!=null){
				login=ol.getShopman().getLogin();
				reference=ol.getClientReference();
			}
		}
		else {
			login="logger";
			reference=-1;
		}
		StackTraceElement traceElement=Thread.currentThread().getStackTrace()[2];
		className=traceElement.getClassName();
		methodName=traceElement.getMethodName();
		lineNumber=traceElement.getLineNumber();
		initLogger();
		logger = LogManager.getLogger(className);
	}
	
	public Log(String name){
		login=name;
		reference=-1;
		StackTraceElement traceElement=Thread.currentThread().getStackTrace()[2];
		className=traceElement.getClassName();
		methodName=traceElement.getMethodName();
		lineNumber=traceElement.getLineNumber();
		initLogger();
		logger = LogManager.getLogger(className);
	}
	
	public Log(String name, int reference){
		login=name;
		this.reference=reference;
		StackTraceElement traceElement=Thread.currentThread().getStackTrace()[2];//index];
		className=traceElement.getClassName();
		methodName=traceElement.getMethodName();
		lineNumber=traceElement.getLineNumber();
		initLogger();
		logger = LogManager.getLogger(className);
	}
	
	private void initLogger(){
		System.setProperty("log4j.configuration", GSettings.getPathTo("LOGGER_CONTEXT"));
		if (loggerContext==null) {
			ThreadContext.put("log-path", GSettings.getPathTo("LOGGING_PATH"));
			LoggerContext loggerContext = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
			File file = new File(GSettings.getPathTo("LOGGER_CONTEXT"));
			// this will force a reconfiguration 
			
			loggerContext.setConfigLocation(file.toURI());
		}
	}
	
	public Logger getLogger(){
		return logger;
	}

	public void info(String msg){
		logger.info(className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\t"+ msg);
	}
	public void entry(){
		logger.log(ENTRY,className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\tentry");
	}
	public void entry(Object... os){
		String oss= new Gson().toJson(os);
		logger.log(ENTRY,className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\tentry("+ oss +")");
	}
	public void exit(){
		logger.log(EXIT,className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\texit");
	}
	public void exit(Object... os){
		String oss= new Gson().toJson(os);
		logger.log(EXIT,className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\texit("+ oss +")");
	}
	public void object(Object... os){
		String oss= new Gson().toJson(os);
		logger.log(OBJECT,className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\tobject("+ oss +")");
	}
	public void debug(String msg){
		logger.debug(className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\t"+ msg);
	}
	public void error(String msg){
		logger.error(className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\t"+ msg);
	}
	public void error(Object... os){
		String oss= new Gson().toJson(os);
		logger.error(className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\tobject("+ oss +")");
	}
	public void fatal(String msg){
		logger.fatal(className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\t"+ msg);
	}
	public void log(Level level, String msg){
		logger.log(level,className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\t"+ msg);
	}
	public void trace(String msg, Throwable t){
		logger.trace(className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\t"+ msg+" - "+t.toString());
		for (StackTraceElement element : t.getStackTrace()){
			logger.trace(className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\t"+ element);
		 }
		//logger.trace(className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\t"+ msg);
	}
	public void trace(Throwable t){
		logger.trace(className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\t"+ t.toString());
		for (StackTraceElement element : t.getStackTrace()){
			logger.trace(className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\t"+ element);
		 }
		//logger.trace(className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\t"+ msg);
	}
	public void warn(String msg){
		logger.warn(className+"-"+methodName+":"+lineNumber+"\t[user:"+login+":sessionid:"+reference+"]\t"+ msg);
	}
}
