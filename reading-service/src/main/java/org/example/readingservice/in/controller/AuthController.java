package org.example.readingservice.in.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.readingservice.dto.request.LoginRequestDto;
import org.example.readingservice.dto.request.UserDto;
import org.example.readingservice.dto.response.AuthResponseDto;
import org.example.readingservice.dto.response.ResponseDto;
import org.example.readingservice.in.controller.swagger.SwaggerAuthController;
import org.example.readingservice.mapper.mapstruct.UserMapper;
import org.example.readingservice.model.user.User;
import org.example.readingservice.service.AuthenticationService;
import org.example.readingservice.util.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling authentication-related operations.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController implements SwaggerAuthController {

    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    /**
     * Endpoint for registering a new user.
     * @param userDto the user information to be registered
     * @return a ResponseEntity with a success message
     */
    @PostMapping("/register")
    @Override
    public ResponseEntity<ResponseDto<?>> registerNewUser(@RequestBody @Valid UserDto userDto) {
        authenticationService.registerUser(userMapper.userDtoToUser(userDto));
        return ResponseEntity.ok(ResponseUtil.okResponse("Регистрация прошла успешно"));
    }

    /**
     * Endpoint for user login.
     *
     * @param loginRequestDto the login request information
     * @return a ResponseEntity with a success message
     */
    @PostMapping( "/login")
    @Override
    public ResponseEntity<ResponseDto<AuthResponseDto>> login(@Valid @RequestBody LoginRequestDto loginRequestDto)  {
        AuthResponseDto authResponseDto = authenticationService.login(loginRequestDto);
        return ResponseEntity.ok(ResponseUtil.okResponseWithData(authResponseDto));
    }

    /**
     * Endpoint for retrieving the current user's authority information.
     * @return a ResponseEntity with the current user's data
     */
    @GetMapping( "/info")
    @Override
    public ResponseEntity<ResponseDto<?>> getCurrentUserInfo(@AuthenticationPrincipal UserDetails userDetails) {
        User user = authenticationService.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(ResponseUtil.okResponseWithData(userMapper
                        .userToUserResponse(user)));
    }
}
