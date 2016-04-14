package org.springframework.util;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class ResourceUtils {
	public static final String CLASSPATH_URL_PREFIX = "classpath:";
	
	public static boolean isUrl(String resourceLocation) {
		if (resourceLocation == null) {
			return false;
		}
		if (resourceLocation.startsWith(CLASSPATH_URL_PREFIX)) {
			return true;
		}
		try {
			//URL 객체가 생성되는지 확인
			new URL(resourceLocation);
			return true;
		}
		catch (MalformedURLException ex) {
			return false;
		}
	}
}
