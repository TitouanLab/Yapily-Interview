package com.springboot.yapily.productservice.exception;

public class ProductNameLengthException extends RuntimeException {
    public ProductNameLengthException() {
        super("Product name length exceeds 200 characters");
    }
}
