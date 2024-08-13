package com.springboot.yapily.orderservice.service;

import com.springboot.yapily.orderservice.dto.CartItemRequest;
import com.springboot.yapily.orderservice.dto.CartResponse;
import com.springboot.yapily.orderservice.exception.CartCheckedOutException;
import com.springboot.yapily.orderservice.exception.CartNotFoundException;
import com.springboot.yapily.orderservice.model.Cart;
import com.springboot.yapily.orderservice.model.CartItem;
import com.springboot.yapily.orderservice.repository.CartRepository;
import com.springboot.yapily.orderservice.valdator.UpdateCartValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @MockBean
    private CartRepository cartRepository;

    @MockBean
    private UpdateCartValidator updateCartValidator;

    @Test
    public void getCarts(){
        CartItem cartItem1 = new CartItem();
        CartItem cartItem2 = new CartItem();
        List<CartItem> cartItems1 = Arrays.asList(cartItem1, cartItem2);

        Cart cart1 = new Cart(1L, cartItems1, true);
        Cart cart2 = new Cart(2L, new ArrayList<>(), false);
        List<Cart> carts = Arrays.asList(cart1, cart2);

        when(cartRepository.findAll()).thenReturn(carts);
        List<CartResponse> result = orderService.getCarts();

        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getCartId(), 1L);
        assertEquals(result.get(0).getCartItemResponses().size(), 2);
        assertEquals(result.get(0).getCheckedOut(), true);
        assertEquals(result.get(1).getCartId(), 2L);
        assertEquals(result.get(1).getCartItemResponses().size(), 0);
        assertEquals(result.get(1).getCheckedOut(), false);
    }

    @Test
    public void getCarts_withNoRecordInDb_assertEmptyResponse(){
        when(cartRepository.findAll()).thenReturn(new ArrayList<>());
        List<CartResponse> result = orderService.getCarts();

        assertEquals(result.size(), 0);
    }

    @Test
    public void createCart(){
        Cart cart = new Cart(1L, new ArrayList<>(), false);
        when(cartRepository.save(any())).thenReturn(cart);
        CartResponse result = orderService.createCart();

        assertEquals(result.getCartId(), 1L);
        assertEquals(result.getCartItemResponses().size(), 0);
        assertEquals(result.getCheckedOut(), false);
    }

    @Test
    public void updateCart_withExistingCart_assertCartItemsPopulated(){
        CartItemRequest cartItemRequest1 = new CartItemRequest(2L, 2);
        CartItemRequest cartItemRequest2 = new CartItemRequest(3L, 3);
        List<CartItemRequest> cartItemRequests = Arrays.asList(cartItemRequest1, cartItemRequest2);

        CartItem cartItem1 = new CartItem(4L, 2L, 2);
        CartItem cartItem2 = new CartItem(5L, 3L, 3);
        List<CartItem> cartItems = Arrays.asList(cartItem1, cartItem2);

        Cart existingCart = new Cart(1L, new ArrayList<>(), false);
        Cart populatedCart = new Cart(1L, cartItems, false);

        when(cartRepository.findById(1L)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any())).thenReturn(populatedCart);

        CartResponse result = orderService.updateCart(1L, cartItemRequests);

        assertEquals(result.getCartId(), 1L);
        assertEquals(result.getCartItemResponses().size(), 2);
        assertEquals(result.getCheckedOut(), false);
    }

    @Test
    public void updateCart_withNonExistingCart_throwsCartNotFoundException(){
        when(cartRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CartNotFoundException.class, () -> orderService.updateCart(1L, new ArrayList<>()));
    }

}
