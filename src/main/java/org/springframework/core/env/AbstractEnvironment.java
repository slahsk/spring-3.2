package org.springframework.core.env;

import static java.lang.String.format;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractEnvironment implements ConfigurableEnvironment{
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private final MutablePropertySources propertySources = new MutablePropertySources(this.logger);
	
	
	public AbstractEnvironment() {
		customizePropertySources(this.propertySources);
		if (this.logger.isDebugEnabled()) {
			this.logger.debug(format(
					"Initialized %s with PropertySources %s", getClass().getSimpleName(), this.propertySources));
		}
	}
	
	protected void customizePropertySources(MutablePropertySources propertySources) {
	}

}
