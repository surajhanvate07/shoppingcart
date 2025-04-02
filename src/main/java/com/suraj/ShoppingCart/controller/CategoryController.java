package com.suraj.ShoppingCart.controller;

import com.suraj.ShoppingCart.exceptions.AlreadyExistsException;
import com.suraj.ShoppingCart.exceptions.ResourceNotFoundException;
import com.suraj.ShoppingCart.model.Category;
import com.suraj.ShoppingCart.response.ApiResponse;
import com.suraj.ShoppingCart.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

	private final CategoryService categoryService;

	@GetMapping
	public ResponseEntity<ApiResponse> getAllCategories() {
		try {
			List<Category> categories = categoryService.getAllCategories();
			return ResponseEntity.status(OK).body(new ApiResponse("Categories fetched successfully", categories));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to fetch categories", INTERNAL_SERVER_ERROR));
		}
	}

	@PostMapping("/add")
	public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
		try {
			Category createdCategory = categoryService.createCategory(category);
			return new ResponseEntity<>(new ApiResponse("Category created successfully", createdCategory), OK);
		} catch (AlreadyExistsException | IllegalArgumentException e) {
			return ResponseEntity.status(CONFLICT).body(new ApiResponse(e.getMessage(), CONFLICT));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to create category", INTERNAL_SERVER_ERROR));
		}
	}

	@GetMapping("/category/{id}")
	public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id) {
		try {
			Category category = categoryService.getCategoryById(id);
			return ResponseEntity.ok(new ApiResponse("Category fetched successfully", category));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to fetch", INTERNAL_SERVER_ERROR));
		}
	}

	@GetMapping("/category/{name}")
	public ResponseEntity<ApiResponse> getCategoryById(@PathVariable String name) {
		try {
			Category category = categoryService.getCategoryByName(name);
			return ResponseEntity.ok(new ApiResponse("Category fetched successfully", category));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to fetch", INTERNAL_SERVER_ERROR));
		}
	}

	@DeleteMapping("/category/{id}/delete")
	public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id) {
		try {
			categoryService.deteteCategoryById(id);
			return ResponseEntity.ok(new ApiResponse("Category deleted successfully", null));
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to delete category", INTERNAL_SERVER_ERROR));
		}
	}

	@PutMapping("/category/{id}/update")
	public ResponseEntity<ApiResponse> updateCategory(@PathVariable Long id, @RequestBody Category category) {
		try {
			Category updatedCategory = categoryService.updateCategory(id, category);
			return ResponseEntity.ok(new ApiResponse("Category updated successfully", updatedCategory));
		} catch (ResourceNotFoundException | IllegalArgumentException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to update category", INTERNAL_SERVER_ERROR));
		}
	}
}
