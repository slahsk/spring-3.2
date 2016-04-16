package org.springframework.beans.factory.config;

import org.springframework.beans.BeansException;

public interface BeanExpressionResolver {
	Object evaluate(String value, BeanExpressionContext evalContext) throws BeansException;
}
