package org.springframework.core.io;

import org.springframework.util.ClassUtils;

public class DefaultResourceLoader implements ResourceLoader {
	private ClassLoader classLoader;

	public DefaultResourceLoader() {
		this.classLoader = ClassUtils.getDefaultClassLoader();
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

}
