package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.ImageGalleryRepository;
import dev.kayange.Multivendor.E.commerce.entity.product.ImageGallery;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.service.ImageGalleryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageGalleryServiceImpl implements ImageGalleryService {
    private final ImageGalleryRepository imageGalleryRepository;


    @Override
    public void saveImage(ImageGallery image) {
        imageGalleryRepository.saveAndFlush(image);
    }

    @Override
    public void saveImage(List<ImageGallery> images) {
        for (ImageGallery gallery : images) {
            imageGalleryRepository.saveAndFlush(gallery);
        }
    }

    @Override
    public void deleteImage(ImageGallery image) {
        imageGalleryRepository.delete(image);
    }

    @Override
    public void deleteImage(Long imageId) {
        ImageGallery image = findByImageId(imageId);
        imageGalleryRepository.deleteById(image.getId());
    }

    private ImageGallery findByImageId(Long imageId) {
        return imageGalleryRepository.findById(imageId).orElseThrow(
                ()-> new ApiException("No Image found with id " + imageId)
        );
    }
}
