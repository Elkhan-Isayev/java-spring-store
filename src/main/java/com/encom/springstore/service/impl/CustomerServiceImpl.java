package com.encom.springstore.service.impl;

import com.encom.springstore.exception.BadResourceException;
import com.encom.springstore.exception.ResourceAlreadyExistsException;
import com.encom.springstore.exception.ResourceNotFoundException;
import com.encom.springstore.model.entity.Customer;
import com.encom.springstore.repository.CustomerRepository;
import com.encom.springstore.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> getAll(int pageNumber, int rowPerPageNumber) throws ResourceNotFoundException {
        List<Customer> customers = new ArrayList<>();
        customerRepository.findAll(PageRequest.of(pageNumber - 1, rowPerPageNumber)).forEach(customers::add);
        if(customers.isEmpty()) {
            throw new ResourceNotFoundException("Can not find any customers");
        }
        return customers;
    }

    @Override
    public Customer getById(int id) throws ResourceNotFoundException {
        Customer customer = customerRepository.findById(id).orElse(null);
        if(customer == null) {
            throw new ResourceNotFoundException("Can not find customer with id: " + id);
        }
        return customer;
    }

    @Override
    public Customer create(Customer customer) throws ResourceAlreadyExistsException, BadResourceException {
        if(!StringUtils.isEmpty(customer.getFullName())
        && !StringUtils.isEmpty(customer.getEmail())
        && !StringUtils.isEmpty(customer.getPhone())) {
            if(customerRepository.existsById(customer.getId())) {
                throw new ResourceAlreadyExistsException("Customer with id: " + customer.getId() + " already exists");
            }
            return customerRepository.save(customer);
        }
        else {
            BadResourceException badResourceException = new BadResourceException("Failed to save customer");
            badResourceException.addErrorMessage("Customer is null or empty");
            throw badResourceException;
        }
    }

    @Override
    public void update(int id, Customer customer) throws BadResourceException, ResourceNotFoundException {
        if(!StringUtils.isEmpty(customer.getFullName())
        && !StringUtils.isEmpty(customer.getEmail())
        && !StringUtils.isEmpty(customer.getPhone())) {
            if(!customerRepository.existsById(id)) {
                throw new ResourceNotFoundException("Can not find customer with id: " + customer.getId());
            }
            Customer foundedCustomer = customerRepository.getById(id);
            foundedCustomer.setFullName(customer.getFullName());
            foundedCustomer.setEmail(customer.getEmail());
            foundedCustomer.setPhone(customer.getPhone());
            customerRepository.save(foundedCustomer);
        }
        else {
            BadResourceException badResourceException = new BadResourceException("Failed to save customer");
            badResourceException.addErrorMessage("Customer is null or empty");
            throw badResourceException;
        }
    }

    @Override
    public void delete(int id) throws ResourceNotFoundException {
        if(!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Can not find customer with id: " + id);
        }
        customerRepository.deleteById(id);
    }
}
