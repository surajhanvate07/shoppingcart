package com.suraj.ShoppingCart.service.impl;

import com.suraj.ShoppingCart.dto.ProductDto;
import com.suraj.ShoppingCart.exceptions.ProductNotFoundException;
import com.suraj.ShoppingCart.model.Category;
import com.suraj.ShoppingCart.model.Product;
import com.suraj.ShoppingCart.repository.CategoryRepository;
import com.suraj.ShoppingCart.repository.ProductRepository;
import com.suraj.ShoppingCart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;

	@Override
	public Product addProduct(ProductDto productDto) {
		// 1. If category is not present in the database, then create a new category
		// 2. Save the product in the new category
		// 3. If category is present in the database, then use the existing category
		// 4. Save the product in the existing category
		Category category = Optional.ofNullable(categoryRepository.findByName(productDto.getCategory().getName()))
				.orElseGet(() -> categoryRepository.save(productDto.getCategory()));

		productDto.setCategory(category);

		Product product = createProduct(productDto, category);

		return productRepository.save(product);
	}

	private Product createProduct(ProductDto productDto, Category category) {
		return new Product(productDto.getName(),
				productDto.getBrand(),
				productDto.getPrice(),
				productDto.getInventory(),
				productDto.getDescription(),
				category);
	}

	@Override
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	@Override
	public Product getProductById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product Not Found with Id: " + id));
	}

	@Override
	public void deleteProductById(Long id) {
		productRepository.findById(id).ifPresentOrElse(productRepository::delete, () -> {
			throw new ProductNotFoundException("Product Not Found with Id: " + id);
		});
	}

	@Override
	public Product updateProductById(ProductDto productDto, Long productId) {
		return productRepository.findById(productId)
				.map(exisitingProduct -> updateProduct(exisitingProduct, productDto))
				.map(productRepository::save)
				.orElseThrow(() -> new ProductNotFoundException("Product Not Found with Id: " + productId));
	}

	private Product updateProduct(Product existingProduct, ProductDto productDto) {
		existingProduct.setName(productDto.getName());
		existingProduct.setBrand(productDto.getBrand());
		existingProduct.setPrice(productDto.getPrice());
		existingProduct.setInventory(productDto.getInventory());
		existingProduct.setDescription(productDto.getDescription());

		Category category = categoryRepository.findByName(productDto.getCategory().getName());
		existingProduct.setCategory(category);

		return existingProduct;
	}

	@Override
	public List<Product> getProductsByCategory(String category) {
		return productRepository.findByCategoryName(category);
	}

	@Override
	public List<Product> getProductsByBrand(String brand) {
		return productRepository.findByBrand(brand);
	}

	@Override
	public List<Product> getProductsByCategoryAndBrand(String category, String brand) {
		return productRepository.findByCategoryNameAndBrand(category, brand);
	}

	@Override
	public List<Product> getProductsByName(String name) {
		return productRepository.findByName(name);
	}

	@Override
	public List<Product> getProductsByBrandAndName(String brand, String name) {
		return productRepository.findByBrandAndName(brand, name);
	}

	@Override
	public Long countProductsByBrandAndName(String brand, String name) {
		return productRepository.countByBrandAndName(brand, name);
	}
}
