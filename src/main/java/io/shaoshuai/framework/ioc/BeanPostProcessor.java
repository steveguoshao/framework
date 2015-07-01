package io.shaoshuai.framework.ioc;


public interface BeanPostProcessor {

	/**
	 * 在初始化前处理
	 * @param bean
	 * @param beanName
	 * @return
	 */
	public Object postProcess(Object bean, String beanName);

	
}
