package com.springboot.yapily.productservice.exception;

public class ProductPriceNullOrNegativeException extends RuntimeException {

    public ProductPriceNullOrNegativeException() {
        super("Product price cannot be null or negative");
    }
}
