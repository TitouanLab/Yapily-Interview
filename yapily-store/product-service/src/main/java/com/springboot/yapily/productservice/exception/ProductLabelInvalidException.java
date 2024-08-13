package com.springboot.yapily.productservice.exception;

public class ProductLabelInvalidException extends RuntimeException {
    public ProductLabelInvalidException(String label) {
        super("Product label is not a valid label : " + label);
    }
}
