package com.springboot.yapily.orderservice.service;

import com.springboot.yapily.orderservice.dto.CheckoutCartResponse;
import com.springboot.yapily.orderservice.exception.CartNotFoundException;
import com.springboot.yapily.orderservice.model.Cart;
import com.springboot.yapily.orderservice.model.CartItem;
import com.springboot.yapily.orderservice.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class CartCheckoutServiceTest {

    @Autowired
    private CartCheckoutService cartCheckoutService;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private ProductService productService;

    @Test
    public void checkoutCart_withExistingCart_assertCheckoutOut_assertTotalCost() {
        CartItem cartItem1 = new CartItem(1L, 2L, 2);
        CartItem cartItem2 = new CartItem(2L, 3L, 2);

        Cart cart = new Cart(1L, Arrays.asList(cartItem1, cartItem2), false);

        HashMap<Long, Double> priceMap = new HashMap<>();
        priceMap.put(cartItem1.getProductId(), 1.0);
        priceMap.put(cartItem2.getProductId(), 2.0);

        when(productService.getCartItemsPricesById(any())).thenReturn(priceMap);
        when(cartRepository.findById(any())).thenReturn(Optional.of(cart));
        cart.setCheckedOut(true);
        when(cartRepository.save(any())).thenReturn(cart);

        CheckoutCartResponse result = cartCheckoutService.checkoutCart(1L);

        assertEquals(result.getCart().getCheckedOut(), true);
        assertEquals(result.getTotalCost(), cartItem1.getQuantity() * priceMap.get(cartItem1.getProductId()) + cartItem2.getQuantity() * priceMap.get(cartItem2.getProductId()));
    }

    @Test
    public void checkoutCart_withNonExistingCart_throwsCartNotFoundException() {
        when(cartRepository.findById(any())).thenReturn(Optional.empty());

        CartNotFoundException exception = assertThrows(CartNotFoundException.class, () -> cartCheckoutService.checkoutCart(1L));
        assertEquals(exception.getMessage(), "Cart not found with id " + 1L);
    }
}
