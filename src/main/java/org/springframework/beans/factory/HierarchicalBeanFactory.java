package org.springframework.beans.factory;

public interface HierarchicalBeanFactory extends BeanFactory{
	BeanFactory getParentBeanFactory();
	
	boolean containsLocalBean(String name);
}
