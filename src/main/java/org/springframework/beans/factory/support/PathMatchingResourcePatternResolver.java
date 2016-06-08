package org.springframework.beans.factory.support;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;

public class PathMatchingResourcePatternResolver implements ResourcePatternResolver {
	
	private static final Log logger = LogFactory.getLog(PathMatchingResourcePatternResolver.class);
	
	private static Method equinoxResolveMethod;
	
	static {
		try {
			// Detect Equinox OSGi (e.g. on WebSphere 6.1)
			Class<?> fileLocatorClass = ClassUtils.forName("org.eclipse.core.runtime.FileLocator", PathMatchingResourcePatternResolver.class.getClassLoader());
			equinoxResolveMethod = fileLocatorClass.getMethod("resolve", URL.class);
			logger.debug("Found Equinox FileLocator for OSGi bundle URL resolution");
		}
		catch (Throwable ex) {
			equinoxResolveMethod = null;
		}
	}
	
	private final ResourceLoader resourceLoader;
	
	public PathMatchingResourcePatternResolver() {
		this.resourceLoader = new DefaultResourceLoader();
	}
	
	@Override
	public Resource getResource(String location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resource[] getResources(String locationPattern) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
