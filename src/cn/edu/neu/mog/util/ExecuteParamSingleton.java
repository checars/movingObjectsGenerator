package cn.edu.neu.mog.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * The abstract base class for each writer.
 * 
 * @author CheQingShou
 */

public class ExecuteParamSingleton {

	private static ExecuteParamSingleton instance = null;
	private Properties properties = null;
	
	private ExecuteParamSingleton(){
		properties = new Properties();
		try{
			
			InputStream in = new BufferedInputStream(new FileInputStream(System.getProperty("user.dir")+"\\properties\\runParam.properties"));
			properties.load(in);	
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static ExecuteParamSingleton getInstance(){
		if(instance == null){
			instance =  new ExecuteParamSingleton(); 
		}
			return instance;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	

}
