package com.app.e_commerce.exception;

public class EmptyCartException  extends RuntimeException{
    public EmptyCartException(String message) {
        super(message);
    }
}
