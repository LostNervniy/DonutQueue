package com.example.DonutQueue.Models;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.MapsId;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;
import java.time.Instant;

@Entity
@Table(name="donutorder")
public class Order {
	@Id
	@Column(name="client_id")
	private int clientId;
	
	@Column(name="quantity")
	private int quantity;
	
	@Column(name="order_date")
	private long orderDate;
	

	
	@OneToOne(fetch = FetchType.EAGER)
	@MapsId
	@JoinColumn(name = "client_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Client client;
	
	public Order(){
	
	}
	
	public Order(int clientId, int quantity){
		this.clientId = clientId;
		this.quantity = quantity;
		this.orderDate = Instant.now().getEpochSecond();;
	}
	
	
	@Transient
	private int approximatedWaitTimeInSeconds;

	public long getOrderDate() {
		return orderDate;
	}
	
	public void setOrderDate(long orderDate) {
		this.orderDate = orderDate;
	}
	
	//public int getClientId() {
	//	return clientId;
	//}
	
	public void setClientId(int id) {
		this.clientId = id;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public Client getClient() {
		return client;
	}
	
	public void setClient(Client client) {
		this.client = client;
	}
	
	public int getApproximatedWaitTimeInSeconds() {
		return approximatedWaitTimeInSeconds;
	}
	
	public void setApproximatedWaitTimeInSeconds(int approximatedWaitTimeInSeconds) {
		this.approximatedWaitTimeInSeconds = approximatedWaitTimeInSeconds;
	}
	
	@Override
	public String toString() {
		return "Order{" +
				"clientId=" + clientId +
				", quantity=" + quantity +
				", orderDate=" + orderDate +
				", client=" + client +
				", approximatedWaitTimeInSeconds=" + approximatedWaitTimeInSeconds +
				'}';
	}
}
