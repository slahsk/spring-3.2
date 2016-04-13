package org.springframework.beans.facotry.access;

import BeanFactoryLocator;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SingletonBeanFactoryLocator implements BeanFactoryLocator{
	private static final String DEFAULT_RESOURCE_LOCATION = "classpath*:beanRefFactory.xml";

	protected static final Log logger = LogFactory.getLog(SingletonBeanFactoryLocator.class);
	
	private static final Map<String, BeanFactoryLocator> instances = new HashMap<String, BeanFactoryLocator>();
}
