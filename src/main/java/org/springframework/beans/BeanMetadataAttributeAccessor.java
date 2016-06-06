package org.springframework.beans;

import org.springframework.core.AttributeAccessorSupport;

public class BeanMetadataAttributeAccessor extends AttributeAccessorSupport implements BeanMetadataElement {

	private Object source;

	public Object getSource() {
		return source;
	}

	public void setSource(Object source) {
		this.source = source;
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

}
