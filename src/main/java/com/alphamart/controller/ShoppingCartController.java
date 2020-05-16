package com.alphamart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alphamart.exception.DataNotValidException;
import com.alphamart.exception.InvalidRequestException;
import com.alphamart.exception.NotFoundException;
import com.alphamart.model.Cart;
import com.alphamart.model.OrderProduct;
import com.alphamart.service.ShoppingCartService;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController	
@RequestMapping("/api")
public class ShoppingCartController {
	
	@Autowired
	ShoppingCartService cartService;
	
	@RequestMapping(value="/cart", method=RequestMethod.GET)
	public ResponseEntity<Cart> viewCart(@RequestParam(value="userId") String userId) {		
		Cart cart = cartService.getProductsInCart(userId);
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@RequestMapping(value="/cart/product", method=RequestMethod.POST)
	public ResponseEntity<Cart> addToCart(@RequestBody OrderProduct orderProducts, @RequestParam(value="userId") String userId) throws NotFoundException {
		Cart cart = cartService.addProductsToCart(orderProducts, userId);		
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@RequestMapping(value="/cart/product", method=RequestMethod.PUT)
	public ResponseEntity<Cart> updateProductQuantity(@RequestBody OrderProduct orderProducts, @RequestParam(value="userId") String userId) throws NotFoundException, InvalidRequestException, DataNotValidException {
		Cart cart = cartService.updateProductQuantity(orderProducts, userId);
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@RequestMapping(value="/cart", method=RequestMethod.DELETE)
	public ResponseEntity<Cart> deleteCart(@RequestParam(value="userId") String userId) throws InvalidRequestException {		
		Cart cart = cartService.deleteAllProductsInCart(userId);
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@RequestMapping(value="/cart/product/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Cart> deleteFromCart(@PathVariable(value="id") Integer id, @RequestParam(value="userId") String userId) throws NotFoundException, InvalidRequestException {
		cartService.removeProductsFromCart(id, userId);
		Cart cart = cartService.getProductsInCart(userId);
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
}
