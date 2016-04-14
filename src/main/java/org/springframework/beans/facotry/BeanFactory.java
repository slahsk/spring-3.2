package org.springframework.beans.facotry;

import org.springframework.beans.BeansException;
import org.springframework.beans.NoSuchBeanDefinitionException;

public interface BeanFactory {
	
	String FACTORY_BEAN_PREFIX = "&";
	
	Object getBean(String name) throws BeansException;
	
	<T> T getBean(String name, Class<T> requiredType) throws BeansException;
	
	<T> T getBean(Class<T> requiredType) throws BeansException;
	
	Object getBean(String name, Object... args) throws BeansException;
	
	boolean containsBean(String name);
	
	boolean isSingleton(String name) throws NoSuchBeanDefinitionException;
	
	boolean isPrototype(String name) throws NoSuchBeanDefinitionException;
	
	boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException;
	
	Class<?> getType(String name) throws NoSuchBeanDefinitionException;
	
	String[] getAliases(String name);

}
