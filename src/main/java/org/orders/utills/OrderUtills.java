package org.orders.utills;

import org.orders.dto.OrderDto;
import org.orders.entity.Orders;
import org.springframework.beans.BeanUtils;

public class OrderUtills {

	public static OrderDto entityToDto(Orders orders) {
		OrderDto orderDto = new OrderDto();
		BeanUtils.copyProperties(orders, orderDto);
		return orderDto;
	}

	public static Orders dtoToEntity(OrderDto ordersDto) {
		Orders orders = new Orders();
		BeanUtils.copyProperties(ordersDto, orders);
		return orders;
	}
}
