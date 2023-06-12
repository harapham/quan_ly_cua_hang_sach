package com.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.model.Product;
import com.library.model.Rate;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long>{
	@Query(value = "select r from Rate r inner join Product p on p.id = r.product.id where p.id = ?1")
	List<Rate> findByProduct(Long productId);
}
