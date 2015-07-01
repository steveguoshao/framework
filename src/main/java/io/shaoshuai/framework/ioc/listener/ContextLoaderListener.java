/**
 * 
 */
package io.shaoshuai.framework.ioc.listener;

import io.shaoshuai.framework.ioc.core.ApplicationContext;
import io.shaoshuai.framework.ioc.core.DefaultApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author shaoshuai
 *
 */
public class ContextLoaderListener implements ServletContextListener{

	private static final String CONFIG_LOCATION = "configLocation";
	
	private ApplicationContext applicationContext;
	public ApplicationContext createApplicationContext(String configLocation) {
		return new DefaultApplicationContext(configLocation);
	}
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		String configLocation = event.getServletContext().getInitParameter(CONFIG_LOCATION);
		applicationContext = createApplicationContext(configLocation);
		applicationContext.prepareContext();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		applicationContext.destoryBean();
	}
}
