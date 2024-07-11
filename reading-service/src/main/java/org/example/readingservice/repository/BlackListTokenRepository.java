package org.example.readingservice.repository;

import org.example.readingservice.model.BlacklistToken;


public interface BlackListTokenRepository {

    boolean existsByToken(String token);
    void deleteExpiredTokens();
    void saveToken(BlacklistToken blacklistToken);
}
