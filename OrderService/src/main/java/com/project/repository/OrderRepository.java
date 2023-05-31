package com.project.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.model.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
//	@Query("SELECT o FROM tb_order WHERE o.uuid = :orderNum")
	public Order findFirstByOrderNumber(@Param("orderNum")String orderNumber);
		
//	Page<Order> findAllOrder(Pageable pageable);
}
