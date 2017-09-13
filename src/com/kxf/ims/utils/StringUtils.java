package com.kxf.ims.utils;

public class StringUtils {
	public static boolean isEmpty(String s) {
		return null == s || s.length() < 1 || "null".equals(s); 
	}
}
