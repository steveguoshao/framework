package io.shaoshuai.framework.dao.builder;

import io.shaoshuai.framework.dao.annotation.Column;
import io.shaoshuai.framework.dao.annotation.Table;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 根据bean对象构建CRUD SQL语句，从繁琐的拼接SQL和参数处理中解放出来
 * @author shaoshuai
 * create: 2015-06-09 19:38:39
 */
public class QueryBuilder {

	/**
	 * 构建插入语句的SQL
	 * @param bean 目标bean
	 * bean的geter,setter必须要遵守JavaBean的规范，必须是public，否则没法获取到值，不遵守规范是要受惩罚的
	 * @param isIgnoreKey 是否忽略主键  
	 * 在同一个方法内同时与buildParamerters被调用的时候，两者中的isIgnoredKey必须一致,
	 * 否则会出现SQL语句中前后参数不一致的情况
	 * @param isIgnoreNullValue 是否忽略null值 
	 * 主要用于多条件查询的参数拼接,如果是true就会不构建到参数列表中去，只有值是null才会被忽略，""值是不会被忽略的
	 * 在同一个方法内同时与buildParamerters被调用的时候，两者中的isIgnoreNullValue必须一致,
	 * 否则会出现SQL语句中前后参数不一致的情况
	 * @return String 格式化之后拼接成的insert语句
	 * 参数出现的顺序按照bean中属性从上到下的顺序出现的
	 */
	public static String buildInsertSQL(Object bean, boolean isIgnoreKey, boolean isIgnoreNullValue){
		String insertSQL = "insert into %1$s (%2$s) values (%3$s)";
		String tableName = getTableName(bean.getClass());
		Object[] columnNames = getColumnNames(bean, isIgnoreKey, isIgnoreNullValue);
		String insertChars = buildInsertChars(columnNames);
		return String.format(insertSQL, tableName, Arrays.toString(columnNames).replace("[", "").replace("]",""), insertChars);
	}
	
	/**
	 * 构建insert sql语句，不忽略主键和空值
	 * @param bean
	 * @param isIgnoreNullValue
	 * @return
	 */
	public static String buildInsertSQL(Object bean, boolean isIgnoreNullValue){
		return buildInsertSQL(bean, false, isIgnoreNullValue);
	}
	
	/**
	 * 构建insert sql语句，不忽略主键和空值
	 * @param bean
	 * @return
	 */
	public static String buildInsertSQL(Object bean){
		return buildInsertSQL(bean, false, false);
	}
	
	/**
	 * 构建更新语句的SQL
	 * @param bean 目标bean
	 * bean的geter,setter必须要遵守JavaBean的规范，必须是public，否则没法获取到值，不遵守规范是要受惩罚的
	 * @param isIgnoreKey 是否忽略主键  
	 * 在同一个方法内同时与buildParamerters被调用的时候，两者中的isIgnoredKey必须一致,
	 * 否则会出现SQL语句中前后参数不一致的情况
	 * @param isIgnoreNullValue 是否忽略null值 
	 * 主要用于多条件查询的参数拼接,如果是true就会不构建到参数列表中去，只有值是null才会被忽略，""值是不会被忽略的
	 * 在同一个方法内同时与buildParamerters被调用的时候，两者中的isIgnoreNullValue必须一致,
	 * 否则会出现SQL语句中前后参数不一致的情况
	 * @param where where条件语句
	 * 如果是null 或者  "" 则会被替换为 " 1=1 " where中的条件名称、数目必须与bean中设置不为空的值对应
	 * where条件中的列出现的顺序按照bean中属性从上到下的顺序出现的
	 * @return String 格式化之后拼接成的update语句
	 */
	public static String buildUpdateSQL(Object bean, boolean isIgnoreKey, boolean isIgnoreNullValue, String where) {
		String updateSQL = "update %1$s set %2$s where %3$s";
		String tableName = getTableName(bean.getClass());
		Object[] columnNames = getColumnNames(bean, isIgnoreKey, isIgnoreNullValue);
		String updateChars = buildUpdateChars(columnNames);
		return String.format(updateSQL, tableName, updateChars, buildWhere(where));
	}
	
