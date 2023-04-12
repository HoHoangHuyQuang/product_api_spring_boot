package com.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
	Inventory findByProductId(String productId);
	Inventory findBySkuCode(String skuCode);
}
