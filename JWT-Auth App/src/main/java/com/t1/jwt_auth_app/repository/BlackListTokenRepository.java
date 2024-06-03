package com.t1.jwt_auth_app.repository;


import com.t1.jwt_auth_app.model.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface BlackListTokenRepository extends JpaRepository<BlacklistToken, Long> {
    boolean existsByToken(String token);
    void deleteByExpiredAtBefore(Date date);
}
