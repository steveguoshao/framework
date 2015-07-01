/**
 * 
 */
package io.shaoshuai.framework.mvc.config;

import io.shaoshuai.framework.mvc.config.entity.ControllerConfig;
import io.shaoshuai.framework.mvc.config.entity.MappingConfig;
import io.shaoshuai.framework.utils.BeanUtils;
import io.shaoshuai.framework.utils.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * mvc.xml配置文件解析
 * @author 少帅
 *
 */
public class MVCXmlResolver {

	private Map<String, ControllerConfig> controllerConfigs;
	public MVCXmlResolver(Set<String> configFileLocations) {
		controllerConfigs = new LinkedHashMap<String, ControllerConfig>();
		resolve(configFileLocations);
	}
	
	/**
	 * 解析mvc.xml等配置文件
	 */
	public void resolve(Set<String> configFileLocations) {
		try {
			SAXReader reader = new SAXReader();
			Document document = null;
			for(String configFileLocation : configFileLocations) {
				document = reader.read(this.getClass().getClassLoader().getResourceAsStream(configFileLocation));
				resolve(document);
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void resolve(Document document) throws DocumentException {
		ControllerConfig controllerConfig;
		Element root = document.getRootElement();
		List<Element> controllerElements = root.elements("controller");
		if(StringUtils.isEmpty(controllerElements)) {
			throw new NullPointerException("配置文件没有配置controller元素");
		}
		for(Element controllerElement : controllerElements) {
			controllerConfig = new ControllerConfig();
			// 解析controller的属性
			String name = controllerElement.attributeValue("name");
			String className = controllerElement.attributeValue("class");
			controllerConfig.setName(name);
			controllerConfig.setClassName(className);
			// 解析controller的子元素mapping
			List<Element> mappingConfigElements = controllerElement.elements("mapping");
			// 可以不配置mapping子元素
			if(StringUtils.isNotEmpty(mappingConfigElements)) {
				controllerConfig.setMappingConfigs(resolveMappingConfig(name, mappingConfigElements));
			}
			controllerConfigs.put(name, controllerConfig);
		}
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, MappingConfig> resolveMappingConfig(String className, List<Element> mappingConfigElements) {
		Map<String, MappingConfig> mappingConfigs = new LinkedHashMap<String, MappingConfig>();
		MappingConfig mappingConfig;
		for(Element mappingConfigElement : mappingConfigElements) {
			mappingConfig = new MappingConfig();
			mappingConfig.setClassName(className);
			List<Attribute> attributes = mappingConfigElement.attributes();
			for(Attribute attribute : attributes) {
				String name = attribute.getName();
				String value = attribute.getValue();
				BeanUtils.copyPropertyValue(mappingConfig, name, value);
			}
			// 暂时不处理url带参数或者有子元素parameter问题
			mappingConfig.setUrl(mappingConfigElement.getText());
			mappingConfigs.put(mappingConfigElement.attributeValue("name"), mappingConfig);
		}
		return mappingConfigs;
	}
	
	public Map<String, ControllerConfig> getControllerConfigs(){
		return controllerConfigs;
	}
	
}