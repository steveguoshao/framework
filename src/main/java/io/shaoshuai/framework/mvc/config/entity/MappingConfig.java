/**
 * 
 */
package io.shaoshuai.framework.mvc.config.entity;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 帅
 *
 */
public class MappingConfig implements Serializable{

	private static final long serialVersionUID = -6088057652087802125L;
	
	private static final String DEFAULT_TYPE = "forward";

	private String className; 
	
	private String name;
	
	private String type;
	
	private String url;
	
	private Map<String, String> parameters;

	public MappingConfig(){
		
	}
	
	public MappingConfig(String className, String name, String type, String url) {
		this.className = className;
		this.name = name;
		this.type = type;
		this.url = url;
		this.parameters = new LinkedHashMap<String, String>();
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return ("".equals(type) || type == null) ? DEFAULT_TYPE : type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result
				+ ((parameters == null) ? 0 : parameters.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
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
		MappingConfig other = (MappingConfig) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (parameters == null) {
			if (other.parameters != null)
				return false;
		} else if (!parameters.equals(other.parameters))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MappingConfig [className=" + className + ", name=" + name
				+ ", type=" + type + ", url=" + url + ", parameters=" + parameters + "]";
	}

	
}
