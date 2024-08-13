package com.springboot.yapily.productservice.validator;

import com.springboot.yapily.productservice.dto.ProductRequest;
import com.springboot.yapily.productservice.exception.*;
import com.springboot.yapily.productservice.model.Product;
import com.springboot.yapily.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductValidatorTest {

    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ProductValidator productValidator;

    @Test
    public void validateProduct_withValidProduct_noException() {
        ProductRequest productRequest = new ProductRequest("Cheese", 10.99, Arrays.asList("food", "limited"));
        productValidator.validateProduct(productRequest);
    }

    @Test
    public void validateProduct_withNullName_throwsNullNameException() {
        ProductRequest productRequest = new ProductRequest(null, 10.99, Arrays.asList("food", "limited"));
        assertThrows(ProductNameNullOrEmpty.class, () -> productValidator.validateProduct(productRequest));
    }

    @Test
    public void validateProduct_withEmptyName_throwsEmptyNameException() {
        ProductRequest productRequest = new ProductRequest("", 10.99, Arrays.asList("food", "limited"));
        assertThrows(ProductNameNullOrEmpty.class, () -> productValidator.validateProduct(productRequest));
    }

    @Test
    public void validateProduct_withLongName_throwsNameLengthException() {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            name.append("a");
        }
        ProductRequest productRequest = new ProductRequest(name.toString(), 10.99, Arrays.asList("food", "limited"));
        assertThrows(ProductNameLengthException.class, () -> productValidator.validateProduct(productRequest));
    }

    @Test
    public void validateProduct_withDuplicateName_throwsDuplicateNameException() {
        ProductRequest productRequest = new ProductRequest("Cheese", 10.99, Arrays.asList("food", "limited"));
        when(productRepository.findByName("Cheese")).thenReturn(Optional.of(new Product()));
        assertThrows(ProductNameAlreadyExistsException.class, () -> productValidator.validateProduct(productRequest));
    }

    @Test
    public void validateProduct_withNullPrice_throwsNullPriceException() {
        ProductRequest productRequest = new ProductRequest("Cheese", null, Arrays.asList("food", "limited"));
        assertThrows(ProductPriceNullOrNegativeException.class, () -> productValidator.validateProduct(productRequest));
    }

    @Test
    public void validateProduct_withNegativePrice_throwsNegativePriceException() {
        ProductRequest productRequest = new ProductRequest("Cheese", -1D, Arrays.asList("food", "limited"));
        assertThrows(ProductPriceNullOrNegativeException.class, () -> productValidator.validateProduct(productRequest));
    }

    @Test
    public void validateProduct_withNullLabel_throwsNullLabelException() {
        ProductRequest productRequest = new ProductRequest("Cheese", 10.99, null);
        assertThrows(ProductLabelNullOrEmpty.class, () -> productValidator.validateProduct(productRequest));
    }

    @Test
    public void validateProduct_withEmptyLabel_throwsEmptyLabelException() {
        ProductRequest productRequest = new ProductRequest("Cheese", 10.99, new ArrayList<>());
        assertThrows(ProductLabelNullOrEmpty.class, () -> productValidator.validateProduct(productRequest));
    }

    @Test
    public void validateProduct_withInvalidLabel_throwsInvalidLabelException() {
        ProductRequest productRequest = new ProductRequest("Cheese", 10.99, Arrays.asList("electronics", "limited"));
        assertThrows(ProductLabelInvalidException.class, () -> productValidator.validateProduct(productRequest));
    }
}
