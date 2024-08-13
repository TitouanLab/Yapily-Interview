package com.springboot.yapily.orderservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class CheckoutCartResponse {
    @JsonProperty("cart")
    private CartResponse cart;
    @JsonProperty("total_cost")
    private Double totalCost;
}
