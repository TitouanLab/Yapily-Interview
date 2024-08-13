package com.springboot.yapily.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.yapily.productservice.dto.ProductRequest;
import com.springboot.yapily.productservice.dto.ProductResponse;
import com.springboot.yapily.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    private final LocalDate date1 = LocalDate.of(2024, 8, 12);
    private final LocalDate date2 = LocalDate.of(2024, 8, 11);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void getProductsAssertPayloadStructure() throws Exception {
        ProductResponse expectedResponse1 = new ProductResponse(1, "Cheese", 10.99, date1, Arrays.asList("food", "limited"));
        ProductResponse expectedResponse2 = new ProductResponse(2, "Coca Cola", 1.99, date2, Arrays.asList("drink"));
        List<ProductResponse> expectedResponses = Arrays.asList(expectedResponse1, expectedResponse2);

        when(productService.getProducts()).thenReturn(expectedResponses);
        this.mockMvc.perform(get("/api/products"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[*].product_id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Cheese", "Coca Cola")))
                .andExpect(jsonPath("$[*].price", containsInAnyOrder(10.99, 1.99)))
                .andExpect(jsonPath("$[*].labels", containsInAnyOrder(Arrays.asList("food", "limited"), Arrays.asList("drink"))))
                .andExpect(jsonPath("$[*].added_at", containsInAnyOrder(date1.toString(), date2.toString())));
    }

    @Test
    public void getProductsByIdAssertPayloadStructure() throws Exception {
        ProductResponse expectedResponse = new ProductResponse(1, "Cheese", 10.99, date1, Arrays.asList("food", "limited"));

        when(productService.getProductById(1L)).thenReturn(expectedResponse);
        this.mockMvc.perform(get("/api/products/" + 1))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.product_id").value(1))
                .andExpect(jsonPath("$.name").value("Cheese"))
                .andExpect(jsonPath("$.price").value(10.99))
                .andExpect(jsonPath("$.added_at").value(date1.toString()));
    }

    @Test
    public void createProductAssertPayloadStructure() throws Exception {
        ProductRequest productRequest = new ProductRequest("Cheese", 10.99, Arrays.asList("food", "limited"));
        when(productService.createProduct(any(ProductRequest.class)))
                .thenReturn(new ProductResponse(1, "Cheese", 10.99, date1, Arrays.asList("food", "limited")));
        this.mockMvc.perform(post("/api/products")
                        .content(toJsonString(productRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.product_id").value(1))
                .andExpect(jsonPath("$.name").value("Cheese"))
                .andExpect(jsonPath("$.price").value(10.99))
                .andExpect(jsonPath("$.added_at").value(date1.toString()));
    }

    @Test
    public void deleteProductByIdAssertErrorCode() throws Exception {
        this.mockMvc.perform(delete("/api/products/" + 1))
                .andDo(print()).andExpect(status().isNoContent());
    }

    private String toJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
