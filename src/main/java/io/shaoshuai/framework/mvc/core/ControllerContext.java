/**
 * 
 */
package io.shaoshuai.framework.mvc.core;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 帅
 *
 */
public class ControllerContext {

	private static ThreadLocal<ControllerContext> controllerContext = new ThreadLocal<ControllerContext>();
	
	private ServletContext servletContext;
	
	private HttpServletRequest request;
	
	private HttpServletResponse response;

	public ControllerContext(ServletContext servletContext,
			HttpServletRequest request, HttpServletResponse response) {
		this.servletContext = servletContext;
		this.request = request;
		this.response = response;
	}

	public static ControllerContext getContext() {
		return controllerContext.get();
	}

	public static void setContext(ControllerContext context) {
		controllerContext.set(context);
	}

	public ServletContext getAppliction() {
		return servletContext;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	
}
