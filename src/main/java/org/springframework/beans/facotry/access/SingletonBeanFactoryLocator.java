package org.springframework.beans.facotry.access;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.facotry.BeanFactory;
import org.springframework.beans.factory.access.BeanFactoryReference;
import org.springframework.beans.factory.access.BootstrapException;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator.BeanFactoryGroup;
import org.springframework.beans.factory.access.SingletonBeanFactoryLocator.CountingBeanFactoryReference;
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
		
		//파일이 존재 하는지 확인
		//파일이 존재안하는경우
		if (!ResourcePatternUtils.isUrl(resourceLocation)) {
			resourceLocation = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resourceLocation;
		}

		synchronized (instances) {
			if (logger.isTraceEnabled()) {
				logger.trace("SingletonBeanFactoryLocator.getInstance(): instances.hashCode=" + instances.hashCode() + ", instances=" + instances);
			}
			//빈팩토리 객체 가져오기
			BeanFactoryLocator bfl = instances.get(resourceLocation);
			
			//빈팩토리가 없으면
			if (bfl == null) {
				//객체생성에서 인스턴스 맵에 저장
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
			}
			else {
				// This group definition doesn't exist, we need to try to load it.
				if (logger.isTraceEnabled()) {
					logger.trace("Factory group with resource name [" + this.resourceLocation +
							"] requested. Creating new instance.");
				}

				// Create the BeanFactory but don't initialize it.
				BeanFactory groupContext = createDefinition(this.resourceLocation, factoryKey);

				// Record its existence now, before instantiating any singletons.
				bfg = new BeanFactoryGroup();
				bfg.definition = groupContext;
				bfg.refCount = 1;
				this.bfgInstancesByKey.put(this.resourceLocation, bfg);
				this.bfgInstancesByObj.put(groupContext, bfg);

				// Now initialize the BeanFactory. This may cause a re-entrant invocation
				// of this method, but since we've already added the BeanFactory to our
				// mappings, the next time it will be found and simply have its
				// reference count incremented.
				try {
					initializeDefinition(groupContext);
				}
				catch (BeansException ex) {
					this.bfgInstancesByKey.remove(this.resourceLocation);
					this.bfgInstancesByObj.remove(groupContext);
					throw new BootstrapException("Unable to initialize group definition. " +
							"Group resource name [" + this.resourceLocation + "], factory key [" + factoryKey + "]", ex);
				}
			}

			try {
				BeanFactory beanFactory;
				if (factoryKey != null) {
					beanFactory = bfg.definition.getBean(factoryKey, BeanFactory.class);
				}
				else {
					beanFactory = bfg.definition.getBean(BeanFactory.class);
				}
				return new CountingBeanFactoryReference(beanFactory, bfg.definition);
			}
			catch (BeansException ex) {
				throw new BootstrapException("Unable to return specified BeanFactory instance: factory key [" +
						factoryKey + "], from group with resource name [" + this.resourceLocation + "]", ex);
			}

		}
	}
	
	private static class BeanFactoryGroup {

		private BeanFactory definition;

		private int refCount = 0;
	}
}
