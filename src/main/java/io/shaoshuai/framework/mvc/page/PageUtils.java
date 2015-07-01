
package io.shaoshuai.framework.mvc.page;

import javax.servlet.http.HttpServletRequest;

/**
 * PageUtils.java
 * @author 	  shaoshuai
 * @version   1.0
 */
public class PageUtils {
	
	/** 分页用到的页面索引 */
	public static final String PAGE_INDEX = "pageIndex";
	/** 分页用到的页面数据大小 */
	public static final String PAGE_SIZE = "pageSize";
	/** 分页默认用到的索引 */
	public static final int PAGE_INDEX_DEFAULT_VALUE = 1;
	/** 分页默认页面数据大小 */
	public static final int PAGE_SIZE_DEFAULT_VALUE = 15;
	
	public static int getPageIndex(HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter(PAGE_INDEX));
		} catch (NumberFormatException e) {
			return PAGE_INDEX_DEFAULT_VALUE;
		}
	}

	public static int getPageSize(HttpServletRequest request) {
		try {
			return Integer.parseInt(request.getParameter(PAGE_SIZE));
		} catch (NumberFormatException e) {
			return PAGE_SIZE_DEFAULT_VALUE;
		}
	}
	
	public static Page getPage(HttpServletRequest request, String url) {
		return new Page(getPageIndex(request), getPageSize(request), url);
	}
}