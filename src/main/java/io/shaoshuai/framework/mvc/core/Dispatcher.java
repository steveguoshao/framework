/**
 * 
 */
package io.shaoshuai.framework.mvc.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shaoshuai
 *
 */
public class Dispatcher {

	private static ThreadLocal<Dispatcher> instance = new ThreadLocal<Dispatcher>();
	
	private ControllerFactory factory;
	public Dispatcher(ControllerFactory factory) {
		this.factory = factory;
	}
	
	public ControllerContext createControllerContext(HttpServletRequest request, HttpServletResponse response) {
		ControllerContext context = ControllerContext.getContext();
		if(null != context) {
			return context;
		}
		context = new ControllerContext(factory.getContainer().getServletContext(), request, response);
		ControllerContext.setContext(context);
		return context;
	}

	public ControllerFactory getFactory() {
		return factory;
	}

	public void assignToThread() {
		Dispatcher.instance.set(this);
	}
	
	public ControllerInvocation createControllerInvocation() {
		return new ControllerInvocation(factory);
	}
	
	public void cleanup() {
		ControllerContext.setContext(null);
		Dispatcher.instance.set(null);
	}
}
