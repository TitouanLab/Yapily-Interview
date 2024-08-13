package com.springboot.yapily.orderservice.service;

import com.springboot.yapily.orderservice.config.WebClientConfig;
import com.springboot.yapily.orderservice.dto.CartItemRequest;
import com.springboot.yapily.orderservice.dto.ProductDTO;
import com.springboot.yapily.orderservice.exception.ProductNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private WebClientConfig webClientConfig;

    public void checkCartItemsAvailability(List<CartItemRequest> cartItemRequests) {
        List<Long> cartItemIds = cartItemRequests.stream().map(CartItemRequest::getProductId).toList();
        for (Long cartItemId : cartItemIds) {
            ProductDTO productDTO = getCartItemProductById(cartItemId);
            if (productDTO == null) {
                throw new ProductNotFoundException(cartItemId);
            }
        }
    }

    public HashMap<Long, Double> getCartItemsPricesById(List<Long> cartItemIds) {
        HashMap<Long, Double> productPrices = new HashMap<>();
        for (Long id : cartItemIds) {
            ProductDTO productDTO = getCartItemProductById(id);
            if (productDTO == null) {
                throw new ProductNotFoundException(id);
            }
            productPrices.put(id, productDTO.getPrice());
        }
        return productPrices;
    }

    private ProductDTO getCartItemProductById(Long cartItemId) {
        try {
            return webClientConfig.webClient().get()
                    .uri("http://localhost:8080/api/products/" + cartItemId)
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();
        } catch (Exception e) {
            return null;
        }
    }
}
