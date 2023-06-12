package com.library.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import com.library.dto.ProductDto;
import com.library.model.Product;

public interface ProductService {

    
	Product getProductById(Long id);
	
	ProductDto getById(Long id);
	void deleteById(Long id);
	Product update(MultipartFile imageProduct, ProductDto productDto);
	Product save(MultipartFile imageProduct, ProductDto productDto);
	List<Product> findAll();
	List<Product> getProductsInCategory(Long categoryId);



   
}