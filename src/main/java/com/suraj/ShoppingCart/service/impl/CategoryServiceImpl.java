package com.suraj.ShoppingCart.service.impl;

import com.suraj.ShoppingCart.exceptions.AlreadyExistsException;
import com.suraj.ShoppingCart.exceptions.ResourceNotFoundException;
import com.suraj.ShoppingCart.model.Category;
import com.suraj.ShoppingCart.repository.CategoryRepository;
import com.suraj.ShoppingCart.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;

	@Override
	public Category getCategoryById(Long id) {
		return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
	}

	@Override
	public Category getCategoryByName(String name) {
		Category category = categoryRepository.findByName(name);
		if (category == null) {
			throw new ResourceNotFoundException("Category not found with name: " + name);
		}
		return category;
	}

	@Override
	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	@Override
	public Category createCategory(Category category) {
//		return Optional.ofNullable(category).filter(c -> !categoryRepository.existsByName(category.getName()))
//				.map(categoryRepository::save)
//				.orElseThrow(() -> new AlreadyExistsException("Category with name " + category.getName() + " already exists"));

		if (category == null) {
			throw new IllegalArgumentException("Category cannot be null");
		}
		if (category.getName() == null || category.getName().isEmpty()) {
			throw new IllegalArgumentException("Category name cannot be null or empty");
		}
		if(categoryRepository.existsByName(category.getName())) {
			throw new AlreadyExistsException("Category with name " + category.getName() + " already exists");
		}
		return categoryRepository.save(category);
	}

	@Override
	public Category updateCategory(Long id, Category category) {
		Category categoryToUpdate = categoryRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

		if (category.getName() != null && !category.getName().isEmpty()) {
			categoryToUpdate.setName(category.getName());
		}

		return categoryRepository.save(categoryToUpdate);
	}

	@Override
	public void deteteCategoryById(Long id) {
		categoryRepository.findById(id).ifPresentOrElse(categoryRepository::delete, () -> {
			throw new ResourceNotFoundException("Category not found with id: " + id);
		});
	}
}
