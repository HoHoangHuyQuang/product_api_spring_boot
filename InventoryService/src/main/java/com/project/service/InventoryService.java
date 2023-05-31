package com.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.dto.InventoryRequest;
import com.project.dto.InventoryResponse;
import com.project.dto.OrderItemRequest;
import com.project.dto.OrderRequest;
import com.project.model.Inventory;
import com.project.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
@Slf4j
public class InventoryService {
	@Autowired
	private final InventoryRepository inventoryRepository;
	private final WebClient.Builder client;

	public void addToStock(InventoryRequest request) {
		// bool check if get id return is Ok
		String productId = request.getProductId();
		Boolean result = client.build()
				.get()
				.uri("http://product-service/api/products/{productId}", productId)
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {
						return Mono.just(true);
					} else {
						return Mono.just(false);
					}
				})
				.block();

		if (result) {
			// is already in stock
			Inventory stock = inventoryRepository.findByProductId(request.getProductId());
			if (stock != null) {
				stock.setQuantity(stock.getQuantity() + request.getQuantity());
				stock.setPrice(request.getPrice());
				stock.setSkuCode(request.getSkuCode());
				inventoryRepository.save(stock);
				log.info("--> Stock of {} added", request.getProductId());
				return;
			} else {
				// new product registration
				Inventory newStock = Inventory.builder()
						.productId(request.getProductId())
						.quantity(request.getQuantity())
						.skuCode(request.getSkuCode())
						.price(request.getPrice())
						.build();
				inventoryRepository.save(newStock);
				log.info("--> New stock {} added", request.getProductId());
				return;
			}
		}
		log.info("---> Something went wrong");
		return;
	}

	@Transactional
	@SneakyThrows
	public List<InventoryResponse> withdrawStock(OrderRequest requests) {
		log.info("Checking Inventory");
		List<InventoryResponse> lst = new ArrayList<>();
		for (OrderItemRequest re : requests.getOrderItems()) {
			Inventory item = inventoryRepository.findBySkuCode(re.getSkuCode());
			if (item == null) {
				log.info("---> Product not found");
				return null;
			}
			if (item.getQuantity() < re.getQuantity()) {
				log.info("---> Product {} is out of stock", item.getSkuCode());
				return null;
			}

			item.setQuantity(item.getQuantity() - re.getQuantity());
			inventoryRepository.save(item);

			lst.add(InventoryResponse.builder()
					.price(item.getPrice())
					.skuCode(item.getSkuCode())
					.quantity(re.getQuantity())
					.build());
		}
		return lst;
	}
}