	/**
	 * 构建update sql语句，主键不构建在更新语句中
	 * @param bean
	 * @param isIgnoreNullValue
	 * @param where
	 * @return
	 */
	public static String buildUpdateSQL(Object bean, boolean isIgnoreNullValue, String where) {
		return buildUpdateSQL(bean, true, isIgnoreNullValue, where);
	}

	/**
	 * 构建update sql语句，主键不构建在更新语句中，空值会更新
	 * @param bean
	 * @param where
	 * @return
	 */
	public static String buildUpdateSQL(Object bean, String where) {
		return buildUpdateSQL(bean, true, false, where);
	}
	
	/**
	 * 构建根据主键更新数据的update sql语句，主键不会出现在更新语句中
	 * @param bean
	 * @param isIgnoreNullValue
	 * @return
	 */
	public static String buildUpdateSQLByPK(Object bean, boolean isIgnoreNullValue) {
		String where = getKeyName(bean.getClass()) + " = ? ";
		return buildUpdateSQL(bean, true, isIgnoreNullValue, where);
	}
	
	/**
	 * 构建根据主键更新数据的update sql语句，主键不会出现在更新语句中，空值会更新
	 * @param bean
	 * @return
	 */
	public static String buildUpdateSQLByPK(Object bean) {
		return buildUpdateSQLByPK(bean, false);
	}
	
	/**
	 * 构建删除语句的SQL
	 * @param bean 目标bean
	 * bean的geter,setter必须要遵守JavaBean的规范，必须是public，否则没法获取到值，不遵守规范是要受惩罚的
	 * @param where where条件语句
	 * 如果是null 或者  "" 则会被替换为 " 1=1 " where中的条件名称、数目必须与bean中设置不为空的值对应
	 * where条件中的列出现的顺序按照bean中属性从上到下的顺序出现的
	 * @return String 格式化之后拼接成的delete语句
	 */
	public static String buildDeleteSQL(Object bean, String where) {
		String deleteSQL = "delete from %1$s where %2$s";
		String tableName = getTableName(bean.getClass());
		return String.format(deleteSQL, tableName, buildWhere(where));
	}
	
	/**
	 * 构建根据主键删除的delete sql语句
	 * @param bean
	 * @return
	 */
	public static String buildDeleteSQLByPK(Object bean) {
		String where = getKeyName(bean.getClass()) + " = ? ";
		return buildDeleteSQL(bean, where);
	}
	
	/**
	 * 构建查询语句的SQL
	 * @param bean 目标bean
	 * bean的geter,setter必须要遵守JavaBean的规范，必须是public，否则没法获取到值，不遵守规范是要受惩罚的
	 * @param where where条件语句
	 * 如果是null 或者  "" 则会被替换为 " 1=1 " where中的条件名称、数目必须与bean中设置不为空的值对应
	 * where条件中的列出现的顺序按照bean中属性从上到下的顺序出现的
	 * @return String 格式化之后拼接成的select语句
	 */
	public static String buildSelectSQL(Object bean, String where) {
		String selectSQL = "select %1$s from %2$s where %3$s";
		String tableName = getTableName(bean.getClass());
		Object[] columnNames = getColumnNames(bean, false, false);
		return String.format(selectSQL, Arrays.toString(columnNames).replace("[", "").replace("]",""), tableName, buildWhere(where));
	}
	
	/**
	 * 构建没有查询条件的Select sql语句
	 * @param bean
	 * @return
	 */
	public static String buildSelectSQL(Object bean) {
		return buildSelectSQL(bean, null);
	}
	
	/**
	 * 构建根据主键查询数据的select sql语句
	 * @param bean
	 * @return
	 */
	public static String buildSelectSQLByPK(Object bean) {
		String where = getKeyName(bean.getClass()) + " = ? ";
		return buildSelectSQL(bean, where);
	}
	
