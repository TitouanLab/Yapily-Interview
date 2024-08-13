package com.springboot.yapily.orderservice.validator;

import com.springboot.yapily.orderservice.dto.CartItemRequest;
import com.springboot.yapily.orderservice.exception.CartCheckedOutException;
import com.springboot.yapily.orderservice.exception.InvalidCartItemQuantityException;
import com.springboot.yapily.orderservice.model.Cart;
import com.springboot.yapily.orderservice.service.ProductService;
import com.springboot.yapily.orderservice.valdator.UpdateCartValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UpdateCartValidatorTest {

    @MockBean
    private ProductService productService;

    @Autowired
    private UpdateCartValidator updateCartValidator;

    @Test
    public void validateCartForUpdate_withValidCartAndItems_throwsNoException() {
        Cart cart = new Cart(1L, new ArrayList<>(), false);
        List<CartItemRequest> cartItemRequests = List.of(new CartItemRequest(1L, 1));

        updateCartValidator.validateCartForUpdate(cart, cartItemRequests);
    }

    @Test
    public void validateCartForUpdate_withCartCheckedOut_throwsCartCheckedOutException() {
        Cart cart = new Cart(1L, new ArrayList<>(), true);

        CartCheckedOutException exception = assertThrows(CartCheckedOutException.class, () -> updateCartValidator.validateCartForUpdate(cart, new ArrayList<>()));
        assertEquals(exception.getMessage(), "Cart with id " + 1 + " is already checked out.");
    }

    @Test
    public void validateCartForUpdate_withItemNegativeQuantity_throwsInvalidItemQuantityException() {
        Cart cart = new Cart(1L, new ArrayList<>(), false);
        List<CartItemRequest> cartItemRequests = List.of(new CartItemRequest(1L, -1));

        assertThrows(InvalidCartItemQuantityException.class, () -> updateCartValidator.validateCartForUpdate(cart, cartItemRequests));
    }
}
