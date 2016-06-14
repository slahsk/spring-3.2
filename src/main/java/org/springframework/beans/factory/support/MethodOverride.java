package org.springframework.beans.factory.support;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.util.Assert;

public class MethodOverride implements BeanMetadataElement{
	
	private final String methodName;

	private boolean overloaded = true;

	private Object source;
	
	protected MethodOverride(String methodName) {
		Assert.notNull(methodName, "Method name must not be null");
		this.methodName = methodName;
	}
	
	protected void setOverloaded(boolean overloaded) {
		this.overloaded = overloaded;
	}

	
	
	@Override
	public Object getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public String getMethodName() {
		return this.methodName;
	}
}
