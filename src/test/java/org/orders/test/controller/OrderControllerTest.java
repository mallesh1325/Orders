package org.orders.test.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.orders.controllers.OrdersControllers;
import org.orders.dto.OrderDto;
import org.orders.exception.OrderNotFoundException;
import org.orders.exception.OrderProcessingException;
import org.orders.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class OrderControllerTest {

	@Mock
	OrderService orderService;

	@InjectMocks
	private OrdersControllers orderController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testGetOrder_ValidId_ReturnsOk() {
		String orderId = "1";
		OrderDto orderDto = new OrderDto();
		orderDto.setId(orderId);
		orderDto.setName("TestOrder");
		orderDto.setQantity(3);
		orderDto.setPrice(100.0);

		when(orderService.getOrders("1")).thenReturn(orderDto);

		ResponseEntity<OrderDto> response = orderController.getOrder(orderId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(orderDto, response.getBody());

	}

	@Test
	public void testOrderNotFoundException() {
		String orderId = "1";
		when(orderService.getOrders(orderId)).thenThrow(new OrderNotFoundException("Order Not Found"));

		Exception exception = assertThrows(OrderNotFoundException.class, () -> {
			orderController.getOrder(orderId);
		});

		assertEquals("Order Not Found", exception.getMessage());
	}

	@Test
	public void testGetAllOrders() {
		OrderDto order1 = new OrderDto();
		order1.setId("1");
		order1.setName("Order 1");
		order1.setPrice(100.0);
		order1.setQantity(1);

		OrderDto order2 = new OrderDto();
		order2.setId("2");
		order2.setName("Order 2");
		order2.setPrice(150.0);
		order2.setQantity(2);

		List<OrderDto> orders = Arrays.asList(order1, order2);

		when(orderService.allOrders()).thenReturn(orders);

		ResponseEntity<List<OrderDto>> response = orderController.getAllOrders();

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(2, response.getBody().size());
	}

	@Test
	public void testCreateOrder() throws OrderProcessingException {

		OrderDto order = new OrderDto();
		order.setId("1");
		order.setName("Order 1");
		order.setPrice(100.0);
		order.setQantity(1);

		List<OrderDto> orders = Arrays.asList(order);
		when(orderService.addOrder(orders)).thenReturn(orders);

		ResponseEntity<List<OrderDto>> response = orderController.addOrders(orders);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		assertEquals(1, response.getBody().size());

	}

	@Test
	public void testUpdateOrder() throws OrderProcessingException {
		OrderDto orderDto = new OrderDto();
		orderDto.setId("1");
		orderDto.setName("Updated Order 1");
		orderDto.setPrice(120.0);
		orderDto.setQantity(2);

		when(orderService.updateOrderById(eq(orderDto), eq("1"))).thenReturn(orderDto);

		ResponseEntity<OrderDto> response = orderController.updateOrder(orderDto, "1");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Updated Order 1", response.getBody().getName());
		assertEquals(120.0, response.getBody().getPrice(), 0.01);
		assertEquals(2, response.getBody().getQantity());
	}

	@Test
	public void testUpdateOrder_InvalidId() {

		OrderDto orderDto = new OrderDto();
		orderDto.setId("1");
		orderDto.setName("Order 1");
		orderDto.setPrice(100.0);
		orderDto.setQantity(1);

		assertThrows(OrderNotFoundException.class, () -> {
			orderController.updateOrder(orderDto, "");
		});
	}

}
