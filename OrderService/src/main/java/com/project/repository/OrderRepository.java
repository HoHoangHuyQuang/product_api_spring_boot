package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
//	@Query("SELECT o FROM tb_order WHERE o.uuid = ?1")
	public Order findFirstByOrderNumber(String orderNumber);
		
	
}
