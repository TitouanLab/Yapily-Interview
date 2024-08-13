package com.springboot.yapily.orderservice.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ProductDTO {
    private long id;
    private String name;
    private Double price;
    private LocalDate addedAt;
    private List<String> labels;
}