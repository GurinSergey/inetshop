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
import java.util.Arrays;
import java.util.List;

public class StatelessAuthenticationFilter extends GenericFilterBean {
    private final TokenAuthService tokenAuthService;

    public StatelessAuthenticationFilter(TokenAuthService tokenAuthService) {
        this.tokenAuthService = tokenAuthService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;

        Authentication authentication = tokenAuthService.getAuthentication(req);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        List<String> incomingURLs = Arrays.asList(
                "http://tolcom.me",
                "http://core.tolcom.me",
                "http://localhost:8065",
                "http://localhost:8075",
                "http://176.36.250.254:8089",
                "http://176.36.250.254:3394",
                "http://176.36.250.254:3392");
        // Get client's origin
        String clientOrigin = ((HttpServletRequest) servletRequest).getHeader("origin");
        String baseOrigin = "http://tolcom.me";


        if (incomingURLs.indexOf(clientOrigin) != -1) {
            baseOrigin = clientOrigin;
        }

        ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Origin", baseOrigin);
        ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, OPTIONS, DELETE");
        ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Credentials", "true");
        ((HttpServletResponse) servletResponse).setHeader("Access-Control-Max-Age", "3600");
        ((HttpServletResponse) servletResponse).setHeader("Access-Control-Allow-Headers", "Origin, Authorization, X-Requested-With, Content-Type, Accept, Key, Cross-Origin, X-AUTH-TOKEN, CLIENT-ID");

        if ("OPTIONS".equalsIgnoreCase((req).getMethod())) {
            ((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(req, servletResponse);
        }
    }
}