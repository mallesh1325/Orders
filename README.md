                                                 Order Application 

Endpoints
1.	Get Order by ID

○	URL: http://localhost:8080/orders/getOrderById/{id}

○	Method: GET

○	Description: Retrieves a specific order by its ID.

○	Path Variable:

■	id: The ID of the order to be fetched.

○	Responses:

■	200 OK: If the order is found, the order details are returned in the response body as OrderDto.

■	404 Not Found: If the order with the specified ID does not exist, the response body is empty, and an error message is logged.

Example Request:

Method : GET
URL : http://localhost:8080/orders/ getOrderById /1

Example Response:
 json

{
  "id": "1",
  "name": "Order1",
  "price": 100.0,
  "quantity": 1}
○	
________________________________________
2.	Get All Orders

○	URL: http://localhost:8080/orders/getOrdersList

○	Method: GET

○	Description: Retrieves a list of all orders in the system.

○	Responses:

■	200 OK: A list of all orders is returned in the response body as a list of OrderDto.

■	404 Not Found: If no orders are found, the response body is No orders available.

Example Request:

Method : GET
 URL : http://localhost:8080/orders/getOrdersList

Example Response:

 json
[
  {
    "id": "1",
    "name": "Order 1",
    "price": 100.0,
    "quantity": 1
  },
  {
    "id": "2",
    "name": "Order 2",
    "price": 150.0,
    "quantity": 2
  }
]
________________________________________
3.	Create New Orders

○	URL: http://localhost:8080/orders/createOrder

○	Method: POST

○	Description: Adds a list of new orders to the system.

○	Request Body: A list of OrderDto objects representing the orders to be created.

○	Responses:

■	201 Created: If the orders are successfully created, the newly created orders are returned in the response body as OrderDto.

■	500 Internal Server Error: If there is an error during the order creation process.

Example Request:

Method : POST 
URL : http://localhost:8080/orders/createOrder

Content-Type: application/json
[
  {
    "id": "3",
    "name": "Order 3",
    "price": 200.0,
    "quantity": 3
  }
]
Example Response:

[
  {
    "id": "3",
    "name": "Order 3",
    "price": 200.0,
    "quantity": 3
  }
]
________________________________________
4.	Update Order by ID

○	URL: http://localhost:8080/orders/updateOrder/{id}

○	Method: PUT

○	Description: Updates the details of an existing order based on the order ID.

○	Path Variable:

■	id: The ID of the order to be updated.

○	Request Body: The OrderDto object with the updated order details.

○	Responses:

■	200 OK: The updated order details are returned in the response body as OrderDto.

■	404 Not Found: If the order with the specified ID does not exist, the response body is empty, and an error message is logged.

Example Request:

Method : PUT 
URL : http://localhost:8080/orders/updateOrder/1

Content-Type: application/json
{
  "id": "1",
  "name": "Updated Order 1",
  "price": 120.0,
  "quantity": 2
}

Example Response:

{
  "id": "1",
  "name": "Updated Order 1",
  "price": 120.0,
  "quantity": 2
}


## Kafka Integration

The system integrates with Kafka to publish create and updates orders. 

- Kafka Topic: `order_topic`
- Consumer group-id=notification_group

When an order is successfully created or updated, an event is triggered, and the order's ID is sent to the `order_topic` Kafka consumer consumes the message from the topic and prints the order id.


