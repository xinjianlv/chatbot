package com.wanda.chatbot.utils;


import java.util.Properties;

public class SystemUtil {

	public static boolean isWindows(){
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		if(os.startsWith("win") || os.startsWith("Win"))
			return true;
		return false;
	}
	public static String getPath(){
		Properties prop = System.getProperties();
		String path = prop.getProperty("user.dir");
		return path;
	}
	public static String getProperties(String attribute){
		Properties prop = System.getProperties();
		String path = prop.getProperty(attribute);
		return path;
	}
}
