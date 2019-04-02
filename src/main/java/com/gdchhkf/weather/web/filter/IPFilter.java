package com.gdchhkf.weather.web.filter;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author gdchhkf@163.com
 * @version 1.0
 **/
@WebFilter(filterName = "IPFilter", urlPatterns = "/*")
@Slf4j
public class IPFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 获取客户端真实IP
        if (request.getHeader("x-forwarded-for") == null){
            log.trace(request.getRemoteAddr() + "  ---  "
                    + request.getRemoteHost() + "  ---  "
                    + request.getRemoteUser());
        } else {
            log.trace(request.getHeader("x-forwarded-for"));
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
