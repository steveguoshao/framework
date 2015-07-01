/**
 * 
 */
package io.shaoshuai.framework.mvc.config.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 帅
 *
 */
public class ControllerConfig implements Serializable{

	private static final long serialVersionUID = 1733243295013318725L;

	private static final String DEFAULT_METHOD = "execute";
	
	private String name;
	
	private String className;
	
	private String methodName;
	
	private Map<String, MappingConfig> mappingConfigs;
	
	private Set<String> allowedMethods;

	public ControllerConfig(){
	}
	
	public ControllerConfig(String name, String className, String methodName) {
		this.name = name;
		this.className = className;
		this.methodName = methodName;
		this.mappingConfigs = new LinkedHashMap<String, MappingConfig>();	
		this.allowedMethods = new HashSet<String>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Map<String, MappingConfig> getMappingConfigs() {
		return mappingConfigs;
	}

	public void setMappingConfigs(Map<String, MappingConfig> mappingConfigs) {
		this.mappingConfigs = mappingConfigs;
	}

	public Set<String> getAllowedMethods() {
		return allowedMethods;
	}

	public void setAllowedMethods(Set<String> allowedMethods) {
		this.allowedMethods = allowedMethods;
	}

	public boolean isAllowedMethod(String method) {
		if(method.equals(methodName != null ? methodName : DEFAULT_METHOD) 
				|| allowedMethods.contains(method)) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((mappingConfigs == null) ? 0 : mappingConfigs.hashCode());
		result = prime * result
				+ ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((allowedMethods == null) ? 0 : allowedMethods.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ControllerConfig other = (ControllerConfig) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (mappingConfigs == null) {
			if (other.mappingConfigs != null)
				return false;
		} else if (!mappingConfigs.equals(other.mappingConfigs))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (allowedMethods == null) {
			if (other.allowedMethods != null)
				return false;
		} else if (!allowedMethods.equals(other.allowedMethods))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ControllerConfig [name=" + name + ", className=" + className
				+ ", methodName=" + methodName + ", mappings=" + mappingConfigs
				+ ", allowedMethods=" + allowedMethods + "]";
	}

	
}
