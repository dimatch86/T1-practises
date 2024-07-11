package org.example.readingservice.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.readingservice.exception.custom.BlackListTokenException;
import org.example.readingservice.service.BlackListService;
import org.example.readingservice.util.TokenGetter;
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
        String token = TokenGetter.getToken(request);
        if (token != null && blackListService.isInvalid(token)) {
            throw new BlackListTokenException("Попытка входа с невалидным токеном");
        }
        filterChain.doFilter(request, response);
    }
}
