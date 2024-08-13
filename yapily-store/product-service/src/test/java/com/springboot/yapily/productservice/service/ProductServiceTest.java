package com.springboot.yapily.productservice.service;


import com.springboot.yapily.productservice.dto.ProductRequest;
import com.springboot.yapily.productservice.dto.ProductResponse;
import com.springboot.yapily.productservice.exception.ProductNotFoundException;
import com.springboot.yapily.productservice.model.Product;
import com.springboot.yapily.productservice.repository.ProductRepository;
import com.springboot.yapily.productservice.util.ListToStringConverter;
import com.springboot.yapily.productservice.validator.ProductValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {

    private final LocalDate date1 = LocalDate.of(2024, 8, 12);
    private final LocalDate date2 = LocalDate.of(2024, 8, 11);

    @Autowired
    private ProductService productService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductValidator productValidator;

    @Test
    public void getProducts_withEmptyRepository_assertNotNull(){
        when(productRepository.findAll()).thenReturn(new ArrayList<>());

        List<ProductResponse> result = productService.getProducts();

        assertNotNull(result);
        assertEquals(result.size(), 0);
    }

    @Test
    public void getProducts_withValidRepository_assertValidResponse(){
        Product product1 = new Product(1, "Cheese", 10.99, date1, ListToStringConverter.convertToString(Arrays.asList("food", "limited")));
        Product product2 = new Product(2, "Coca Cola", 1.99, date2, ListToStringConverter.convertToString(Arrays.asList("drink")));

        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));
        List<ProductResponse> result = productService.getProducts();

        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getName(), product1.getName());
        assertEquals(result.get(0).getPrice(), product1.getPrice());
        assertEquals(result.get(0).getAddedAt(), product1.getAddedDate());
        assertEquals(result.get(0).getLabels(), ListToStringConverter.convertToList(product1.getLabels()));
        assertEquals(result.get(1).getName(), product2.getName());
        assertEquals(result.get(1).getPrice(), product2.getPrice());
        assertEquals(result.get(1).getAddedAt(), product2.getAddedDate());
        assertEquals(result.get(1).getLabels(), ListToStringConverter.convertToList(product2.getLabels()));
    }

    @Test
    public void getProductById_withEmptyRepository_throwException(){
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getProductById(1L));
        assertEquals(exception.getMessage(), "Product not found with id " + 1L);
    }

    @Test
    public void getProductById_withValidId_assertValidResponse(){
        Product product = new Product(1, "Cheese", 10.99, date1, ListToStringConverter.convertToString(Arrays.asList("food", "limited")));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse result = productService.getProductById(1L);
        assertNotNull(result);
        assertEquals(result.getId(), product.getId());
        assertEquals(result.getName(), product.getName());
        assertEquals(result.getPrice(), product.getPrice());
        assertEquals(result.getAddedAt(), product.getAddedDate());
        assertEquals(result.getLabels(), ListToStringConverter.convertToList(product.getLabels()));
    }

    @Test
    public void createProduct_withValidProduct_assertValidResponse(){
        ProductRequest productRequest = new ProductRequest("Cheese", 10.99, Arrays.asList("food", "limited"));
        Product productWithId = new Product(1, "Cheese", 10.99, date1, ListToStringConverter.convertToString(Arrays.asList("food", "limited")));

        when(productRepository.save(any(Product.class))).thenReturn(productWithId);

        ProductResponse result = productService.createProduct(productRequest);
        assertNotNull(result);
        assertEquals(result.getId(), productWithId.getId());
        assertEquals(result.getName(), productWithId.getName());
        assertEquals(result.getPrice(), productWithId.getPrice());
        assertEquals(result.getAddedAt(), productWithId.getAddedDate());
        assertEquals(result.getLabels(), ListToStringConverter.convertToList(productWithId.getLabels()));
    }

    @Test
    public void getDeleteById_verifyDeleteCalled(){
        productService.deleteProductById(1L);
        verify(productRepository).deleteById(1L);
    }

}
