package com.library.repository;

import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.model.Category;
import com.library.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{

    @Query(value = "select p from Product p inner join Category c on c.id = p.category.id where c.id = ?1")
    List<Product> getProductsInCategory(Long categoryId);

}
