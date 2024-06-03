package com.t1.jwt_auth_app.service;

public interface BlackListService {

    void pushTokenToBlacklist(String token);
    boolean isInvalid(String token);
}
