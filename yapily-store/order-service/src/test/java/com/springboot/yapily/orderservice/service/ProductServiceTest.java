package com.springboot.yapily.orderservice.service;

import com.springboot.yapily.orderservice.config.WebClientConfig;
import com.springboot.yapily.orderservice.dto.CartItemRequest;
import com.springboot.yapily.orderservice.dto.ProductDTO;
import com.springboot.yapily.orderservice.exception.ProductNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockBean
    private WebClientConfig webClientConfig;

    @Mock
    private WebClient webClientMock;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpecMock;

    @SuppressWarnings("rawtypes")
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpecMock;

    @Mock
    private WebClient.ResponseSpec responseSpecMock;

    @BeforeEach
    public void setup() {
        when(webClientConfig.webClient()).thenReturn(webClientMock);
        when(webClientMock.get()).thenReturn(requestHeadersUriSpecMock);
        when(requestHeadersUriSpecMock.uri(anyString())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
    }

    @Test
    public void checkCartItemsAvailability_withAvailableCartItem_noError() {
        CartItemRequest cartItemRequest = new CartItemRequest(1L, 1);

        ProductDTO productDto = new ProductDTO(1L, "", 1.0, LocalDate.of(2024, 8, 12), new ArrayList<>());


        when(responseSpecMock.bodyToMono(
                ArgumentMatchers.<Class<ProductDTO>>notNull())).thenReturn(Mono.just(productDto));

        productService.checkCartItemsAvailability(List.of(cartItemRequest));
    }

    @Test
    public void checkCartItemsAvailability_withUnavailableCartItem_throwsProductNotFoundException() {
        CartItemRequest cartItemRequest = new CartItemRequest(1L, 1);

        when(responseSpecMock.bodyToMono(
                ArgumentMatchers.<Class<ProductDTO>>notNull())).thenThrow(RuntimeException.class);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.checkCartItemsAvailability(List.of(cartItemRequest)));
        assertEquals(exception.getMessage(), "Product not found with id " + 1L);
    }

    @Test
    public void getCartItemsPricesById_withAvailableProduct_assertMapPopulated() {
        ProductDTO productDto = new ProductDTO(1L, "", 1.0, LocalDate.of(2024, 8, 12), new ArrayList<>());

        when(responseSpecMock.bodyToMono(
                ArgumentMatchers.<Class<ProductDTO>>notNull())).thenReturn(Mono.just(productDto));

        HashMap<Long, Double> result = productService.getCartItemsPricesById(List.of(1L));
        assertEquals(result.get(1L), 1.0);
    }

    @Test
    public void getCartItemsPricesById_withUnavailableProduct_throwsProductNotFoundException() {
        when(responseSpecMock.bodyToMono(
                ArgumentMatchers.<Class<ProductDTO>>notNull())).thenThrow(RuntimeException.class);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> productService.getCartItemsPricesById(List.of(1L)));
        assertEquals(exception.getMessage(), "Product not found with id " + 1L);
    }
}
