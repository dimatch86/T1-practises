package com.t1.jwt_auth_app.controller;

import com.t1.jwt_auth_app.controller.swagger.SwaggerUserController;
import com.t1.jwt_auth_app.mapper.UserMapper;
import com.t1.jwt_auth_app.model.entity.RoleType;
import com.t1.jwt_auth_app.model.entity.User;
import com.t1.jwt_auth_app.model.request.UserDto;
import com.t1.jwt_auth_app.model.response.UserResponseDto;
import com.t1.jwt_auth_app.security.AppUserDetails;
import com.t1.jwt_auth_app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController implements SwaggerUserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping( "/info")
    @Override
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal AppUserDetails userDetails) {
        User user = userService.findById(userDetails.getId());
        return ResponseEntity.ok(userMapper.userToUserResponse(user));
    }

    @GetMapping("/users")
    @Override
    public ResponseEntity<List<UserResponseDto>> getUsers() {
        return ResponseEntity.ok(userMapper.usersToUserResponseDtos(userService.getUsers()));
    }

    @PostMapping("/add-role/{id}")
    @Override
    public ResponseEntity<UserResponseDto> addRole(@PathVariable Long id, @RequestParam RoleType type) {
        User user = userService.addRole(id, type);
        return ResponseEntity.ok(userMapper.userToUserResponse(user));
    }

    @PostMapping("/remove-role/{id}")
    @Override
    public ResponseEntity<UserResponseDto> removeRole(@PathVariable Long id, @RequestParam RoleType type) {
        User user = userService.removeRole(id, type);
        return ResponseEntity.ok(userMapper.userToUserResponse(user));
    }

    @PostMapping("/update/{id}")
    @Override
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id, @RequestBody @Valid UserDto userDto) {
        User user = userService.updateUser(id, userMapper.userDtoToUser(userDto));
        return ResponseEntity.ok(userMapper.userToUserResponse(user));
    }


    @DeleteMapping( "/delete/{id}")
    @Override
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
