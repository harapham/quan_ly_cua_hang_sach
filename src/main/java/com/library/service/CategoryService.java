package com.library.service;

import java.util.List;

import com.library.model.Category;


public interface CategoryService {

	void deleteById(Long id);

	Category findById(Long id);

	Category update(Category category);

	Category save(Category category);

	List<Category> findAll();

	
	
}
