package com.alphamart.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alphamart.exception.DataNotValidException;
import com.alphamart.exception.InvalidRequestException;
import com.alphamart.exception.NotFoundException;
import com.alphamart.model.Cart;
import com.alphamart.model.CartDetail;
import com.alphamart.model.OrderProduct;
import com.alphamart.model.Product;
import com.alphamart.model.User;
import com.alphamart.repository.CartDetailsRepository;
import com.alphamart.repository.ShoppingCartRepository;

@Service
public class ShoppingCartService {

	@Autowired
	ShoppingCartRepository cartRepository;

	@Autowired
	CartDetailsRepository cartDetailRepository;

	@Autowired
	ProductService productService;
	
	@Autowired
	UserService userService;

	public Cart getProductsInCart(String userId) {
		Cart cart = cartRepository.findByUser(new User(userId));
		cart = calculateTotalDue(cart);
		return cart;
	}

	@Transactional
	public Cart updateProductQuantity(OrderProduct orderProduct, String userId)
			throws NotFoundException, InvalidRequestException, DataNotValidException {
		Cart shoppingCart = null;
		if (orderProduct != null) {	
			if(orderProduct.getQuantity()< 0){
				throw new DataNotValidException("Product Quantity cannot be negative");
			}
			shoppingCart = getProductsInCart(userId);
			if (shoppingCart != null && shoppingCart.getCartId() != 0) {
				List<CartDetail> cartDetailsInDB = shoppingCart.getCartDetails();
				Boolean found = false;
				for (CartDetail cartDetail : cartDetailsInDB) {
					if (cartDetail.getProduct().getProductId() == orderProduct.getProduct().getProductId()) {
						if (orderProduct.getQuantity() == 0) {
							// Delete product from cart if quantity updated to 0
							cartDetailRepository.deleteById(cartDetail.getCartDetailId());
						} else {
							cartDetail.setQuantity(orderProduct.getQuantity());
							// update product quantity in cart
							cartDetailRepository.updateProductQuantity(cartDetail.getQuantity(),
									cartDetail.getCartDetailId());
						}
						found = true;
					}
				}
				if (!found) {
					throw new NotFoundException("Product not found in cart.");
				}
			} else {
				throw new NotFoundException("Cart does not exist.");
			}
		} else {
			throw new InvalidRequestException("Invalid Request");
		}
		return getProductsInCart(userId);
	}

	@Transactional
	public void removeProductsFromCart(Integer id, String userId)
			throws NotFoundException, InvalidRequestException {
		Cart shoppingCart = null;
		Boolean deleted = false;
		if (id != 0) {
			shoppingCart = getProductsInCart(userId);
			if (shoppingCart != null && shoppingCart.getCartId() != 0) {
				List<CartDetail> cartDetailInDB = shoppingCart.getCartDetails();
				for (CartDetail cartDetail : cartDetailInDB) {
					if (cartDetail.getProduct().getProductId() == id) {
						cartDetailRepository.deleteById(cartDetail.getCartDetailId());
						cartDetailRepository.flush();
						deleted = true;
					}
				}
				if (!deleted) {
					throw new NotFoundException("Product not found in cart.");
				}
			}
		} else {
			throw new InvalidRequestException("Invalid Request");
		}
	}

	@Transactional
	public Cart addProductsToCart(OrderProduct orderProduct, String userId) throws NotFoundException {
		User user = userService.getUser(userId);
		if(user==null){
			user = userService.addUser(new User(userId, "password", true, "User Name", "ROLE_USER"));
		}
		Cart shoppingCart = null;
		if (orderProduct != null) {
			shoppingCart = getProductsInCart(userId);
			if (shoppingCart != null && shoppingCart.getCartId() != 0) {
				addToCart(orderProduct, shoppingCart);
			} else {
				// Create new cart and add product if not present
				shoppingCart = addShoppingCart(orderProduct, user);
			}

		}

		return getProductsInCart(userId);
	}

	private void addToCart(OrderProduct orderProduct, Cart shoppingCart) throws NotFoundException {
		List<CartDetail> cartDetailFromDB = shoppingCart.getCartDetails();
		Boolean found = false;
		for (CartDetail cartDetail : cartDetailFromDB) {
			if (cartDetail.getProduct().getProductId() == orderProduct.getProduct().getProductId()) {
				cartDetail.setQuantity(cartDetail.getQuantity() + orderProduct.getQuantity());
				// update product quantity in cart
				cartDetailRepository.updateProductQuantity(cartDetail.getQuantity(), cartDetail.getCartDetailId());
				found = true;
			}
		}
		if (!found) {
			// add product to cart
			Product product = productService.getProductById(orderProduct.getProduct().getProductId());
			cartDetailRepository.save(new CartDetail(shoppingCart, product, orderProduct.getQuantity()));
		}

	}

	private Cart addShoppingCart(OrderProduct orderProduct, User user) throws NotFoundException {
		Cart shoppingCart;
		// create new cart and cart details
		shoppingCart = new Cart();
		shoppingCart.setUser(user);
		shoppingCart = cartRepository.save(shoppingCart);
		Product product = productService.getProductById(orderProduct.getProduct().getProductId());
		CartDetail cartDetail = cartDetailRepository
				.save(new CartDetail(shoppingCart, product, orderProduct.getQuantity()));
		List<CartDetail> cartDetails = new ArrayList<CartDetail>();
		cartDetails.add(cartDetail);
		shoppingCart.setCartDetails(cartDetails);
		return shoppingCart;
	}

	private Cart calculateTotalDue(Cart cart) {
		Float totalDue = 0F;
		if (cart != null) {
			List<CartDetail> orderDetail = cart.getCartDetails();
			for (CartDetail od : orderDetail) {
				totalDue += od.getCartDetailId().getProduct().getPrice() * od.getQuantity();
			}
			cart.setTotalDue(totalDue);
		}

		return cart;
	}

	public Cart deleteAllProductsInCart(String userId) throws InvalidRequestException {
		Cart shoppingCart = getProductsInCart(userId);
		if (shoppingCart != null && shoppingCart.getCartId() != 0) {
			List<CartDetail> cartDetailInDB = shoppingCart.getCartDetails();
			for (CartDetail cartDetail : cartDetailInDB) {
				cartDetailRepository.deleteById(cartDetail.getCartDetailId());
			}
		} else {
			throw new InvalidRequestException("Invalid Request");
		}
		return getProductsInCart(userId);
	}

}
