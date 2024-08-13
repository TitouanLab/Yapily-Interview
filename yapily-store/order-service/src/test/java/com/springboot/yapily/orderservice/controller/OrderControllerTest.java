package com.springboot.yapily.orderservice.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.yapily.orderservice.dto.CartItemRequest;
import com.springboot.yapily.orderservice.dto.CartItemResponse;
import com.springboot.yapily.orderservice.dto.CartResponse;
import com.springboot.yapily.orderservice.dto.CheckoutCartResponse;
import com.springboot.yapily.orderservice.service.CartCheckoutService;
import com.springboot.yapily.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private CartCheckoutService cartCheckoutService;

    @Test
    public void getCarts_with2Carts_assertPayloadStructure() throws Exception {
        CartItemResponse cartItemResponse1 = new CartItemResponse(1L, 1);
        List<CartItemResponse> cartItemResponses1 = List.of(cartItemResponse1);
        CartItemResponse cartItemResponse2 = new CartItemResponse(2L, 2);
        CartItemResponse cartItemResponse3 = new CartItemResponse(3L, 3);
        List<CartItemResponse> cartItemResponses2 = Arrays.asList(cartItemResponse2, cartItemResponse3);

        CartResponse cartResponse1 = new CartResponse(1L, cartItemResponses1, true);
        CartResponse cartResponse2 = new CartResponse(2L, cartItemResponses2, false);
        List<CartResponse> cartResponses = Arrays.asList(cartResponse1, cartResponse2);

        when(orderService.getCarts()).thenReturn(cartResponses);
        MvcResult result = this.mockMvc.perform(get("/api/carts"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        List<CartResponse> actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(actual.get(0), cartResponse1);
        assertEquals(actual.get(0).getCartItemResponses().size(), 1);
        assertEquals(actual.get(1), cartResponse2);
        assertEquals(actual.get(1).getCartItemResponses().size(), 2);
    }

    @Test
    public void createCart_assertPayloadStructure() throws Exception {
        CartItemResponse cartItemResponse = new CartItemResponse(1L, 1);
        CartResponse cartResponse = new CartResponse(1L, List.of(cartItemResponse), true);

        when(orderService.createCart()).thenReturn(cartResponse);
        MvcResult result = this.mockMvc.perform(post("/api/carts"))
                .andDo(print()).andExpect(status().isCreated()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        CartResponse actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertEquals(actual, cartResponse);
    }

    @Test
    public void updateCart_assertPayloadStructure() throws Exception {
        CartItemRequest cartItemRequest1 = new CartItemRequest(2L, 1);
        CartItemRequest cartItemRequest2 = new CartItemRequest(3L, 2);
        List<CartItemRequest> cartItemRequests = Arrays.asList(cartItemRequest1, cartItemRequest2);

        CartItemResponse cartItemResponse1 = new CartItemResponse(2L, 1);
        CartItemResponse cartItemResponse2 = new CartItemResponse(3L, 2);
        CartResponse cartResponse = new CartResponse(1L, Arrays.asList(cartItemResponse1, cartItemResponse2), true);

        when(orderService.updateCart(any(Long.class), anyList())).thenReturn(cartResponse);
        MvcResult result = this.mockMvc.perform(put("/api/carts/" + 1)
                        .content(toJsonString(cartItemRequests))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        CartResponse actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        assertEquals(actual, cartResponse);
        assertEquals(actual.getCartItemResponses().size(), 2);
    }

    @Test
    public void checkoutCart_assertPayloadStructure() throws Exception {
        CartResponse cartResponse = new CartResponse(1L, List.of(new CartItemResponse(2L, 2)), true);
        CheckoutCartResponse checkoutCartResponse = new CheckoutCartResponse(cartResponse, 20.00);

        when(cartCheckoutService.checkoutCart(any(Long.class))).thenReturn(checkoutCartResponse);
        MvcResult result = this.mockMvc.perform(post("/api/carts/" + 1 + "/checkout"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        ObjectMapper mapper = new ObjectMapper();
        CheckoutCartResponse actual = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});

        assertEquals(actual, checkoutCartResponse);
        assertEquals(actual.getCart(), cartResponse);
    }

    private String toJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
