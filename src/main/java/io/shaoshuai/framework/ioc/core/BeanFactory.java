package io.shaoshuai.framework.ioc.core;

public interface BeanFactory {

	/**
	 * 获取Bean容器
	 * @return
	 */
	public BeanContainer getBeanContainer();
	
	/**
	 * 装配单例Bean
	 */
	public void assembleSingletonBean();
	
	/**
	 * 获取已经装配好的Bean
	 * @param beanName
	 * @return
	 */
	public Object getBean(String beanName);

	/**
	 * 根据class类型获取Bean
	 * @param beanType
	 * @return
	 */
	public <T> T getBean(Class<T> beanType);
	
	/**
	 * 根据类型和名称获取bean
	 * @param beanName
	 * @param beanType
	 * @return
	 */
	public <T> T getBean(String beanName, Class<T> beanType);
	
	/**
	 * 获取没有装配的Bean
	 * @param beanName
	 * @return
	 */
	public Object getUnAssembleBean(String beanName);
	
	/**
	 * 销毁bean
	 */
	public void destoryBean();

}