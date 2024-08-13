package com.springboot.yapily.orderservice.exception;

public class InvalidCartItemQuantityException extends RuntimeException {

    public InvalidCartItemQuantityException() {
        super("Product quantity must be higher than 0");
    }
}
