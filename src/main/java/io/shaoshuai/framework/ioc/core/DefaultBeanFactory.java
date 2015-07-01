/**
 *
 */
package io.shaoshuai.framework.ioc.core;

import io.shaoshuai.framework.ioc.config.beans.BeanConfig;
import io.shaoshuai.framework.ioc.config.beans.PropertyConfig;
import io.shaoshuai.framework.utils.BeanUtils;
import io.shaoshuai.framework.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


/**
 * @author shaoshuai
 *
 */
public class DefaultBeanFactory implements BeanFactory {

	private BeanContainer container;

	public DefaultBeanFactory(BeanContainer container) {
		this.container = container;
	}

	@Override
	public BeanContainer getBeanContainer() {
		return this.container;
	}
	
	@Override
	public void assembleSingletonBean() {
		List<String> allSingletonBeanNames = container.getAllSingletonBeanNames();
		for (String singletonBeanName : allSingletonBeanNames) {
			getBean(singletonBeanName);
		}
	}

	@Override
	public Object getBean(String beanName) {
		return getBean(beanName, null);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T getBean(Class<T> beanType) {
		String beanName = container.getBeanName(beanType);
		return (T) getBean(beanName);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanName, Class<T> beanType) {
		BeanConfig beanConfig = container.getBeanConfig(beanName);
		if(null == beanConfig) {
			//throw new NullPointerException("没有" + beanName +"的Bean配置信息");
			System.err.println("没有" + beanName +"的Bean配置信息");
			return null;
		}
		Object canonicalBean = null;
		if(beanConfig.isSingleton()) {
			canonicalBean = container.getCanonicalSingletonBean(beanName);
			if(null == canonicalBean) {
				canonicalBean = assembleBean(beanName);
				container.registerCanonicalSingletonBean(beanName, canonicalBean);
			}
		} else if(beanConfig.isPrototype()){
			canonicalBean = assembleBean(beanName);
		}
		if(null != canonicalBean && null != beanType) {
			if(canonicalBean.getClass() != beanType) {
				canonicalBean = getBean(beanType);
			}
		}
		return (T) canonicalBean;
	}
	
	@Override
	public Object getUnAssembleBean(String beanName) {
		return BeanUtils.createInstance(container.getBeanClass(beanName));
	}
	
	/**
	 * 装配整个依赖链上的bean
	 * @param canonicalBean
	 * @param beanName
	 */
	protected Object assembleBean(String beanName) {
		Object canonicalBean = getUnAssembleBean(beanName); 
		Map<String, PropertyConfig> propertyConfigs = container.getPropertyConfig(beanName);
		if(null != propertyConfigs) {
			Class<?> beanClass = container.getBeanClass(beanName);
			Field[] originalField = beanClass.getDeclaredFields();
			for (Field field : originalField) {
				String fieldName = field.getName();
				PropertyConfig propertyConfig = propertyConfigs.get(fieldName);
				if(null == propertyConfig) {
					System.out.println("找不到类" + beanClass.getCanonicalName() +"的" + fieldName + "属性配置，无法注入!");
					continue;
				}
				String ref = propertyConfig.getRef();
				String value = propertyConfig.getValue();
				try {
					field.setAccessible(true);
					if(StringUtils.isNotEmpty(ref)) {
						// 需要递归处理ref的初始化
						field.set(canonicalBean, getBean(ref));
					} else if(StringUtils.isNotEmpty(value)) {
						field.set(canonicalBean, value);
					} else {
						throw new NullPointerException("类" + beanClass.getCanonicalName() +"的" + fieldName + "属性没有ref或value的配置，无法注入!");
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
		return canonicalBean;
	}
	
	@Override
	public void destoryBean() {
		container.destoryBean();
	}

}
