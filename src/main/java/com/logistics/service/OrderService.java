package com.logistics.service;

import com.logistics.dto.OrderDto;
import com.logistics.entity.CustomerOrder;

import java.util.List;

public interface OrderService {
    List<OrderDto> findAll();
    OrderDto findById(Long id);
    OrderDto findByOrderNumber(String orderNumber);
    OrderDto save(OrderDto dto);
    OrderDto update(Long id, OrderDto dto);
    void delete(Long id);
    OrderDto updateStatus(Long id, CustomerOrder.OrderStatus status);
    List<OrderDto> findByCustomer(Long customerId);
    List<OrderDto> findByStatus(CustomerOrder.OrderStatus status);
    List<OrderDto> findRecent(int limit);
    String generateOrderNumber();
}
