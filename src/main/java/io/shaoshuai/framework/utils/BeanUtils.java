/**
 * 
 */
package io.shaoshuai.framework.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

/**
 * @author 帅
 *
 */
public class BeanUtils {

	/**
	 * 加载指定类名的类
	 * @param className
	 * @return
	 */
	public static Class<?> loadClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 创建 实例
	 * @param clazz
	 * @return
	 */
	public static Object createInstance(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 创建 实例
	 * @param className
	 * @return
	 */
	public static Object createInstance(String className) {
		try {
			return loadClass(className).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据方法名获取方法对象，这样可以避免有参数的方法没法获取的问题
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	public static Method getMethod(Class<?> clazz, String methodName) {
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			if(methodName.equals(method.getName())) {
				return method;
			}
		}
		return null;
	}
	
	/**
	 * 判断父类是否是Object类
	 * @param superClass
	 * @return
	 */
	public static boolean isObjectClass(Class<?> superClass) {
		boolean isObjectClass = false;
		if(Object.class == superClass) {
			isObjectClass = true;
		}
		return isObjectClass;
	}
	
	/**
	 * 判断是否是某个类或者接口的子类
	 * @param childClass
	 * @param superClass
	 * @return
	 */
	public static boolean isChildClass(Class<?> childClass, Class<?> superClass) {
		try{
			childClass.asSubclass(superClass);
			return true;
		}catch(ClassCastException cce) {
			//System.err.println(childClass.getName() + "不是" + superClass.getName() + "的子类");
			return false;
		}
	}
	
	/**
	 * 根据属性名称，把对应的值设置到bean中
	 * 目前只能设置简单对象，不支持集合
	 * @param bean
	 * @param propertyName
	 * @param value
	 */
	public static void copyPropertyValue(Object bean, String propertyName, String value) {
		try {
			Class<?> beanClass = bean.getClass();
			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
			Method readMethod = propertyDescriptor.getReadMethod();
			Method writeMethod = propertyDescriptor.getWriteMethod();
			if(!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
				writeMethod.setAccessible(true);
			}
			Class<?> returnType = readMethod.getReturnType();
			Constructor<?> constructor = returnType.getDeclaredConstructor(String.class);
			Object args = constructor.newInstance(value);
			writeMethod.invoke(bean, args);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * 根据属性名称从bean中获取属性值
	 * @param bean
	 * @param propertyName
	 * @return
	 */
	public static Object getValueByPropertyName(Object bean, String propertyName) {
		try {
			Class<?> beanClass = bean.getClass();
			PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
			Method readMethod = propertyDescriptor.getReadMethod();
			return readMethod.invoke(bean);
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/**
	 * 把request中的请求参数值映射到bean中
	 * 目前只支持基本数据类型，不支持集合
	 * @param request
	 * @param beanClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T mappingRequest2Bean(HttpServletRequest request, Class<T> beanClass) {
		try {
			T instance = beanClass.newInstance();
			Enumeration<String> names = request.getParameterNames();
			while(names.hasMoreElements()) {
				String propertyName = names.nextElement();
				String value = request.getParameter(propertyName);
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
				Method readMethod = propertyDescriptor.getReadMethod();
				Method writeMethod = propertyDescriptor.getWriteMethod();
				if(!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
					writeMethod.setAccessible(true);
				}
				Class<?> returnType = readMethod.getReturnType();
				Constructor<?> constructor = returnType.getDeclaredConstructor(String.class);
				Object args = constructor.newInstance(value);
				writeMethod.invoke(instance, args);
			}
			return instance;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 表单数据封装
	 * * 例如： User user = BeanUtils.populate(User.class, request.getParameterMap())
	 * @param beanClass
	 * @param properties
	 * @return
	 */
	public static <T> T populate(Class<T> beanClass, Map<String,String[]> properties){
		//1 反射实例化
		T bean;
		try {
			bean = beanClass.newInstance();
			//2 时间类型转换器
			DateConverter dateConverter = new DateConverter();
			dateConverter.setPattern("yyyy-MM-dd");
			ConvertUtils.register(dateConverter, java.util.Date.class);
			//3 添加
			org.apache.commons.beanutils.BeanUtils.populate(bean, properties);
			return bean;
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
