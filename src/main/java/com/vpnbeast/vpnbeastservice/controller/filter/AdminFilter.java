package com.vpnbeast.vpnbeastservice.controller.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        if (req.getHeader("roles").contains("ADMIN"))
            chain.doFilter(request, response);
        else
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Bad permissions");
    }

}