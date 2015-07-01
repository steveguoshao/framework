/**
 * 
 */
package io.shaoshuai.framework.ioc.test;

import io.shaoshuai.framework.ioc.ApplicationContextAware;
import io.shaoshuai.framework.ioc.core.ApplicationContext;


/**
 * @author shaoshuai
 *
 */
public class IoCControllerFactory extends ControllerFactory implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		System.out.println("我 被调用了");
		this.applicationContext = applicationContext;
	}
	
	@Override
	public Class<?> getControllerClass(String className) {
		return applicationContext.getBeanFactory().getBeanContainer().getBeanClass(className);
	}

	@Override
	public Object createInstance(Class<?> clazz) {
		return applicationContext.getBeanFactory().getBean(clazz);
	}

	@Override
	public Object createInstance(String className) {
		return applicationContext.getBeanFactory().getBean(className);
	}

	

	
}
