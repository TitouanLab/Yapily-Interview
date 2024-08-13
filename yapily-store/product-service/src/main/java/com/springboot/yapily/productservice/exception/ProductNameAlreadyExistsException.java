package com.springboot.yapily.productservice.exception;

public class ProductNameAlreadyExistsException extends RuntimeException{
    public ProductNameAlreadyExistsException(String name){
        super("Product name already exists : " + name);
    }
}
