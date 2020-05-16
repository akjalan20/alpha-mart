package com.alphamart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alphamart.model.CartDetail;
import com.alphamart.model.CartDetailId;

@Repository
public interface CartDetailsRepository extends JpaRepository<CartDetail, CartDetailId> {

	Optional<CartDetail> findById(CartDetailId id);
	
	List<CartDetail> findAll();
	
	CartDetail save(CartDetail cartDetail);
	
	void deleteById(CartDetailId id);
	
	
	@Modifying/*(flushAutomatically = true)*/
	@Query("update CartDetail cd set cd.quantity = :quantity where cd.cartDetailId = :cartDetailId")
	void updateProductQuantity(@Param("quantity") int quantity, @Param("cartDetailId") CartDetailId cartDetailId);
	
}