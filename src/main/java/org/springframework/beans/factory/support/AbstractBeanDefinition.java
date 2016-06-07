package org.springframework.beans.factory.support;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.BeanMetadataAttributeAccessor;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.core.io.DescriptiveResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

@SuppressWarnings("serial")
public abstract class AbstractBeanDefinition extends BeanMetadataAttributeAccessor implements BeanDefinition, Cloneable {
	public static final String SCOPE_DEFAULT = "";
	
	public static final int AUTOWIRE_NO = AutowireCapableBeanFactory.AUTOWIRE_NO;
	
	public static final int DEPENDENCY_CHECK_NONE = 0;
	
	private String scope = SCOPE_DEFAULT;
	
	private boolean primary = false;
	
	private boolean nonPublicAccessAllowed = true;
	
	private boolean lenientConstructorResolution = true;
	
	private final Map<String, AutowireCandidateQualifier> qualifiers = new LinkedHashMap<String, AutowireCandidateQualifier>(0);
	
	private int autowireMode = AUTOWIRE_NO;
	
	private int dependencyCheck = DEPENDENCY_CHECK_NONE;
	
	private String[] dependsOn;
	
	private boolean autowireCandidate = true;

	private boolean prototype = false;
	
	private boolean abstractFlag = false;
	
	private boolean singleton = true;
	
	private boolean lazyInit = false;
	
	private volatile Object beanClass;
	
	private String factoryBeanName;
	
	private String factoryMethodName;

	private String initMethodName;

	private String destroyMethodName;

	private boolean enforceInitMethod = true;

	private boolean enforceDestroyMethod = true;

	private boolean synthetic = false;

	private int role = BeanDefinition.ROLE_APPLICATION;

	private String description;

	private Resource resource;
	
	private MethodOverrides methodOverrides = new MethodOverrides();
	
	private ConstructorArgumentValues constructorArgumentValues;
	
	private MutablePropertyValues propertyValues;
	
	
	protected AbstractBeanDefinition(BeanDefinition original) {
		setParentName(original.getParentName());
		setBeanClassName(original.getBeanClassName());
		setFactoryBeanName(original.getFactoryBeanName());
		setFactoryMethodName(original.getFactoryMethodName());
		setScope(original.getScope());
		setAbstract(original.isAbstract());
		setLazyInit(original.isLazyInit());
		setRole(original.getRole());
		setConstructorArgumentValues(new ConstructorArgumentValues(original.getConstructorArgumentValues()));
		setPropertyValues(new MutablePropertyValues(original.getPropertyValues()));
		setSource(original.getSource());
		copyAttributesFrom(original);
		
		if (original instanceof AbstractBeanDefinition) {
			AbstractBeanDefinition originalAbd = (AbstractBeanDefinition) original;
			if (originalAbd.hasBeanClass()) {
				setBeanClass(originalAbd.getBeanClass());
			}
			setAutowireMode(originalAbd.getAutowireMode());
			setDependencyCheck(originalAbd.getDependencyCheck());
			setDependsOn(originalAbd.getDependsOn());
			setAutowireCandidate(originalAbd.isAutowireCandidate());
			copyQualifiersFrom(originalAbd);
			setPrimary(originalAbd.isPrimary());
			setNonPublicAccessAllowed(originalAbd.isNonPublicAccessAllowed());
			setLenientConstructorResolution(originalAbd.isLenientConstructorResolution());
			setInitMethodName(originalAbd.getInitMethodName());
			setEnforceInitMethod(originalAbd.isEnforceInitMethod());
			setDestroyMethodName(originalAbd.getDestroyMethodName());
			setEnforceDestroyMethod(originalAbd.isEnforceDestroyMethod());
			setMethodOverrides(new MethodOverrides(originalAbd.getMethodOverrides()));
			setSynthetic(originalAbd.isSynthetic());
			setResource(originalAbd.getResource());
		}
		else {
			setResourceDescription(original.getResourceDescription());
		}
		
	}
	
	
	public void copyQualifiersFrom(AbstractBeanDefinition source) {
		Assert.notNull(source, "Source must not be null");
		this.qualifiers.putAll(source.qualifiers);
	}
	
	
	public void setResourceDescription(String resourceDescription) {
		this.resource = new DescriptiveResource(resourceDescription);
	}
	
	public boolean isSynthetic() {
		return synthetic;
	}


	public void setSynthetic(boolean synthetic) {
		this.synthetic = synthetic;
	}


	public Resource getResource() {
		return resource;
	}


	public void setResource(Resource resource) {
		this.resource = resource;
	}


