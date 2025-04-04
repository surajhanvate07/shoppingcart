package com.suraj.ShoppingCart.controller;

import com.suraj.ShoppingCart.dto.ProductDto;
import com.suraj.ShoppingCart.exceptions.ProductNotFoundException;
import com.suraj.ShoppingCart.model.Product;
import com.suraj.ShoppingCart.response.ApiResponse;
import com.suraj.ShoppingCart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public ResponseEntity<ApiResponse> getAllProducts() {
		try {
			List<Product> products = productService.getAllProducts();
			if (products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found", products));
			}
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Products fetched successfully", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to fetch products", INTERNAL_SERVER_ERROR));
		}
	}

	@GetMapping("/product/{productId}")
	public ResponseEntity<ApiResponse> getProductById(@PathVariable("productId") Long id) {
		try {
			Product product = productService.getProductById(id);
			ProductDto productDto = productService.convertToDto(product);
			return ResponseEntity.ok(new ApiResponse("Product fetched successfully", productDto));
		} catch (ProductNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to fetch products", INTERNAL_SERVER_ERROR));
		}
	}

	@PostMapping("/product/add")
	public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto productDto) {
		try {
			Product product = productService.addProduct(productDto);
			ProductDto productDto1 = productService.convertToDto(product);
			return ResponseEntity.ok(new ApiResponse("Product added successfully", productDto1));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to add product", INTERNAL_SERVER_ERROR));
		}
	}

	@PutMapping("/product/{productId}/update")
	public ResponseEntity<ApiResponse> updateProduct(@RequestBody ProductDto productDto, @PathVariable("productId") Long productId) {
		try {
			Product updatedProduct = productService.updateProductById(productDto, productId);
			ProductDto updatedProductDto = productService.convertToDto(updatedProduct);
			return ResponseEntity.ok(new ApiResponse("Product updated successfully", updatedProductDto));
		} catch (ProductNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to update product", INTERNAL_SERVER_ERROR));
		}
	}

	@DeleteMapping("/product/{productId}/delete")
	public ResponseEntity<ApiResponse> deleteProduct(@PathVariable("productId") Long id) {
		try {
			productService.deleteProductById(id);
			return ResponseEntity.status(OK).body(new ApiResponse("Product deleted successfully", OK));
		} catch (ProductNotFoundException e) {
			return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), NOT_FOUND));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to delete product", INTERNAL_SERVER_ERROR));
		}
	}

	@GetMapping("/product/by-category/{category}")
	public ResponseEntity<ApiResponse> getProductsByCategory(@PathVariable("category") String category) {
		try {
			List<Product> products = productService.getProductsByCategory(category);
			if (products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found in this category", NOT_FOUND));
			}
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Products fetched successfully", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to fetch products", INTERNAL_SERVER_ERROR));
		}
	}

	@GetMapping("/product/by-brand")
	public ResponseEntity<ApiResponse> getProductsByBrand(@RequestParam String brand) {
		try {
			List<Product> products = productService.getProductsByBrand(brand);
			if (products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found for this brand", NOT_FOUND));
			}
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Products fetched successfully", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to fetch products", INTERNAL_SERVER_ERROR));
		}
	}

	@GetMapping("/product/by/category-and-brand")
	public ResponseEntity<ApiResponse> getProductsByCategoryAndBrand(@RequestParam String category, @RequestParam String brand) {
		try {
			List<Product> products = productService.getProductsByCategoryAndBrand(category, brand);
			if (products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found for this category and brand", NOT_FOUND));
			}
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Products fetched successfully", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to fetch products", INTERNAL_SERVER_ERROR));
		}
	}

	@GetMapping("/product/by-name")
	public ResponseEntity<ApiResponse> getProductsByName(@RequestParam String name) {
		try {
			List<Product> products = productService.getProductsByName(name);
			if (products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found with this name", NOT_FOUND));
			}
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Products fetched successfully", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to fetch products", INTERNAL_SERVER_ERROR));
		}
	}

	@GetMapping("/product/by/brand-and-name")
	public ResponseEntity<ApiResponse> getProductsByBrandAndName(@RequestParam String brand, @RequestParam String name) {
		try {
			List<Product> products = productService.getProductsByBrandAndName(brand, name);
			if (products.isEmpty()) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found for this brand and name", NOT_FOUND));
			}
			List<ProductDto> convertedProducts = productService.getConvertedProducts(products);
			return ResponseEntity.ok(new ApiResponse("Products fetched successfully", convertedProducts));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to fetch products", INTERNAL_SERVER_ERROR));
		}
	}

	@GetMapping("/product/count/{brand}/{name}")
	public ResponseEntity<ApiResponse> countProductsByBrandAndName(@PathVariable("brand") String brand, @PathVariable("name") String name) {
		try {
			Long count = productService.countProductsByBrandAndName(brand, name);
			if (count == 0) {
				return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("No products found for this brand and name", NOT_FOUND));
			}
			return ResponseEntity.ok(new ApiResponse("Product count fetched successfully", count));
		} catch (Exception e) {
			return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Failed to fetch product count", INTERNAL_SERVER_ERROR));
		}
	}
}
