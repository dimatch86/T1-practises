package org.example.readingservice.repository;

import lombok.RequiredArgsConstructor;
import org.example.readingservice.model.BlacklistToken;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RequiredArgsConstructor
@Repository
public class BlackListTokenRepositoryImpl implements BlackListTokenRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void saveToken(BlacklistToken blacklistToken) {
        String sql = "INSERT INTO public.blacklist_token " +
                "(id, token, expired_at) " +
                "VALUES (NEXTVAL('jdbc_sequence'), ?, ?)";
        jdbcTemplate.update(sql, blacklistToken.getToken(), blacklistToken.getExpiredAt());
    }

    @Override
    public boolean existsByToken(String token) {
        String sql = "SELECT EXISTS(SELECT 1 FROM public.blacklist_token bl WHERE bl.token = ?)";
        return jdbcTemplate.queryForObject(sql, new Object[] {token}, Boolean.class);
    }

    @Override
    @Transactional
    public void deleteExpiredTokens() {
        String sql = "DELETE FROM public.blacklist_token bl where bl.expired_at < ?";
        jdbcTemplate.update(sql, new Date());
    }
}
