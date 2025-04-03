package com.suraj.ShoppingCart.service;

import com.suraj.ShoppingCart.dto.ImageDto;
import com.suraj.ShoppingCart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
	Image getImageById(Long id);
	void deleteImageById(Long id);
	List<ImageDto> saveImages(Long productId, List<MultipartFile> files);
	void updateImage(MultipartFile file, Long imageId);

}
