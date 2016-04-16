package org.springframework.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.StringValueResolver;

public class SimpleAliasRegistry implements AliasRegistry {
	private final Map<String, String> aliasMap = new ConcurrentHashMap<String, String>(16);

	public void registerAlias(String name, String alias) {
		Assert.hasText(name, "'name' must not be empty");
		Assert.hasText(alias, "'alias' must not be empty");
		if (alias.equals(name)) {
			this.aliasMap.remove(alias);
		} else {
			if (!allowAliasOverriding()) {
				String registeredName = this.aliasMap.get(alias);
				if (registeredName != null && !registeredName.equals(name)) {
					throw new IllegalStateException("Cannot register alias '" + alias + "' for name '" + name + "': It is already registered for name '" + registeredName + "'.");
				}
			}
			checkForAliasCircle(name, alias);
			this.aliasMap.put(alias, name);
		}
	}

	protected boolean allowAliasOverriding() {
		return true;
	}

	public void removeAlias(String alias) {
		String name = this.aliasMap.remove(alias);
		if (name == null) {
			throw new IllegalStateException("No alias '" + alias + "' registered");
		}
	}

	public boolean isAlias(String name) {
		return this.aliasMap.containsKey(name);
	}

	public String[] getAliases(String name) {
		List<String> result = new ArrayList<String>();
		synchronized (this.aliasMap) {
			retrieveAliases(name, result);
		}
		return StringUtils.toStringArray(result);
	}

	private void retrieveAliases(String name, List<String> result) {
		for (Map.Entry<String, String> entry : this.aliasMap.entrySet()) {
			String registeredName = entry.getValue();
			if (registeredName.equals(name)) {
				String alias = entry.getKey();
				result.add(alias);
				// alias 이름으로 다시 검색하기
				retrieveAliases(alias, result);
			}
		}
	}

	public void resolveAliases(StringValueResolver valueResolver) {
		Assert.notNull(valueResolver, "StringValueResolver must not be null");
		synchronized (this.aliasMap) {
			Map<String, String> aliasCopy = new HashMap<String, String>(this.aliasMap);
			for (String alias : aliasCopy.keySet()) {
				String registeredName = aliasCopy.get(alias);
				String resolvedAlias = valueResolver.resolveStringValue(alias);
				String resolvedName = valueResolver.resolveStringValue(registeredName);
				if (resolvedAlias.equals(resolvedName)) {
					this.aliasMap.remove(alias);
				} else if (!resolvedAlias.equals(alias)) {
					String existingName = this.aliasMap.get(resolvedAlias);
					//값가지고 다시 맵을 조회하여 있는지 검사하기
					if (existingName != null && !existingName.equals(resolvedName)) {
						throw new IllegalStateException("Cannot register resolved alias '" + resolvedAlias + "' (original: '" + alias + "') for name '" + resolvedName
								+ "': It is already registered for name '" + registeredName + "'.");
					}
					//맵에 같은게 있는지 검사
					checkForAliasCircle(resolvedName, resolvedAlias);
					this.aliasMap.remove(alias);
					this.aliasMap.put(resolvedAlias, resolvedName);
				} else if (!registeredName.equals(resolvedName)) {
					this.aliasMap.put(alias, resolvedName);
				}
			}
		}
	}

	public String canonicalName(String name) {
		String canonicalName = name;
		String resolvedName;
		do {
			resolvedName = this.aliasMap.get(canonicalName);
			if (resolvedName != null) {
				canonicalName = resolvedName;
			}
		} while (resolvedName != null);
		return canonicalName;
	}

	protected void checkForAliasCircle(String name, String alias) {
		if (alias.equals(canonicalName(name))) {
			throw new IllegalStateException("Cannot register alias '" + alias + "' for name '" + name + "': Circular reference - '" + name
					+ "' is a direct or indirect alias for '" + alias + "' already");
		}
	}
}
