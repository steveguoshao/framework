/**
 * 
 */
package io.shaoshuai.framework.dao.builder;

import io.shaoshuai.framework.dao.Conditions;

/**
 * 构建SQL语句中Where查询条件语句
 * @author shaoshuai
 * create: 2015-06-10 12:24:50
 */
public class ConditionBuilder {

	public static Conditions buildConditions() {
		return new Conditions(new StringBuilder(300)); 
	}
	
}