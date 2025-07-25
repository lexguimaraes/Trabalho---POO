package com.alex.exception;

public class EstoquesVaziosException extends RuntimeException {
    public EstoquesVaziosException(String message) {
        super(message);
    }
}
