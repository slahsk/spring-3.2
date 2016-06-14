package org.springframework.beans.factory.config;

import java.beans.PropertyEditor;

import org.springframework.beans.PropertyEditorRegistrar;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.TypeConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.ConversionService;

public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry{
	
	String SCOPE_SINGLETON = "singleton";
	
	String SCOPE_PROTOTYPE = "prototype";
	
	
	BeanDefinition getMergedBeanDefinition(String beanName) throws NoSuchBeanDefinitionException;
	
	void setParentBeanFactory(BeanFactory parentBeanFactory) throws IllegalStateException;
	
	void setBeanClassLoader(ClassLoader beanClassLoader);
	
	ClassLoader getBeanClassLoader();
	
	void setTempClassLoader(ClassLoader tempClassLoader);
	
	ClassLoader getTempClassLoader();
	
	void setCacheBeanMetadata(boolean cacheBeanMetadata);

	boolean isCacheBeanMetadata();
	
	void setBeanExpressionResolver(BeanExpressionResolver resolver);
	
	BeanExpressionResolver getBeanExpressionResolver();
	
	void setConversionService(ConversionService conversionService);
	
	ConversionService getConversionService();
	
	void addPropertyEditorRegistrar(PropertyEditorRegistrar registrar);
	
	void registerCustomEditor(Class<?> requiredType, Class<? extends PropertyEditor> propertyEditorClass);
	
	void copyRegisteredEditorsTo(PropertyEditorRegistry registry);
	
	void setTypeConverter(TypeConverter typeConverter);

}
