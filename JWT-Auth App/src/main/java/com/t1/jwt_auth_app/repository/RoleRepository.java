package com.t1.jwt_auth_app.repository;

import com.t1.jwt_auth_app.model.entity.Role;
import com.t1.jwt_auth_app.model.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByAuthority(RoleType authority);
}
