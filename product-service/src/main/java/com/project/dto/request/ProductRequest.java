package com.project.dto.request;

import java.math.BigDecimal;
import java.util.Set;

import com.project.model.Variant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductRequest {
	private String name;

	private BigDecimal price;

	private String description;

	private Set<Variant> variants;

}
