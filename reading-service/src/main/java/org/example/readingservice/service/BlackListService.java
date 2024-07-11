package org.example.readingservice.service;

public interface BlackListService {

    void pushTokenToBlacklist(String token);
    boolean isInvalid(String token);
}
