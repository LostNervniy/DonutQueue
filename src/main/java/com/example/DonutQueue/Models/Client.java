package com.example.DonutQueue.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="client")
public class Client {
	@Id
	@Column(name="client_id")
	private Integer clientId;
	
	@Column(name="name")
	private String name;
	
	@Column(name="is_premium")
	private boolean isPremium;
	
	public Client(){
	
	}
	
	public Client(int id, String name, boolean isPremium){
		this.clientId = id;
		this.name = name;
		this.isPremium = isPremium;
	}
	
	/*
	@OneToOne(cascade = CascadeType.ALL)
	@JoinTable(name="client_order",
	joinColumns = {
			@JoinColumn(name="client_id", referencedColumnName = "id")
	},
	inverseJoinColumns = {
			@JoinColumn(name="order_id",referencedColumnName = "id")
	})
	private Order clientOrder;
	
	
	
	
	public Order getOrder() {
		return clientOrder;
	}
	
	public void setOrder(Order order) {
		this.clientOrder = order;
	}
	
 */
	public boolean getIsPremium() {
		return isPremium;
	}
	
	
	public void setPremium(boolean premium) {
		isPremium = premium;
	}
	
	public int getId() {
		return clientId;
	}
	
	public void setId(int id) {
		this.clientId = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
}
