package com.example.exception;

public class UsernameOrPasswordNotFoundException extends RuntimeException {
    public UsernameOrPasswordNotFoundException(String message) {
        super(message);
    }
}
