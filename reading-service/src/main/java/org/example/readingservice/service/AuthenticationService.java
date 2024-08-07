package org.example.readingservice.service;

import org.example.readingservice.dto.request.LoginRequestDto;
import org.example.readingservice.dto.response.AuthResponseDto;
import org.example.readingservice.model.user.User;

/**
 * Interface for handling user authentication.
 */
public interface AuthenticationService {

    /**
     * Registers a new user.
     * @param user the user to be registered
     */
    void registerUser(User user);

    User findByEmail(String email);

    /**
     * Logs a user in.
     * @param loginRequestDto the user's login request
     */
    AuthResponseDto login(LoginRequestDto loginRequestDto);
}
