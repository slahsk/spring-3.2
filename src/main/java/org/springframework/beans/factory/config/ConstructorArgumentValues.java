package org.springframework.beans.factory.config;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.Mergeable;
import org.springframework.util.ObjectUtils;

public class ConstructorArgumentValues {
	
	private final Map<Integer, ValueHolder> indexedArgumentValues = new LinkedHashMap<Integer, ValueHolder>(0);

	private final List<ValueHolder> genericArgumentValues = new LinkedList<ValueHolder>();
	
	public ConstructorArgumentValues() {
	}
	
	public ConstructorArgumentValues(ConstructorArgumentValues original) {
		addArgumentValues(original);
	}
	
	public void addArgumentValues(ConstructorArgumentValues other) {
		if (other != null) {
			for (Map.Entry<Integer, ValueHolder> entry : other.indexedArgumentValues.entrySet()) {
				addOrMergeIndexedArgumentValue(entry.getKey(), entry.getValue().copy());
			}
			for (ValueHolder valueHolder : other.genericArgumentValues) {
				if (!this.genericArgumentValues.contains(valueHolder)) {
					addOrMergeGenericArgumentValue(valueHolder.copy());
				}
			}
		}
	}
	
	private void addOrMergeGenericArgumentValue(ValueHolder newValue) {
		if (newValue.getName() != null) {
			for (Iterator<ValueHolder> it = this.genericArgumentValues.iterator(); it.hasNext();) {
				ValueHolder currentValue = it.next();
				if (newValue.getName().equals(currentValue.getName())) {
					if (newValue.getValue() instanceof Mergeable) {
						Mergeable mergeable = (Mergeable) newValue.getValue();
						if (mergeable.isMergeEnabled()) {
							newValue.setValue(mergeable.merge(currentValue.getValue()));
						}
					}
					it.remove();
				}
			}
		}
		this.genericArgumentValues.add(newValue);
	}
	
	private void addOrMergeIndexedArgumentValue(Integer key, ValueHolder newValue) {
		ValueHolder currentValue = this.indexedArgumentValues.get(key);
		if (currentValue != null && newValue.getValue() instanceof Mergeable) {
			Mergeable mergeable = (Mergeable) newValue.getValue();
			if (mergeable.isMergeEnabled()) {
				newValue.setValue(mergeable.merge(currentValue.getValue()));
			}
		}
		this.indexedArgumentValues.put(key, newValue);
	}
	
	public void addIndexedArgumentValue(int index, Object value) {
		addIndexedArgumentValue(index, new ValueHolder(value));
	}
	
	public static class ValueHolder implements BeanMetadataElement {

		private Object value;

		private String type;

		private String name;

		private Object source;

		private boolean converted = false;

		private Object convertedValue;

		/**
		 * Create a new ValueHolder for the given value.
		 * @param value the argument value
		 */
		public ValueHolder(Object value) {
			this.value = value;
		}

		/**
		 * Create a new ValueHolder for the given value and type.
		 * @param value the argument value
		 * @param type the type of the constructor argument
		 */
		public ValueHolder(Object value, String type) {
			this.value = value;
			this.type = type;
		}

		/**
		 * Create a new ValueHolder for the given value, type and name.
		 * @param value the argument value
		 * @param type the type of the constructor argument
		 * @param name the name of the constructor argument
		 */
		public ValueHolder(Object value, String type, String name) {
			this.value = value;
			this.type = type;
			this.name = name;
		}

		/**
		 * Set the value for the constructor argument.
		 * @see PropertyPlaceholderConfigurer
		 */
		public void setValue(Object value) {
			this.value = value;
		}

		/**
		 * Return the value for the constructor argument.
		 */
		public Object getValue() {
			return this.value;
		}

		/**
		 * Set the type of the constructor argument.
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * Return the type of the constructor argument.
		 */
		public String getType() {
			return this.type;
		}

		/**
		 * Set the name of the constructor argument.
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Return the name of the constructor argument.
		 */
		public String getName() {
			return this.name;
		}

		/**
		 * Set the configuration source {@code Object} for this metadata element.
		 * <p>The exact type of the object will depend on the configuration mechanism used.
		 */
		public void setSource(Object source) {
			this.source = source;
		}

		public Object getSource() {
			return this.source;
		}

		/**
		 * Return whether this holder contains a converted value already ({@code true}),
		 * or whether the value still needs to be converted ({@code false}).
		 */
		public synchronized boolean isConverted() {
			return this.converted;
		}

		/**
		 * Set the converted value of the constructor argument,
		 * after processed type conversion.
		 */
		public synchronized void setConvertedValue(Object value) {
			this.converted = true;
			this.convertedValue = value;
		}

		/**
		 * Return the converted value of the constructor argument,
		 * after processed type conversion.
		 */
		public synchronized Object getConvertedValue() {
			return this.convertedValue;
		}

		/**
		 * Determine whether the content of this ValueHolder is equal
		 * to the content of the given other ValueHolder.
		 * <p>Note that ValueHolder does not implement {@code equals}
		 * directly, to allow for multiple ValueHolder instances with the
		 * same content to reside in the same Set.
		 */
		private boolean contentEquals(ValueHolder other) {
			return (this == other ||
					(ObjectUtils.nullSafeEquals(this.value, other.value) && ObjectUtils.nullSafeEquals(this.type, other.type)));
		}

		/**
		 * Determine whether the hash code of the content of this ValueHolder.
		 * <p>Note that ValueHolder does not implement {@code hashCode}
		 * directly, to allow for multiple ValueHolder instances with the
		 * same content to reside in the same Set.
		 */
		private int contentHashCode() {
			return ObjectUtils.nullSafeHashCode(this.value) * 29 + ObjectUtils.nullSafeHashCode(this.type);
		}

		/**
		 * Create a copy of this ValueHolder: that is, an independent
		 * ValueHolder instance with the same contents.
		 */
		public ValueHolder copy() {
			ValueHolder copy = new ValueHolder(this.value, this.type, this.name);
			copy.setSource(this.source);
			return copy;
		}
	}

}
