package dev.kayange.Multivendor.E.commerce.service;


import dev.kayange.Multivendor.E.commerce.entity.product.ImageGallery;

import java.util.List;

public interface ImageGalleryService {
    void saveImage(ImageGallery image);
    void saveImage(List<ImageGallery> images);
    void deleteImage(ImageGallery image);
    void deleteImage(Long imageId);
}
