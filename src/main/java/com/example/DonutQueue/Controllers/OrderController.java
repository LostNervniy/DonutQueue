package com.example.DonutQueue.Controllers;

import com.example.DonutQueue.Estimation;
import com.example.DonutQueue.Exceptions.ResourceNotFoundException;
import com.example.DonutQueue.Models.Client;
import com.example.DonutQueue.Models.Order;
import com.example.DonutQueue.Repositories.ClientRepository;
import com.example.DonutQueue.Repositories.OrderRepository;
import com.example.DonutQueue.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@Controller
@RequestMapping(path = "/api")
public class OrderController {
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ClientRepository clientRepository;
	
	
	//gets order with an id, comment out if necessary
	/*
	@GetMapping({"/orders/{id}", "/clients/{id}/orders"})
	public ResponseEntity<Order> getOrderById(@PathVariable(value = "id") int id) throws ResourceNotFoundException {
		Order order = orderRepository.findById(id)
				.orElse(null);
		
		if(order == null)
			return new ResponseEntity<>( HttpStatus.NO_CONTENT );
		
		return new ResponseEntity<>( order, HttpStatus.OK );
	}
	 */
	
	
	//An endpoint for adding items to the queue. This endpoint must take two parameters,
	//the ID of the client and the quantity
	@PostMapping("/clients/{clientId}/orders")
	public ResponseEntity<Object> createOrder(@PathVariable(value = "clientId") int clientId,
	                                         @RequestBody Order orderReq) throws ResourceNotFoundException {
		if (orderReq.getQuantity() < 1 || orderReq.getQuantity() > 50){
			return new ResponseEntity<>( new Response("Error", "Invalid input"),HttpStatus.BAD_REQUEST );
		}
		
		try{
			Client client = clientRepository.findById( clientId ).orElse(null);
			
			if (client == null)
				return new ResponseEntity<>(new Response("Error", "Client ID does not exists"), HttpStatus.BAD_REQUEST );
			
			orderReq.setOrderDate( Instant.now().getEpochSecond());
			orderReq.setClient( client );
			
			Order order = orderRepository.save( orderReq );
			
			return new ResponseEntity<>( new Response( "Success", "Order for client accepted",order ), HttpStatus.CREATED );
		}catch (org.springframework.dao.DataIntegrityViolationException ex){
			return new ResponseEntity<>( new Response("Error", "Client has already an order"), HttpStatus.BAD_REQUEST );
		}
		
	}
	
	//An endpoint to cancel an order. This endpoint should accept only the client ID
	@DeleteMapping("/orders/{clientId}")
	public ResponseEntity<Object> deleteOrder(@PathVariable("clientId") int clientId){
		try{
			orderRepository.deleteById( clientId );
			return new ResponseEntity<>( new Response( "Success", "Order deleted" ), HttpStatus.OK );
		}catch (org.springframework.dao.EmptyResultDataAccessException erdae){
			return new ResponseEntity<>( new Response( "Error", "Client has no order" ),HttpStatus.BAD_REQUEST );
		}
		
	}
	
	//deletes a client with order, comment out if necessary
	/*
	@DeleteMapping("/clients/{clientId}/orders")
	public ResponseEntity<Order> deleteOrderOfClient(@PathVariable(value = "clientId") Integer clientId) throws ResourceNotFoundException {
		if(!orderRepository.existsById( clientId )){
			return new ResponseEntity<>( HttpStatus.BAD_REQUEST );
		}
		
		orderRepository.deleteByClientId( clientId );
		return new ResponseEntity<>( HttpStatus.NO_CONTENT );
	}
	 */
	
	
	//An endpoint to retrieve his next delivery which should be placed in the cart
	@GetMapping("/clients/orders/next")
	public ResponseEntity<Object> getNextOrder(){
		try{
			//compare order date ASC
			Comparator<Order> compareByOrderDate = Comparator.comparingLong( Order::getOrderDate );
			//compare is premium DESC
			Comparator<Order> compareByIsPremium = (o1, o2) -> Boolean.compare( o2.getClient().getIsPremium(), o1.getClient().getIsPremium() );
			Comparator<Order> compareByIsPremiumAndOrderDate = compareByIsPremium.thenComparing( compareByOrderDate );
			List<Order> orderList = new ArrayList<Order>( orderRepository.findAll());
			orderList.sort(compareByIsPremiumAndOrderDate);
			
			
			Order nextOrder = orderList.get( 0 );
			
			return new ResponseEntity<>( new Response( "Success", "Next order fetched", nextOrder ), HttpStatus.OK );
		}catch(IndexOutOfBoundsException ioobe){
			return new ResponseEntity<>( new Response( "Error", "No order found" ), HttpStatus.OK );
		}
		
		
	}
	
