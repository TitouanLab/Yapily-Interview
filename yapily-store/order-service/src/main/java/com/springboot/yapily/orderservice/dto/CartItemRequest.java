package com.springboot.yapily.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartItemRequest {
    @JsonProperty("product_id")
    private Long productId;
    @JsonProperty("quantity")
    private int quantity;
}
