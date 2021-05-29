package com.vpnbeast.vpnbeastservice.security;

import com.vpnbeast.vpnbeastservice.exception.ExceptionInfo;
import com.vpnbeast.vpnbeastservice.exception.TokenExpiredException;
import com.vpnbeast.vpnbeastservice.model.enums.ExceptionMessage;
import com.vpnbeast.vpnbeastservice.model.enums.OperationType;
import com.vpnbeast.vpnbeastservice.service.JwtTokenService;
import com.vpnbeast.vpnbeastservice.persistent.service.impl.JwtUserDetailsServiceImpl;
import com.vpnbeast.vpnbeastservice.util.DateUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUserDetailsServiceImpl jwtUserDetailsService;
    private final JwtTokenService jwtTokenService;
    private HandlerExceptionResolver resolver;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    public void setResolver(HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            final String usernameHeader = request.getHeader("username");
            final String rolesHeader = request.getHeader("roles");

            log.info("usernameHeader = {}, rolesHeader = {}", usernameHeader, rolesHeader);
            if (usernameHeader != null && rolesHeader != null) {
                UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(usernameHeader);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

            chain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
            logger.error("", e);
        }
    }

}