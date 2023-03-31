package com.project.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.project.model.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
	private long id;
	private String orderNumber;
	private BigDecimal totalAmount;
	private LocalDateTime createDate;
	private List<OrderItem> orderItems;
}
