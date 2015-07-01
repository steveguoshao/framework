package io.shaoshuai.framework.ioc.test;

public class LoginController {

	public LoginController() {
		System.out.println("LoginController被IoC容器调用了");
	}

	private AuthenticationServiceImpl authenticationService;

	public AuthenticationServiceImpl getAuthenticationService() {
		return authenticationService;
	}

	public void setAuthenticationService(
			AuthenticationServiceImpl authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	
}
