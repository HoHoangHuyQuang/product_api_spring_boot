package com.project.model;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value = "tb_product")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {
	@Id
	private String id;

	private String name;
	
	private BigDecimal price;

	private String description;

	private Set<Variant> variants;		


}
