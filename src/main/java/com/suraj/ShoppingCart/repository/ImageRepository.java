package com.suraj.ShoppingCart.repository;

import com.suraj.ShoppingCart.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
