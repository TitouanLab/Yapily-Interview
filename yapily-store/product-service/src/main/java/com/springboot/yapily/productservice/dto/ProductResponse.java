package com.springboot.yapily.productservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductResponse {
    @JsonProperty("product_id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("price")
    private Double price;
    @JsonProperty("added_at")
    private LocalDate addedAt;
    @JsonProperty("labels")
    private List<String> labels;
}
