package com.t1.jwt_auth_app.mapper;


import com.t1.jwt_auth_app.model.entity.User;
import com.t1.jwt_auth_app.model.request.UserDto;
import com.t1.jwt_auth_app.model.response.UserResponseDto;
import com.t1.jwt_auth_app.util.DateConverter;
import org.springframework.security.crypto.bcrypt.BCrypt;

public abstract class UserMapperDelegate implements UserMapper {

    public User userDtoToUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail().toLowerCase())
                .password(BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt()))
                .build();
    }

    @Override
    public UserResponseDto userToUserResponse(User user) {
        return UserResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .registrationDate(DateConverter.formatDate(user.getRegistrationDate()))
                .build();
    }
}
