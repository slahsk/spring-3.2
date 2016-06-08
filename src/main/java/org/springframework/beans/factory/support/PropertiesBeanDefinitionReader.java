package org.springframework.beans.factory.support;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyAccessor;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

public class PropertiesBeanDefinitionReader extends AbstractBeanDefinitionReader {

	public static final String SEPARATOR = ".";

	public PropertiesBeanDefinitionReader(BeanDefinitionRegistry registry) {
		super(registry);
	}

	public int registerBeanDefinitions(ResourceBundle rb) throws BeanDefinitionStoreException {
		return registerBeanDefinitions(rb, null);
	}

	public int registerBeanDefinitions(ResourceBundle rb, String prefix) throws BeanDefinitionStoreException {
		// Simply create a map and call overloaded method.
		Map<String, Object> map = new HashMap<String, Object>();
		Enumeration keys = rb.getKeys();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			map.put(key, rb.getObject(key));
		}
		return registerBeanDefinitions(map, prefix);
	}

	public int registerBeanDefinitions(Map map) throws BeansException {
		return registerBeanDefinitions(map, null);
	}

	public int registerBeanDefinitions(Map map, String prefix) throws BeansException {
		return registerBeanDefinitions(map, prefix, "Map " + map);
	}

	public int registerBeanDefinitions(Map map, String prefix, String resourceDescription) throws BeansException {

		if (prefix == null) {
			prefix = "";
		}
		int beanCount = 0;

		for (Object key : map.keySet()) {
			if (!(key instanceof String)) {
				throw new IllegalArgumentException("Illegal key [" + key + "]: only Strings allowed");
			}
			String keyString = (String) key;
			if (keyString.startsWith(prefix)) {
				// Key is of form: prefix<name>.property
				String nameAndProperty = keyString.substring(prefix.length());
				// Find dot before property name, ignoring dots in property
				// keys.
				int sepIdx = -1;
				int propKeyIdx = nameAndProperty.indexOf(PropertyAccessor.PROPERTY_KEY_PREFIX);
				if (propKeyIdx != -1) {
					sepIdx = nameAndProperty.lastIndexOf(SEPARATOR, propKeyIdx);
				} else {
					sepIdx = nameAndProperty.lastIndexOf(SEPARATOR);
				}

				if (sepIdx != -1) {
					String beanName = nameAndProperty.substring(0, sepIdx);
					if (logger.isDebugEnabled()) {
						logger.debug("Found bean name '" + beanName + "'");
					}
					if (!getRegistry().containsBeanDefinition(beanName)) {
						// If we haven't already registered it...
						registerBeanDefinition(beanName, map, prefix + beanName, resourceDescription);
						++beanCount;
					}
				}
				else {
					// Ignore it: It wasn't a valid bean name and property,
					// although it did start with the required prefix.
					if (logger.isDebugEnabled()) {
						logger.debug("Invalid bean name and property [" + nameAndProperty + "]");
					}
				}
			}
		}

		return beanCount;
	}
	
	protected void registerBeanDefinition(String beanName, Map<?, ?> map, String prefix, String resourceDescription)
			throws BeansException {

		String className = null;
		String parent = null;
		String scope = GenericBeanDefinition.SCOPE_SINGLETON;
		boolean isAbstract = false;
		boolean lazyInit = false;

		ConstructorArgumentValues cas = new ConstructorArgumentValues();
		MutablePropertyValues pvs = new MutablePropertyValues();

		for (Map.Entry entry : map.entrySet()) {
			String key = StringUtils.trimWhitespace((String) entry.getKey());
			if (key.startsWith(prefix + SEPARATOR)) {
				String property = key.substring(prefix.length() + SEPARATOR.length());
				if (CLASS_KEY.equals(property)) {
					className = StringUtils.trimWhitespace((String) entry.getValue());
				}
				else if (PARENT_KEY.equals(property)) {
					parent = StringUtils.trimWhitespace((String) entry.getValue());
				}
				else if (ABSTRACT_KEY.equals(property)) {
					String val = StringUtils.trimWhitespace((String) entry.getValue());
					isAbstract = TRUE_VALUE.equals(val);
				}
				else if (SCOPE_KEY.equals(property)) {
					// Spring 2.0 style
					scope = StringUtils.trimWhitespace((String) entry.getValue());
				}
				else if (SINGLETON_KEY.equals(property)) {
					// Spring 1.2 style
					String val = StringUtils.trimWhitespace((String) entry.getValue());
					scope = ((val == null || TRUE_VALUE.equals(val) ? GenericBeanDefinition.SCOPE_SINGLETON :
							GenericBeanDefinition.SCOPE_PROTOTYPE));
				}
				else if (LAZY_INIT_KEY.equals(property)) {
					String val = StringUtils.trimWhitespace((String) entry.getValue());
					lazyInit = TRUE_VALUE.equals(val);
				}
				else if (property.startsWith(CONSTRUCTOR_ARG_PREFIX)) {
					if (property.endsWith(REF_SUFFIX)) {
						int index = Integer.parseInt(property.substring(1, property.length() - REF_SUFFIX.length()));
						cas.addIndexedArgumentValue(index, new RuntimeBeanReference(entry.getValue().toString()));
					}
					else {
						int index = Integer.parseInt(property.substring(1));
						cas.addIndexedArgumentValue(index, readValue(entry));
					}
				}
				else if (property.endsWith(REF_SUFFIX)) {
					// This isn't a real property, but a reference to another prototype
					// Extract property name: property is of form dog(ref)
					property = property.substring(0, property.length() - REF_SUFFIX.length());
					String ref = StringUtils.trimWhitespace((String) entry.getValue());

					// It doesn't matter if the referenced bean hasn't yet been registered:
					// this will ensure that the reference is resolved at runtime.
					Object val = new RuntimeBeanReference(ref);
					pvs.add(property, val);
				}
				else {
					// It's a normal bean property.
					pvs.add(property, readValue(entry));
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Registering bean definition for bean name '" + beanName + "' with " + pvs);
		}

		// Just use default parent if we're not dealing with the parent itself,
		// and if there's no class name specified. The latter has to happen for
		// backwards compatibility reasons.
		if (parent == null && className == null && !beanName.equals(this.defaultParentBean)) {
			parent = this.defaultParentBean;
		}

		try {
			AbstractBeanDefinition bd = BeanDefinitionReaderUtils.createBeanDefinition(
					parent, className, getBeanClassLoader());
			bd.setScope(scope);
			bd.setAbstract(isAbstract);
			bd.setLazyInit(lazyInit);
			bd.setConstructorArgumentValues(cas);
			bd.setPropertyValues(pvs);
			getRegistry().registerBeanDefinition(beanName, bd);
		}
		catch (ClassNotFoundException ex) {
			throw new CannotLoadBeanClassException(resourceDescription, beanName, className, ex);
		}
		catch (LinkageError err) {
			throw new CannotLoadBeanClassException(resourceDescription, beanName, className, err);
		}
	}


	@Override
	public ResourceLoader getResourceLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClassLoader getBeanClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BeanNameGenerator getBeanNameGenerator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int loadBeanDefinitions(Resource... resources) throws BeanDefinitionStoreException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int loadBeanDefinitions(String location) throws BeanDefinitionStoreException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Environment getEnvironment() {
		// TODO Auto-generated method stub
		return null;
	}

}
