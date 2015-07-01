package io.shaoshuai.framework.mvc.page;

import io.shaoshuai.framework.utils.StringUtils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @Title PageTag.java
 * @fileName PageTag.java
 * @Description TODO
 * @author guoqiang
 * @createDate 2013-12-31
 * @version 1.0
 */
public class PageTag extends TagSupport {

	private static final long serialVersionUID = -7945190302922846261L;
	/** 页面默认显示数据条数 */
	private int pageSize = 15; // 如果外面不指定，将默认显示15条
	
	/** 是否显示总记录数 */
	private boolean totalAccount = true; // 如果外面不指定，将默认为显示

	/** 是否显示总页数 */
	private boolean pageAccount = true; // 如果外面不指定，将默认为显示

	/** 当前页码的显示样式 **/
	private String curPageNumberStyle = "pagenumbervisited"; // 如果外面不指定，将按默认值

	/** 带链接的页码的显示样式 **/
	private String linkPageNumberStyle = "pagenumber";// 如果外面不指定，将按默认值
	
	public int doStartTag() throws JspException {
		// 在标签类中定义Request对象
		HttpServletRequest request = (HttpServletRequest) pageContext
				.getRequest();
		String ctx = request.getContextPath();
		if (null != request.getAttribute("page")) {
			Page page = getPage(request);
			JspWriter out = pageContext.getOut();
			StringBuffer sb = new StringBuffer("");
			try {
				// 添加隐藏域保存pagetag的几个参数的值
				sb.append("<input type=\"hidden\" id=\"pageIndex\" name=\"pageIndex\" value=\"").append(page.getPageIndex()).append("\"/>");
				sb.append("<input type=\"hidden\" id=\"pageSize\" name=\"pageSize\" value=\"").append(page.getPageSize()).append("\"/>");
				sb.append("<input type=\"hidden\" id=\"pageUrl\" name=\"pageUrl\" value=\"").append(page.getUrl()).append("\"/>");
				sb.append("<input type=\"hidden\" id=\"totalAccount\" name=\"totalAccount\" value=\"").append(isTotalAccount()).append("\"/>");
				sb.append("<input type=\"hidden\" id=\"pageAccount\" name=\"pageAccount\" value=\"").append(isPageAccount()).append("\"/>");
				sb.append("<input type=\"hidden\" id=\"curPageNumberStyle\" name=\"curPageNumberStyle\" value=\"").append(getCurPageNumberStyle()).append("\"/>");
				sb.append("<input type=\"hidden\" id=\"linkPageNumberStyle\" name=\"linkPageNumberStyle\" value=\"").append(getLinkPageNumberStyle()).append("\"/>");
				// 如果设定了要打印总页数，则在JSP页面中输出当前页和总页数
				if (this.isPageAccount())
					sb.append("<span class=\"pagefirst\">当前页/总页数：</span><span class=\"pagetotal\">").append(page.getPageIndex())
							.append("/").append(page.getPageCount())
							.append("</span>");

				sb.append("<span class=\"pagefirst\" style=\"margin-left:20px;\">页码：</span>");

				// 如果当前页码大于1时，应该输出开始一页和上一页的连接
				sb.append("<a class=\"pageicon\" title=\"首页\" href=\"javascript:void(0)\"");
				if (page.hasPrevious()) {
					sb.append("onclick=\"queryListByPage('").append(1).append("')\"");
				}
				sb.append(">");
				sb.append("<img align=\"middle\"");
				if (!page.hasPrevious()) {
					sb.append("class=\"dis\"");
				}
				sb.append("src=\"").append(ctx)
				  .append("/images/icon-first.gif\" border=\"0\" style=\"float:left;margin-right:5px\"/></a>");
				sb.append("<a class=\"pageicon\" title=\"上一页\" href=\"javascript:void(0)\" ");
				if (page.hasPrevious()) {
					sb.append("onclick=\"queryListByPage('").append(page.getPageIndex() - 1).append("')\"");
				}
				sb.append(">");
				sb.append("<img align=\"middle\"");
				if (!page.hasPrevious()) {
					sb.append("class=\"dis\"");
				}
				sb.append("src=\"").append(ctx)
						.append("/images/icon-prev.gif\" border=\"0\" style=\"float:left;margin-right:5px\"/></a>");

				// 开始在总页数里循环
				for (int cur = 1; cur <= page.getPageCount(); cur++) {
					// 假如有几十页、几百页、甚至n多，我们不能全部将页码输出吧？这里只取5页
					if (page.getPageIndex() < 3 && cur < 6) {
						if (cur == page.getPageIndex()) {
							// 当前页不应该有连接
							sb.append("<span class=\"pagenumbervisited\"><div>")
									.append(cur).append("</div></span>");
						} else {
							sb.append("<span class=\"pagenumbervisited\"><a href=\"javascript:void(0)\"")
									.append(" onclick=\"queryListByPage('").append(cur).append("')\"")
									.append(" class=\"pagenumber\" title=\"第")
									.append(cur).append("页\">").append(cur)
									.append("</a></span>");
						}
					} else {
						// 进入5页以后，要让当前页码总在中间。
						if (cur > page.getPageIndex() - 3
								&& cur < page.getPageIndex() + 3) {
							if (cur == page.getPageIndex()) {
								sb.append("<span class=\"pagenumbervisited\"><div>")
										.append(cur).append("</div></span>");
							} else {
								sb.append("<span class=\"pagenumbervisited\"><a href=\"javascript:void(0)\"")
									    .append(" onclick=\"queryListByPage('").append(cur).append("')\"")
										.append(" class=\"pagenumber\" title=\"第")
										.append(cur).append("页\">").append(cur)
										.append("</a></span>");
							}
						}
					}
				}

				// 只要没有进到最后一页，都应该输出下一页和最后一页连接
				sb.append("<a class=\"pageicon\" title=\"下一页\" href=\"javascript:void(0)\" ");
				if (!page.isEmpty() && page.hasNext()) {
					sb.append("onclick=\"queryListByPage('").append(page.getPageIndex() + 1).append("')\"");
				}
				sb.append(">");
				sb.append("<img align=\"middle\" ");
				if (page.isEmpty() || !page.hasNext()) {
					sb.append("class=\"dis\"");
				}
				sb.append(" src=\"").append(ctx)
						.append("/images/icon-next.gif\" border=\"0\" style=\"float:left;margin-right:5px\"/></a>");
				sb.append("<a class=\"pageicon\" title=\"最后一页\" href=\"javascript:void(0)\" ");
				if (!page.isEmpty() && page.hasNext()) {
					sb.append("onclick=\"queryListByPage('").append(page.getPageCount()).append("')\"");
				}
				sb.append(">");
				sb.append("<img align=\"middle\" ");
				if (page.isEmpty() || page.isLast()) {
					sb.append("class=\"dis\"");
				}
				sb.append(" src=\"").append(ctx)
						.append("/images/icon-last.gif\" border=\"0\" style=\"float:left;margin-right:5px\"/></a>");

				// 标记中调用是否指定输出每页记录数和总记录数
				if (this.isTotalAccount())
					sb.append("<span class=\"pagefirst\" style=\"margin-left:30px;\">")
					.append("每页记录数/总记录数：</span><span class=\"pagetotal\">")
					.append(page.getPageSize() + "/" + page.getTotalCount()).append("</span>");

			} catch (Exception e) {
				e.printStackTrace();
				sb.append(e.getMessage());
			} finally {
				// 往页面上输出
				try {
					out.println(sb.toString());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return TagSupport.SKIP_BODY;
	}

	private Page getPage(HttpServletRequest request) {
		Page page = (Page) request.getAttribute("page");
		String totalAccount = request.getParameter("totalAccount");
		if(StringUtils.isNotEmpty(totalAccount)) {
			this.setTotalAccount(new Boolean(totalAccount).booleanValue());
		}
		String pageAccount = request.getParameter("pageAccount");
		if(StringUtils.isNotEmpty(pageAccount)) {
			this.setPageAccount(new Boolean(pageAccount).booleanValue());
		}
		String curPageNumberStyle = request.getParameter("curPageNumberStyle");
		if(StringUtils.isNotEmpty(curPageNumberStyle)) {
			this.setCurPageNumberStyle(curPageNumberStyle);
		}
		String linkPageNumberStyle = request.getParameter("linkPageNumberStyle");
		if(StringUtils.isNotEmpty(pageAccount)) {
			this.setLinkPageNumberStyle(linkPageNumberStyle);
		}
		return page;
	}
	
	public boolean isTotalAccount() {
		return totalAccount;
	}

	public boolean isPageAccount() {
		return pageAccount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalAccount(boolean totalAccount) {
		this.totalAccount = totalAccount;
	}

	public void setPageAccount(boolean pagecount) {
		this.pageAccount = pagecount;
	}

	public String getCurPageNumberStyle() {
		return curPageNumberStyle;
	}

	public void setCurPageNumberStyle(String curPageNumberStyle) {
		this.curPageNumberStyle = curPageNumberStyle;
	}

	public String getLinkPageNumberStyle() {
		return linkPageNumberStyle;
	}

	public void setLinkPageNumberStyle(String linkPageNumberStyle) {
		this.linkPageNumberStyle = linkPageNumberStyle;
	}

}
