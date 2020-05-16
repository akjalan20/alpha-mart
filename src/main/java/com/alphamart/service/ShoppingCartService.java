package com.alphamart.service;

import javax.transaction.Transactional;

import com.alphamart.exception.DataNotValidException;
import com.alphamart.exception.InvalidRequestException;
import com.alphamart.exception.NotFoundException;
import com.alphamart.model.Cart;
import com.alphamart.model.OrderProduct;

public interface ShoppingCartService {

	Cart getProductsInCart(String userId);

	Cart updateProductQuantity(OrderProduct orderProduct, String userId)
			throws NotFoundException, InvalidRequestException, DataNotValidException;

	void removeProductsFromCart(Integer id, String userId) throws NotFoundException, InvalidRequestException;

	Cart addProductsToCart(OrderProduct orderProduct, String userId) throws NotFoundException;

	Cart deleteAllProductsInCart(String userId) throws InvalidRequestException;

}