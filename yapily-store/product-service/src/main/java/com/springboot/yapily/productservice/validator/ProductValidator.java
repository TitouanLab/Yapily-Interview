package com.springboot.yapily.productservice.validator;

import com.springboot.yapily.productservice.dto.ProductRequest;
import com.springboot.yapily.productservice.exception.*;
import com.springboot.yapily.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ProductValidator {

    public static final List<String> validLabels = Arrays.asList("drink", "food", "clothes", "limited");

    @Autowired
    private ProductRepository productRepository;

    public void validateProduct(ProductRequest productRequest) {
        validateProductName(productRequest.getName());
        validateProductPrice(productRequest.getPrice());
        validateProductLabels(productRequest.getLabels());
    }

    private void validateProductName(String name) {
        if (name == null || name.isEmpty()) {
            throw new ProductNameNullOrEmpty();
        }
        if (name.length() > 200) {
            throw new ProductNameLengthException();
        }
        if (productRepository.findByName(name).isPresent()) {
            throw new ProductNameAlreadyExistsException(name);
        }
    }

    private void validateProductPrice(Double price) {
        if (price == null || price < 0) {
            throw new ProductPriceNullOrNegativeException();
        }
    }

    private void validateProductLabels(List<String> labels) {
        if (labels == null || labels.isEmpty()) {
            throw new ProductLabelNullOrEmpty();
        }
        for (String label : labels) {
            if (!validLabels.contains(label)) {
                throw new ProductLabelInvalidException(label);
            }
        }
    }
}
