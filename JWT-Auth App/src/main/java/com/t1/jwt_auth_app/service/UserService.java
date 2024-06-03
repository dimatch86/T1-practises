package com.t1.jwt_auth_app.service;

import com.t1.jwt_auth_app.model.entity.RoleType;
import com.t1.jwt_auth_app.model.entity.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();
    User findById(Long idd);
    User addRole(Long id, RoleType roleType);
    User removeRole(Long id, RoleType roleType);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
}
