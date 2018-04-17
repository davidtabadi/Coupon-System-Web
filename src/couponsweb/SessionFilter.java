package couponsweb;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import facades.AdminFacade;
import facades.CompanyFacade;
import facades.CustomerFacade;

@WebFilter("/api/*")
public class SessionFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// before processing
		String url = ((HttpServletRequest) request).getRequestURI();
		HttpSession session = ((HttpServletRequest) request).getSession(false);

		if (url.contains("/api/admin/login") || url.contains("/api/company/login")
				|| url.contains("/api/customer/login")) {
			// pass the request along the filter chain
			// post processing
			chain.doFilter(request, response);
			return;
		} else if (session == null) {
			HttpServletResponse res = (HttpServletResponse) response;
			res.getWriter().println("{\"error\":\"you are not logged in\"}");
			res.setContentType(MediaType.APPLICATION_JSON);
			res.setStatus(500);
			return;
		} else if (url.contains("/api/admin")) {
			// url admin/company/customer
			AdminFacade af = ((AdminFacade) session.getAttribute("admin"));
			if (af != null) {
				chain.doFilter(request, response);
				return;
			} else {
				HttpServletResponse res = (HttpServletResponse) response;
				res.getWriter().println("{\"error\":\"you are not logged in as Admin\"}");
				res.setContentType(MediaType.APPLICATION_JSON);
				res.setStatus(500);
				return;
			}
		} else if (url.contains("/api/company")) {
			CompanyFacade cf = (CompanyFacade) session.getAttribute("company");
			if (cf != null) {
				chain.doFilter(request, response);
				return;
			} else {
				HttpServletResponse res = (HttpServletResponse) response;
				res.getWriter().println("{\"error\":\"you are not logged in as Company\"}");
				res.setContentType(MediaType.APPLICATION_JSON);
				res.setStatus(500);
				return;
			}
		} else if (url.contains("/api/customer")) {
			CustomerFacade custF = ((CustomerFacade) session.getAttribute("customer"));
			if (custF != null) {
				chain.doFilter(request, response);
				return;
			} else {
				HttpServletResponse res = (HttpServletResponse) response;
				res.getWriter().println("{\"error\":\"you are not logged in as Customer\"}");
				res.setContentType(MediaType.APPLICATION_JSON);
				res.setStatus(500);
				return;
			}
		} else {
			HttpServletResponse res = (HttpServletResponse) response;
			res.getWriter().println("{\"error\":\"you are not logged in\"}");
			res.setContentType(MediaType.APPLICATION_JSON);
			res.setStatus(500);
			return;
		}
	}
	// if (session == null || af == null || cf == null || custF ==
	// null) {
	// System.out.println("Try login again. Redirecting browser to
	// login");
	// ((HttpServletResponse) response).sendRedirect("index.html");
	// return;

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}
}