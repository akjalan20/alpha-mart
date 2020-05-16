package com.alphamart;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.alphamart.model.Apparel;
import com.alphamart.model.Book;
import com.alphamart.model.User;
import com.alphamart.service.ProductService;
import com.alphamart.service.UserService;

@SpringBootApplication
public class AlphaMartApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlphaMartApplication.class, args);
	}
	
	@Bean
	CommandLineRunner runner(ProductService productService, UserService userService) {
	    return args -> {
	        productService.addProduct(new Book("Fablehaven", 8F, "Fantasy", "Brandon Mull", "Shadow Mountain"));
	        productService.addProduct(new Book("If It Bleeds", 15F, "Fiction", "Stephen King", "Hodder & Stoughton"));
	        productService.addProduct(new Book("Educated", 10F, "Memoir", "Tara Westover", "Random House"));
	        productService.addProduct(new Apparel("Shirt", 10F, "Half Sleeve", "Levis", "Printed"));
	        productService.addProduct(new Apparel("Jacket", 25F, "Leather", "UCB", "Reversible"));
	        productService.addProduct(new Apparel("Shoes", 20F, "Running", "Adidas", "Boost"));
	        
	        userService.addUser(new User("user", "password", true, "User Name", "ROLE_USER"));
	        userService.addUser(new User("foo", "bar", true, "User Foo", "ROLE_USER"));
	    };
	}

}
