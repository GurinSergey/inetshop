package com.webstore.security.config;

import com.webstore.security.services.TokenAuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatelessAuthenticationFilter extends GenericFilterBean {
    private final TokenAuthService tokenAuthService;

    public StatelessAuthenticationFilter(TokenAuthService tokenAuthService) {
        this.tokenAuthService = tokenAuthService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        Authentication authentication = tokenAuthService.getAuthentication((HttpServletRequest) req);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("hoho");
        ((HttpServletResponse)servletResponse).setHeader("Access-Control-Allow-Origin", /*"http://192.168.0.53:8065,*/ "http://localhost:8065");
        ((HttpServletResponse)servletResponse).setHeader("Access-Control-Allow-Methods","POST, GET, PATCH, OPTIONS, DELETE");
        ((HttpServletResponse)servletResponse).setHeader("Access-Control-Allow-Credentials","true");
        ((HttpServletResponse)servletResponse).setHeader("Access-Control-Max-Age","3600");
        ((HttpServletResponse)servletResponse).setHeader("Access-Control-Allow-Headers","Origin, Authorization, X-Requested-With, Content-Type, Accept, Key, Cross-Origin, X-AUTH-TOKEN, CLIENT-ID");

        if("OPTIONS".equalsIgnoreCase(((HttpServletRequest)req).getMethod())) {
            ((HttpServletResponse)servletResponse).setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(req, servletResponse);
        }
    }
}