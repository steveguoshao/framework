/**
 * 
 */
package io.shaoshuai.framework.ioc.config;

import io.shaoshuai.framework.ioc.config.beans.BeanConfig;
import io.shaoshuai.framework.ioc.config.beans.PropertyConfig;
import io.shaoshuai.framework.ioc.config.beans.Scope;
import io.shaoshuai.framework.utils.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;

/**
 * @author shaoshuai
 *
 */
public class XmlConfigurationResolver {

	private List<Document> configDocuments;
	public XmlConfigurationResolver(List<Document> configDocuments) {
		this.configDocuments = configDocuments;
	}

	private Map<String, BeanConfig> beanConfigs;
	public Map<String, BeanConfig> resolve() {
		beanConfigs = new LinkedHashMap<String, BeanConfig>();
		for (Document document : configDocuments) {
			resolveBean(document);
		}
		return beanConfigs;
	}
	
	@SuppressWarnings("unchecked")
	private void resolveBean(Document configDocument) {
		Element beans = configDocument.getRootElement();
		List<Element> beanElements = beans.elements("bean");
		BeanConfig beanConfig = null;
		for (Element element : beanElements) {
			beanConfig = new BeanConfig();
			String id = element.attributeValue("id");
			String className = element.attributeValue("class");
			String scope = element.attributeValue("scope");
			beanConfig.setId(id);
			beanConfig.setClassName(className);
			beanConfig.setScope(StringUtils.isEmpty(scope) ? Scope.PROTOTYPE.toString() : scope);
			
			List<Element> propertyElements = element.elements("property");
			if(null != propertyElements && propertyElements.size() > 0) {
				beanConfig.setPropertyConfigs(resolveProperty(propertyElements));
			}
			beanConfigs.put(id, beanConfig);
		}
		
	}
	
	private Map<String, PropertyConfig> resolveProperty(List<Element> propertyElements) {
		Map<String, PropertyConfig> propertyConfigs = new LinkedHashMap<String, PropertyConfig>();
		PropertyConfig propertyConfig = null;
		for (Element propertyElement : propertyElements) {
			propertyConfig = new PropertyConfig();
			String name = propertyElement.attributeValue("name");
			propertyConfig.setName(name);
			propertyConfig.setRef(propertyElement.attributeValue("ref"));
			propertyConfig.setValue(propertyElement.getTextTrim());
			propertyConfigs.put(name, propertyConfig);
		}
		return propertyConfigs;
	}
}
