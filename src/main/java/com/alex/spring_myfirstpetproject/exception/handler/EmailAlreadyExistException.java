package com.alex.spring_myfirstpetproject.exception.handler;

public class EmailAlreadyExistException extends Exception {
    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
