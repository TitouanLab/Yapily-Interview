package com.springboot.yapily.orderservice.service;

import com.springboot.yapily.orderservice.dto.CheckoutCartResponse;
import com.springboot.yapily.orderservice.exception.CartNotFoundException;
import com.springboot.yapily.orderservice.model.Cart;
import com.springboot.yapily.orderservice.model.CartItem;
import com.springboot.yapily.orderservice.repository.CartRepository;
import com.springboot.yapily.orderservice.util.CartDTOConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CartCheckoutService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    public CheckoutCartResponse checkoutCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new CartNotFoundException(cartId));
        cart.setCheckedOut(true);
        cart = cartRepository.save(cart);
        return CartDTOConverter.buildCheckoutCartResponse(cart, getSumCartItemsPrice(cart.getCartItems()));
    }

    private Double getSumCartItemsPrice(List<CartItem> cartItems) {
        List<Long> cartItemIds = cartItems.stream().map(CartItem::getProductId).toList();
        HashMap<Long, Double> productPrices = productService.getCartItemsPricesById(cartItemIds);

        double sum = 0D;
        for (CartItem cartItem : cartItems) {
            sum += productPrices.get(cartItem.getProductId()) * cartItem.getQuantity();
        }
        return sum;
    }
}
