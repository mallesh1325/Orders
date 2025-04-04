package org.orders.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// Unable to use  Lombokok

/*@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString*/

@Entity
@Table(name = "orders")
public class Orders {

	@Id()
	@Column(nullable = false)
	private String id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Integer qantity;

	@Column(nullable = false)
	private Double price;

	public Orders() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQantity() {
		return qantity;
	}

	public void setQantity(Integer qantity) {
		this.qantity = qantity;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Orders{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", qantity=" + qantity + ", price=" + price
				+ '}';
	}

}
