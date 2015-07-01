/**
 * 
 */
package io.shaoshuai.framework.mvc.config;

import io.shaoshuai.framework.utils.StringUtils;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterConfig;

/**
 * @author 帅
 *
 */
public class MVCResource {
	
	private static final String CONFIG_FILE_LOCATION = "configFileLocation";
	
	private static final String LAZY_LOAD_CONTROLLER_CLASS = "lazyLoadControllerClass";
	
	private static final boolean DEFAULT_LAZY_LOAD_CONTROLLER_CLASS = true;
	
	private static final String CONTROLLER_FACTORY_CLASS_NAME = "controllerFactoryClassName";
	
	private static final String DEFAULT_CONTROLLER_FACTORY_CLASS_NAME = "io.shaoshuai.framework.mvc.core.ControllerFactory";
	
	private FilterConfig filterConfig;
	
	public MVCResource(){
	}
	
	public MVCResource(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	public Set<String> getConfigFileLocations() {
		Set<String> configFileLocations = new HashSet<String>();
		String configFileLocation = filterConfig.getInitParameter(CONFIG_FILE_LOCATION);
		if(StringUtils.isEmpty(configFileLocation)) {
			throw new NullPointerException("请在web.xml文件中配置mvc.xml文件的路径");
		}
		String[] configFileLocationArray = configFileLocation.replace("，", ",").split(",");
		for (String configFile : configFileLocationArray) {
			configFileLocations.add(configFile);
		}
		return configFileLocations;
	}
	
	public boolean isLazyLoadControllerClass() {
		String lazyLoadControllerClass = filterConfig.getInitParameter(LAZY_LOAD_CONTROLLER_CLASS);
		if(StringUtils.isNotEmpty(lazyLoadControllerClass)) {
			 return Boolean.getBoolean(lazyLoadControllerClass);
		}
		return DEFAULT_LAZY_LOAD_CONTROLLER_CLASS;
	}
	
	public String getControllerFactoryClassName() {
		String controllerFactoryClassName = filterConfig.getInitParameter(CONTROLLER_FACTORY_CLASS_NAME);
		if(StringUtils.isEmpty(controllerFactoryClassName)){
			controllerFactoryClassName = DEFAULT_CONTROLLER_FACTORY_CLASS_NAME;
		}
		return controllerFactoryClassName;
	}
}
