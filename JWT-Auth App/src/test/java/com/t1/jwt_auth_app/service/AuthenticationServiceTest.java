package com.t1.jwt_auth_app.service;

import com.t1.jwt_auth_app.model.entity.Role;
import com.t1.jwt_auth_app.model.entity.RoleType;
import com.t1.jwt_auth_app.model.entity.User;
import com.t1.jwt_auth_app.model.request.AuthRequestDto;
import com.t1.jwt_auth_app.model.response.AuthResponseDto;
import com.t1.jwt_auth_app.repository.UserRepository;
import com.t1.jwt_auth_app.security.AppUserDetails;
import com.t1.jwt_auth_app.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {
    private final UserRepository userRepository = mock(UserRepository.class);
    private final JwtUtils jwtUtils = mock(JwtUtils.class);
    Authentication authentication = mock(Authentication.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final AuthenticationService authenticationService = new AuthenticationServiceImpl(userRepository, authenticationManager, jwtUtils);
    User currentUser;
    User newUser;

    @BeforeEach
    public void setUp() {
        currentUser = User.builder()
                .name("user")
                .email("user@mail.ru")
                .password(BCrypt.hashpw("user", BCrypt.gensalt()))
                .roles(Set.of(Role.from(RoleType.ROLE_USER)))
                .build();
        newUser = User.builder()
                .name("user2")
                .email("user2@mail.ru")
                .password(BCrypt.hashpw("user2", BCrypt.gensalt()))
                .roles(Set.of(Role.from(RoleType.ROLE_USER)))
                .build();
    }

    @Test
    void registerUser_whenRegisterNotExistingUser_thenSuccess() {

        when(userRepository.findByEmail(newUser.getEmail())).thenReturn(Optional.empty());

        authenticationService.registerUser(newUser);

        verify(userRepository, times(1))
                .save(newUser);
    }

    @Test
    void testLogin() {
        AuthRequestDto authRequestDto = new AuthRequestDto(currentUser.getEmail(), currentUser.getPassword());
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(authRequestDto.getEmail(), authRequestDto.getPassword());

        when(authenticationManager.authenticate(usernamePasswordAuthenticationToken)).thenReturn(authentication);

        AppUserDetails userDetails = new AppUserDetails(currentUser);

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtUtils.generateJwtToken(userDetails)).thenReturn("jwtToken");

        AuthResponseDto authResponseDto = authenticationService.login(authRequestDto);
        assertThat(authResponseDto.getToken()).isEqualTo("jwtToken");
        assertThat(authResponseDto.getEmail()).isEqualTo("user@mail.ru");
        assertThat(authResponseDto.getRoles()).isEqualTo(Collections.singletonList("ROLE_USER"));
    }
}
