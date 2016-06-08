package org.springframework.core.env;

public class StandardEnvironment extends AbstractEnvironment{

	@Override
	public boolean containsProperty(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getProperty(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getProperty(String key, Class<T> targetType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Class<T> getPropertyAsClass(String key, Class<T> targetType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequiredProperty(String key) throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String resolvePlaceholders(String text) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String resolveRequiredPlaceholders(String text) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

}
