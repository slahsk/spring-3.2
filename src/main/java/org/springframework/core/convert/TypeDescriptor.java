package org.springframework.core.convert;

import java.beans.ParameterDescriptor;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

@SuppressWarnings("serial")
public class TypeDescriptor implements Serializable {
	static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];
	private static final Map<Class<?>, TypeDescriptor> typeDescriptorCache = new HashMap<Class<?>, TypeDescriptor>(18);

	static {
		typeDescriptorCache.put(boolean.class, new TypeDescriptor(boolean.class));
		typeDescriptorCache.put(Boolean.class, new TypeDescriptor(Boolean.class));
		typeDescriptorCache.put(byte.class, new TypeDescriptor(byte.class));
		typeDescriptorCache.put(Byte.class, new TypeDescriptor(Byte.class));
		typeDescriptorCache.put(char.class, new TypeDescriptor(char.class));
		typeDescriptorCache.put(Character.class, new TypeDescriptor(Character.class));
		typeDescriptorCache.put(double.class, new TypeDescriptor(double.class));
		typeDescriptorCache.put(Double.class, new TypeDescriptor(Double.class));
		typeDescriptorCache.put(int.class, new TypeDescriptor(int.class));
		typeDescriptorCache.put(Integer.class, new TypeDescriptor(Integer.class));
		typeDescriptorCache.put(long.class, new TypeDescriptor(long.class));
		typeDescriptorCache.put(Long.class, new TypeDescriptor(Long.class));
		typeDescriptorCache.put(float.class, new TypeDescriptor(float.class));
		typeDescriptorCache.put(Float.class, new TypeDescriptor(Float.class));
		typeDescriptorCache.put(short.class, new TypeDescriptor(short.class));
		typeDescriptorCache.put(Short.class, new TypeDescriptor(Short.class));
		typeDescriptorCache.put(String.class, new TypeDescriptor(String.class));
		typeDescriptorCache.put(Object.class, new TypeDescriptor(Object.class));
	}

	private final Class<?> type;

	private final TypeDescriptor elementTypeDescriptor;

	private final TypeDescriptor mapKeyTypeDescriptor;

	private final TypeDescriptor mapValueTypeDescriptor;

	private final Annotation[] annotations;

	public TypeDescriptor(MethodParameter methodParameter) {
		this(new ParameterDescriptor(methodParameter));
	}

	public TypeDescriptor(Field field) {
		this(new FieldDescriptor(field));
	}

	public TypeDescriptor(Property property) {
		this(new BeanPropertyDescriptor(property));
	}

	public static TypeDescriptor valueOf(Class<?> type) {
		if (type == null) {
			type = Object.class;
		}
		TypeDescriptor desc = typeDescriptorCache.get(type);
		return (desc != null ? desc : new TypeDescriptor(type));
	}

	public static TypeDescriptor collection(Class<?> collectionType, TypeDescriptor elementTypeDescriptor) {
		if (!Collection.class.isAssignableFrom(collectionType)) {
			throw new IllegalArgumentException("collectionType must be a java.util.Collection");
		}
		return new TypeDescriptor(collectionType, elementTypeDescriptor);
	}

	public static TypeDescriptor map(Class<?> mapType, TypeDescriptor keyTypeDescriptor, TypeDescriptor valueTypeDescriptor) {
		if (!Map.class.isAssignableFrom(mapType)) {
			throw new IllegalArgumentException("mapType must be a java.util.Map");
		}
		return new TypeDescriptor(mapType, keyTypeDescriptor, valueTypeDescriptor);
	}

	public static TypeDescriptor array(TypeDescriptor elementTypeDescriptor) {
		if (elementTypeDescriptor == null) {
			return null;
		}
		Class<?> type = Array.newInstance(elementTypeDescriptor.getType(), 0).getClass();
		return new TypeDescriptor(type, elementTypeDescriptor, null, null, elementTypeDescriptor.getAnnotations());
	}

	public static TypeDescriptor nested(MethodParameter methodParameter, int nestingLevel) {
		if (methodParameter.getNestingLevel() != 1) {
			throw new IllegalArgumentException("methodParameter nesting level must be 1: "
					+ "use the nestingLevel parameter to specify the desired nestingLevel for nested type traversal");
		}
		return nested(new ParameterDescriptor(methodParameter), nestingLevel);
	}

	public static TypeDescriptor nested(Field field, int nestingLevel) {
		return nested(new FieldDescriptor(field), nestingLevel);
	}

	public static TypeDescriptor nested(Property property, int nestingLevel) {
		return nested(new BeanPropertyDescriptor(property), nestingLevel);
	}

	public static TypeDescriptor forObject(Object source) {
		return (source != null ? valueOf(source.getClass()) : null);
	}

	public Class<?> getType() {
		return this.type;
	}

	public Class<?> getObjectType() {
		return ClassUtils.resolvePrimitiveIfNecessary(getType());
	}

	public TypeDescriptor narrow(Object value) {
		if (value == null) {
			return this;
		}
		return new TypeDescriptor(value.getClass(), this.elementTypeDescriptor, this.mapKeyTypeDescriptor, this.mapValueTypeDescriptor, this.annotations);
	}

	public TypeDescriptor upcast(Class<?> superType) {
		if (superType == null) {
			return null;
		}
		Assert.isAssignable(superType, getType());
		return new TypeDescriptor(superType, this.elementTypeDescriptor, this.mapKeyTypeDescriptor, this.mapValueTypeDescriptor, this.annotations);
	}

	
	public String getName() {
		return ClassUtils.getQualifiedName(getType());
	}
	
	public boolean isPrimitive() {
		return getType().isPrimitive();
	}
	
	public Annotation[] getAnnotations() {
		return this.annotations;
	}
	
	public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
		return getAnnotation(annotationType) != null;
	}

	static Annotation[] nullSafeAnnotations(Annotation[] annotations) {
		return (annotations != null ? annotations : EMPTY_ANNOTATION_ARRAY);
	}
}
