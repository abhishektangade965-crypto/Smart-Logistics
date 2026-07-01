package com.logistics.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SHIPMENT_QUEUE = "shipment.status.queue";
    public static final String SHIPMENT_EXCHANGE = "shipment.exchange";
    public static final String SHIPMENT_ROUTING_KEY = "shipment.status.changed";

    @Bean
    public Queue shipmentQueue() {
        return new Queue(SHIPMENT_QUEUE, true);
    }

    @Bean
    public TopicExchange shipmentExchange() {
        return new TopicExchange(SHIPMENT_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue shipmentQueue, TopicExchange shipmentExchange) {
        return BindingBuilder.bind(shipmentQueue).to(shipmentExchange).with(SHIPMENT_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
