package org.springframework.core.convert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.util.ConcurrentReferenceHashMap;

public class Property {
	private static Map<Property, Annotation[]> annotationCache = new ConcurrentReferenceHashMap<Property, Annotation[]>();

	private final Class<?> objectType;

	private final Method readMethod;

	private final Method writeMethod;

	private final String name;
}
