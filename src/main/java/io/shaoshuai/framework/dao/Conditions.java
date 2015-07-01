/**
 * 
 */
package io.shaoshuai.framework.dao;


import java.util.Arrays;

/**
 * where条件语句
 * @author shaoshuai
 * create  2015-06-10 17:08:20
 */
public class Conditions {

	private StringBuilder builder;
	public Conditions(StringBuilder builder) {
		this.builder = builder;
	}
	
	public String build() {
		return builder.toString();
	}
	
	public Conditions and() {
		builder.append(Operation.AND);
		return this;
	}
	
	public Conditions or() {
		builder.append(Operation.OR);
		return this;
	}
	
	public Conditions lBracket() {
		builder.append(" ( ");
		return this;
	}
	
	public Conditions rBracket() {
		builder.append(" ) ");
		return this;
	}
	
	public Conditions lt(String propertyName, Object propertyValue) {
		builder.append(" ").append(propertyName).append(Operation.LT.value()).append(" '").append(propertyValue).append("' ");
		return this;
	}
	
	public Conditions le(String propertyName, Object propertyValue) {
		builder.append(" ").append(propertyName).append(Operation.LE.value()).append(" '").append(propertyValue).append("' ");
		return this;
	}

	public Conditions eq(String propertyName, Object propertyValue) {
		builder.append(" ").append(propertyName).append(Operation.EQ.value()).append(" '").append(propertyValue).append("' ");
		return this;
	}
	
	public Conditions ne(String propertyName, Object propertyValue) {
		builder.append(" ").append(propertyName).append(Operation.NE.value()).append(" '").append(propertyValue).append("' ");
		return this;
	}
	
	public Conditions gt(String propertyName, Object propertyValue) {
		builder.append(" ").append(propertyName).append(Operation.GT.value()).append(" '").append(propertyValue).append("' ");
		return this;
	}
	
	public Conditions ge(String propertyName, Object propertyValue) {
		builder.append(" ").append(propertyName).append(Operation.GE.value()).append(" '").append(propertyValue).append("' ");
		return this;
	}
	
	public Conditions between(String propertyName, Object startPropertyValue, Object endPropertyValue) {
		builder.append(" ").append(propertyName).append(Operation.BETWEEN.value())
				.append(" '").append(startPropertyValue).append("' ")
				.append(Operation.AND.value()).append(" '").append(endPropertyValue).append("' ");
		return this;
	}
	
	public Conditions isNull(String propertyName) {
		builder.append(" ").append(propertyName).append(" ").append(Operation.ISNULL.value()).append(" ");
		return this;
	}
	
	public Conditions isNotNull(String propertyName) {
		builder.append(" ").append(propertyName).append(Operation.ISNOTNULL.value()).append(" ");
		return this;
	}
	
	public Conditions in(String propertyName, Object propertyValue) {
		builder.append(" ").append(propertyName).append(Operation.IN.value()).append(" ('").append(propertyValue).append("') ");
		return this;
	}
	
	public Conditions in(String propertyName, Object[] propertyValue) {
		builder.append(" ").append(propertyName).append(Operation.IN.value()).
			append(" ('").append(Arrays.toString(propertyValue).replace("[", "").replace("]", "")).append("') ");
		return this;
	}
	
	public Conditions notIn(String propertyName, Object propertyValue) {
		builder.append(" ").append(propertyName).append(Operation.NOTIN.value()).append(" ('").append(propertyValue).append("') ");
		return this;
	}
	
	public Conditions like(String propertyName, Object propertyValue) {
		builder.append(" ").append(propertyName).append(Operation.LIKE.value()).append(" '%").append(propertyValue).append("%' ");
		return this;
	}
	
	public Conditions llike(String propertyName, Object propertyValue) {
		builder.append(" ").append(propertyName).append(Operation.LIKE.value()).append(" '%").append(propertyValue).append("' ");
		return this;
	}
	
	public Conditions rlike(String propertyName, Object propertyValue) {
		builder.append(" ").append(propertyName).append(Operation.LIKE.value()).append(" '").append(propertyValue).append("%' ");
		return this;
	}
	
	public Conditions limit(int offset, int fetchSize) {
		builder.append(" limit ").append(offset).append(" , ").append(fetchSize);
		return this;
	}
	
	public Conditions groupBy(String propertyName) {
		builder.append(" group by ").append(propertyName).append(" ");
		return this;
	}
	
	public Conditions having(String propertyName, Operation operation, Object propertyValue) {
		builder.append(" having ").append(propertyName).append(" ").append(operation.value()).append(" ").append(propertyValue);
		return this;
	}
	
	public Conditions asc(String propertyName) {
		builder.append(" ").append(" order by ").append(propertyName).append(" asc ");
		return this;
	}
	
	public Conditions desc(String propertyName) {
		builder.append(" ").append(" order by ").append(propertyName).append(" desc ");
		return this;
	}

}
