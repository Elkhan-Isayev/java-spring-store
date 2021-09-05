package com.encom.springstore.service;

import com.encom.springstore.exception.BadResourceException;
import com.encom.springstore.exception.ResourceAlreadyExistsException;
import com.encom.springstore.exception.ResourceNotFoundException;
import com.encom.springstore.model.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAll(int pageNumber, int rowPerPageNumber) throws ResourceNotFoundException;
    Product getById(int id) throws ResourceNotFoundException;
    Product create(Product product) throws ResourceAlreadyExistsException, BadResourceException;
    void update(int id, Product product) throws BadResourceException, ResourceNotFoundException;
    void delete(int id) throws ResourceNotFoundException;
}
