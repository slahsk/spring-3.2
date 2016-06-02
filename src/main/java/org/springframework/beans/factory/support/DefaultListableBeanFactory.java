package org.springframework.beans.factory.support;

import java.io.Serializable;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

@SuppressWarnings("serial")
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable {

}
