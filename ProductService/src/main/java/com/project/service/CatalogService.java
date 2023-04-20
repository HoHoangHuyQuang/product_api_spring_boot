package com.project.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.dto.request.CatalogRequest;
import com.project.dto.response.CatalogResponse;
import com.project.model.Catalog;
import com.project.model.Product;
import com.project.repository.CatalogRepository;
import com.project.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {
	@Autowired
	private final CatalogRepository catalogRepository;
	@Autowired
	private final ProductRepository productRepository;

	private CatalogResponse mapToCatalogResponse(Catalog catalog) {
		return CatalogResponse.builder()
				.id(catalog.getId())
				.name(catalog.getName())
				.description(catalog.getDescription())
				.products(catalog.getProducts())
				.build();
	}

	public Catalog createCatelog(CatalogRequest request) {
		List<Product> lst = new ArrayList<>();
		if (request.getProducts() != null) {
			
			if (!request.getProducts().isEmpty()) {
				
				for (String id : request.getProducts()) {
					Optional<Product> item = productRepository.findById(id);
					if(item.isPresent()) {
						lst.add(item.get());
					}
					else {
						log.warn("--->Product id {} could not found", id);
					}
				}
			}
		}
		Catalog catalog = Catalog.builder()
				.name(request.getName())
				.description(request.getDescription())
				.products(lst)
				.build();

		catalogRepository.save(catalog);
		log.info("--->Catalog {} is saved", catalog.getId());
		return catalog;

	}

	public List<CatalogResponse> findAllCatalogs() {
		List<Catalog> catalogs = catalogRepository.findAll();
		return catalogs.stream()
				.map(this::mapToCatalogResponse)
				.toList();

	}

	public CatalogResponse findCatalogById(String id) {
		Optional<Catalog> catalog = catalogRepository.findById(id);
		if (catalog.isPresent()) {
			return mapToCatalogResponse(catalog.get());
		}
		log.info("--->Catalog id {} not found", id);
		return null;
	}

	public CatalogResponse updateCatelog(String id, CatalogRequest request) {
		Catalog catalog = catalogRepository.findById(id).get();
		if (catalog == null) {
			log.info("--->Catalog id {} not found", id);
			return null;
		}
		catalog.setName(request.getName());
		catalog.setDescription(request.getDescription());
		
		List<Product> lst = new ArrayList<>();
		if (request.getProducts() != null) {
			
			if (!request.getProducts().isEmpty()) {
				
				for (String pid : request.getProducts()) {
					Optional<Product> item = productRepository.findById(pid);
					if(item.isPresent()) {
						lst.add(item.get());
					}
					else {
						log.warn("--->Product id {} could not found", pid);
					}
				}
			}
		}
		catalog.setProducts(lst);

		catalogRepository.save(catalog);
		log.info("--->Catalog id {} updated", id);

		return mapToCatalogResponse(catalog);
	}

	public CatalogResponse deleteCatelogById(String id) {
		Catalog catalog = catalogRepository.findById(id).get();
		if (catalog == null) {
			log.info("--->Catalog id {} not found", id);
			return null;
		}
		catalogRepository.deleteById(id);
		log.info("--->Catalog id {} deleted", id);
		return mapToCatalogResponse(catalog);
	}

	public List<Product> getCatalogProducts(String catalogId) {
		Catalog catalog = catalogRepository.findById(catalogId).get();
		if (catalog == null) {
			return null;
		}
		return catalog.getProducts();
			
	}
	public void addProduct(String catalogId, Product product) {
		Catalog catalog = catalogRepository.findById(catalogId).get();
		if (catalog == null || product == null) {
			return;
		}
		if (catalog.getProducts() == null) {
			catalog.setProducts(new ArrayList<Product>());
		}
		if (catalog.getProducts().contains(product)) {
			return;
		}
		catalog.getProducts().add(product);
		catalogRepository.save(catalog);
		log.info("--->Catalog id {} updated", catalogId);
	}

	public void deleteProduct(String catalogId, String productId) {
		Product product = productRepository.findById(productId).get();
		Catalog catalog = catalogRepository.findById(catalogId).get();

		if (catalog == null || product == null) {
			return;
		}
		if (catalog.getProducts().contains(product)) {
			catalog.getProducts().remove(product);
			catalogRepository.save(catalog);
			log.info("--->Catalog id {} updated", catalogId);
		}
	

	}
}
