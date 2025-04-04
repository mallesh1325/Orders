package org.orders.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

	@KafkaListener(topics = "order_topic", groupId = "notification_group")
	public void listen(String message) {

		log.info(" Kafka message consumed for  with Order ID: {} ", message);

		System.out.println("Notification Received With Id: " + message);

	}

}
