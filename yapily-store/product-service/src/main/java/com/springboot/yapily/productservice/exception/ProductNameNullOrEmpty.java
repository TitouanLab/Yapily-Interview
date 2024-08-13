package com.springboot.yapily.productservice.exception;

public class ProductNameNullOrEmpty extends RuntimeException{
    public ProductNameNullOrEmpty() {
        super("Product name is null or empty");
    }
}
