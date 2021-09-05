package com.encom.springstore.controller;

import com.encom.springstore.exception.BadResourceException;
import com.encom.springstore.exception.ResourceAlreadyExistsException;
import com.encom.springstore.exception.ResourceNotFoundException;
import com.encom.springstore.model.entity.Customer;
import com.encom.springstore.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/customer")
public class CustomerController {
    private final CustomerService customerService;

    //  return all with pagination and filter logic
    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> findAll(
            @RequestParam(value="page", defaultValue="1") int pageNumber,
            @RequestParam(value="rowPerPage", defaultValue="5") int rowPerPageNumber) {
        try {
            List<Customer> customers = customerService.getAll(pageNumber, rowPerPageNumber);
            return ResponseEntity.ok(customers);
        }
        catch (ResourceNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception exception) {
            log.error("findAll: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    //  return specific customer by id
    @GetMapping(value = "/{customerID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> findCustomerById(@PathVariable int customerID) {
        try {
            Customer foundCustomer = customerService.getById(customerID);
            return ResponseEntity.ok(foundCustomer);
        }
        catch (ResourceNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception exception) {
            log.error("findCustomerById: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // return new created customer
    @PostMapping(value = "/customers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Customer> addCustomer(@RequestBody Customer customer) {
        try {
            Customer createdCustomer = customerService.create(customer);
            return new ResponseEntity<Customer>(createdCustomer, HttpStatus.CREATED);
        }
        catch (ResourceAlreadyExistsException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        catch (BadResourceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception exception) {
            log.error("addCustomer: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // change existed customer
    @PutMapping(value="/{customerID}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable int customerID) {
        try {
            customerService.update(customerID, customer);
            return ResponseEntity.ok().build();
        }
        catch (ResourceNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        catch (BadResourceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception exception) {
            log.error("updateCustomer: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // delete customer by id
    @DeleteMapping(value="/{customerID}")
    public ResponseEntity<Void> deleteCustomerById(@PathVariable int customerID) {
        try {
            customerService.delete(customerID);
            return ResponseEntity.ok().build();
        }
        catch (ResourceNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception exception) {
            log.error("deleteCustomerById: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