	/**
	 * 根据主键统计记录数，这样可以 统计有null的值
	 * @param bean 目标bean
	 * @param where where条件语句
	 * 如果是null 或者  "" 则会被替换为 " 1=1 " where中的条件名称、数目必须与bean中设置不为空的值对应
	 * where条件中的列出现的顺序按照bean中属性从上到下的顺序出现的
	 * @return 格式化之后拼接成的统计条目sql语句
	 */
	public static String buildCountSQL(Class<?> beanClass, String where) {
		String selectSQL = "select count(%1$s) from %2$s where %3$s";
		String keyName = getKeyName(beanClass);
		String tableName = getTableName(beanClass);
		return String.format(selectSQL, keyName, tableName, buildWhere(where));
	}
	
	/**
	 * 构建没有查询条件的统计sql语句
	 * @param beanClass
	 * @return
	 */
	public static String buildCountSQL(Class<?> beanClass) {
		return buildCountSQL(beanClass, null);
	}
	
	/**
	 * 获取bean对应数据库中的表名
	 * @param beanClass
	 * @return
	 */
	public static String getTableName(Class<?> beanClass) {
		String tableName = beanClass.getAnnotation(Table.class).value();
		if(null == tableName || "".equals(tableName)) {
			String errorMsg = "无法获取类" + beanClass.getName() + "所对应数据库中的表名，请检查在类名是否添加了注解@Table(value='tableName',key='keyName')";
			throw new NullPointerException(errorMsg);
		}
		return tableName;
	}
	
	/**
	 * 获取bean对应数据库中的主键名
	 * @param beanClass
	 * @return
	 */
	public static String getKeyName(Class<?> beanClass) {
		String keyName = beanClass.getAnnotation(Table.class).key();
		if(null == keyName || "".equals(keyName)) {
			String errorMsg = "无法获取类" + beanClass.getName() + "所对应数据库中的表的主键，请检查在类名是否添加了注解@Table(value='tableName',key='keyName')";
			throw new NullPointerException(errorMsg);
		}
		return keyName;
	}
	
