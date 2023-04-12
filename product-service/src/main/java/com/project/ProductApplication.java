package com.project;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EnableMongoRepositories("com.project.repository")
@EntityScan("com.project.model")
@ComponentScan("com.project")

@EnableEurekaClient
public class ProductApplication {
	 public static void main(String[] args) {
		    SpringApplication.run(ProductApplication.class, args);
		  }

}
