package com.encom.springstore.service;

import com.encom.springstore.exception.BadResourceException;
import com.encom.springstore.exception.ResourceAlreadyExistsException;
import com.encom.springstore.exception.ResourceNotFoundException;
import com.encom.springstore.model.entity.Order;

import java.util.List;

public interface OrderService {
    List<Order> getAll(int pageNumber, int rowPerPageNumber) throws ResourceNotFoundException;
    Order getById(int id) throws ResourceNotFoundException;
    Order create(Order order) throws ResourceAlreadyExistsException, BadResourceException;
    void update(int id, Order order) throws BadResourceException, ResourceNotFoundException;
    void delete(int id) throws ResourceNotFoundException;
}