	/**
	 * 获取bean对应数据库中的表的所有列
	 * @param beanClass
	 * @return bean所对应数据库中的列名组成的数组 
	 * 列名在数组中的顺序是按照bean中属性从上到下的顺序出现的
	 */
	public static Object[] getColumnNames(Object bean, boolean isIgnoreKey, boolean isIgnoreNullValue) {
		if(null == bean) {
			throw new NullPointerException("构建所有列的目标bean为空");
		}
		List<Object> allColumnNames = new ArrayList<Object>();
		Class<?> beanClass = bean.getClass();
		Field[] fields = beanClass.getDeclaredFields();
		for (Field field : fields) {
			// 只有是private的才被认为是javabean的标准属性
			if(Modifier.PRIVATE != field.getModifiers()) {
				continue;
			}
			String propertyName = field.getName();
			if(isIgnoreKey && propertyName.equals(getKeyName(beanClass))){
				continue;
			}
			Object propertyValue = null;
			try {
				PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
				propertyValue = propertyDescriptor.getValue(propertyName);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (IntrospectionException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
			if(isIgnoreNullValue && null == propertyValue) {
				continue;
			}
			// 对数据库中的映射列处理必须要放到获取property值之后
			Column column = field.getAnnotation(Column.class);
			if(null != column) {
				propertyName = column.value();
			}
			allColumnNames.add(propertyName);
		}
		return allColumnNames.toArray();
	}
	
	/**
	 * 获取bean所对应数据库中表的所有列名，包含主键列和空值列
	 * @param bean
	 * @return
	 */
	public static Object[] getColumnNames(Object bean) {
		return getColumnNames(bean, false, false);
	}
	
	/**
	 * 获取bean所对应数据库中表的所有列名，包含主键列
	 * @param bean
	 * @param isIgnoreNullValue
	 * @return
	 */
	public static Object[] getColumnNames(Object bean, boolean isIgnoreNullValue) {
		return getColumnNames(bean, false, isIgnoreNullValue);
	}
	
	/**
     * 构建SQL语句的参数数组
     * @param bean 目标bean 
     * bean的geter,setter必须要遵守JavaBean的规范，必须是public，否则没法获取到值，不遵守规范是要受惩罚的
     * @param isIgnoreKey 是否忽略主键  
     * 在同一个方法内同时与buildInsertSQL/buildUpdateSQL被调用的时候，两者中的isIgnoredKey必须一致,
     * 否则会出现SQL语句中前后参数不一致的情况
     * @param isIgnoreNullValue 是否忽略null值 
     * 主要用于多条件查询的参数拼接,如果是true就会不构建到参数列表中去，只有值是null才会被忽略，""值是不会被忽略的
     * 在同一个方法内同时与buildInsertSQL/buildUpdateSQL被调用的时候，两者中的isIgnoreNullValue必须一致,
     * 否则会出现SQL语句中前后参数不一致的情况
     * @return Object[] 构建成的参数值数组
     * 参数值在数组中的顺序是按照bean中从上到下的顺序出现的
     */
    public static Object[] getParameters(Object bean, boolean isIgnoreKey, boolean isIgnoreNullValue) {
        if(null == bean) {
            throw new NullPointerException("构建所有列的目标bean为空");
        }
        List<Object> parameters = new ArrayList<Object>();
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            // 只有仅仅是private的属性才被认为是javabean的标准属性
            if(Modifier.PRIVATE != field.getModifiers()) {
                continue;
            }
            String propertyName = field.getName();
            if(isIgnoreKey && propertyName.equals(getKeyName(beanClass))){
                continue;
            }
            Object propertyValue = null;
            try {
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(propertyName, beanClass);
                propertyValue = propertyDescriptor.getValue(propertyName);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IntrospectionException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            if(isIgnoreNullValue && null == propertyValue) {
                continue;
            }
            parameters.add(propertyValue);
        }
        return parameters.toArray();
    }
	
    /**
     * 获取Sql的参数值，包含主键
     * @param bean
     * @param isIgnoreNullValue
     * @return
     */
    public static Object[] getParameters(Object bean, boolean isIgnoreNullValue) {
    	return getParameters(bean, false, isIgnoreNullValue);
    }
    
    /**
     * 获取sql的参数值，包含主键k列和空值列
     * @param bean
     * @return
     */
    public static Object[] getParameters(Object bean) {
    	return getParameters(bean, false, false);
    }
    
    /**
     * 获取参数列表,把主键放在最后
     * @param bean
     * @param isIgnoreNullValue
     * @return
     */
    public static Object[] getParametersEndPK(Object bean, boolean isIgnoreNullValue) {
    	Object[] params = QueryBuilder.getParameters(bean, true, isIgnoreNullValue);
		params = Arrays.copyOf(params, params.length + 1);
		params[params.length - 1] = getKeyValue(bean);
		return params;
    }
    
    /**
     * 获取参数列表,把主键放在最后
     * @param bean
     * @param isIgnoreNullValue
     * @return
     */
    public static Object[] getParametersEndPK(Object bean) {
    	return getParametersEndPK(bean, false);
    }
    
    /**
     * 根据主键名称获取主键值
     * @param bean
     * @param keyName
     * @return
     */
	public static Object getKeyValue(Object bean, String keyName) {
		PropertyDescriptor propertyDescriptor = null;
		try {
			propertyDescriptor = new PropertyDescriptor(keyName, bean.getClass());
		} catch (IntrospectionException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return propertyDescriptor.getValue(keyName);
	}
	
	/**
	 * 获取主键值
	 * @param bean
	 * @return
	 */
	public static Object getKeyValue(Object bean) {
		Class<?> beanClass = bean.getClass();
		String keyName = getKeyName(beanClass);
		return getKeyValue(beanClass, keyName);
	}
    
	private static String buildInsertChars(Object[] columns) {
		if(null == columns) {
			return null;
		}
		Object[] insertChars = new Object[columns.length];
		Arrays.fill(insertChars, "?");
		return Arrays.toString(insertChars).replace("[", "").replace("]","");
	}
	
	
	private static String buildUpdateChars(Object[] columns) {
		if(null == columns) {
			return null;
		}
		StringBuilder updateChars = new StringBuilder();
		for(Object column : columns) {
			updateChars.append(column).append(" = ?, ");
		}
		return updateChars.toString().substring(0, updateChars.length() - 2);
	}
	
	private static String buildWhere(String where) {
		return (null == where || "".equals(where)) ? " 1=1 " : where.replace("where", "");
	}
	
}