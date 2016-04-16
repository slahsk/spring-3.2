package org.springframework.beans.factory.support;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.core.SimpleAliasRegistry;
import org.springframework.util.Assert;

public class DefaultSingletonBeanRegistry extends SimpleAliasRegistry implements SingletonBeanRegistry {
	protected static final Object NULL_OBJECT = new Object();
	protected final Log logger = LogFactory.getLog(getClass());
	private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(64);
	private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>(16);
	private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);
	private final Set<String> registeredSingletons = new LinkedHashSet<String>(64);
	private final Map<String, Boolean> singletonsCurrentlyInCreation = new ConcurrentHashMap<String, Boolean>(16);
	private final Map<String, Boolean> inCreationCheckExclusions = new ConcurrentHashMap<String, Boolean>(16);
	private Set<Exception> suppressedExceptions;
	private boolean singletonsCurrentlyInDestruction = false;
	private final Map<String, Object> disposableBeans = new LinkedHashMap<String, Object>();
	private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap<String, Set<String>>(16);
	private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<String, Set<String>>(64);
	private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<String, Set<String>>(64);

	public void registerSingleton(String beanName, Object singletonObject) throws IllegalStateException {
		Assert.notNull(beanName, "'beanName' must not be null");
		synchronized (this.singletonObjects) {
			Object oldObject = this.singletonObjects.get(beanName);
			if (oldObject != null) {
				throw new IllegalStateException("Could not register object [" + singletonObject + "] under bean name '" + beanName + "': there is already object [" + oldObject
						+ "] bound");
			}
			addSingleton(beanName, singletonObject);
		}
	}
	
	protected void addSingleton(String beanName, Object singletonObject) {
		synchronized (this.singletonObjects) {
			this.singletonObjects.put(beanName, (singletonObject != null ? singletonObject : NULL_OBJECT));
			this.singletonFactories.remove(beanName);
			this.earlySingletonObjects.remove(beanName);
			this.registeredSingletons.add(beanName);
		}
	}
	
	protected void addSingletonFactory(String beanName, ObjectFactory singletonFactory) {
		Assert.notNull(singletonFactory, "Singleton factory must not be null");
		synchronized (this.singletonObjects) {
			if (!this.singletonObjects.containsKey(beanName)) {
				this.singletonFactories.put(beanName, singletonFactory);
				this.earlySingletonObjects.remove(beanName);
				this.registeredSingletons.add(beanName);
			}
		}
	}

	public Object getSingleton(String beanName) {
		return getSingleton(beanName, true);
	}
	
	protected Object getSingleton(String beanName, boolean allowEarlyReference) {
		Object singletonObject = this.singletonObjects.get(beanName);
		if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
			synchronized (this.singletonObjects) {
				singletonObject = this.earlySingletonObjects.get(beanName);
				if (singletonObject == null && allowEarlyReference) {
					ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
					if (singletonFactory != null) {
						singletonObject = singletonFactory.getObject();
						this.earlySingletonObjects.put(beanName, singletonObject);
						this.singletonFactories.remove(beanName);
					}
				}
			}
		}
		return (singletonObject != NULL_OBJECT ? singletonObject : null);
	}
}
