package com.alphamart.service;

import java.util.List;

import com.alphamart.exception.DataNotValidException;
import com.alphamart.exception.InvalidRequestException;
import com.alphamart.exception.NotFoundException;
import com.alphamart.model.Product;

public interface ProductService {

	List<Product> getAllProducts();

	Product getProductById(Integer id) throws NotFoundException;

	Product addProduct(Product product);

	List<Product> getProductByName(String prodName) throws NotFoundException;

	List<Product> getProductByType(String prodType) throws NotFoundException;

	List<Product> searchProducts(String key, String value)
			throws NumberFormatException, NotFoundException, InvalidRequestException, DataNotValidException;

}