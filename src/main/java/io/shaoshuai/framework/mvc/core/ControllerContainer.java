/**
 * 
 */
package io.shaoshuai.framework.mvc.core;

import io.shaoshuai.framework.mvc.config.entity.ControllerConfig;
import io.shaoshuai.framework.mvc.config.entity.MappingConfig;
import io.shaoshuai.framework.utils.BeanUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletContext;

/**
 * 控制器容器
 * @author 帅
 *
 */
public class ControllerContainer {

	private ServletContext servletContext;
	
	private Map<String, ControllerConfig> controllerConfigs;
		
	private Map<String, Class<?>> controllerClasses = new ConcurrentHashMap<String, Class<?>>();

	private final Lock lock = new ReentrantLock();
	
	public ControllerContainer(ServletContext servletContext, Map<String, ControllerConfig> controllerConfigs) {
		this.servletContext = servletContext;
		this.controllerConfigs = controllerConfigs;
	}
	
	public ServletContext getServletContext() {
		return servletContext;
	}

	public Class<?> getControllerClass(String className) {
		lock.lock();
		try {
			Class<?> clazz;
			if(controllerClasses.containsKey(className)) {
				clazz = controllerClasses.get(className);
			}
			clazz = BeanUtils.loadClass(className);
			controllerClasses.put(className, clazz);
			return clazz;
		} finally {
			lock.unlock();
		}
	}
	
	public MappingConfig getMappingConfig(String controllerName, String mappingName) {
		return controllerConfigs.get(controllerName).getMappingConfigs().get(mappingName);
	}
	
}
