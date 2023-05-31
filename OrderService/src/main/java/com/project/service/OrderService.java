package com.project.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.dto.request.OrderRequest;
import com.project.dto.response.InventoryResponse;
import com.project.dto.response.OrderResponse;
import com.project.model.Order;
import com.project.model.OrderItem;
import com.project.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {
	@Autowired
	private final OrderRepository orderRepository;

	private final WebClient.Builder client;

	private OrderResponse mapToOrderResponse(Order order) {
		return OrderResponse.builder()
				.id(order.getId())
				.orderNumber(order.getOrderNumber())
				.totalAmount(order.getTotalAmount())
				.createDate(order.getCreateDate())
				.orderItems(order.getOrderItems())
				.build();

	}

//
	private OrderItem mapToOrderItem(InventoryResponse itemResponse) {
		return OrderItem.builder()
				.price(itemResponse.getPrice())
				.quantity(itemResponse.getQuantity())
				.skuCode(itemResponse.getSku())
				.build();

	}

	private BigDecimal calculateTotal(List<OrderItem> orderItems) {
		BigDecimal total = new BigDecimal(0);
		if (!orderItems.isEmpty() || orderItems != null) {
			for (OrderItem items : orderItems) {
				total = total.add(items.getPrice().multiply(BigDecimal.valueOf(items.getQuantity())));
			}
		}
		return total;
	}

	public void placeOrder(OrderRequest request) {
		String uuid;
		do {
			uuid = UUID.randomUUID().toString();

		} while (orderRepository.findFirstByOrderNumber(uuid) != null);

		Order order = Order.builder().orderNumber(uuid).createDate(LocalDateTime.now()).build();

		if (request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
			log.info("--->No product Order not save");
			return;
		}
		InventoryResponse[] inventoryResponseArray = client.build()
				.post()
				.uri("http://inventory-service/api/inventory")
				.body(Mono.just(request), OrderRequest.class)
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();

		if (inventoryResponseArray != null) {
			order.setOrderItems(Arrays.stream(inventoryResponseArray).map(this::mapToOrderItem).toList());
			order.setTotalAmount(calculateTotal(order.getOrderItems()));
			orderRepository.save(order);
			log.info("--->Order saved");
		} else {
			throw new IllegalArgumentException("Product is not in stock, please try again later");
		}
	}

	public List<OrderResponse> findAllOrders(int pageNo, int pageSize) {
		Sort sort = Sort.by("id").ascending();
		Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

		try {
			Page<Order> orders = orderRepository.findAll(pageable);
			return orders.getContent().stream().map(this::mapToOrderResponse).toList();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

	}

	public OrderResponse findByOrderNumber(String orderNumber) {
		return mapToOrderResponse(orderRepository.findFirstByOrderNumber(orderNumber));
	}

	public OrderResponse findOrderById(long id) {
		Optional<Order> order = orderRepository.findById(id);
		if (order.isPresent()) {
			return mapToOrderResponse(order.get());
		}
		log.info("--->Order id {} not found", id);
		return null;
	}

	public void deleteOrder(String orderNumber) {
		Order order = orderRepository.findFirstByOrderNumber(orderNumber);
		if (order == null) {
			log.info("--->Order {} not found", orderNumber);
			return;
		}
		orderRepository.delete(order);
		log.info("--->Order {} deleted", orderNumber);
	}

	public void addOrderItem(OrderRequest request, Long orderId) {
		Order order = orderRepository.findById(orderId).get();
		if (order == null) {
			return;
		}
		InventoryResponse[] inventoryResponseArray = client.build()
				.post()
				.uri("http://inventory-service/api/inventory")
				.body(Mono.just(request), OrderRequest.class)
				.retrieve()
				.bodyToMono(InventoryResponse[].class)
				.block();

		if (inventoryResponseArray != null) {
			order.setOrderItems(Arrays.stream(inventoryResponseArray).map(this::mapToOrderItem).toList());
			order.setTotalAmount(calculateTotal(order.getOrderItems()));
			orderRepository.save(order);
			log.info("--->Order saved");
		} else {
			throw new IllegalArgumentException("Product is not in stock, please try again later");
		}
	}

}
