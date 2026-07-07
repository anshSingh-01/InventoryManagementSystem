package org.springproject.inventorymangaement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springproject.inventorymangaement.services.ProductService;

@SpringBootApplication
public class InventorymangaementApplication implements CommandLineRunner {

    @Autowired
    ProductService productService;

    public static void main(String[] args) {
        SpringApplication.run(InventorymangaementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
//        productService.addAllProducts(productDtos);
    }
}
