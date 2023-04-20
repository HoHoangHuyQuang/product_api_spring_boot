package com.project.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.project.model.Catalog;

@Repository
public interface CatalogRepository extends MongoRepository<Catalog, String>{

}
