package com.springboot.yapily.orderservice.exception;

public class CartCheckedOutException extends RuntimeException {
    public CartCheckedOutException(Long id) {
        super("Cart with id " + id + " is already checked out.");
    }
}
