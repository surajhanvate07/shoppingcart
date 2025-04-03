package com.suraj.ShoppingCart.service;

import com.suraj.ShoppingCart.dto.ProductDto;
import com.suraj.ShoppingCart.model.Product;

import java.util.List;

public interface ProductService {
	Product addProduct(ProductDto productDto);
	List<Product> getAllProducts();
	Product getProductById(Long id);
	void deleteProductById(Long id);
	Product updateProductById(ProductDto productDto, Long productId);
	List<Product> getProductsByCategory(String category);
	List<Product> getProductsByBrand(String brand);
	List<Product> getProductsByCategoryAndBrand(String category, String brand);
	List<Product> getProductsByName(String name);
	List<Product> getProductsByBrandAndName(String brand, String  name);
	Long countProductsByBrandAndName(String brand, String name);

	List<ProductDto> getConvertedProducts(List<Product> products);

	ProductDto convertToDto(Product product);
}
