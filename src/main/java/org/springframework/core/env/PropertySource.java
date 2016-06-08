package org.springframework.core.env;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class  PropertySource<T> {
	
	protected final Log logger = LogFactory.getLog(getClass());

	protected final String name="";
	
	public String getName() {
		return this.name;
	}

}
