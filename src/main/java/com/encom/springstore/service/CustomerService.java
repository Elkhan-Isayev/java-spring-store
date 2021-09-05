package com.encom.springstore.service;

import com.encom.springstore.exception.BadResourceException;
import com.encom.springstore.exception.ResourceAlreadyExistsException;
import com.encom.springstore.exception.ResourceNotFoundException;
import com.encom.springstore.model.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAll(int pageNumber, int rowPerPageNumber) throws ResourceNotFoundException;
    Customer getById(int id) throws ResourceNotFoundException;
    Customer create(Customer customer) throws ResourceAlreadyExistsException, BadResourceException;
    void update(int id, Customer customer) throws BadResourceException, ResourceNotFoundException;
    void delete(int id) throws ResourceNotFoundException;
}
