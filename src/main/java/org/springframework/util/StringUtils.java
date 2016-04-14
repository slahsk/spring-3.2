package org.springframework.util;

public abstract class StringUtils {
	
	public static boolean hasLength(String str) {
		return hasLength((CharSequence) str);
	}
	
	public static boolean hasLength(CharSequence str) {
		return (str != null && str.length() > 0);
	}

}
