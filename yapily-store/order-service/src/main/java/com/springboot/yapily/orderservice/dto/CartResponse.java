package com.springboot.yapily.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class CartResponse {

    @JsonProperty("cart_id")
    private Long cartId;
    @JsonProperty("products")
    private List<CartItemResponse> cartItemResponses;
    @JsonProperty("checked_out")
    private Boolean checkedOut;
}
