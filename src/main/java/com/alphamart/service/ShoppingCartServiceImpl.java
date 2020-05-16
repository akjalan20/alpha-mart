package com.alphamart.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class ShoppingCartServiceImpl implements ShoppingCartService {
	
	final static Logger logger = LogManager.getLogger(ShoppingCartServiceImpl.class); 

	@Autowired
	ShoppingCartRepository cartRepository;

	@Autowired
	CartDetailsRepository cartDetailRepository;

	@Autowired
	ProductService productService;
	
	@Autowired
	UserService userService;

	@Override
	public Cart getProductsInCart(String userId) {
		logger.info("Get Product in cart for user: {}", userId);
		Cart cart = cartRepository.findByUser(new User(userId));
		cart = calculateTotalDue(cart);
		logger.info("Cart for user {}:, {}", userId, cart);
		return cart;
	}

	@Override
	@Transactional
	public Cart updateProductQuantity(OrderProduct orderProduct, String userId)
			throws NotFoundException, InvalidRequestException, DataNotValidException {
		Cart shoppingCart = null;
		if (orderProduct != null) {	
			logger.info("Update quantity of product: {}", orderProduct.getProduct().getProductName());
			if(orderProduct.getQuantity()< 0){
				logger.error("Product Quantity cannot be negative: {}", orderProduct.getQuantity());
				throw new DataNotValidException("Product Quantity cannot be negative");
			}
			shoppingCart = getProductsInCart(userId);
			if (shoppingCart != null && shoppingCart.getCartId() != 0) {
				List<CartDetail> cartDetailsInDB = shoppingCart.getCartDetails();
				Boolean found = false;
				for (CartDetail cartDetail : cartDetailsInDB) {
					if (cartDetail.getProduct().getProductId() == orderProduct.getProduct().getProductId()) {
						if (orderProduct.getQuantity() == 0) {
							logger.info("Delete product from cart as quantity is 0");
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
					logger.error("Product not found in cart.");
					throw new NotFoundException("Product not found in cart.");
				}
			} else {
				logger.error("Cart does not exist.");
				throw new NotFoundException("Cart does not exist.");
			}
		} else {
			logger.error("Invalid Request.");
			throw new InvalidRequestException("Invalid Request");
		}
		return getProductsInCart(userId);
	}

	@Override
	@Transactional
	public void removeProductsFromCart(Integer id, String userId)
			throws NotFoundException, InvalidRequestException {
		Cart shoppingCart = null;
		Boolean deleted = false;
		logger.info("Remove product {} from cart", id);
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
					logger.error("Product not found in cart.");
					throw new NotFoundException("Product not found in cart.");
				}
			}
		} else {
			logger.error("Invalid Request.");
			throw new InvalidRequestException("Invalid Request");
		}
	}

	@Override
	@Transactional
	public Cart addProductsToCart(OrderProduct orderProduct, String userId) throws NotFoundException {
		User user = userService.getUser(userId);
		if(user==null){
			user = userService.addUser(new User(userId, "password", true, "User Name", "ROLE_USER"));
			logger.info("Adding user: {}", user);
		}
		Cart shoppingCart = null;
		if (orderProduct != null) {
			shoppingCart = getProductsInCart(userId);
			if (shoppingCart != null && shoppingCart.getCartId() != 0) {
				addToCart(orderProduct, shoppingCart);
			} else {
				// Create new cart and add product if not present
				logger.info("Adding product: {} to new cart", orderProduct.getProduct().getProductName());
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
				logger.info("Update quantity: {} of product {}:", cartDetail.getQuantity(), orderProduct.getProduct().getProductName());
				cartDetailRepository.updateProductQuantity(cartDetail.getQuantity(), cartDetail.getCartDetailId());
				found = true;
			}
		}
		if (!found) {
			// add product to cart
			logger.info("Adding product: {} to cart", orderProduct.getProduct().getProductName());
			Product product = productService.getProductById(orderProduct.getProduct().getProductId());
			cartDetailRepository.save(new CartDetail(shoppingCart, product, orderProduct.getQuantity()));
		}

	}

	private Cart addShoppingCart(OrderProduct orderProduct, User user) throws NotFoundException {
		Cart shoppingCart;
		// create new cart and cart details
		shoppingCart = new Cart();
		shoppingCart.setUser(user);
		logger.info("Creating cart");
		shoppingCart = cartRepository.save(shoppingCart);
		Product product = productService.getProductById(orderProduct.getProduct().getProductId());
		logger.info("Adding product: {} to cart", product.getProductName());
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
		logger.info("Total due: {} for cart: {}", totalDue, cart.getCartId());
		
		return cart;
	}

	@Override
	public Cart deleteAllProductsInCart(String userId) throws InvalidRequestException {
		Cart shoppingCart = getProductsInCart(userId);
		if (shoppingCart != null && shoppingCart.getCartId() != 0) {
			List<CartDetail> cartDetailInDB = shoppingCart.getCartDetails();
			for (CartDetail cartDetail : cartDetailInDB) {
				logger.info("Deleting product: {} from cart", cartDetail.getProduct().getProductName());
				cartDetailRepository.deleteById(cartDetail.getCartDetailId());
			}
		} else {
			logger.error("Invalid Request.");
			throw new InvalidRequestException("Invalid Request");
		}
		return getProductsInCart(userId);
	}

}
