package com.example.DonutQueue.Controllers;

import com.example.DonutQueue.ErrorResponse;
import com.example.DonutQueue.Exceptions.ResourceNotFoundException;
import com.example.DonutQueue.Models.Client;
import com.example.DonutQueue.Repositories.ClientRepository;
import com.example.DonutQueue.Repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping(path = "/api")
public class ClientController {
	@Autowired
	private ClientRepository clientRepository ;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@GetMapping("/clients")
	public ResponseEntity<Object> getAllClients(
			@RequestParam(required = false) Integer id
	){
		List<Client> clients = new ArrayList<>();
		
		if (id == null){
			clients.addAll(clientRepository.findAll());
		}else{
			clients.addAll( clientRepository.findByClientId( id ) );
		}
		
		if (clients.isEmpty()){
			return new ResponseEntity<>(new ErrorResponse("There are no clients"), HttpStatus.NO_CONTENT );
		}else{
			return new ResponseEntity<>( clients, HttpStatus.OK );
		}
	}
	
	
	//return a client by his id
	@GetMapping("/clients/{id}")
	public ResponseEntity<Object> getClientById(@PathVariable("id") int id) throws ResourceNotFoundException {
		Client client = clientRepository.findById( id ).orElse(null);
		
		if (client == null)
			return new ResponseEntity<>(new ErrorResponse( "Client ID not found"), HttpStatus.BAD_REQUEST );
		
		return new ResponseEntity<>( client, HttpStatus.OK );
	}
	
	/**
	 *
	 * @param client client is sent in json. Example: {"id":1234, "name":"pascal"}
	 *               isPremium field could be added, but will be ignored
	 *               id can only be between has a minimum of 1 and maximum of 20000
	 * @return client and http status code
	 */
	@PostMapping("/clients")
	public ResponseEntity<Object> createClient(@RequestBody Client client){
		
		
		//checks for sent client name
		if (client.getName() == null || client.getName().trim().isEmpty() || client.getName().isEmpty()){
			return new ResponseEntity<>(new ErrorResponse(  "Client name must be a string and not empty"),HttpStatus.BAD_REQUEST );
		}
		Client _client = new Client();
		try(InputStream propFile = Files.newInputStream( Paths.get( "src/main/resources/config.properties" ) )) {
			Properties prop = new Properties();
			prop.load( propFile );
			if (client.getId() < Integer.parseInt( prop.getProperty( "PREMIUM_THRESHOLD" )) &&
					client.getId() >= Integer.parseInt(prop.getProperty( "CLIENT_ID_MIN" )) &&
					client.getId() <= Integer.parseInt( prop.getProperty( "CLIENT_ID_MAX" ) )){
				_client = clientRepository.save(new Client(client.getId(), client.getName(), true));
				return new ResponseEntity<>( _client, HttpStatus.CREATED );
			}else if(client.getId() > Integer.parseInt( prop.getProperty( "PREMIUM_THRESHOLD" )) &&
					client.getId() >= Integer.parseInt(prop.getProperty( "CLIENT_ID_MIN" )) &&
					client.getId() <= Integer.parseInt( prop.getProperty( "CLIENT_ID_MAX" ) )){
				_client = clientRepository.save(new Client(client.getId(), client.getName(), false));
				return new ResponseEntity<>( _client, HttpStatus.CREATED );
			}
		} catch (Exception e){
			return new ResponseEntity<>( new ErrorResponse(  "Config file is not set"), HttpStatus.BAD_REQUEST );
		}
		
		
		return new ResponseEntity<>( _client, HttpStatus.BAD_REQUEST );
	}
	
	//let's edit a client with his id
	@PutMapping("/clients/{clientId}")
	public ResponseEntity<Object> updateClient(@PathVariable("clientId") int clientId, @RequestBody Client client) throws ResourceNotFoundException {
		Client _client = clientRepository.findById( clientId ).orElse(null);
		
		if (_client == null)
			return new ResponseEntity<>( new ErrorResponse( "Client ID not found"),HttpStatus.NO_CONTENT );
		
		
		_client.setId( client.getId() );
		_client.setName( client.getName() );
		_client.setPremium( client.getIsPremium() );
		
		return new ResponseEntity<>( clientRepository.save( _client ), HttpStatus.OK );
	}
	
	//deletes a client by his id
	@DeleteMapping("/clients/{clientId}")
	public ResponseEntity<Object> deleteClient(@PathVariable("clientId") int clientId){
		try{
			if (orderRepository.existsById(clientId)){
				orderRepository.deleteById( clientId );
			}
			
			clientRepository.deleteById( clientId );
			
			return new ResponseEntity<>( HttpStatus.NO_CONTENT );
		}catch (org.springframework.dao.EmptyResultDataAccessException erdae){
			return new ResponseEntity<>( new ErrorResponse( "No client to delete found" ),HttpStatus.BAD_REQUEST );
		}
		
	}
	
	//return a list of all premium clients
	@GetMapping("/clients/premium")
	public ResponseEntity<Object> findByPremium(){
		List<Client> clients = clientRepository.findByIsPremium( true );
		
		if (clients.isEmpty()){
			return new ResponseEntity<>(new ErrorResponse( "No clients found" ), HttpStatus.NO_CONTENT );
		}
		
		return new ResponseEntity<>( clients, HttpStatus.OK );
	}
}
