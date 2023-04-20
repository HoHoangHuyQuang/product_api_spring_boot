package com.project.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {

//	private String productId;
	private String skuCode;
	private BigDecimal price;
	private int quantity;
//	private boolean isInStock;
}
