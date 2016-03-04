package com.cache.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesPool {

	private static String propertiesUrl="src/main/resources/config/datacache.properties";
	private static Map<String, Object> params;
	
	private static PropertiesPool instance = null;


	public static void setPropertiesUrl(String propertiesUrl) {
		PropertiesPool.propertiesUrl = propertiesUrl;
	}

	private PropertiesPool() throws Exception {
		System.out.println(propertiesUrl);
		params = loadAllParams(propertiesUrl);
		if (params.size() == 0) {
			throw new Exception("读取配置失败");
		}
	}
	
	public static PropertiesPool getInstance() throws Exception {
	    if (instance == null)
	    {
	    	instance = new PropertiesPool();
	    }
	    return instance;
	  }

	// 读取具体的配置
	public Object getParam(String key) {
		try {
			Object value = params.get(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// 装载所有的配置,并返回配置map
	@SuppressWarnings("unchecked")
	public static Map<String, Object> loadAllParams(String filePath) {
		Map<String, Object> properties = null;
		Properties props = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			props.load(in);
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<String, Object>();
		}
		properties = new HashMap<String, Object>((Map) props);
		return properties;
	}
	public static void main(String[] args) throws Exception {
		PropertiesPool.setPropertiesUrl("src/main/resources/config/application.properties");
		System.out.println(PropertiesPool.getInstance().getParam("systemId"));
	}
}
