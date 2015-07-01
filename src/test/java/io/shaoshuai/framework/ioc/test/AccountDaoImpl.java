/**
 * 
 */
package io.shaoshuai.framework.ioc.test;

/**
 * @author 帅
 *
 */
public class AccountDaoImpl {

	private QueryRunner queryRunner;

	public AccountDaoImpl() {
		System.out.println("AccountDaoImpl被IoC容器调用了");
	}

	public QueryRunner getQueryRunner() {
		return queryRunner;
	}

	public void setQueryRunner(QueryRunner queryRunner) {
		this.queryRunner = queryRunner;
	}
	
	
}
