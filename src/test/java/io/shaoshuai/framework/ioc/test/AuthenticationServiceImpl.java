package io.shaoshuai.framework.ioc.test;

public class AuthenticationServiceImpl {

	public AuthenticationServiceImpl(){
		System.out.println("AuthenticationServiceImpl被IoC容器调用了");
	}
	
	private AccountDaoImpl accountDao;

	public AccountDaoImpl getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(AccountDaoImpl accountDao) {
		this.accountDao = accountDao;
	}
	
}
