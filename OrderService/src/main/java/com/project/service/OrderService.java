package com.project.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.project.dto.request.OrderItemRequest;
import com.project.dto.request.OrderRequest;
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

	private OrderItem mapToOrderItem(OrderItemRequest itemRequest) {
		
		
		return OrderItem.builder()
				.price(itemRequest.getPrice())
				.product(itemRequest.getProduct())
				.quantity(itemRequest.getQuantity())
				.build();

	}

	private BigDecimal calculateTotal(List<OrderItemRequest> orderItems) {
		float total = 0;
		if (!orderItems.isEmpty() || orderItems != null) {
			for (OrderItemRequest items : orderItems) {
				total += items.getPrice() * items.getQuantity();
			}
		}
		return new BigDecimal(total);
	}

	public void placeOrder(OrderRequest request) {
		String uuid;
		do {
			uuid = UUID.randomUUID().toString();

		} while (orderRepository.findFirstByOrderNumber(uuid) != null);

		Order order = Order.builder()
				.orderNumber(uuid)
				.totalAmount(calculateTotal(request.getOrderItems()))
				.createDate(LocalDateTime.now())
				.build();

		if (request.getOrderItems() == null || request.getOrderItems().isEmpty()) {
			log.info("--->No product Order not save");
			return;
		}
		boolean allIsInStock = client.build()
				.post()
				.uri("http://localhost:8080/api/inventory")
				.body(Mono.just(request), OrderRequest.class)
				.retrieve()
				.bodyToMono(Boolean.class)
				.block();

		if (allIsInStock) {
			order.setOrderItems(request.getOrderItems().stream().map(this::mapToOrderItem).toList());
			orderRepository.save(order);
			log.info("--->Order saved");
		} else {
			throw new IllegalArgumentException("Product is not in stock, please try again later");
		}
	}

	public List<OrderResponse> findAllOrders() {
		List<Order> orders = orderRepository.findAll();
		return orders.stream().map(this::mapToOrderResponse).toList();
	}

	public OrderResponse findOrderById(long id) {
		Optional<Order> order = orderRepository.findById(id);
		if (order.isPresent()) {
			return mapToOrderResponse(order.get());
		}
		log.info("--->Order id {} not found", id);
		return null;
	}

	public void updateOrder(String id, OrderRequest request) {

	}

	public void deleteOrder(String id) {

	}

	public void addOrderItem(OrderItem orderItem, Long orderId) {
		Order order = orderRepository.findById(orderId).get();
		order.getOrderItems().add(orderItem);

		orderRepository.save(order);
	}

	public OrderResponse findByOrderNumber(String orderNumber) {
		return mapToOrderResponse(orderRepository.findFirstByOrderNumber(orderNumber));
	}
}
