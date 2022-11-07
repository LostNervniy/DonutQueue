package com.example.DonutQueue.Repositories;

import com.example.DonutQueue.Models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
	List<Client> findByIsPremium(boolean isPremium);
	List<Client> findByClientId(int id);
}
