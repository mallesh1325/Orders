package org.orders.controllers;

import java.util.List;

import org.orders.dto.OrderDto;
import org.orders.exception.OrderNotFoundException;
import org.orders.exception.OrderProcessingException;
import org.orders.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders/")
public class OrdersControllers {

	final private OrderService orderService;

	public OrdersControllers(OrderService orderService){
		this.orderService = orderService;
	}

	private static final Logger log = LoggerFactory.getLogger(OrdersControllers.class);

	@GetMapping("getOrderById/{id}")
	public ResponseEntity<OrderDto> getOrder(@PathVariable String id) {
		if (id == null || id.isEmpty()) {
			throw new OrderNotFoundException("Order ID must not be null or empty: " + id);
		}
		OrderDto orderDto = orderService.getOrders(id);
		return ResponseEntity.ok(orderDto);
	}

	@GetMapping("getOrdersList")
	public ResponseEntity<List<OrderDto>> getAllOrders() {
		List<OrderDto> orderDto = orderService.allOrders();
		if (orderDto.isEmpty()) {
			throw new OrderNotFoundException("No orders available.");
		}
		return ResponseEntity.ok(orderDto);
	}

	@PostMapping("createOrder")
	public ResponseEntity<List<OrderDto>> addOrders(@RequestBody List<OrderDto> orderDto)
			throws OrderProcessingException {
		List<OrderDto> orderDt = orderService.addOrder(orderDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(orderDt);
	}

	@PutMapping("updateOrder/{id}")
	public ResponseEntity<OrderDto> updateOrder(@RequestBody OrderDto orderDto, @PathVariable String id)
			throws OrderProcessingException {
		if (id == null || id.isEmpty() || orderDto == null) {
			throw new OrderNotFoundException("Order ID must not be null or empty: " + id);
		}
		OrderDto orderDt = orderService.updateOrderById(orderDto, id);
		if (orderDt == null) {
			throw new OrderNotFoundException("Order Not found with ID: " + id);
		}
		return ResponseEntity.ok(orderDt);
	}
}
