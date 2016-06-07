package org.springframework.beans.factory;

public interface SmartFactoryBean <T> extends FactoryBean<T>{
	boolean isPrototype();
	boolean isEagerInit();
}
