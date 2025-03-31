package com.suraj.ShoppingCart.service;

import com.suraj.ShoppingCart.model.Category;

import java.util.List;

public interface CategoryService {
	Category getCategoryById(Long id);
	Category getCategoryByName(String name);
	List<Category> getAllCategories();
	Category createCategory(Category category);
	Category updateCategory(Long id, Category category);
	void deteteCategoryById(Long id);

}
