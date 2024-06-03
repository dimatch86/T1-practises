package com.t1.jwt_auth_app.service;

import com.t1.jwt_auth_app.model.entity.User;
import com.t1.jwt_auth_app.model.request.AuthRequestDto;
import com.t1.jwt_auth_app.model.response.AuthResponseDto;
import com.t1.jwt_auth_app.repository.UserRepository;
import com.t1.jwt_auth_app.security.AppUserDetails;
import com.t1.jwt_auth_app.security.jwt.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public User registerUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public AuthResponseDto login(AuthRequestDto authRequestDto) {
        String email = authRequestDto.getEmail().toLowerCase();
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                email, authRequestDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        return AuthResponseDto.builder()
                .token(jwtUtils.generateJwtToken(userDetails))
                .email(userDetails.getUsername())
                .roles(roles)
                .build();
    }
}
