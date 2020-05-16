package com.alphamart.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphamart.exception.DataNotValidException;
import com.alphamart.exception.InvalidRequestException;
import com.alphamart.exception.NotFoundException;
import com.alphamart.model.Apparel;
import com.alphamart.model.Book;
import com.alphamart.model.Product;
import com.alphamart.service.ProductService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController	
@RequestMapping("/api")
public class ProductController {
	
	@Autowired
	private ProductService productService;

	@RequestMapping(value="/products", method=RequestMethod.GET)
	public ResponseEntity<List<Product>> getAllProducts() {
		List<Product> products = productService.getAllProducts();
		return new ResponseEntity<List<Product>>(products, HttpStatus.OK);
	}
	
	@RequestMapping(value="/products/search", method=RequestMethod.GET)
	public ResponseEntity<List<Product>> searchProducts(@RequestParam String key, @RequestParam String value) throws NotFoundException, InvalidRequestException, NumberFormatException, DataNotValidException {
		List<Product> product = productService.searchProducts(key, value);
		return new ResponseEntity<List<Product>>(product, HttpStatus.OK);
	}
	
	@RequestMapping(value="/products/book", method=RequestMethod.POST)
	public ResponseEntity<Product> addProduct(@RequestBody Book product) {
		Product newProduct = productService.addProduct(product);
		return new ResponseEntity<Product>(newProduct, HttpStatus.OK);
	}
	
	@RequestMapping(value="/products/apparel", method=RequestMethod.POST)
	public ResponseEntity<Product> addProduct(@RequestBody Apparel product) {
		Product newProduct = productService.addProduct(product);
		return new ResponseEntity<Product>(newProduct, HttpStatus.OK);
	}
}
