package com.encom.springstore.repository;

import com.encom.springstore.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    // custom methods for specific queries
}
