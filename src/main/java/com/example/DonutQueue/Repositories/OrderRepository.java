package com.example.DonutQueue.Repositories;

import com.example.DonutQueue.Models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
	@Transactional
	void deleteById(int id);
	@Transactional
	void deleteByClientId(int clientId);
	
	
	
}
