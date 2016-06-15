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
	int reference;
	public Log(OnlineClient oc){
		login = oc.getShopman().getLogin();
		reference = oc.getClientReference();
		StackTraceElement traceElement=Thread.currentThread().getStackTrace()[2];//index];
		className=traceElement.getClassName();
		methodName=traceElement.getMethodName();
		initLogger();
		logger = LogManager.getLogger(className);
	}
	
	public Log(){
		login="logger";
		reference=-1;
		StackTraceElement traceElement=Thread.currentThread().getStackTrace()[2];//index];
		className=traceElement.getClassName();
		methodName=traceElement.getMethodName();
		initLogger();
		logger = LogManager.getLogger(className);
	}
	
	public Log(String name){
		login=name;
		reference=-1;
		StackTraceElement traceElement=Thread.currentThread().getStackTrace()[2];//index];
		className=traceElement.getClassName();
		methodName=traceElement.getMethodName();
		initLogger();
		logger = LogManager.getLogger(className);
	}
	
	public Log(String name, int reference){
		login=name;
		this.reference=reference;
		StackTraceElement traceElement=Thread.currentThread().getStackTrace()[2];//index];
		className=traceElement.getClassName();
		methodName=traceElement.getMethodName();
		initLogger();
		logger = LogManager.getLogger(className);
	}
	
	private static void initLogger(){
		if (loggerContext==null) {
			LoggerContext loggerContext = (org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false);
			File file = new File(GSettings.getPathTo("LOGGER_CONTEXT"));
			// this will force a reconfiguration 
			ThreadContext.put("log-path", GSettings.getPathTo("LOGGING_PATH"));
			loggerContext.setConfigLocation(file.toURI());
		}
	}
	
	public Logger getLogger(){
		return logger;
	}

	public void info(String msg){
		logger.info(className+"-"+methodName+"\t[user:"+login+"."+reference+"]\t"+ msg);
	}
	public void entry(){
		logger.log(ENTRY,className+"-"+methodName+" \t[user:"+login+"."+reference+"]\tentry");
	}
	public void entry(Object... os){
		String oss= new Gson().toJson(os);
		logger.log(ENTRY,className+"-"+methodName+"\t[user:"+login+"."+reference+"]\tentry("+ oss +")");
	}
	public void exit(){
		logger.log(EXIT,className+"-"+methodName+" \t[user:"+login+"."+reference+"]\texit");
	}
	public void exit(Object... os){
		String oss= new Gson().toJson(os);
		logger.log(EXIT,className+"-"+methodName+"\t[user:"+login+"."+reference+"]\texit("+ oss +")");
	}
	public void object(Object... os){
		String oss= new Gson().toJson(os);
		logger.log(OBJECT,className+"-"+methodName+"\t[user:"+login+"."+reference+"]\tobject("+ oss +")");
	}
	public void debug(String msg){
		logger.debug(className+"-"+methodName+"\t[user:"+login+"."+reference+"]\t"+ msg);
	}
	public void error(String msg){
		logger.error(className+"-"+methodName+"\t[user:"+login+"."+reference+"]\t"+ msg);
	}
	public void fatal(String msg){
		logger.fatal(className+"-"+methodName+"\t[user:"+login+"."+reference+"]\t"+ msg);
	}
	public void log(Level level, String msg){
		logger.log(level,className+"-"+methodName+"\t[user:"+login+"."+reference+"]\t"+ msg);
	}
	public void trace(String msg){
		logger.trace(className+"-"+methodName+"\t[user:"+login+"."+reference+"]\t"+ msg);
	}
	public void warn(String msg){
		logger.warn(className+"-"+methodName+"\t[user:"+login+"."+reference+"]\t"+ msg);
	}
}
