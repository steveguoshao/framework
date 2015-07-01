/**
 * 
 */
package io.shaoshuai.framework.mvc.core;

import io.shaoshuai.framework.mvc.config.entity.MappingConfig;
import io.shaoshuai.framework.utils.BeanUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 帅
 *
 */
public class ControllerInvocation {
	
	private ControllerFactory factory;
	public ControllerInvocation(ControllerFactory factory) {
		this.factory = factory;
	}

	public boolean invoke(HttpServletRequest request, HttpServletResponse response) {
		String ctx = request.getContextPath();
		String name = request.getRequestURI().replace(ctx, "");
		String methodName = request.getParameter("method");
		System.out.println("requestURI:" + name);
		System.out.println("method name:" + methodName);
		try {
			if("/".equals(name)) {
				return true;
			}
			if(name.indexOf(".jsp") > -1) {
				request.getRequestDispatcher(name).forward(request, response);
			}
	 		Class<?> clazz = factory.getControllerClass(name);
			Method method = BeanUtils.getMethod(clazz, methodName);
			Object instance = factory.createInstance(clazz);
			String result = (String) method.invoke(instance);
			System.out.println("method invoke result : " + result);
			Class<?> returnType = method.getReturnType();
			if("java.lang.String".equals(returnType.getName())) {
				MappingConfig mappingConfig = factory.getContainer().getMappingConfig(name, result);
				if(null == mappingConfig) {
					System.out.println("无法找到 " + result + "映射的URL");
					return false;
				}
				String url = mappingConfig.getUrl();
				if(!url.startsWith("/")) {
					url +="/";
				}
				if("redirect".equals(mappingConfig.getType())) {
					String targetUrl = ctx + url;
					String encodedRedirectURL = response.encodeRedirectURL(targetUrl);
					System.out.println("encodedRedirectURL:" + encodedRedirectURL);
					response.sendRedirect(encodedRedirectURL);
				} else {
					request.getRequestDispatcher(url).forward(request, response);
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return false;
	}
	

}