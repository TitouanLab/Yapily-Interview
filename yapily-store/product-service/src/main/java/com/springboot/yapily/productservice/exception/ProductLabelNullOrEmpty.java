package com.springboot.yapily.productservice.exception;

public class ProductLabelNullOrEmpty extends RuntimeException {
    public ProductLabelNullOrEmpty() {
        super("Product labels are null or empty");
    }
}
