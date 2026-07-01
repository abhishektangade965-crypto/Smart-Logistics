package com.logistics.serviceimpl;

import com.logistics.dto.OrderDto;
import com.logistics.dto.OrderItemDto;
import com.logistics.entity.CustomerOrder;
import com.logistics.entity.OrderItem;
import com.logistics.exception.ResourceNotFoundException;
import com.logistics.repository.CustomerOrderRepository;
import com.logistics.repository.CustomerRepository;
import com.logistics.repository.ProductRepository;
import com.logistics.repository.WarehouseRepository;
import com.logistics.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final CustomerOrderRepository orderRepo;
    private final CustomerRepository customerRepo;
    private final WarehouseRepository warehouseRepo;
    private final ProductRepository productRepo;

    @Override @Transactional(readOnly = true)
    public List<OrderDto> findAll() {
        return orderRepo.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public OrderDto findById(Long id) {
        return toDto(orderRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Order", "id", id)));
    }

    @Override @Transactional(readOnly = true)
    public OrderDto findByOrderNumber(String orderNumber) {
        return toDto(orderRepo.findByOrderNumber(orderNumber).orElseThrow(() ->
                new ResourceNotFoundException("Order", "orderNumber", orderNumber)));
    }

    @Override
    @CacheEvict(value = "dashboardStats", allEntries = true)
    public OrderDto save(OrderDto dto) {
        CustomerOrder order = new CustomerOrder();
        order.setOrderNumber(generateOrderNumber());
        copyFields(dto, order);
        CustomerOrder saved = orderRepo.save(order);
        return toDto(saved);
    }

    @Override
    @CacheEvict(value = "dashboardStats", allEntries = true)
    public OrderDto update(Long id, OrderDto dto) {
        CustomerOrder order = orderRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Order", "id", id));
        copyFields(dto, order);
        return toDto(orderRepo.save(order));
    }

    @Override
    @CacheEvict(value = "dashboardStats", allEntries = true)
    public void delete(Long id) {
        if (!orderRepo.existsById(id)) throw new ResourceNotFoundException("Order", "id", id);
        orderRepo.deleteById(id);
    }

    @Override
    @CacheEvict(value = "dashboardStats", allEntries = true)
    public OrderDto updateStatus(Long id, CustomerOrder.OrderStatus status) {
        CustomerOrder order = orderRepo.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Order", "id", id));
        order.setStatus(status);
        return toDto(orderRepo.save(order));
    }

    @Override @Transactional(readOnly = true)
    public List<OrderDto> findByCustomer(Long customerId) {
        return orderRepo.findByCustomerId(customerId).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<OrderDto> findByStatus(CustomerOrder.OrderStatus status) {
        return orderRepo.findByStatus(status).stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override @Transactional(readOnly = true)
    public List<OrderDto> findRecent(int limit) {
        return orderRepo.findTop10ByOrderByCreatedAtDesc().stream()
                .limit(limit).map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public String generateOrderNumber() {
        String prefix = "ORD-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = orderRepo.count() + 1;
        return prefix + "-" + String.format("%04d", count);
    }

    private void copyFields(OrderDto dto, CustomerOrder order) {
        if (dto.getCustomerId() != null)
            customerRepo.findById(dto.getCustomerId()).ifPresent(order::setCustomer);
        if (dto.getWarehouseId() != null)
            warehouseRepo.findById(dto.getWarehouseId()).ifPresent(order::setWarehouse);
        order.setStatus(dto.getStatus() != null ? dto.getStatus() : CustomerOrder.OrderStatus.PENDING);
        order.setPaymentStatus(dto.getPaymentStatus() != null ? dto.getPaymentStatus() : CustomerOrder.PaymentStatus.PENDING);
        order.setShippingAddress(dto.getShippingAddress());
        order.setShippingMethod(dto.getShippingMethod());
        order.setExpectedDeliveryDate(dto.getExpectedDeliveryDate());
        order.setNotes(dto.getNotes());
        order.setShippingCost(dto.getShippingCost() != null ? dto.getShippingCost() : BigDecimal.ZERO);
        order.setDiscountAmount(dto.getDiscountAmount() != null ? dto.getDiscountAmount() : BigDecimal.ZERO);
        order.setTaxAmount(dto.getTaxAmount() != null ? dto.getTaxAmount() : BigDecimal.ZERO);

        if (dto.getItems() != null) {
            List<OrderItem> items = new ArrayList<>();
            BigDecimal subtotal = BigDecimal.ZERO;
            for (OrderItemDto itemDto : dto.getItems()) {
                OrderItem item = new OrderItem();
                item.setOrder(order);
                if (itemDto.getProductId() != null)
                    productRepo.findById(itemDto.getProductId()).ifPresent(item::setProduct);
                item.setQuantity(itemDto.getQuantity());
                item.setUnitPrice(itemDto.getUnitPrice());
                item.setDiscount(itemDto.getDiscount() != null ? itemDto.getDiscount() : BigDecimal.ZERO);
                item.calculateLineTotal();
                subtotal = subtotal.add(item.getLineTotal() != null ? item.getLineTotal() : BigDecimal.ZERO);
                items.add(item);
            }
            order.setItems(items);
            order.setSubtotal(subtotal);
            BigDecimal total = subtotal
                    .add(order.getTaxAmount()).add(order.getShippingCost())
                    .subtract(order.getDiscountAmount());
            order.setTotalAmount(total);
        }
    }

    private OrderDto toDto(CustomerOrder o) {
        List<OrderItemDto> itemDtos = o.getItems() != null ?
                o.getItems().stream().map(i -> OrderItemDto.builder()
                        .id(i.getId())
                        .productId(i.getProduct() != null ? i.getProduct().getId() : null)
                        .productName(i.getProduct() != null ? i.getProduct().getName() : null)
                        .productSku(i.getProduct() != null ? i.getProduct().getSku() : null)
                        .quantity(i.getQuantity()).unitPrice(i.getUnitPrice())
                        .discount(i.getDiscount()).lineTotal(i.getLineTotal()).build())
                .collect(Collectors.toList()) : List.of();

        return OrderDto.builder()
                .id(o.getId()).orderNumber(o.getOrderNumber())
                .customerId(o.getCustomer() != null ? o.getCustomer().getId() : null)
                .customerName(o.getCustomer() != null ? o.getCustomer().getName() : null)
                .warehouseId(o.getWarehouse() != null ? o.getWarehouse().getId() : null)
                .warehouseName(o.getWarehouse() != null ? o.getWarehouse().getName() : null)
                .status(o.getStatus()).paymentStatus(o.getPaymentStatus())
                .subtotal(o.getSubtotal()).taxAmount(o.getTaxAmount())
                .shippingCost(o.getShippingCost()).discountAmount(o.getDiscountAmount())
                .totalAmount(o.getTotalAmount()).shippingAddress(o.getShippingAddress())
                .shippingMethod(o.getShippingMethod())
                .expectedDeliveryDate(o.getExpectedDeliveryDate())
                .actualDeliveryDate(o.getActualDeliveryDate())
                .notes(o.getNotes()).createdAt(o.getCreatedAt()).items(itemDtos).build();
    }
}
