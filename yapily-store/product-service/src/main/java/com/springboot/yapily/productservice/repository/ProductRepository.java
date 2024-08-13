package com.springboot.yapily.productservice.repository;

import com.springboot.yapily.productservice.model.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Long> {

        public List<Product> findAll();

        public Optional<Product> findById(Long id);

        public Optional<Product> findByName(String name);

        public void deleteById(Long id);

}
