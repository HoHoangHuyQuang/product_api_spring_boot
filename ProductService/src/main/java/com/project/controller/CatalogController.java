package com.project.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.dto.request.CatalogRequest;
import com.project.dto.response.CatalogResponse;
import com.project.model.Product;
import com.project.service.CatalogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/catalogs")
@RequiredArgsConstructor
public class CatalogController {
	private final CatalogService catalogService;

	@PostMapping
	public ResponseEntity<String> createCatalog(@RequestBody CatalogRequest request) {
		try {
			catalogService.createCatelog(request);
			return new ResponseEntity<String>("Catalog added", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Some thing went wrong!!!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping
	public ResponseEntity<List<CatalogResponse>> getAllCatalog() {
		try {
			List<CatalogResponse> catalogs = catalogService.findAllCatalogs();
			if (catalogs.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(catalogs, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<CatalogResponse> getCatalogById(@PathVariable String id) {
		try {
			CatalogResponse catalog = catalogService.findCatalogById(id);
			if (catalog == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<>(catalog, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PutMapping("/{id}")
	public ResponseEntity<String> updateCatalog(@RequestBody CatalogRequest request, @PathVariable String id) {
		try {
			CatalogResponse catalog = catalogService.updateCatelog(id, request);
			if (catalog == null) {
				return new ResponseEntity<String>("Catalog not found", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<String>("Catalog updated", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Some thing went wrong!!!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteCatalogById(@PathVariable String id) {
		try {
			CatalogResponse catalog = catalogService.deleteCatelogById(id);
			if (catalog == null) {
				return new ResponseEntity<String>("Catalog not found", HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<String>("Catalog deleted", HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("Some thing went wrong!!!", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{id}/products")
	public ResponseEntity<List<Product>> getCatalogProducts(@PathVariable(name = "id") String catalogId) {
		try {
			List<Product> lst = catalogService.getCatalogProducts(catalogId);
			return new ResponseEntity<>(lst, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/{id}/products")
	public ResponseEntity<Void> addProduct(@PathVariable(name = "id") String catalogId, @RequestBody Product product) {
		try {
			catalogService.addProduct(catalogId, product);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/{id}/products/{prodId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable String catalogId, @PathVariable String productId) {
		try {
			catalogService.deleteProduct(catalogId, productId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
