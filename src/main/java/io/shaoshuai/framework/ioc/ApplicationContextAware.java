/**
 * 
 */
package io.shaoshuai.framework.ioc;

import io.shaoshuai.framework.ioc.core.ApplicationContext;

/**
 * @author shaoshuai
 *
 */
public interface ApplicationContextAware {

	/**
	 * 设置IoC应用上下文
	 * @param applicationContext
	 */
	public void setApplicationContext(ApplicationContext applicationContext);
}
