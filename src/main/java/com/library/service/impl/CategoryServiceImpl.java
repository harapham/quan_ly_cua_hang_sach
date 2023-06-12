package com.library.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.library.model.Category;
import com.library.repository.CategoryRepository;
import com.library.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	
	@Autowired
	public CategoryRepository categoryRepository;
	@Override
	public List<Category> findAll() {
		// TODO Auto-generated method stub
		return categoryRepository.findAll();
	}

	@Override
	public Category save(Category category) {
		// TODO Auto-generated method stub
			Category categorySave = new Category(category.getName());
			return categoryRepository.save(categorySave);		
	}

	@Override
	public Category update(Category category) {
		// TODO Auto-generated method stub
		Category categoryUpdate = null;
		try {
			categoryUpdate = categoryRepository.findById(category.getId()).get();
			categoryUpdate.setName(category.getName());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return categoryRepository.save(categoryUpdate);
	}
	@Override
	public Category findById(Long id) {
		return categoryRepository.findById(id).get();
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		categoryRepository.deleteById(id);
		
	}

}
