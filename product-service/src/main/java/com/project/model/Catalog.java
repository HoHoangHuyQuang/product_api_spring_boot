package com.project.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(value="tb_category")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Catalog {
	@Id
	private String id;
	private String name;
	private String description;
	@DBRef
	@Field("productId")
	private List<Product> products;
}
