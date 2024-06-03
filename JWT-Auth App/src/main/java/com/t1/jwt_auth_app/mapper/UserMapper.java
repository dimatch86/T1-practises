package com.t1.jwt_auth_app.mapper;

import com.t1.jwt_auth_app.model.entity.User;
import com.t1.jwt_auth_app.model.request.UserDto;
import com.t1.jwt_auth_app.model.response.UserResponseDto;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import java.util.List;


@Mapper(componentModel = "spring")
@DecoratedWith(UserMapperDelegate.class)
public interface UserMapper {

    User userDtoToUser(UserDto userDto);
    UserResponseDto userToUserResponse(User user);
    List<UserResponseDto> usersToUserResponseDtos(List<User> users);
}
