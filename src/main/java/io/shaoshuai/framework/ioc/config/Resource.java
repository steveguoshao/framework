/**
 * 
 */
package io.shaoshuai.framework.ioc.config;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 * @author 帅
 *
 */
public class Resource {

	private Set<String> configLocations;
	
	public Resource(String configLocation) {
		this.configLocations = parseConfigLocation(configLocation);
	}

	private Set<String> parseConfigLocation(String configLocation) {
		if(null == configLocation || "".equals(configLocation)) {
			throw new NullPointerException("bean配置文件为空，请在web.xml文件中配置configLocation路径");
		}
		Set<String> configLocations = new LinkedHashSet<String>();
		String[] configLocationArray = configLocation.replace("，", ",").split(",");
		for (String config : configLocationArray) {
			configLocations.add(config);
		}
		return configLocations;
	}
	
	public Set<String> getConfigLocations() {
		return configLocations;
	}
	
	public List<Document> getConfigDocuments() {
		List<Document> configDocuments = new LinkedList<Document>();
		SAXReader reader = new SAXReader();
		for (String configLocation : configLocations) {
			Document configDocument = null;
			try {
				configDocument = reader.read(this.getClass().getClassLoader().getResourceAsStream(configLocation));
			} catch (DocumentException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			configDocuments.add(configDocument);
		}
		return configDocuments;
	}
	
}
