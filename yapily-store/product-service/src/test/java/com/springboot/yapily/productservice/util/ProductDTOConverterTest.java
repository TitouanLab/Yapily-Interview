package com.springboot.yapily.productservice.util;

import com.springboot.yapily.productservice.dto.ProductRequest;
import com.springboot.yapily.productservice.dto.ProductResponse;
import com.springboot.yapily.productservice.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProductDTOConverterTest {

    private final LocalDate date1 = LocalDate.of(2024,8,13);
    private final LocalDate date2 = LocalDate.of(2024,8,12);

    @Test
    public void convertProductsToDTOs_withValidProducts_assertProductResponse() {
        Product product1 = new Product(1, "Cheese", 10.99, date1, "food,limited");
        Product product2 = new Product(2, "Coca Cola", 1.99, date2, "drink");
        List<Product> products = Arrays.asList(product1, product2);

        List<ProductResponse> result = ProductDTOConverter.convertProductsToDTOs(products);

        assertEquals(result.get(0).getId(), product1.getId());
        assertEquals(result.get(0).getName(), product1.getName());
        assertEquals(result.get(0).getPrice(), product1.getPrice());
        assertEquals(result.get(0).getAddedAt(), product1.getAddedDate());
        assertEquals(result.get(0).getLabels(), ListToStringConverter.convertToList(product1.getLabels()));

        assertEquals(result.get(1).getId(), product2.getId());
        assertEquals(result.get(1).getName(), product2.getName());
        assertEquals(result.get(1).getPrice(), product2.getPrice());
        assertEquals(result.get(1).getAddedAt(), product2.getAddedDate());
        assertEquals(result.get(1).getLabels(), ListToStringConverter.convertToList(product2.getLabels()));
    }

    @Test
    public void convertDTOToProduct_withValidProductRequest_assertProduct() {
        ProductRequest productRequest = new ProductRequest("Cheese", 10.99, Arrays.asList("food", "limited"));
        Product result = ProductDTOConverter.convertDTOToProduct(productRequest);

        assertEquals(result.getName(), productRequest.getName());
        assertEquals(result.getPrice(), productRequest.getPrice());
        assertEquals(result.getLabels(), ListToStringConverter.convertToString(productRequest.getLabels()));
    }
}
