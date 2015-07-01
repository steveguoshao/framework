/**
 * 
 */
package io.shaoshuai.framework.ioc.core;

import io.shaoshuai.framework.ioc.ApplicationContextAware;
import io.shaoshuai.framework.ioc.ApplicationContextAwareBeanPostProcessor;
import io.shaoshuai.framework.ioc.BeanPostProcessor;
import io.shaoshuai.framework.ioc.config.Resource;
import io.shaoshuai.framework.ioc.config.XmlConfigurationResolver;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shaoshuai
 *
 */
public class DefaultApplicationContext implements ApplicationContext {

	private List<BeanPostProcessor> processors = new ArrayList<BeanPostProcessor>();
	
	private String configLocation;
	
	private BeanFactory beanFactory;
	
	public DefaultApplicationContext(String configLocation) {
		this.configLocation = configLocation;
	}

	@Override
	public void prepareContext() {
		Resource resource = createResource();
		XmlConfigurationResolver resolver = createResolver(resource);
		BeanContainer beanContainer = createBeanContainer(resolver);
		createBeanFactory(beanContainer);
		beanFactory.assembleSingletonBean();
		registerBeanPostProcessor();
		invokeBeanPostProcessor();
	}
	
	private Resource createResource() {
		return new Resource(configLocation);
	}
	
	private XmlConfigurationResolver createResolver(Resource resource) {
		return new XmlConfigurationResolver(resource.getConfigDocuments());
	}
	
	private BeanContainer createBeanContainer(XmlConfigurationResolver resolver) {
		return new DefaultBeanContainer(resolver.resolve());
	}
	
	private void createBeanFactory(BeanContainer beanContainer) {
		this.beanFactory = new DefaultBeanFactory(beanContainer);
	}
	
	@Override
	public void destoryBean() {
		beanFactory.destoryBean();
	}
	
	@Override
	public BeanFactory getBeanFactory() {
		return this.beanFactory;
	}

	@Override
	public void registerBeanPostProcessor() {
		processors.add(new ApplicationContextAwareBeanPostProcessor(this));
	}

	@Override
	public void invokeBeanPostProcessor() {
		for (BeanPostProcessor beanPostProcessor : processors) {
			beanPostProcessor.postProcess(beanFactory.getBean(ApplicationContextAware.class), 
					beanFactory.getBeanContainer().getBeanName(ApplicationContextAware.class));
		}
	}
}
