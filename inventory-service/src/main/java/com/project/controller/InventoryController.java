package com.project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.InventoryRequest;
import com.project.dto.OrderRequest;
import com.project.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/inventory")
public class InventoryController {
	private final InventoryService inventoryService;

	@PostMapping
	public ResponseEntity<Boolean> isInstock(@RequestBody OrderRequest request) {
		try {
			boolean responses = inventoryService.isInStock(request);
			return new ResponseEntity<Boolean>(responses, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/add")
	public ResponseEntity<Void> addToStock(@RequestBody InventoryRequest request) {
		try {
			inventoryService.addToStock(request);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
