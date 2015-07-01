package io.shaoshuai.framework.ioc.config.beans;

import java.util.Map;

public class BeanConfig {

	private String id;
	private String className;
	private String scope = Scope.PROTOTYPE.toString();
	private Map<String, PropertyConfig> propertyConfigs;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public Map<String, PropertyConfig> getPropertyConfigs() {
		return propertyConfigs;
	}
	public void setPropertyConfigs(Map<String, PropertyConfig> propertyConfigs) {
		this.propertyConfigs = propertyConfigs;
	}
	
	public boolean isSingleton(){
		return Scope.SINGLETON.toString().equalsIgnoreCase(getScope());
	}

	public boolean isPrototype(){
		return Scope.PROTOTYPE.toString().equalsIgnoreCase(getScope());
	}
}