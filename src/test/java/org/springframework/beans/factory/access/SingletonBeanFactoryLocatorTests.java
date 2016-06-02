package org.springframework.beans.factory.access;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.ClassUtils;

public class SingletonBeanFactoryLocatorTests {
	
	private static final Class<?> CLASS = SingletonBeanFactoryLocatorTests.class;
	private static final String REF1_XML = CLASS.getSimpleName() + "-ref1.xml";
	
	@Test
	public void testBasicFunctionality() {
		SingletonBeanFactoryLocator facLoc = new SingletonBeanFactoryLocator(
				"classpath*:" + ClassUtils.addResourcePathToPackagePath(CLASS, REF1_XML));

		basicFunctionalityTest(facLoc);
	}
	protected void basicFunctionalityTest(SingletonBeanFactoryLocator facLoc) {
		BeanFactoryReference bfr = facLoc.useBeanFactory("a.qualified.name.of.some.sort");
		BeanFactory fac = bfr.getFactory();
		BeanFactoryReference bfr2 = facLoc.useBeanFactory("another.qualified.name");
		fac = bfr2.getFactory();
		// verify that the same instance is returned
		TestBean tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("beans1.bean1"));
		tb.setName("was beans1.bean1");
		BeanFactoryReference bfr3 = facLoc.useBeanFactory("another.qualified.name");
		fac = bfr3.getFactory();
		tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("was beans1.bean1"));
		BeanFactoryReference bfr4 = facLoc.useBeanFactory("a.qualified.name.which.is.an.alias");
		fac = bfr4.getFactory();
		tb = (TestBean) fac.getBean("beans1.bean1");
		assertTrue(tb.getName().equals("was beans1.bean1"));
		// Now verify that we can call release in any order.
		// Unfortunately this doesn't validate complete release after the last one.
		bfr2.release();
		bfr3.release();
		bfr.release();
		bfr4.release();
	}
	

class TestBean {

	private String name;

	private List<?> list;

	private Object objRef;

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the list.
	 */
	public List<?> getList() {
		return list;
	}

	/**
	 * @param list The list to set.
	 */
	public void setList(List<?> list) {
		this.list = list;
	}

	/**
	 * @return Returns the object.
	 */
	public Object getObjRef() {
		return objRef;
	}

	/**
	 * @param object The object to set.
	 */
	public void setObjRef(Object object) {
		this.objRef = object;
	}
}
}
