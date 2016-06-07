package org.springframework.beans.factory.support;

import org.springframework.beans.factory.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanDefinition;

@SuppressWarnings("serial")
public class RootBeanDefinition extends AbstractBeanDefinition {
	
	boolean allowCaching = true;
	private BeanDefinitionHolder decoratedDefinition;
	private volatile Class<?> targetType;
	boolean isFactoryMethodUnique = false;

	
	public RootBeanDefinition() {
		super();
	}

	public RootBeanDefinition(Class<?> beanClass) {
		super();
		setBeanClass(beanClass);
	}
	
	public RootBeanDefinition(RootBeanDefinition original) {
		super((BeanDefinition) original);
		this.allowCaching = original.allowCaching;
		this.decoratedDefinition = original.decoratedDefinition;
		this.targetType = original.targetType;
		this.isFactoryMethodUnique = original.isFactoryMethodUnique;
	}

	@Override
	public RootBeanDefinition cloneBeanDefinition() {
		return new RootBeanDefinition(this);
	}

	@Override
	public void setAttribute(String name, Object value) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object removeAttribute(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasAttribute(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] attributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getSource() {
		// TODO Auto-generated method stub
		return null;
	}

}
