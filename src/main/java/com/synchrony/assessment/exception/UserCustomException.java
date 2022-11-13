package com.synchrony.assessment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserCustomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserCustomException() {
        super();
    }

    public UserCustomException(String message) {
        super(message);
    }

    public UserCustomException(String message, Throwable cause) {
        super(message, cause);
    }
}