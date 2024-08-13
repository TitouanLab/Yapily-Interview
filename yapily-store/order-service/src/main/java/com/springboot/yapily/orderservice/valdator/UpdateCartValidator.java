package com.springboot.yapily.orderservice.valdator;

import com.springboot.yapily.orderservice.dto.CartItemRequest;
import com.springboot.yapily.orderservice.exception.CartCheckedOutException;
import com.springboot.yapily.orderservice.exception.InvalidCartItemQuantityException;
import com.springboot.yapily.orderservice.model.Cart;
import com.springboot.yapily.orderservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateCartValidator {

    @Autowired
    private ProductService productService;

    public void validateCartForUpdate(Cart cart, List<CartItemRequest> cartItemRequests) {
        validateCartCheckout(cart);
        validateCartItems(cartItemRequests);
    }

    private void validateCartCheckout(Cart cart) {
        if (cart.getCheckedOut()) throw new CartCheckedOutException(cart.getId());
    }

    private void validateCartItems(List<CartItemRequest> cartItemRequests) {
        validateCartItemQuantities(cartItemRequests);
        validateCartItemAvailability(cartItemRequests);
    }

    private void validateCartItemQuantities(List<CartItemRequest> cartItemRequests) {
        for (CartItemRequest cartItemRequest : cartItemRequests) {
            if (cartItemRequest.getQuantity() <= 0) {
                throw new InvalidCartItemQuantityException();
            }
        }
    }

    private void validateCartItemAvailability(List<CartItemRequest> cartItemRequests) {
        productService.checkCartItemsAvailability(cartItemRequests);
    }
}
