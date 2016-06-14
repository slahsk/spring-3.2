package org.springframework.beans.factory;

import org.springframework.util.Assert;

public abstract class BeanFactoryUtils {
	
	public static String transformedBeanName(String name) {
		Assert.notNull(name, "'name' must not be null");
		String beanName = name;
		while (beanName.startsWith(BeanFactory.FACTORY_BEAN_PREFIX)) {
			beanName = beanName.substring(BeanFactory.FACTORY_BEAN_PREFIX.length());
		}
		return beanName;
	}
}
