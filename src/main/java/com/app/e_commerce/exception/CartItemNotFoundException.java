package com.app.e_commerce.exception;

public class CartItemNotFoundException extends RuntimeException{
    public CartItemNotFoundException(String msg ){
        super(msg);
    }
}
