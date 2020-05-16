package com.alphamart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.alphamart.model.CartDetailId;
import com.alphamart.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

	Optional<Product> findByProductId(Integer productId);
	
	Optional<Product> findById(Integer id);
	
	List<Product> findAll();
	
	Product save(Product product);

	Optional<List<Product>> findByProductNameContainingIgnoreCase(String prodName);

	@Query("select t from Product t where t.class = :prodType")
	Optional<List<Product>> findByProductType( @Param("prodType") String prodType);

}
