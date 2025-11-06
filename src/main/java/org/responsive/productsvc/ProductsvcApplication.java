package org.responsive.productsvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.responsive.productsvc")
public class ProductsvcApplication {
	public static void main(String[] args) {
		SpringApplication.run(ProductsvcApplication.class, args);
	}
}