	//An endpoint which allows his manager to see all entries in the queue with the
	//approximate wait time
	@GetMapping("/clients/orders/queue")
	public ResponseEntity<Object> getOrderQueueList(){
		try{
			//compare order date ASC
			Comparator<Order> compareByOrderDate = Comparator.comparingLong( Order::getOrderDate );
			//compare is premium DESC
			Comparator<Order> compareByIsPremium = (o1, o2) -> Boolean.compare( o2.getClient().getIsPremium(), o1.getClient().getIsPremium() );
			Comparator<Order> compareByIsPremiumAndOrderDate = compareByIsPremium.thenComparing( compareByOrderDate );
			List<Order> orderList = new ArrayList<Order>( orderRepository.findAll());
			orderList.sort(compareByIsPremiumAndOrderDate);
			for (int i = 0; i < orderList.size(); i++){
				Order order = orderList.get( i );
				order.setApproximatedWaitTimeInSeconds( (i+1)*300 );
				orderList.set( i, order);
			}
			return new ResponseEntity<>(new Response("Success", "Order queue fetched", orderList) , HttpStatus.OK );
		}catch (IndexOutOfBoundsException ioobe){
			return new ResponseEntity<>( new Response("Error", "No order found" ), HttpStatus.BAD_REQUEST );
		}
		
	}
	
	//An endpoint for the client to check his queue position and approximate wait time.
	//Counting starts at 1.
	@GetMapping("/clients/{clientId}/orders/queue")
	public ResponseEntity<Object> getApproximateWaitTime(@PathVariable(value="clientId") Integer clientId) throws ResourceNotFoundException {
		
		Order order = orderRepository.findById( clientId )
				.orElse(null);
		
		if (order == null)
			return new ResponseEntity<>(new Response("Error", "Client ID does not exist or does not have an order"), HttpStatus.BAD_REQUEST );
		
		try{
			//compare order date ASC
			Comparator<Order> compareByOrderDate = Comparator.comparingLong( Order::getOrderDate );
			//compare is premium DESC
			Comparator<Order> compareByIsPremium = (o1, o2) -> Boolean.compare( o2.getClient().getIsPremium(), o1.getClient().getIsPremium() );
			Comparator<Order> compareByIsPremiumAndOrderDate = compareByIsPremium.thenComparing( compareByOrderDate );
			List<Order> orderList = new ArrayList<Order>( orderRepository.findAll());
			orderList.sort(compareByIsPremiumAndOrderDate);
			
			//queue position
			int orderPosition = orderList.indexOf(order)+1;
			//estimated time of arrival
			//since jim is looking every 5 minutes for an order,
			//I assume that he needs like ~5 minutes for every order
			//before the order for the client
			int estimatedTimeOfArrival = orderPosition * 300;
			Estimation est = new Estimation( orderPosition, estimatedTimeOfArrival );
			
			return new ResponseEntity<>( new Response("Success", "Position and estimated arrival fetched", est), HttpStatus.OK );
		}catch (IndexOutOfBoundsException ioobe){
			return new ResponseEntity<>( new Response( "Error","No order found" ), HttpStatus.BAD_REQUEST );
		}
		
	}
}