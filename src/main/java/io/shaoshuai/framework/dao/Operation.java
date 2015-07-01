package io.shaoshuai.framework.dao;

/**
 * 条件操作
 * @author shaoshuai
 * create  2015-06-10 17:08:20
 *
 */
public enum Operation {

	LT(" < "),
	LE(" <= "),
	EQ(" = "),
	NE(" <> "),
	GT(" > "),
	GE(" >= "),
	BETWEEN(" between "),
	AND(" and "),
	OR(" or "),
	ISNULL(" is null "),
	ISNOTNULL(" is not null "),
	IN(" in "),
	NOTIN(" not in "),
	LIKE(" like ");
	
	
	private String value;
	private Operation(String value){
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
}
