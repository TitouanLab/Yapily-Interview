package com.springboot.yapily.orderservice.repository;

import com.springboot.yapily.orderservice.model.Cart;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CartRepository extends CrudRepository<Cart, Long> {

    public List<Cart> findAll();

}
