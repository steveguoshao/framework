/**
 * 
 */
package io.shaoshuai.framework.ioc.test;

import io.shaoshuai.framework.ioc.core.ApplicationContext;
import io.shaoshuai.framework.ioc.core.DefaultApplicationContext;

import org.junit.Test;

/**
 * @author 帅
 *
 */
public class ApplicationContextTest {

	@Test
	public void testPrepareContextAndAssembleBean() {
		ApplicationContext ctx = new DefaultApplicationContext("beans.xml");
		ctx.prepareContext();
		
		LoginController loginController = (LoginController) ctx.getBeanFactory().getBean("loginController");
		
		System.out.println(loginController.getAuthenticationService().getAccountDao().getQueryRunner().hashCode());
		System.out.println(loginController.getAuthenticationService().getAccountDao().getQueryRunner().toString());
		
		IoCControllerFactory controllerFactory = ctx.getBeanFactory().getBean(IoCControllerFactory.class);
		System.out.println(controllerFactory.getControllerClass("loginController").getCanonicalName());
		
		ctx.destoryBean();
	}
}
