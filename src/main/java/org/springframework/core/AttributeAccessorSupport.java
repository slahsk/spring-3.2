package org.springframework.core;

import java.io.Serializable;

import org.springframework.util.Assert;

public abstract class AttributeAccessorSupport implements AttributeAccessor, Serializable{
	
	protected void copyAttributesFrom(AttributeAccessor source) {
		Assert.notNull(source, "Source must not be null");
		String[] attributeNames = source.attributeNames();
		for (String attributeName : attributeNames) {
			setAttribute(attributeName, source.getAttribute(attributeName));
		}
	}
}
