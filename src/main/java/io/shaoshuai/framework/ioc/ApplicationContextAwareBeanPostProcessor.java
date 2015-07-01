/** 
 * @Title: ApplicationContextAwareBeanPostProcessor.java 
 * @Package io.shaoshuai.framework.ioc 
 * @Description: TODO
 * @author shaoshuai 
 * @date Jun 30, 2015 4:16:56 PM 
 * @version V1.0 
 */  
package io.shaoshuai.framework.ioc;

import io.shaoshuai.framework.ioc.core.ApplicationContext;

/** 
 * @ClassName: ApplicationContextAwareBeanPostProcessor 
 * @Description: TODO
 * @author shaoshuai
 * @date Jun 30, 2015 4:16:56 PM 
 *  
 */
public class ApplicationContextAwareBeanPostProcessor implements
		BeanPostProcessor {

	private ApplicationContext applicationContext;
	
	public ApplicationContextAwareBeanPostProcessor(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
	
	@Override
	public Object postProcess(Object bean, String beanName) {
		if(bean instanceof ApplicationContextAware) {
			((ApplicationContextAware) bean).setApplicationContext(applicationContext);
		}
		return bean;
	}

}
