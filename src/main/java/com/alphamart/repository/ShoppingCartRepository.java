package com.alphamart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.alphamart.model.Cart;
import com.alphamart.model.User;

@Repository
public interface ShoppingCartRepository extends JpaRepository<Cart, Integer> {

	Optional<Cart> findById(Integer id);
	
	List<Cart> findAll();
	
	Cart save(Cart cart);

	Cart findByUser(User user);
	

}
