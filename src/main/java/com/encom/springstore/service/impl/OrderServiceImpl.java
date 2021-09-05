package com.encom.springstore.service.impl;

import com.encom.springstore.exception.BadResourceException;
import com.encom.springstore.exception.ResourceAlreadyExistsException;
import com.encom.springstore.exception.ResourceNotFoundException;
import com.encom.springstore.model.entity.Order;
import com.encom.springstore.repository.OrderRepository;
import com.encom.springstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public List<Order> getAll(int pageNumber, int rowPerPageNumber) throws ResourceNotFoundException {
        List<Order> orders = new ArrayList<>();
        orderRepository.findAll(PageRequest.of(pageNumber - 1, rowPerPageNumber)).forEach(orders::add);
        if(orders.isEmpty()) {
            throw new ResourceNotFoundException("Can not find any orders");
        }
        return orders;
    }

    @Override
    public Order getById(int id) throws ResourceNotFoundException {
        Order order = orderRepository.findById(id).orElse(null);
        if(order == null) {
            throw new ResourceNotFoundException("Can not find order with id: " + id);
        }
        else {
            return order;
        }
    }

    @Override
    public Order create(Order order) throws ResourceAlreadyExistsException, BadResourceException {
        if(!StringUtils.isEmpty(order.getCustomerId()) && !StringUtils.isEmpty(order.getProductId())) {
            if(orderRepository.existsById(order.getId())) {
                throw new ResourceAlreadyExistsException("Order with id: " + order.getId() + " already exists");
            }
            return orderRepository.save(order);
        }
        else {
            BadResourceException badResourceException = new BadResourceException("Failed to save order");
            badResourceException.addErrorMessage("Order is null or empty");
            throw badResourceException;
        }
    }

    @Override
    public void update(int id, Order order) throws BadResourceException, ResourceNotFoundException {
        if(!StringUtils.isEmpty(order.getCustomerId()) && !StringUtils.isEmpty(order.getProductId())) {
            if(!orderRepository.existsById(id)) {
                throw new ResourceNotFoundException("Can not find order with id: " + order.getId());
            }
            Order foundedOrder = orderRepository.getById(id);
            foundedOrder.setCustomerId(order.getCustomerId());
            foundedOrder.setProductId(order.getProductId());
            orderRepository.save(foundedOrder);
        }
        else {
            BadResourceException badResourceException = new BadResourceException("Failed to save order");
            badResourceException.addErrorMessage("Order is null or empty");
            throw badResourceException;
        }
    }

    @Override
    public void delete(int id) throws ResourceNotFoundException {
        if(!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Can not find order with id: " + id);
        }
        orderRepository.deleteById(id);
    }
}
