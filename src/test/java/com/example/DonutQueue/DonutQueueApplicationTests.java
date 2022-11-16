package com.example.DonutQueue;

import com.example.DonutQueue.Controllers.ClientController;
import com.example.DonutQueue.Exceptions.ResourceNotFoundException;
import com.example.DonutQueue.Models.Client;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder( MethodOrderer.OrderAnnotation.class)
class DonutQueueApplicationTests {
	
	@Autowired
	ClientController clientController;
	
	
	
	@Test
	@Order( 1 )
	void contextLoads() {
		assertNotNull( clientController );
	}
	
	@Test
	@Order( 2 )
	public void testCreateNewClientSuccessfully(){
		Client client = new Client();
		client.setId( 500 );
		client.setName( "Alex" );
		client.setPremium( true );
		ResponseEntity<Object> responseEntity = clientController.createClient(client);
		Response res = (Response) responseEntity.getBody();
		assert res != null;
		assertEquals( "New user created", res.getMessage());
	}
	
	@Test
	@Order( 3 )
	public void testCreateNewClientAlreadyExists(){
		Client client = new Client();
		client.setId( 500 );
		client.setName( "Alex" );
		client.setPremium( true );
		ResponseEntity<Object> responseEntity = clientController.createClient(client);
		Response res = (Response) responseEntity.getBody();
		assert res != null;
		assertEquals( "Client already exists", res.getMessage());
	}
	
	@Test
	@Order( 4 )
	public void testCreateNewClientNoName(){
		Client client = new Client();
		client.setId( 501 );
		client.setPremium( true );
		ResponseEntity<Object> responseEntity = clientController.createClient(client);
		Response res = (Response) responseEntity.getBody();
		assert res != null;
		assertEquals( "Client name must be a string and not empty", res.getMessage());
	}
	
	@Test
	@Order( 5 )
	public void testDeleteClientSuccessfully() throws ResourceNotFoundException {
		ResponseEntity<Object> responseEntity = clientController.deleteClient( 500 );
		Response res = (Response) responseEntity.getBody();
		assert res != null;
		assertEquals( "Client deleted", res.getMessage() );
		//Client deleted
	}
	
	@Test
	@Order( 6 )
	public void testDeleteClientClientNotFound() throws ResourceNotFoundException {
		ResponseEntity<Object> responseEntity = clientController.deleteClient( 500 );
		Response res = (Response) responseEntity.getBody();
		assert res != null;
		assertEquals( "No client to delete found", res.getMessage() );
		//Client deleted
	}
	/*
	@Test
	public void testReadAllClients(){
		List<Client> clientList = (List<Client>) clientRepository.findAll();
		assertNotEquals(0, clientList.size());
	}
	
	@Test
	public void testOneClientForID(){
		Client client = clientRepository.findByClientId( 500 );
		assertEquals( 500, client.getId() );
	}
	
	@Test
	public void testOneClientForName(){
		Client client = clientRepository.findByClientId( 500 );
		assertEquals( "Alex", client.getName() );
	}
	
	@Test
	public void testOneClientForPremium(){
		Client client = clientRepository.findByClientId( 500 );
		assertTrue( client.getIsPremium() );
	}
	*/
	
}
