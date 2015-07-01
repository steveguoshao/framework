/**
 * 
 */
package io.shaoshuai.framework.ioc.core;

import io.shaoshuai.framework.ioc.config.beans.BeanConfig;
import io.shaoshuai.framework.ioc.config.beans.PropertyConfig;
import io.shaoshuai.framework.utils.BeanUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shaoshuai
 *
 */
public class DefaultBeanContainer implements BeanContainer {

	private Map<String, BeanConfig> beanConfigs;	
	
	private List<String> allBeanNames = new LinkedList<String>();
	
	private List<String> singletonBeanNames = new LinkedList<String>();
	
	private Map<String, Class<?>> beanClassCache = new LinkedHashMap<String, Class<?>>();

	private ConcurrentHashMap<String, Object> canonicalSingletonBeanCache = new ConcurrentHashMap<String, Object>();
	
	public DefaultBeanContainer(){}
	
	public DefaultBeanContainer(Map<String, BeanConfig> beanConfigs) {
		this.beanConfigs = beanConfigs;
		loadBeanClass();
	}
	
	private void loadBeanClass() {
		beanClassCache.clear();
		Iterator<Map.Entry<String, BeanConfig>> iterator = beanConfigs.entrySet().iterator(); 
		BeanConfig beanConfig;
		while(iterator.hasNext()) {
			beanConfig = iterator.next().getValue();
			String id = beanConfig.getId();
			String className = beanConfig.getClassName();
			Class<?> clazz = BeanUtils.loadClass(className);
			if(beanConfig.isSingleton()) {
				singletonBeanNames.add(id);
			}
			System.out.println("加载类:" +clazz + "到BeanContainer");
			allBeanNames.add(id);
			beanClassCache.put(id, clazz);
		}
	}
	
	@Override
	public BeanConfig getBeanConfig(String beanName) {
		return beanConfigs.get(beanName);
	}
	
	@Override
	public Map<String, PropertyConfig> getPropertyConfig(String beanName) {
		return getBeanConfig(beanName).getPropertyConfigs();
	}
	
	@Override
	public List<String> getAllBeanNames() {
		return allBeanNames;
	}
	
	@Override
	public List<String> getAllSingletonBeanNames() {
		return singletonBeanNames;
	}

	@Override
	public Class<?> getBeanClass(String beanName) {
		Class<?> clazz = null;
		if(beanClassCache.containsKey(beanName)) {
			clazz = beanClassCache.get(beanName);
		}
		if(null == clazz) {
			throw new NullPointerException("不存在名称" + beanName + "的bean，请检查配置！");
		}
		return clazz;
	}
	
	@Override
	public String getBeanName(Class<?> beanType){
		if(BeanUtils.isObjectClass(beanType)) {
			throw new RuntimeException("BeanType不能是Object");
		}
		String beanName = null;
		//if(beanClassCache.containsValue(beanType)) {
			Iterator<Map.Entry<String, Class<?>>> iterator = beanClassCache.entrySet().iterator();
			while(iterator.hasNext()) {
				Map.Entry<String, Class<?>> entry = iterator.next();
				Class<?> childClass = entry.getValue();
				if(childClass == beanType || BeanUtils.isChildClass(childClass, beanType)) {
					beanName = entry.getKey();
					break;
				}
			}
		//}
		if(null == beanName) {
			//throw new NullPointerException("不存在类" + beanType.getCanonicalName() + "的配置信息！");
			System.err.print("不存在类" + beanType.getCanonicalName() + "的配置信息！");
		}
		return beanName;
	}
	
	@Override
	public Object getCanonicalSingletonBean(String beanName) {
		return canonicalSingletonBeanCache.get(beanName);
	}
	
	@Override
	public void registerCanonicalSingletonBean(String beanName, Object canonicalBean) {
		canonicalSingletonBeanCache.putIfAbsent(beanName, canonicalBean);
	}
	
	@Override
	public void destoryBean() {
		beanConfigs = null;
		allBeanNames.clear();
		singletonBeanNames.clear();
		beanClassCache.clear();
		canonicalSingletonBeanCache.clear();
	}
	
}
