package com.suraj.ShoppingCart.service.impl;

import com.suraj.ShoppingCart.dto.ImageDto;
import com.suraj.ShoppingCart.exceptions.ResourceNotFoundException;
import com.suraj.ShoppingCart.model.Image;
import com.suraj.ShoppingCart.model.Product;
import com.suraj.ShoppingCart.repository.ImageRepository;
import com.suraj.ShoppingCart.service.ImageService;
import com.suraj.ShoppingCart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

	private final ImageRepository imageRepository;
	private final ProductService productService;

	@Override
	public Image getImageById(Long id) {
		return imageRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + id));
	}

	@Override
	public void deleteImageById(Long id) {
		imageRepository.findById(id).ifPresentOrElse(imageRepository::delete, () -> {
			throw new ResourceNotFoundException("Image not found with id: " + id);
		});
	}

	@Override
	public List<ImageDto> saveImages(Long productId, List<MultipartFile> files) {
		Product product = productService.getProductById(productId);
		List<ImageDto> savedImageDto = new ArrayList<>();
		for (MultipartFile file : files) {
			try {
				Image image = new Image();
				image.setFileName(file.getOriginalFilename());
				image.setFileType(file.getContentType());
				image.setImage(new SerialBlob(file.getBytes()));
				image.setProduct(product);

				String buildDownloadUrl = "/api/v1/images/image/download/";
				String downloadUrl = buildDownloadUrl + image.getId();
				image.setDownloadUrl(downloadUrl);

				Image savedImage = imageRepository.save(image);
				savedImage.setDownloadUrl(buildDownloadUrl + savedImage.getId());
				imageRepository.save(savedImage);

				ImageDto imageDto = new ImageDto();
				imageDto.setId(savedImage.getId());
				imageDto.setFileName(savedImage.getFileName());
				imageDto.setDownloadUrl(savedImage.getDownloadUrl());
				savedImageDto.add(imageDto);

			} catch (IOException | SQLException e) {
				throw new RuntimeException(e.getMessage());
			}
		}
		return savedImageDto;
	}

	@Override
	public void updateImage(MultipartFile file, Long imageId) {
		Image image = getImageById(imageId);
		try {
			image.setFileName(file.getOriginalFilename());
			image.setFileType(file.getContentType());
			image.setImage(new SerialBlob(file.getBytes()));
		} catch (IOException | SQLException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
