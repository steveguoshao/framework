package io.shaoshuai.framework.ioc.core;

public interface ApplicationContext {

	/**
	 * 准备IoC应用上下文
	 */
	public void prepareContext();

	/**
	 * 获取BeanFactory
	 * @return
	 */
	public BeanFactory getBeanFactory();

	/**
	 * 销毁Bean
	 */
	public void destoryBean();
	
	/**
	 * 注册bean处理器
	 * @param applicationContext
	 */
	public void registerBeanPostProcessor();
	
	/**
	 * 调用bean处理器
	 */
	public void invokeBeanPostProcessor();
	
}