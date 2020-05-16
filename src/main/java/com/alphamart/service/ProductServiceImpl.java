package com.alphamart.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alphamart.exception.DataNotValidException;
import com.alphamart.exception.InvalidRequestException;
import com.alphamart.exception.NotFoundException;
import com.alphamart.model.Product;
import com.alphamart.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
	final static Logger logger = LogManager.getLogger(ProductServiceImpl.class); 

	@Autowired
	ProductRepository prdctRepository;

	@Override
	public List<Product> getAllProducts() {
		logger.info("Get all products");
		return prdctRepository.findAll();
	}

	@Override
	public Product getProductById(Integer id) throws NotFoundException {
		logger.info("Get Product for Id: {}", id);
		Product product = prdctRepository.findByProductId(id)
				.orElseThrow(() -> new com.alphamart.exception.NotFoundException("Product not found"));
		return product;
	}

	@Override
	public Product addProduct(Product product) {
		logger.info("Adding Product: " + product);
		return prdctRepository.save(product);
	}

	@Override
	public List<Product> getProductByName(String prodName) throws NotFoundException {
		logger.info("Get Product for name: {}", prodName);
		List<Product> product = prdctRepository.findByProductNameContainingIgnoreCase(prodName)
				.orElseThrow(() -> new com.alphamart.exception.NotFoundException("Product not found"));
		return product;
	}

	@Override
	public List<Product> getProductByType(String prodType) throws NotFoundException {
		logger.info("Get Product by type: {}", prodType);
		List<Product> product = prdctRepository.findByProductType(prodType)
				.orElseThrow(() -> new com.alphamart.exception.NotFoundException("Product not found"));
		return product;
	}

	@Override
	public List<Product> searchProducts(String key, String value) throws NumberFormatException, NotFoundException, InvalidRequestException, DataNotValidException {
		logger.error("Search key: {}, value: {}.", key, value);
		List<Product> porductList = new ArrayList<Product>();
		if (key != null) {
			switch (key) {
			case AlphaMartConstants.SEARCH_BY_TYPE:
				porductList = getProductByType(value);
				break;
			case AlphaMartConstants.SEARCH_BY_NAME:
				porductList = getProductByName(value);
				break;
			case AlphaMartConstants.SEARCH_BY_ID:
				if(!value.matches("[0-9]+")) {
					logger.error("Search by Id has non-numeric character.");
					throw new DataNotValidException("Invalid Product Id. Please enter only digits");
				}
				Product product = getProductById(Integer.parseInt(value));
				porductList.add(product);
				break;
			default:
				logger.error("Invalid Search Criteria: {}", key);
				throw new InvalidRequestException("Invalid Search Criteria");
			}
		}
		logger.info("Search product results: {}", porductList);
		return porductList;
	}

}
