package com.example.testcenter.security;


import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.SignatureException;

// @Component
@AllArgsConstructor
@Slf4j
public class JwtTokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String bearerToken =  ((HttpServletRequest)request).getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")){
            bearerToken = bearerToken.substring(7);
        }

        try {
            if (jwtTokenProvider.isValidToken(bearerToken)) {
                Authentication authentication = jwtTokenProvider.authentication(bearerToken);
                if (authentication != null) {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            else log.debug("Время жизни токена вышло");
        } catch (Exception e){
            log.error("-----ERROR IN DO-FILTER BLOCK:  {}", e.getMessage());
        }

        chain.doFilter(request, response);
    }


}
