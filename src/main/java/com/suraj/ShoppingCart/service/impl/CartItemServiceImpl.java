package com.suraj.ShoppingCart.service.impl;

import com.suraj.ShoppingCart.exceptions.ProductNotFoundException;
import com.suraj.ShoppingCart.model.Cart;
import com.suraj.ShoppingCart.model.CartItem;
import com.suraj.ShoppingCart.model.Product;
import com.suraj.ShoppingCart.repository.CartItemRepository;
import com.suraj.ShoppingCart.repository.CartRepository;
import com.suraj.ShoppingCart.service.CartItemService;
import com.suraj.ShoppingCart.service.CartService;
import com.suraj.ShoppingCart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class CartItemServiceImpl implements CartItemService {

	private final CartItemRepository cartItemRepository;
	private final CartRepository cartRepository;
	private final ProductService productService;
	private final CartService cartService;

	@Override
	@Transactional
	public void addItemToCart(Long cartId, Long productId, int quantity) {
		// 1. Get the cart by cartId
		// 2. Get the product by productId
		// 3. Check if the product is already in the cart
		// 4. If it is, update the quantity
		// 5. If it is not, create a new CartItem and add it to the cart
		// 6. Save the cart item to the database

		Cart cart = cartService.getCart(cartId);
		Product product = productService.getProductById(productId);
		CartItem cartItem = cart.getCartItems()
				.stream()
				.filter(item -> item.getProduct().getId().equals(productId))
				.findFirst().orElse(new CartItem());

		if (cartItem.getId() == null) {
			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);
			cartItem.setUnitPrice(product.getPrice());
		} else {
			cartItem.setQuantity(cartItem.getQuantity() + quantity);
		}
		cartItem.setTotalPrice();
		cart.addItem(cartItem);
		cartItemRepository.save(cartItem);
		cartRepository.save(cart);
	}

	@Override
	public void removeItemFromCart(Long cartId, Long productId) {
		Cart cart = cartService.getCart(cartId);
		CartItem cartItemToRemove = getCartItem(cartId, productId);
		cart.removeItem(cartItemToRemove);
		cartRepository.save(cart);
	}

	@Override
	public void updateItemQuantity(Long cartId, Long productId, int quantity) {
		Cart cart = cartService.getCart(cartId);
		cart.getCartItems().stream()
				.filter(cartItem -> cartItem.getProduct().getId().equals(productId))
				.findFirst()
				.ifPresent(cartItem -> {
					cartItem.setQuantity(quantity);
					cartItem.setUnitPrice(cartItem.getProduct().getPrice());
					cartItem.setTotalPrice();
				});
		BigDecimal totalAmount = cart.getCartItems().stream().map(CartItem::getTotalPrice)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		cart.setTotalAmount(totalAmount);
		cartRepository.save(cart);
	}

	@Override
	public CartItem getCartItem(Long cartId, Long productId) {
		Cart cart = cartService.getCart(cartId);
		return cart.getCartItems()
				.stream()
				.filter(cartItem -> cartItem.getProduct().getId().equals(productId))
				.findFirst()
				.orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));

	}
}
