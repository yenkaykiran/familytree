package yuown.yenkay.familytree.auth;

import static yuown.yenkay.familytree.auth.JwtUtil.BASIC_PREFIX;
import static yuown.yenkay.familytree.auth.JwtUtil.HEADER_STRING;
import static yuown.yenkay.familytree.auth.JwtUtil.TOKEN_PREFIX;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

@Component
public class YuownAuthenticationFilter extends GenericFilterBean {

	@Autowired
	private JwtUtil jwtUtil;

	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
		String header = req.getHeader(HEADER_STRING);
		if (header == null || !header.startsWith(TOKEN_PREFIX)) {
			if("/api/login".equals(req.getServletPath()) && null != header && !header.startsWith(BASIC_PREFIX)) {
				throw new ServletException("Authentication Header missing for Login Validation!");
			} else {
				chain.doFilter(req, res);
				return;
			}
		}
		UsernamePasswordAuthenticationToken authentication = jwtUtil.getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		doFilterInternal((HttpServletRequest) req, (HttpServletResponse) res, chain);
	}
}