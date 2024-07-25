package org.example.readingservice.service;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import lombok.RequiredArgsConstructor;

import org.example.readingservice.dto.request.LoginRequestDto;
import org.example.readingservice.dto.response.AuthResponseDto;
import org.example.readingservice.exception.custom.UserAlreadyExistException;
import org.example.readingservice.exception.custom.UserNotFoundException;
import org.example.readingservice.model.user.User;
import org.example.readingservice.repository.UserRepository;
import org.example.readingservice.security.AppUserDetails;
import org.example.readingservice.security.jwt.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * Implementation of the Authentication Service interface.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final Tracer tracer;

    /**
     * Registers a new user.
     * @param user the user to be registered
     * @throws UserAlreadyExistException if the user is already registered
     */
    @Override
    public void registerUser(User user) {
        Span span = tracer.spanBuilder("registerUser").startSpan();
        try (Scope scope = span.makeCurrent()) {
            span.setAttribute("user.email", user.getEmail());
            userRepository.saveUser(user);
            span.setStatus(StatusCode.OK);
        } catch (Exception e) {
            span.recordException(e);
            throw e;
        } finally {
            span.end();
        }
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    /**
     * Logs a user in.
     * @param loginRequestDto the user's login request
     * @throws UserNotFoundException if the user is not found
     */
    @Override
    public AuthResponseDto login(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail().toLowerCase();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email, loginRequestDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return AuthResponseDto.builder()
                .token(jwtUtils.generateJwtToken(userDetails))
                .email(userDetails.getUsername())
                .roles(roles)
                .build();
    }
}
