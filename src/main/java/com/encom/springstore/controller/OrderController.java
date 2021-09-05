package com.encom.springstore.controller;


import com.encom.springstore.exception.BadResourceException;
import com.encom.springstore.exception.ResourceAlreadyExistsException;
import com.encom.springstore.exception.ResourceNotFoundException;
import com.encom.springstore.model.entity.Order;
import com.encom.springstore.service.OrderService;
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
@RequestMapping(path = "/order")
public class OrderController {
    private final OrderService orderService;

    //  return all with pagination and filter logic
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> findAll(
            @RequestParam(value="page", defaultValue="1") int pageNumber,
            @RequestParam(value="rowPerPage", defaultValue="5") int rowPerPageNumber) {
        try {
            List<Order> orders = orderService.getAll(pageNumber, rowPerPageNumber);
            return ResponseEntity.ok(orders);
        }
        catch (ResourceNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception exception) {
            log.error("findAll: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    //  return specific order by id
    @GetMapping(value = "/{orderID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> findOrderById(@PathVariable int orderID) {
        try {
            Order foundOrder = orderService.getById(orderID);
            return ResponseEntity.ok(foundOrder);
        }
        catch (ResourceNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        catch (Exception exception) {
            log.error("findOrderById: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // return new created order
    @PostMapping(value = "/orders", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        try {
            Order createdOrder = orderService.create(order);
            return new ResponseEntity<Order>(createdOrder, HttpStatus.CREATED);
        }
        catch (ResourceAlreadyExistsException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        catch (BadResourceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception exception) {
            log.error("addOrder: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // change existed order
    @PutMapping(value="/{orderID}")
    public ResponseEntity<Order> updateOrder(@RequestBody Order order, @PathVariable int orderID) {
        try {
            orderService.update(orderID, order);
            return ResponseEntity.ok().build();
        }
        catch (ResourceNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        catch (BadResourceException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        catch (Exception exception) {
            log.error("updateOrder: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // delete order by id
    @DeleteMapping(value="/{orderID}")
    public ResponseEntity<Void> deleteOrderById(@PathVariable int orderID) {
        try {
            orderService.delete(orderID);
            return ResponseEntity.ok().build();
        }
        catch (ResourceNotFoundException exception) {
            return ResponseEntity.notFound().build();
        }
        catch (Exception exception) {
            log.error("deleteOrderById: {}", exception.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
