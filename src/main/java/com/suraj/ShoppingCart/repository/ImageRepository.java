package com.suraj.ShoppingCart.repository;

import com.suraj.ShoppingCart.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
	List<Image> findByProductId(Long id);
}
