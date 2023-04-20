package com.project.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Variant {
	private String sku;
    private Double price;
    private Integer quantity;
    private List<Option> options;
}
