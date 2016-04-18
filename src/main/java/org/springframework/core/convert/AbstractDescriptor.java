package org.springframework.core.convert;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;

import org.springframework.util.Assert;

public abstract class AbstractDescriptor {

	private final Class<?> type;


	protected AbstractDescriptor(Class<?> type) {
		Assert.notNull(type, "Type must not be null");
		this.type = type;
	}
	
	public Class<?> getType() {
		return this.type;
	}

	public TypeDescriptor getElementTypeDescriptor() {
		if (isCollection()) {
			Class<?> elementType = resolveCollectionElementType();
			return (elementType != null ? new TypeDescriptor(nested(elementType, 0)) : null);
		}
		else if (isArray()) {
			Class<?> elementType = getType().getComponentType();
			return new TypeDescriptor(nested(elementType, 0));
		}
		else {
			return null;
		}
	}
	
	public TypeDescriptor getMapKeyTypeDescriptor() {
		if (isMap()) {
			Class<?> keyType = resolveMapKeyType();
			return keyType != null ? new TypeDescriptor(nested(keyType, 0)) : null;
		}
		else {
			return null;
		}
	}
	
	public TypeDescriptor getMapValueTypeDescriptor() {
		if (isMap()) {
			Class<?> valueType = resolveMapValueType();
			return valueType != null ? new TypeDescriptor(nested(valueType, 1)) : null;
		}
		else {
			return null;
		}
	}
	
	public AbstractDescriptor nested() {
		if (isCollection()) {
			Class<?> elementType = resolveCollectionElementType();
			return (elementType != null ? nested(elementType, 0) : null);
		}
		else if (isArray()) {
			return nested(getType().getComponentType(), 0);
		}
		else if (isMap()) {
			Class<?> mapValueType = resolveMapValueType();
			return (mapValueType != null ? nested(mapValueType, 1) : null);
		}
		else if (Object.class.equals(getType())) {
			return this;
		}
		else {
			throw new IllegalStateException("Not a collection, array, or map: cannot resolve nested value types");
		}
	}
	
	public abstract Annotation[] getAnnotations();

	protected abstract Class<?> resolveCollectionElementType();

	protected abstract Class<?> resolveMapKeyType();

	protected abstract Class<?> resolveMapValueType();

	protected abstract AbstractDescriptor nested(Class<?> type, int typeIndex);

	private boolean isCollection() {
		return Collection.class.isAssignableFrom(getType());
	}

	private boolean isArray() {
		return getType().isArray();
	}

	private boolean isMap() {
		return Map.class.isAssignableFrom(getType());
	}
}
