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
            final String requestTokenHeader = request.getHeader("Authorization");
            String userName = null;
            String jwtToken = null;
            // JWT Token is in the form "Bearer token". Remove Bearer word and get
            // only the Token
            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
                userName = jwtTokenService.getUsernameFromToken(jwtToken);
            }

            // Once we get the token validate it.
            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(userName);
                // if token is valid configure Spring Security to manually set
                // authentication
                if (jwtTokenService.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // After setting the Authentication in the context, we specify
                    // that the current user is authenticated. So it passes the
                    // Spring Security Configurations successfully.
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            chain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            resolver.resolveException(request, response, null, new TokenExpiredException(ExceptionInfo.builder()
                    .tag(OperationType.VALIDATE_TOKEN.getType())
                    .httpCode(HttpStatus.UNAUTHORIZED.value())
                    .status(false)
                    .errorMessage(ExceptionMessage.JWT_EXPIRED.getMessage())
                    .timestamp(DateUtil.getCurrentLocalDateTime())
                    .build()));
        } catch (IllegalArgumentException e) {
            logger.error("", e);
        }
    }

}