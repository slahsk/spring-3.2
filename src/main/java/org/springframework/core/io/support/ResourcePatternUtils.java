package org.springframework.core.io.support;

import org.springframework.util.ResourceUtils;

public abstract class ResourcePatternUtils {
	
	public static boolean isUrl(String resourceLocation) {
		return (resourceLocation != null &&
				(resourceLocation.startsWith(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX) ||
						ResourceUtils.isUrl(resourceLocation)));
	}
	

}
