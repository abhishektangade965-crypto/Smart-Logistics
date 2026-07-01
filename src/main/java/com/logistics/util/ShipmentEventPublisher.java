package com.logistics.util;

import com.logistics.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipmentEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publishStatusChange(String trackingNumber, String status, String destination, String driverName) {
        ShipmentStatusEvent event = ShipmentStatusEvent.builder()
                .trackingNumber(trackingNumber)
                .status(status)
                .destination(destination)
                .driverName(driverName)
                .timestamp(LocalDateTime.now().toString())
                .build();
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.SHIPMENT_EXCHANGE, RabbitMQConfig.SHIPMENT_ROUTING_KEY, event);
            log.info("Successfully published shipment status change event to RabbitMQ for: {}", trackingNumber);
        } catch (Exception e) {
            log.error("Failed to publish event to RabbitMQ for {}. Error: {}", trackingNumber, e.getMessage());
        }
    }
}
