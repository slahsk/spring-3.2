package org.springframework.beans.factory.access;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator.BeanFactoryGroup;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator.CountingBeanFactoryReference;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;

public class SingletonBeanFactoryLocator implements BeanFactoryLocator {
	private static final String DEFAULT_RESOURCE_LOCATION = "classpath*:beanRefFactory.xml";

	protected static final Log logger = LogFactory.getLog(SingletonBeanFactoryLocator.class);

	private static final Map<String, BeanFactoryLocator> instances = new HashMap<String, BeanFactoryLocator>();

	public static BeanFactoryLocator getInstance() throws BeansException {
		return getInstance(null);
	}

	public static BeanFactoryLocator getInstance(String selector) throws BeansException {
		String resourceLocation = selector;
		if (resourceLocation == null) {
			resourceLocation = DEFAULT_RESOURCE_LOCATION;
		}

		// 파일이 존재 하는지 확인
		// 파일이 존재안하는경우
		if (!ResourcePatternUtils.isUrl(resourceLocation)) {
			resourceLocation = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resourceLocation;
		}

		synchronized (instances) {
			if (logger.isTraceEnabled()) {
				logger.trace("SingletonBeanFactoryLocator.getInstance(): instances.hashCode=" + instances.hashCode() + ", instances=" + instances);
			}
			// 빈팩토리 객체 가져오기
			BeanFactoryLocator bfl = instances.get(resourceLocation);

			// 빈팩토리가 없으면
			if (bfl == null) {
				// 객체생성에서 인스턴스 맵에 저장
				bfl = new SingletonBeanFactoryLocator(resourceLocation);
				instances.put(resourceLocation, bfl);
			}
			return bfl;
		}
	}

	private final Map<String, BeanFactoryGroup> bfgInstancesByKey = new HashMap<String, BeanFactoryGroup>();

	private final Map<BeanFactory, BeanFactoryGroup> bfgInstancesByObj = new HashMap<BeanFactory, BeanFactoryGroup>();

	private final String resourceLocation;

	protected SingletonBeanFactoryLocator(String resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

	public BeanFactoryReference useBeanFactory(String factoryKey) throws BeansException {
		synchronized (this.bfgInstancesByKey) {
			BeanFactoryGroup bfg = this.bfgInstancesByKey.get(this.resourceLocation);
			
			if (bfg != null) {
				bfg.refCount++;
			} else {
				if (logger.isTraceEnabled()) {
					logger.trace("Factory group with resource name [" + this.resourceLocation + "] requested. Creating new instance.");
				}

				BeanFactory groupContext = createDefinition(this.resourceLocation, factoryKey);

				bfg = new BeanFactoryGroup();
				bfg.definition = groupContext;
				bfg.refCount = 1;
				this.bfgInstancesByKey.put(this.resourceLocation, bfg);
				this.bfgInstancesByObj.put(groupContext, bfg);

				try {
					initializeDefinition(groupContext);
				} catch (BeansException ex) {
					//초기화 못하면 삭제
					this.bfgInstancesByKey.remove(this.resourceLocation);
					this.bfgInstancesByObj.remove(groupContext);
					throw new BootstrapException("Unable to initialize group definition. " + "Group resource name [" + this.resourceLocation + "], factory key [" + factoryKey
							+ "]", ex);
				}
			}

			try {
				BeanFactory beanFactory;
				if (factoryKey != null) {
					beanFactory = bfg.definition.getBean(factoryKey, BeanFactory.class);
				} else {
					beanFactory = bfg.definition.getBean(BeanFactory.class);
				}
				return new CountingBeanFactoryReference(beanFactory, bfg.definition);
			} catch (BeansException ex) {
				throw new BootstrapException("Unable to return specified BeanFactory instance: factory key [" + factoryKey + "], from group with resource name ["
						+ this.resourceLocation + "]", ex);
			}

		}
	}

	protected BeanFactory createDefinition(String resourceLocation, String factoryKey) {
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
		ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

		try {
			Resource[] configResources = resourcePatternResolver.getResources(resourceLocation);
			if (configResources.length == 0) {
				throw new FatalBeanException("Unable to find resource for specified definition. " + "Group resource name [" + this.resourceLocation + "], factory key ["
						+ factoryKey + "]");
			}
			reader.loadBeanDefinitions(configResources);
		} catch (IOException ex) {
			throw new BeanDefinitionStoreException("Error accessing bean definition resource [" + this.resourceLocation + "]", ex);
		} catch (BeanDefinitionStoreException ex) {
			throw new FatalBeanException("Unable to load group definition: " + "group resource name [" + this.resourceLocation + "], factory key [" + factoryKey + "]", ex);
		}

		return factory;
	}

	protected void initializeDefinition(BeanFactory groupDef) {
		if (groupDef instanceof ConfigurableListableBeanFactory) {
			((ConfigurableListableBeanFactory) groupDef).preInstantiateSingletons();
		}
	}

	protected void destroyDefinition(BeanFactory groupDef, String selector) {
		if (groupDef instanceof ConfigurableBeanFactory) {
			if (logger.isTraceEnabled()) {
				logger.trace("Factory group with selector '" + selector + "' being released, as there are no more references to it");
			}
			((ConfigurableBeanFactory) groupDef).destroySingletons();
		}
	}

	private static class BeanFactoryGroup {

		private BeanFactory definition;

		private int refCount = 0;
	}

	private class CountingBeanFactoryReference implements BeanFactoryReference {

		private BeanFactory beanFactory;

		private BeanFactory groupContextRef;

		public CountingBeanFactoryReference(BeanFactory beanFactory, BeanFactory groupContext) {
			this.beanFactory = beanFactory;
			this.groupContextRef = groupContext;
		}

		public BeanFactory getFactory() {
			return this.beanFactory;
		}

		// Note that it's legal to call release more than once!
		public void release() throws FatalBeanException {
			synchronized (bfgInstancesByKey) {
				BeanFactory savedRef = this.groupContextRef;
				if (savedRef != null) {
					this.groupContextRef = null;
					BeanFactoryGroup bfg = bfgInstancesByObj.get(savedRef);
					if (bfg != null) {
						bfg.refCount--;
						if (bfg.refCount == 0) {
							destroyDefinition(savedRef, resourceLocation);
							bfgInstancesByKey.remove(resourceLocation);
							bfgInstancesByObj.remove(savedRef);
						}
					} else {
						// This should be impossible.
						logger.warn("Tried to release a SingletonBeanFactoryLocator group definition " + "more times than it has actually been used. Resource name ["
								+ resourceLocation + "]");
					}
				}
			}
		}
	}
}
