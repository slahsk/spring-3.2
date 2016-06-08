package org.springframework.beans.factory.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;

public abstract class AbstractBeanDefinitionReader implements EnvironmentCapable, BeanDefinitionReader{
	protected final Log logger = LogFactory.getLog(getClass());
	
	private final BeanDefinitionRegistry registry;
	
	private ResourceLoader resourceLoader;
	
	private Environment environment;
	
	protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
		this.registry = registry;

		// Determine ResourceLoader to use.
		if (this.registry instanceof ResourceLoader) {
			this.resourceLoader = (ResourceLoader) this.registry;
		}
		else {
			this.resourceLoader = new PathMatchingResourcePatternResolver();
		}

		// Inherit Environment if possible
		if (this.registry instanceof EnvironmentCapable) {
			this.environment = ((EnvironmentCapable) this.registry).getEnvironment();
		}
		else {
			this.environment = new StandardEnvironment();
		}
	}
	
	public final BeanDefinitionRegistry getRegistry() {
		return this.registry;
	}
}
