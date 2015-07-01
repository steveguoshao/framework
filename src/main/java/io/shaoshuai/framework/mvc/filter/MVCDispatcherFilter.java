/**
 * 
 */
package io.shaoshuai.framework.mvc.filter;

import io.shaoshuai.framework.mvc.config.MVCResource;
import io.shaoshuai.framework.mvc.config.MVCXmlResolver;
import io.shaoshuai.framework.mvc.core.ControllerFactory;
import io.shaoshuai.framework.mvc.core.ControllerInvocation;
import io.shaoshuai.framework.mvc.core.Dispatcher;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 帅
 *
 */
public class MVCDispatcherFilter implements Filter {

	private Dispatcher dispatcher;
	private ControllerFactory factory;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		try {
			MVCResource resource = new MVCResource(filterConfig);
			MVCXmlResolver resolver = new MVCXmlResolver(resource.getConfigFileLocations());
			factory = ControllerFactory.getInstance(resource.getControllerFactoryClassName());
			factory.createContainer(filterConfig.getServletContext(), resolver.getControllerConfigs());
		} finally {
			dispatcher.cleanup();
		}
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		try {
			dispatcher = factory.createDispatcher();
			dispatcher.assignToThread();
			dispatcher.createControllerContext(request, response);
			ControllerInvocation invocation = dispatcher.createControllerInvocation();
			boolean handled = invocation.invoke(request, response);
			if(handled) {
				chain.doFilter(request, response);
			}
		} finally {
			dispatcher.cleanup();
		}
	}

	@Override
	public void destroy() {
		dispatcher.cleanup();
	}


}
