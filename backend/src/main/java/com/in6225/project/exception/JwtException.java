package com.in6225.project.exception;

public class JwtException extends RuntimeException{
    public JwtException(String msg, Exception e) {
        super(msg, e);
    }
}
