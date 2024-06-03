package com.t1.jwt_auth_app.security.jwt;

import com.t1.jwt_auth_app.exception.BlackListTokenException;
import com.t1.jwt_auth_app.service.BlackListService;
import com.t1.jwt_auth_app.util.TokenExtractor;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class BlackListTokenFilter extends OncePerRequestFilter {

    private final BlackListService blackListService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        String token = TokenExtractor.getToken(request);
        if (token != null && blackListService.isInvalid(token)) {
            throw new BlackListTokenException("Попытка входа с невалидным токеном");
        }
        filterChain.doFilter(request, response);
    }
}
