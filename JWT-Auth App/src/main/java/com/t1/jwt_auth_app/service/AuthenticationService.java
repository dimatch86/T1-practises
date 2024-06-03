package com.t1.jwt_auth_app.service;


import com.t1.jwt_auth_app.model.entity.User;
import com.t1.jwt_auth_app.model.request.AuthRequestDto;
import com.t1.jwt_auth_app.model.response.AuthResponseDto;

public interface AuthenticationService {

    User registerUser(User user);

    AuthResponseDto login(AuthRequestDto authRequestDto);
}
