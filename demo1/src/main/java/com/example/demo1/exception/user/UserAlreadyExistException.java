package com.example.demo1.exception.user;

public class UserAlreadyExistException extends UserException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
