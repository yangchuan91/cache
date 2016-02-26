package com.cache.config;

import com.cache.util.PropertiesPool;
import com.cache.util.StringUtils;

public class CacheConfig {
	
	public static String getConfig(String key){
		String value="";
		try {
			value= StringUtils.trim(PropertiesPool.getInstance().getParam(key));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

}
