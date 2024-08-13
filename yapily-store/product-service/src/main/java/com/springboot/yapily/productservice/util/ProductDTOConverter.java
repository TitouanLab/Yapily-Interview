package com.springboot.yapily.productservice.util;

import com.springboot.yapily.productservice.dto.ProductRequest;
import com.springboot.yapily.productservice.dto.ProductResponse;
import com.springboot.yapily.productservice.model.Product;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ProductDTOConverter {

    public static List<ProductResponse> convertProductsToDTOs(List<Product> products) {
        return products.stream().map(ProductDTOConverter::convertProductToDTO).collect(Collectors.toList());
    }

    public static ProductResponse convertProductToDTO(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .addedAt(product.getAddedDate())
                .labels(ListToStringConverter.convertToList(product.getLabels()))
                .build();
    }

    public static Product convertDTOToProduct(ProductRequest productRequest) {
        return Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .labels(ListToStringConverter.convertToString(productRequest.getLabels()))
                .addedDate(LocalDate.now())
                .build();
    }
}
