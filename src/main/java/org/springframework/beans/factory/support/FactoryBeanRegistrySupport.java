package org.springframework.beans.factory.support;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanCurrentlyInCreationException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.FactoryBeanNotInitializedException;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

	private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>(16);

	protected Class<?> getTypeForFactoryBean(final FactoryBean<?> factoryBean) {
		try {
			if (System.getSecurityManager() != null) {
				return AccessController.doPrivileged(new PrivilegedAction<Class<?>>() {
					public Class<?> run() {
						return factoryBean.getObjectType();
					}
				}, getAccessControlContext());
			} else {
				return factoryBean.getObjectType();
			}
		} catch (Throwable ex) {
			logger.warn("FactoryBean threw exception from getObjectType, despite the contract saying "
					+ "that it should return null if the type of its object cannot be determined yet", ex);
			return null;
		}
	}

	protected Object getCachedObjectForFactoryBean(String beanName) {
		Object object = this.factoryBeanObjectCache.get(beanName);
		return (object != NULL_OBJECT ? object : null);
	}

	protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName, boolean shouldPostProcess) {
		if (factory.isSingleton() && containsSingleton(beanName)) {
			synchronized (getSingletonMutex()) {
				//빈 가져오기
				Object object = this.factoryBeanObjectCache.get(beanName);
				
				if (object == null) {
					//FactoryBean 에서 실제 오브젝트 가져오기
					object = doGetObjectFromFactoryBean(factory, beanName);
					
					Object alreadyThere = this.factoryBeanObjectCache.get(beanName);
					if (alreadyThere != null) {
						//캐쉬에 객체가 있으면 캐쉬에 있는 객체로 할당
						object = alreadyThere;
					} else {
						//캐쉬에 저장전 해야할 일이 있으면
						if (object != null && shouldPostProcess) {
							try {
								object = postProcessObjectFromFactoryBean(object, beanName);
							} catch (Throwable ex) {
								throw new BeanCreationException(beanName, "Post-processing of FactoryBean's singleton object failed", ex);
							}
						}
						this.factoryBeanObjectCache.put(beanName, (object != null ? object : NULL_OBJECT));
					}
				}
				return (object != NULL_OBJECT ? object : null);
			}
		} else {
			Object object = doGetObjectFromFactoryBean(factory, beanName);
			if (object != null && shouldPostProcess) {
				try {
					object = postProcessObjectFromFactoryBean(object, beanName);
				} catch (Throwable ex) {
					throw new BeanCreationException(beanName, "Post-processing of FactoryBean's object failed", ex);
				}
			}
			return object;
		}
	}

	private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, final String beanName) throws BeanCreationException {

		Object object;
		try {
			if (System.getSecurityManager() != null) {
				//접근 제어 하는 먼가 있는거같음 스프링 시큐리티랑 관련 있는거 같음
				AccessControlContext acc = getAccessControlContext();
				try {
					object = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {
						public Object run() throws Exception {
							return factory.getObject();
						}
					}, acc);
				} catch (PrivilegedActionException pae) {
					throw pae.getException();
				}
			} else {
				object = factory.getObject();
			}
		} catch (FactoryBeanNotInitializedException ex) {
			throw new BeanCurrentlyInCreationException(beanName, ex.toString());
		} catch (Throwable ex) {
			throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
		}

		if (object == null && isSingletonCurrentlyInCreation(beanName)) {
			throw new BeanCurrentlyInCreationException(beanName, "FactoryBean which is currently in creation returned null from getObject");
		}
		
		return object;
	}

	protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException {
		return object;
	}

	protected FactoryBean<?> getFactoryBean(String beanName, Object beanInstance) throws BeansException {
		if (!(beanInstance instanceof FactoryBean)) {
			throw new BeanCreationException(beanName, "Bean instance of type [" + beanInstance.getClass() + "] is not a FactoryBean");
		}
		return (FactoryBean<?>) beanInstance;
	}

	@Override
	protected void removeSingleton(String beanName) {
		super.removeSingleton(beanName);
		this.factoryBeanObjectCache.remove(beanName);
	}

	protected AccessControlContext getAccessControlContext() {
		return AccessController.getContext();
	}

}
