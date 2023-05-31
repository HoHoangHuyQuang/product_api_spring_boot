package com.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.request.ProductRequest;
import com.project.dto.response.ProductResponse;
import com.project.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/products")
@RequiredArgsConstructor
public class ProductController {
	@Autowired
	private final ProductService productService;

	@PostMapping
	public ResponseEntity<String> createProduct(@RequestBody ProductRequest request) {
		try {
			productService.createProduct(request);
			return new ResponseEntity<String>("New product added", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Some thing went wrong!!!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping()
	public ResponseEntity<List<ProductResponse>> getAllProducts(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "30") int size) {
		try {
			List<ProductResponse> products = productService.findAllProducts(page, size);
			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
		try {
			ProductResponse product = productService.findProductById(id);
			if (product == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(product, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateProduct(@RequestBody ProductRequest request, @PathVariable String id) {
		try {
			ProductResponse product = productService.updateProduct(id, request);
			if (product == null) {
				return new ResponseEntity<String>("Product not found", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<String>("Product updated", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Some thing went wrong!!!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteProductById(@PathVariable String id) {
		try {
			ProductResponse product = productService.deleteProductById(id);
			if (product == null) {
				return new ResponseEntity<String>("Product not found", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<String>("Product deleted", HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Some thing went wrong!!!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
