package com.t1.jwt_auth_app.service;

import com.t1.jwt_auth_app.model.entity.BlacklistToken;
import com.t1.jwt_auth_app.repository.BlackListTokenRepository;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class BlackListServiceImpl implements BlackListService {

    private final BlackListTokenRepository blackListTokenRepository;

    @Value("${jwt.secret}")
    private String secret;
    @Override
    public void pushTokenToBlacklist(String token) {
        if (token != null) {
            Date expiration = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token)
                    .getBody().getExpiration();
            BlacklistToken blacklistToken = BlacklistToken.builder()
                    .token(token)
                    .expiredAt(expiration)
                    .build();

            blackListTokenRepository.save(blacklistToken);
        }
    }

    @Override
    public boolean isInvalid(String token) {
        return blackListTokenRepository.existsByToken(token);
    }

    @Scheduled(cron = "${scheduled-tasks.blacklist}")
    public void deleteExpiredTokens() {
        blackListTokenRepository.deleteByExpiredAtBefore(new Date());
    }
}
