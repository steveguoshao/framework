package io.shaoshuai.framework.ioc.test;

public class QueryRunner {

	public QueryRunner(){
		System.out.println("QueryRunner被IoC容器调用了");
	}

	@Override
	public String toString() {
		return "QueryRunner [我最终从BeanFacotry中出来被调用了]";
	}
	
}
