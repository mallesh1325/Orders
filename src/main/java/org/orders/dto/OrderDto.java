package org.orders.dto;

import java.util.Objects;

/*@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder*/
public class OrderDto {

	private String id;
	private String name;
	private Integer qantity;
	private Double price;

	public OrderDto() {
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQantity(Integer qantity) {
		this.qantity = qantity;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Integer getQantity() {
		return qantity;
	}

	public Double getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return "OrderDto{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", qantity=" + qantity + ", price=" + price
				+ '}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		OrderDto orderDto = (OrderDto) o;
		return Objects.equals(id, orderDto.id) && Objects.equals(name, orderDto.name)
				&& Objects.equals(qantity, orderDto.qantity) && Objects.equals(price, orderDto.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, qantity, price);
	}

}
