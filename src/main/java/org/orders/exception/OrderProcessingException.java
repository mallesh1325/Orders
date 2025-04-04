package org.orders.exception;

public class OrderProcessingException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public OrderProcessingException(String message, Throwable issue) {
		super(message,issue);
	}

}
