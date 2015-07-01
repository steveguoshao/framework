/**
 * 
 */
package io.shaoshuai.framework.mvc.core;

import io.shaoshuai.framework.mvc.config.entity.ControllerConfig;
import io.shaoshuai.framework.utils.BeanUtils;

import java.util.Map;

import javax.servlet.ServletContext;


/**
 * @author 帅
 *
 */
public class ControllerFactory {

	private ControllerContainer container;
	
	public static ControllerFactory getInstance(String controllerFactoryClassName) {
		return (ControllerFactory) BeanUtils.createInstance(controllerFactoryClassName);
	}
	
	public ControllerContainer createContainer(ServletContext servletContext, Map<String, ControllerConfig> controllerConfigs) {
		container = new ControllerContainer(servletContext, controllerConfigs);
		return container;
	}
	
	public Dispatcher createDispatcher() {
		return new Dispatcher(this);
	}

	public ControllerContainer getContainer() {
		return container;
	}

	public Class<?> getControllerClass(String className) {
		return container.getControllerClass(className);
	}
	
	public Object createInstance(Class<?> clazz) {
		return BeanUtils.createInstance(clazz);
	}
	
	public Object createInstance(String className) {
		return BeanUtils.createInstance(className);
	}
}
