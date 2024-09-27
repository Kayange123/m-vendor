package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.dto.ProductResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.ProductRequest;
import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import dev.kayange.Multivendor.E.commerce.entity.product.ProductSku;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import org.springframework.data.domain.Page;


public interface ProductService {
    void saveProduct(ProductRequest request, UserPrincipal user);

    Page<Product> findAllProducts(String sortBy, String query, int page, int size);

    Page<Product> findProductsByCategory(Long categoryId, int page, int size);

    void editProduct(Long id, ProductRequest productRequest, UserPrincipal user);

    void verifyProduct(Long id, UserPrincipal user);

    void deactivateProduct(Long id, UserPrincipal user);

    ProductResponse getProductById(Long id);

    Product findProductById(Long id);

    Page<Product> findProductsByHost(String vendorId, int page, int size);

    Page<Product> findFeaturedProducts(int page, int size);

    Page<Product> findAllPromotedProducts(int page, int size);

    Page<Product> findAllFeaturedPromotedProducts(int page, int size);

    Product findProductByPublicId(String publicId);

    ProductSku findSkuByProductId(Long productId);
}
