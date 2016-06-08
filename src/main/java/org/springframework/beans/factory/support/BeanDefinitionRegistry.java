package org.springframework.beans.factory.support;

import org.springframework.core.AliasRegistry;

public interface BeanDefinitionRegistry extends AliasRegistry{
	
	boolean containsBeanDefinition(String beanName);
	
	

}
