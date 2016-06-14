package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;

public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement{

	int ROLE_APPLICATION = 0;
	
	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;
	
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;
	
	
	void setParentName(String parentName);
	
	String getParentName();
	
	String getBeanClassName();
	
	String getFactoryBeanName();
	
	String getFactoryMethodName();
	
	String getScope();
	
	boolean isAbstract();
	
	boolean isLazyInit();
	
	int getRole();
	
	ConstructorArgumentValues getConstructorArgumentValues();
	
	MutablePropertyValues getPropertyValues();
	
	String getResourceDescription();

}