	public MethodOverrides getMethodOverrides() {
		return methodOverrides;
	}


	public void setMethodOverrides(MethodOverrides methodOverrides) {
		this.methodOverrides = methodOverrides;
	}


	public boolean isEnforceDestroyMethod() {
		return enforceDestroyMethod;
	}


	public void setEnforceDestroyMethod(boolean enforceDestroyMethod) {
		this.enforceDestroyMethod = enforceDestroyMethod;
	}


	public String getDestroyMethodName() {
		return destroyMethodName;
	}


	public void setDestroyMethodName(String destroyMethodName) {
		this.destroyMethodName = destroyMethodName;
	}


	public boolean isEnforceInitMethod() {
		return enforceInitMethod;
	}


	public void setEnforceInitMethod(boolean enforceInitMethod) {
		this.enforceInitMethod = enforceInitMethod;
	}


	public String getInitMethodName() {
		return initMethodName;
	}


	public void setInitMethodName(String initMethodName) {
		this.initMethodName = initMethodName;
	}


	public boolean isLenientConstructorResolution() {
		return lenientConstructorResolution;
	}


	public void setLenientConstructorResolution(boolean lenientConstructorResolution) {
		this.lenientConstructorResolution = lenientConstructorResolution;
	}


	public boolean isNonPublicAccessAllowed() {
		return nonPublicAccessAllowed;
	}


	public void setNonPublicAccessAllowed(boolean nonPublicAccessAllowed) {
		this.nonPublicAccessAllowed = nonPublicAccessAllowed;
	}


	public boolean isPrimary() {
		return primary;
	}


	public void setPrimary(boolean primary) {
		this.primary = primary;
	}


	public void setAutowireCandidate(boolean autowireCandidate) {
		this.autowireCandidate = autowireCandidate;
	}

	public boolean isAutowireCandidate() {
		return this.autowireCandidate;
	}
	
	public void setDependsOn(String[] dependsOn) {
		this.dependsOn = dependsOn;
	}

	public String[] getDependsOn() {
		return this.dependsOn;
	}
	
	public void setDependencyCheck(int dependencyCheck) {
		this.dependencyCheck = dependencyCheck;
	}


	public int getDependencyCheck() {
		return this.dependencyCheck;
	}
	
	public void setAutowireMode(int autowireMode) {
		this.autowireMode = autowireMode;
	}


	public int getAutowireMode() {
		return this.autowireMode;
	}
	
	public Class<?> getBeanClass() throws IllegalStateException {
		Object beanClassObject = this.beanClass;
		if (beanClassObject == null) {
			throw new IllegalStateException("No bean class specified on bean definition");
		}
		if (!(beanClassObject instanceof Class)) {
			throw new IllegalStateException(
					"Bean class name [" + beanClassObject + "] has not been resolved into an actual Class");
		}
		return (Class<?>) beanClassObject;
	}
	
	public boolean hasBeanClass() {
		return (this.beanClass instanceof Class);
	}
	
	public void setBeanClassName(String beanClassName) {
		this.beanClass = beanClassName;
	}
	
	public void setFactoryBeanName(String factoryBeanName) {
		this.factoryBeanName = factoryBeanName;
	}
	
	public void setFactoryMethodName(String factoryMethodName) {
		this.factoryMethodName = factoryMethodName;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
		this.singleton = SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
		this.prototype = SCOPE_PROTOTYPE.equals(scope);
	}
	
	public void setLazyInit(boolean lazyInit) {
		this.lazyInit = lazyInit;
	}
	
	public void setAbstract(boolean abstractFlag) {
		this.abstractFlag = abstractFlag;
	}
	
	public void setRole(int role) {
		this.role = role;
	}
	
	public void setPropertyValues(MutablePropertyValues propertyValues) {
		this.propertyValues = propertyValues;
	}

	public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
		this.constructorArgumentValues = (constructorArgumentValues != null ? constructorArgumentValues : new ConstructorArgumentValues());
	}
	
	
	public abstract AbstractBeanDefinition cloneBeanDefinition();
	
	public boolean isAbstract() {
		return this.abstractFlag;
	}
	
	public boolean isSingleton() {
		return this.singleton;
	}

	public boolean isLazyInit() {
		return this.lazyInit;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}


	@Override
	public void setParentName(String parentName) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getParentName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getBeanClassName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getFactoryBeanName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getFactoryMethodName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getScope() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public int getRole() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public ConstructorArgumentValues getConstructorArgumentValues() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public MutablePropertyValues getPropertyValues() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getResourceDescription() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	
	
	
}
