package com.springboot.yapily.orderservice.service;

import com.springboot.yapily.orderservice.dto.*;
import com.springboot.yapily.orderservice.exception.CartCheckedOutException;
import com.springboot.yapily.orderservice.exception.CartNotFoundException;
import com.springboot.yapily.orderservice.model.Cart;
import com.springboot.yapily.orderservice.repository.CartRepository;
import com.springboot.yapily.orderservice.util.CartDTOConverter;
import com.springboot.yapily.orderservice.valdator.UpdateCartValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UpdateCartValidator updateCartValidator;

    public List<CartResponse> getCarts() {
        List<Cart> carts = cartRepository.findAll();

        return carts.stream().map(CartDTOConverter::buildCartResponse)
                .collect(Collectors.toList());
    }

    public CartResponse createCart() {
        Cart cart = cartRepository.save(new Cart());
        return CartDTOConverter.buildCartResponse(cart);
    }

    public CartResponse updateCart(Long cartId, List<CartItemRequest> cartItemRequests) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));

        updateCartValidator.validateCartForUpdate(cart, cartItemRequests);

        cart.getCartItems().addAll(cartItemRequests.stream()
                .map(CartDTOConverter::buildCartItem)
                .toList());
        cart = cartRepository.save(cart);
        return CartDTOConverter.buildCartResponse(cart);
    }
}
