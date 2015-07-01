package io.shaoshuai.framework.mvc.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MVCEncodingFilter implements Filter {

	private final String DEFAULT_ENCODING = "UTF-8";
	private String encoding = null;
	private boolean foreEncoding = false;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		encoding = filterConfig.getInitParameter("encoding");
		if(null == encoding) {
			encoding = DEFAULT_ENCODING;
		}
		foreEncoding = Boolean.parseBoolean(filterConfig.getInitParameter("foreEncoding"));
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		// 设置编码，统一解决get、post中文乱码问题
		request.setCharacterEncoding(encoding);
		if(this.foreEncoding) {
			response.setCharacterEncoding(encoding);
			//response.setContentType("text/html;charset=" + encoding);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
