package com.cache.util;

public class StringUtils {

	// 对象去空
	public static String trim(Object src) {
		return src == null ? "" : src.toString().trim();
	}

	// 对象去空
	public static String trim(String src) {
		return src == null ? "" : src.trim();
	}

}
