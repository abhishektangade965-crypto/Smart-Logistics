package com.logistics.util;

import com.logistics.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ShipmentEventListener {

    @RabbitListener(queues = RabbitMQConfig.SHIPMENT_QUEUE)
    public void receiveShipmentStatusEvent(ShipmentStatusEvent event) {
        log.info("Received Shipment Status Change Event from RabbitMQ queue [{}]:", RabbitMQConfig.SHIPMENT_QUEUE);
        log.info(" - Tracking Number: {}", event.getTrackingNumber());
        log.info(" - New Status: {}", event.getStatus());
        log.info(" - Destination: {}", event.getDestination());
        log.info(" - Driver: {}", event.getDriverName());
        log.info(" - Event Timestamp: {}", event.getTimestamp());
    }
}
