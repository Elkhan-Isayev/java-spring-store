package com.encom.springstore.repository;

import com.encom.springstore.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // custom methods for specific queries
}
