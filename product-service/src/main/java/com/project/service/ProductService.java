package com.project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.request.ProductRequest;
import com.project.dto.response.ProductResponse;
import com.project.model.Product;
import com.project.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
	@Autowired
	private final ProductRepository productRepository;

	private ProductResponse mapToProductResponse(Product product) {
		return ProductResponse.builder()
				.id(product.getId())
				.name(product.getName())
				.price(product.getPrice())
				.description(product.getDescription())
				.variants(product.getVariants())
				.build();
	}

	public void createProduct(ProductRequest request) {
		Product product = Product.builder()
				.name(request.getName())
				.price(request.getPrice())
				.description(request.getDescription())
				.variants(request.getVariants())
				.build();

		productRepository.save(product);
		log.info("--->Product {} is saved", product.getId());

	}

	public List<ProductResponse> findAllProducts() {
		List<Product> products = productRepository.findAll();
		return products.stream().map(this::mapToProductResponse).toList();

	}

	public ProductResponse findProductById(String id) {
		
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent()) {
			return mapToProductResponse(product.get());
		}
		log.info("--->Product id {} not found", id);
		return null;
	}

	public ProductResponse updateProduct(String id, ProductRequest request) {
		Product product = productRepository.findById(id).get();
		if (product == null) {
			log.info("--->Product id {} not found", id);
			return null;
		}
		product.setName(request.getName());
		product.setPrice(request.getPrice());
		product.setDescription(request.getDescription());
		product.setVariants(request.getVariants());

		productRepository.save(product);
		log.info("--->Product id {} updated", id);
		return mapToProductResponse(product);
	}

	public ProductResponse deleteProductById(String id) {
		Product product = productRepository.findById(id).get();
		if (product == null) {
			log.info("--->Product id  {} not found", id);
			return null;
		}
		productRepository.deleteById(id);
		log.info("--->Product id {} deleted", id);
		return mapToProductResponse(product);
	}

}
