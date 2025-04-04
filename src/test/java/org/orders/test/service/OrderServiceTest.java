package org.orders.test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.orders.dto.OrderDto;
import org.orders.entity.Orders;
import org.orders.exception.OrderNotFoundException;
import org.orders.exception.OrderProcessingException;
import org.orders.repository.OrdersRepository;
import org.orders.services.OrderService;
import org.springframework.kafka.core.KafkaTemplate;

public class OrderServiceTest {

	@Mock
	private OrdersRepository ordersRepository;

	@Mock
	private KafkaTemplate<String, String> kafkaTemplate;

	@InjectMocks
	private OrderService orderService;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);

	}

	@Test
	public void testGetOrders() {

		OrderDto orderDto = new OrderDto();
		orderDto.setId("1");
		orderDto.setName("TestOrder");
		orderDto.setQantity(3);
		orderDto.setPrice(100.0);

		Orders order = new Orders();
		order.setId("1");
		order.setName("TestOrder");
		order.setQantity(3);
		order.setPrice(100.0);

		when(ordersRepository.findById("1")).thenReturn(Optional.of(order));

		OrderDto result = orderService.getOrders("1");

		assertEquals("TestOrder", result.getName());
		assertEquals(100.0, result.getPrice());
		assertEquals(3, result.getQantity());
	}

	@Test
	public void testGetOrders_OrderNotFound() {
		when(ordersRepository.findById("1")).thenReturn(Optional.empty());

		assertThrows(OrderNotFoundException.class, () -> {
			orderService.getOrders("1");
		});
	}

	@Test
	public void testAllOrders_Success() {
		Orders order1 = new Orders();
		order1.setId("1");
		order1.setName("Order 1");
		order1.setPrice(100.0);
		order1.setQantity(1);

		Orders order2 = new Orders();
		order2.setId("2");
		order2.setName("Order 2");
		order2.setPrice(150.0);
		order2.setQantity(2);

		Iterable<Orders> ordersIterable = Arrays.asList(order1, order2);

		when(ordersRepository.findAll()).thenReturn(ordersIterable);

		List<OrderDto> result = orderService.allOrders();

		assertEquals(2, result.size());
		assertEquals("Order 1", result.get(0).getName());
		assertEquals("Order 2", result.get(1).getName());
	}

	@Test
	public void testAllOrders_OrderNotFound() {
		when(ordersRepository.findAll()).thenReturn(Arrays.asList());

		assertThrows(OrderNotFoundException.class, () -> {
			orderService.allOrders();
		});

	}

	@Test
	public void testAddOrder_Success() throws OrderProcessingException { //

		OrderDto orderDto = new OrderDto();
		orderDto.setId("1");
		orderDto.setName("Order 1");
		orderDto.setPrice(100.0);
		orderDto.setQantity(1);

		List<OrderDto> orderDtos = Arrays.asList(orderDto);

		Orders order1 = new Orders();
		order1.setId("1");
		order1.setName("Order 1");
		order1.setPrice(100.0);
		order1.setQantity(1);

		List<Orders> orders = Arrays.asList(order1);

		when(ordersRepository.saveAll(anyList())).thenReturn(orders);

		List<OrderDto> result = orderService.addOrder(orderDtos);

		assertEquals(1, result.size());
		assertEquals("Order 1", result.get(0).getName());
	}

	@Test
	public void testUpdateOrderById_Success() throws OrderProcessingException {

		OrderDto orderDto = new OrderDto();
		orderDto.setId("1");
		orderDto.setName("Order 1");
		orderDto.setPrice(100.0);
		orderDto.setQantity(1);

		Orders existingOrder = new Orders();
		existingOrder.setId("1");
		existingOrder.setName("Order 1");
		existingOrder.setPrice(100.0);
		existingOrder.setQantity(1);

		Orders updatedOrder = new Orders();
		updatedOrder.setId("1");
		updatedOrder.setName("Updated Order");
		updatedOrder.setPrice(120.0);
		updatedOrder.setQantity(2);

		when(ordersRepository.findById("1")).thenReturn(Optional.of(existingOrder));
		when(ordersRepository.save(any(Orders.class))).thenReturn(updatedOrder);

		OrderDto result = orderService.updateOrderById(orderDto, "1");

		assertEquals("Updated Order", result.getName());
		assertEquals(120.0, result.getPrice(), 0.01);
		assertEquals(2, result.getQantity());
	}

	@Test
	public void testUpdateOrderById_OrderNotFound() throws OrderNotFoundException {

		OrderDto orderDto = new OrderDto();
		orderDto.setId("1");
		orderDto.setName("Order 1");
		orderDto.setPrice(100.0);
		orderDto.setQantity(1);

		when(ordersRepository.findById("1")).thenReturn(Optional.empty());

		assertThrows(OrderNotFoundException.class, () -> {
			orderService.updateOrderById(orderDto, "1");
		});
	}
}
