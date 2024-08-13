package com.springboot.yapily.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<CartItem> cartItems;
    private Boolean checkedOut;

    public Cart() {
        cartItems = new ArrayList<>();
        checkedOut = false;
    }
}
