package com.springboot.yapily.orderservice.util;

import com.springboot.yapily.orderservice.dto.CartItemRequest;
import com.springboot.yapily.orderservice.dto.CartItemResponse;
import com.springboot.yapily.orderservice.dto.CartResponse;
import com.springboot.yapily.orderservice.dto.CheckoutCartResponse;
import com.springboot.yapily.orderservice.model.Cart;
import com.springboot.yapily.orderservice.model.CartItem;

import java.util.stream.Collectors;

public class CartDTOConverter {

    public static CartResponse buildCartResponse(Cart cart) {
        return CartResponse.builder()
                .cartId(cart.getId())
                .cartItemResponses(cart.getCartItems().stream().map(CartDTOConverter::buildCartItemResponse).collect(Collectors.toList()))
                .checkedOut(cart.getCheckedOut())
                .build();
    }

    public static CartItemResponse buildCartItemResponse(CartItem cartItem) {
        return CartItemResponse.builder()
                .productId(cartItem.getProductId())
                .quantity(cartItem.getQuantity())
                .build();
    }

    public static CartItem buildCartItem(CartItemRequest cartItemRequest) {
        return CartItem.builder()
                .productId(cartItemRequest.getProductId())
                .quantity(cartItemRequest.getQuantity())
                .build();
    }

    public static CheckoutCartResponse buildCheckoutCartResponse(Cart cart, Double sumCartItemsPrice) {
        return CheckoutCartResponse.builder()
                .cart(buildCartResponse(cart))
                .totalCost(sumCartItemsPrice)
                .build();
    }
}
