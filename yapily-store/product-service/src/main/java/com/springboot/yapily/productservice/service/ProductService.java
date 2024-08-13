package com.springboot.yapily.productservice.service;

import com.springboot.yapily.productservice.util.ProductDTOConverter;
import com.springboot.yapily.productservice.dto.ProductRequest;
import com.springboot.yapily.productservice.dto.ProductResponse;
import com.springboot.yapily.productservice.exception.ProductNotFoundException;
import com.springboot.yapily.productservice.model.Product;
import com.springboot.yapily.productservice.repository.ProductRepository;
import com.springboot.yapily.productservice.validator.ProductValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductValidator productValidator;

    public List<ProductResponse> getProducts() {
        return ProductDTOConverter.convertProductsToDTOs(productRepository.findAll());
    }

    public ProductResponse getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            throw new ProductNotFoundException(id);
        }
        return ProductDTOConverter.convertProductToDTO(product.get());
    }

    public ProductResponse createProduct(ProductRequest productRequest) {
        productValidator.validateProduct(productRequest);
        Product product = ProductDTOConverter.convertDTOToProduct(productRequest);

        Product createdProduct = productRepository.save(product);

        return ProductDTOConverter.convertProductToDTO(createdProduct);
    }

    public void deleteProductById(Long id) {
        productRepository.deleteById(id);  
    }
}
