package org.springframework.core.env;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Spliterator;
import java.util.function.Consumer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MutablePropertySources implements PropertySources{
	
	private final Log logger;
	
	private final LinkedList<PropertySource<?>> propertySourceList = new LinkedList<PropertySource<?>>();
	
	
	public MutablePropertySources() {
		this.logger = LogFactory.getLog(this.getClass());
	}
	
	public MutablePropertySources(PropertySources propertySources) {
		this();
		for (PropertySource<?> propertySource : propertySources) {
			this.addLast(propertySource);
		}
	}
	
	MutablePropertySources(Log logger) {
		this.logger = logger;
	}
	
	public void addLast(PropertySource<?> propertySource) {
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("Adding [%s] PropertySource with lowest search precedence",
					propertySource.getName()));
		}
		removeIfPresent(propertySource);
		this.propertySourceList.addLast(propertySource);
	}
	
	protected void removeIfPresent(PropertySource<?> propertySource) {
		if (this.propertySourceList.contains(propertySource)) {
			this.propertySourceList.remove(propertySource);
		}
	}

	@Override
	public Iterator<PropertySource<?>> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void forEach(Consumer<? super PropertySource<?>> action) {
		// TODO Auto-generated method stub
	//	Iterable.super.forEach(action);
	}

	@Override
	public Spliterator<PropertySource<?>> spliterator() {
		// TODO Auto-generated method stub
		return null;
	}

}
