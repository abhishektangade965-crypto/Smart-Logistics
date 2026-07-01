package com.logistics.dto;

import com.logistics.entity.CustomerOrder;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderDto {
    private Long id;
    private String orderNumber;
    private Long customerId;
    private String customerName;
    private Long warehouseId;
    private String warehouseName;
    private CustomerOrder.OrderStatus status;
    private CustomerOrder.PaymentStatus paymentStatus;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal shippingCost;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String shippingMethod;
    private LocalDate expectedDeliveryDate;
    private LocalDate actualDeliveryDate;
    private String notes;
    private String createdBy;
    private LocalDateTime createdAt;
    private List<OrderItemDto> items;
}
