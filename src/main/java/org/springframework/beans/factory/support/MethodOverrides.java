package org.springframework.beans.factory.support;

import java.util.HashSet;
import java.util.Set;

public class MethodOverrides {
	
	private final Set<MethodOverride> overrides = new HashSet<MethodOverride>(0);
	
	public MethodOverrides() {
	}

	public MethodOverrides(MethodOverrides other) {
		addOverrides(other);
	}
	
	public void addOverrides(MethodOverrides other) {
		if (other != null) {
			this.overrides.addAll(other.getOverrides());
		}
	}

	public Set<MethodOverride> getOverrides() {
		return overrides;
	}
	
	

}
