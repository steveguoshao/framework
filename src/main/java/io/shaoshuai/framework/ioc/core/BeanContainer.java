package io.shaoshuai.framework.ioc.core;

import io.shaoshuai.framework.ioc.config.beans.BeanConfig;
import io.shaoshuai.framework.ioc.config.beans.PropertyConfig;

import java.util.List;
import java.util.Map;

public interface BeanContainer {

	public BeanConfig getBeanConfig(String beanName);

	public Map<String, PropertyConfig> getPropertyConfig(
			String beanName);

	public Class<?> getBeanClass(String beanName);

	/**
	 * 根据本类/父类/接口获取子类的bean名称，但是beanType不能是Object.class
	 * @param beanType
	 * @return
	 */
	public String getBeanName(Class<?> beanType);
	
	public List<String> getAllBeanNames();

	public List<String> getAllSingletonBeanNames();
	
	public Object getCanonicalSingletonBean(String beanName);

	public void registerCanonicalSingletonBean(String beanName,
			Object canonicalBean);

	public void destoryBean();

}