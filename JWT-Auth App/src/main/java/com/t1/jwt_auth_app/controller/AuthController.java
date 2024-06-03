package com.t1.jwt_auth_app.controller;


import com.t1.jwt_auth_app.controller.swagger.SwaggerAuthController;
import com.t1.jwt_auth_app.mapper.UserMapper;
import com.t1.jwt_auth_app.model.entity.User;
import com.t1.jwt_auth_app.model.request.AuthRequestDto;
import com.t1.jwt_auth_app.model.request.UserDto;
import com.t1.jwt_auth_app.model.response.AuthResponseDto;
import com.t1.jwt_auth_app.model.response.UserResponseDto;
import com.t1.jwt_auth_app.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController implements SwaggerAuthController {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @Override
    public ResponseEntity<UserResponseDto> registerNewUser(@RequestBody @Valid UserDto userDto) {
        User user = authenticationService.registerUser(userMapper.userDtoToUser(userDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.userToUserResponse(user));
    }


    @PostMapping( "/login")
    @Override
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto authRequestDto)  {
        AuthResponseDto authResponseDto = authenticationService.login(authRequestDto);
        return ResponseEntity.ok(authResponseDto);
    }
}
