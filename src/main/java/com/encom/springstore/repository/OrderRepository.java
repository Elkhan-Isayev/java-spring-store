package com.encom.springstore.repository;

import com.encom.springstore.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    // custom methods for specific queries
}
