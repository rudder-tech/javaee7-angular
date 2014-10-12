package com.cortez.samples.javaee7angular.rest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * CORS Support
 *
 * CORS is a technique to prevent websites from doing bad things with your
 * personal data. Most browsers + javascript toolkits not only support CORS
 * but enforce it, which has implications for your API server which supports
 * Swagger.
 */
@WebFilter(filterName = "ApiOriginFilter", urlPatterns = { "/*" })
public class ApiOriginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        res.addHeader("Access-Control-Allow-Origin", "*");
        res.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        res.addHeader("Access-Control-Allow-Headers", "Content-Type");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
