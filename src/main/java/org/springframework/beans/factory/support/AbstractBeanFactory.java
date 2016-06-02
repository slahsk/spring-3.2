package org.springframework.beans.factory.support;

import java.beans.PropertyEditor;

import org.springframework.beans.BeansException;
import org.springframework.beans.NoSuchBeanDefinitionException;
import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanExpressionResolver;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.convert.ConversionService;

public class AbstractBeanFactory  extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

	@Override
	public BeanFactory getParentBeanFactory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsLocalBean(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Object getBean(String name) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getBean(Class<T> requiredType) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getBean(String name, Object... args) throws BeansException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsBean(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTypeMatch(String name, Class<?> targetType) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setBeanClassLoader(ClassLoader beanClassLoader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ClassLoader getBeanClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTempClassLoader(ClassLoader tempClassLoader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ClassLoader getTempClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCacheBeanMetadata(boolean cacheBeanMetadata) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCacheBeanMetadata() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBeanExpressionResolver(BeanExpressionResolver resolver) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BeanExpressionResolver getBeanExpressionResolver() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setConversionService(ConversionService conversionService) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ConversionService getConversionService() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void copyRegisteredEditorsTo(PropertyEditorRegistry registry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTypeConverter(TypeConverter typeConverter) {
		// TODO Auto-generated method stub
		
	}

}
