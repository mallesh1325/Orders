package org.orders.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.orders.dto.OrderDto;
import org.orders.entity.Orders;
import org.orders.exception.OrderNotFoundException;
import org.orders.exception.OrderProcessingException;
import org.orders.repository.OrdersRepository;
import org.orders.utills.OrderUtills;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderService(OrdersRepository ordersRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.ordersRepository = ordersRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderDto getOrders(String id) {

        if (id == null || id.isEmpty()) {
            throw new OrderNotFoundException("Order id not be null or emoty " + id);
        }


        Orders order = ordersRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order with ID " + id + " not found"));
        return OrderUtills.entityToDto(order);
    }

    public List<OrderDto> allOrders() {

        List<OrderDto> allOrders = StreamSupport.stream(ordersRepository.findAll().spliterator(), false)
                .map(OrderUtills::entityToDto).collect(Collectors.toList());

        if (allOrders.isEmpty()) {
            throw new OrderNotFoundException("No orders found");
        }

        return allOrders;

    }

    public List<OrderDto> addOrder(List<OrderDto> orderDto) throws OrderProcessingException {

        try {
            log.info("Creating order: {}", orderDto);

            List<Orders> addOrder = orderDto.stream().map(OrderUtills::dtoToEntity).collect(Collectors.toList());

            List<Orders> ordersSave = StreamSupport.stream(ordersRepository.saveAll(addOrder).spliterator(), false)
                    .toList();
            ordersSave.forEach(order -> {

                kafkaTemplate.send("order_topic", order.getId());

                log.info("Sending Kafka Notification for order creation with ID: {} ", order.getId());
            });

            return ordersSave.stream().map(OrderUtills::entityToDto) // Convert Entity to DTO
                    .collect(Collectors.toList());

        } catch (Exception ex) {

            log.error("Error occurred while creating the order: {}", ex.getMessage(), ex);

            throw new OrderProcessingException("Failed to create order", ex);
        }
    }

    public OrderDto updateOrderById(OrderDto orderDto, String id) throws OrderProcessingException {
        try {
            Orders exstingOrder = ordersRepository.findById(id)
                    .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));

            if (orderDto.getId() != null && !orderDto.getId().equals(exstingOrder.getId())) {
                throw new OrderNotFoundException("The ID from the provider does not match the DB ID.");
            }

            if (orderDto.getId() != null)
                exstingOrder.setId(orderDto.getId());
            if (orderDto.getName() != null)
                exstingOrder.setName(orderDto.getName());
            if (orderDto.getPrice() != null)
                exstingOrder.setPrice(orderDto.getPrice());
            if (orderDto.getQantity() != null)
                exstingOrder.setQantity(orderDto.getQantity());

            Orders updatedOrder = ordersRepository.save(exstingOrder);

            kafkaTemplate.send("order_topic", updatedOrder.getId());
            log.info("Sent Kafka Notification for order update: {}", updatedOrder.getId());

            return OrderUtills.entityToDto(updatedOrder);

        } catch (OrderNotFoundException ex) {
            throw ex;

        } catch (Exception ex) {
            log.error("Error occurred while updating order: {}", ex.getMessage(), ex);
            throw new OrderProcessingException("Failed to update order", ex);
        }

    }
}
