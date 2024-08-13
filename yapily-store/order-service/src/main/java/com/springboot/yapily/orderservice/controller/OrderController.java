package com.springboot.yapily.orderservice.controller;

import com.springboot.yapily.orderservice.dto.CartItemRequest;
import com.springboot.yapily.orderservice.dto.CartResponse;
import com.springboot.yapily.orderservice.dto.CheckoutCartResponse;
import com.springboot.yapily.orderservice.service.CartCheckoutService;
import com.springboot.yapily.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartCheckoutService cartCheckoutService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CartResponse> getCarts() {
        return orderService.getCarts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartResponse createCart() {
        return orderService.createCart();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CartResponse updateCart(@PathVariable("id") Long cartId,
                                   @RequestBody List<CartItemRequest> cartItemRequests) {
        return orderService.updateCart(cartId, cartItemRequests);
    }

    @PostMapping("/{id}/checkout")
    @ResponseStatus(HttpStatus.OK)
    public CheckoutCartResponse checkoutCart(@PathVariable("id") Long cartId) {
        return cartCheckoutService.checkoutCart(cartId);
    }


}
