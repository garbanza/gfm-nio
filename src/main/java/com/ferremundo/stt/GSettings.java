package com.ferremundo.stt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.WatchService;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import com.ferremundo.FileWatcher;
import com.ferremundo.Log;
import com.profact.test.ObtieneTimbresDisponibles;

public class GSettings extends Properties{


	private static GSettings gSettings=null;
	private static String folderBase=System.getProperty("user.home")+File.separator+".fm"+File.separator;
	private static FileWatcher fileWatcher;
	private static boolean fileWatcherExecuted=false;
	/**
	 * 
	 */
	private GSettings(){
		
		if(gSettings==null){
			if(System.getProperty("FM_HOME")!=null){
				folderBase=System.getProperty("FM_HOME")+File.separator;
			}

			String path=
					folderBase+
					//this.getClass().getName().replace(".",File.separator)+
					this.getClass().getName()+
					".xml";
			InputStream is=null;
			try {
				is = new FileInputStream(path);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				this.loadFromXML(is);
				is.close();
			} catch (InvalidPropertiesFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(new Boolean(System.getProperty("TEST"))==true){
				this.setProperty("TEST", "true");
			}
			this.setProperty("FM_HOME", folderBase);
			
			/*Watch if timbres are in good amount. This check is here temporarily :\*/
			/*ObtieneTimbresDisponibles disponibles= new ObtieneTimbresDisponibles();
			Obt*/
		}
		if(!fileWatcherExecuted){
			String path=
					folderBase+
					//this.getClass().getName().replace(".",File.separator)+
					this.getClass().getName()+
					".xml";
			fileWatcherExecuted=true;
			fileWatcher= new FileWatcher(new File(path), new FileWatcher.DoOnChangeFile() {
				@Override
				public void execute() {
					GSettings.gSettings=null;
					new Log().info(path +" changed");
				}
			});
			fileWatcher.run();
		}
	}
	
	public static GSettings instance(){
		if(gSettings==null){
			gSettings=new GSettings();
		}
		return gSettings;
	}
	
	public boolean hasKey(String key){
		return this.containsKey(key);
	}
	
	public static boolean has(String key){
		if(gSettings==null){
			gSettings=new GSettings();
		}
		return gSettings.hasKey(key);
	}
	
	public String getKey(String key){
		if(gSettings==null){
			gSettings=new GSettings();
		}
		return gSettings.getProperty(key);
	}
	
	public static String get(String key){
		if(gSettings==null){
			gSettings=new GSettings();
		}
		return gSettings.getKey(key);
	}
	public static String getHome(){
		if(gSettings==null){
			gSettings=new GSettings();
		}
		return folderBase;
	}
	
	public static String getPathTo(String key){
		if(gSettings==null){
			gSettings=new GSettings();
		}
		String path=getHome()+gSettings.getKey(key);
		File file=new File(path);
		if(file.isDirectory())return path+file.separator;
		return path;
		
	}
	
}