package com.alphamart.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alphamart.exception.DataNotValidException;
import com.alphamart.exception.InvalidRequestException;
import com.alphamart.exception.NotFoundException;
import com.alphamart.model.Product;
import com.alphamart.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository prdctRepository;

	public List<Product> getAllProducts() {
		return prdctRepository.findAll();
	}

	public Product getProductById(Integer id) throws NotFoundException {
		Product product = prdctRepository.findByProductId(id)
				.orElseThrow(() -> new com.alphamart.exception.NotFoundException("Product not found"));
		return product;
	}

	public Product addProduct(Product product) {
		return prdctRepository.save(product);
	}

	public List<Product> getProductByName(String prodName) throws NotFoundException {
		List<Product> product = prdctRepository.findByProductNameContainingIgnoreCase(prodName)
				.orElseThrow(() -> new com.alphamart.exception.NotFoundException("Product not found"));
		return product;
	}

	public List<Product> getProductByType(String prodType) throws NotFoundException {
		List<Product> product = prdctRepository.findByProductType(prodType)
				.orElseThrow(() -> new com.alphamart.exception.NotFoundException("Product not found"));
		return product;
	}

	public List<Product> searchProducts(String key, String value) throws NumberFormatException, NotFoundException, InvalidRequestException, DataNotValidException {
		List<Product> porductList = new ArrayList<Product>();
		if (key != null) {
			switch (key) {
			case IAlphaMartConstants.SEARCH_BY_TYPE:
				porductList = getProductByType(value);
				break;
			case IAlphaMartConstants.SEARCH_BY_NAME:
				porductList = getProductByName(value);
				break;
			case IAlphaMartConstants.SEARCH_BY_ID:
				if(!value.matches("[0-9]+")) {
				   throw new DataNotValidException("Invalid Product Id. Please enter only digits");
				}
				Product product = getProductById(Integer.parseInt(value));
				porductList.add(product);
				break;
			default:
				throw new InvalidRequestException("Invalid Search Criteria");
			}
		}
		return porductList;
	}

}
