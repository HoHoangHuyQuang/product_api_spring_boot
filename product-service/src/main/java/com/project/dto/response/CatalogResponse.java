package com.project.dto.response;

import java.util.List;

import com.project.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CatalogResponse {

	private String id;
	private String name;
	private String description;	
	private List<Product> products;
}
