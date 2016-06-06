package org.springframework.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MutablePropertyValues implements PropertyValues, Serializable {
	private final List<PropertyValue> propertyValueList;
	public MutablePropertyValues() {
		this.propertyValueList = new ArrayList<PropertyValue>(0);
	}
	
	public MutablePropertyValues(PropertyValues original) {
		if (original != null) {
			PropertyValue[] pvs = original.getPropertyValues();
			this.propertyValueList = new ArrayList<PropertyValue>(pvs.length);
			for (PropertyValue pv : pvs) {
				this.propertyValueList.add(new PropertyValue(pv));
			}
		}
		else {
			this.propertyValueList = new ArrayList<PropertyValue>(0);
		}
	}
	
	@Override
	public PropertyValue[] getPropertyValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyValue getPropertyValue(String propertyName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PropertyValues changesSince(PropertyValues old) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean contains(String propertyName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
