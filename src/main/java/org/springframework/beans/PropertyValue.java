package org.springframework.beans;

import java.beans.PropertyDescriptor;
import java.io.Serializable;

import org.springframework.core.AttributeAccessor;
import org.springframework.util.Assert;

public class PropertyValue  extends BeanMetadataAttributeAccessor implements Serializable {
	
	private final String name;

	private final Object value;

	private Object source;

	private boolean optional = false;

	private boolean converted = false;

	private Object convertedValue;

	volatile Boolean conversionNecessary;

	volatile Object resolvedTokens;

	volatile PropertyDescriptor resolvedDescriptor;
	
	
	public PropertyValue(PropertyValue original) {
		Assert.notNull(original, "Original must not be null");
		this.name = original.getName();
		this.value = original.getValue();
		this.source = original.getSource();
		this.optional = original.isOptional();
		this.converted = original.converted;
		this.convertedValue = original.convertedValue;
		this.conversionNecessary = original.conversionNecessary;
		this.resolvedTokens = original.resolvedTokens;
		this.resolvedDescriptor = original.resolvedDescriptor;
		copyAttributesFrom(original);
	}
	
	public PropertyValue(String name, Object value) {
		this.name = name;
		this.value = value;
	}
	
	protected void copyAttributesFrom(AttributeAccessor source) {
		Assert.notNull(source, "Source must not be null");
		String[] attributeNames = source.attributeNames();
		for (String attributeName : attributeNames) {
			setAttribute(attributeName, source.getAttribute(attributeName));
		}
	}


	public boolean isOptional() {
		return optional;
	}


	public void setOptional(boolean optional) {
		this.optional = optional;
	}


	public String getName() {
		return name;
	}


	public Object getValue() {
		return value;
	}
	
	
}
