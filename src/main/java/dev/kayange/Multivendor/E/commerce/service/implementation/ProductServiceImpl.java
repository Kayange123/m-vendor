package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.ProductAttributeRepository;
import dev.kayange.Multivendor.E.commerce.dao.ProductRepository;
import dev.kayange.Multivendor.E.commerce.dao.ProductSkuRepository;
import dev.kayange.Multivendor.E.commerce.dto.ProductResponse;
import dev.kayange.Multivendor.E.commerce.dto.request.Attribute;
import dev.kayange.Multivendor.E.commerce.dto.request.ProductRequest;
import dev.kayange.Multivendor.E.commerce.entity.category.Category;
import dev.kayange.Multivendor.E.commerce.entity.notification.Notification;
import dev.kayange.Multivendor.E.commerce.entity.product.Product;
import dev.kayange.Multivendor.E.commerce.entity.product.ProductAttribute;
import dev.kayange.Multivendor.E.commerce.entity.product.ProductSku;
import dev.kayange.Multivendor.E.commerce.entity.users.Customer;
import dev.kayange.Multivendor.E.commerce.enumeration.NotificationType;
import dev.kayange.Multivendor.E.commerce.enumeration.SystemUserRole;
import dev.kayange.Multivendor.E.commerce.events.NotificationEvent;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.exception.ResourceNotFoundException;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import dev.kayange.Multivendor.E.commerce.service.CategoryService;
import dev.kayange.Multivendor.E.commerce.service.NotificationService;
import dev.kayange.Multivendor.E.commerce.service.ProductService;
import dev.kayange.Multivendor.E.commerce.service.UserService;
import dev.kayange.Multivendor.E.commerce.utils.EntityMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static dev.kayange.Multivendor.E.commerce.utils.RandomUtil.generatePublicId;
import static dev.kayange.Multivendor.E.commerce.utils.Utils.generateProductSku;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    public static final String message = "Congratulations! Your product has been successfully verified. It is now made available to customers";

    private final ProductRepository productRepository;
    private final ProductSkuRepository productSkuRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final CategoryService categoryService;
    private final NotificationService notificationService;
    private final ApplicationEventPublisher publisher;
    private final UserService userService;

    @Override
    @Transactional
    public void saveProduct(ProductRequest request, UserPrincipal user) {
        var roles = List.of( SystemUserRole.VENDOR.getRoleName());

        if(!userService.hasRole(roles, user.getAuthorities())){
            throw new ApiException("You should be vendor to create a product");
        }

        var category = categoryService.findByCategoryAndMainCategory(request.getSubCategoryId(), request.getCategoryId());
        var product = EntityMapper.convertToProduct(request);
        product.setOnSale(true);
        product.setIsTaxable(true);
        product.setPromoted(false);
        product.setPublishable(false);
        product.setPublicId(generatePublicId());
        product.setFeatured(false);
        product.setCategory(category);
        product.setVendor(user.getVendor());

        Product savedProduct = productRepository.save(product);

        var sku = ProductSku.builder()
                .sku(generateProductSku(request.getProductName()))
                .product(savedProduct)
                .price(BigDecimal.valueOf(request.getBasePrice()))
                .discountPrice(BigDecimal.ZERO)
                .quantity(request.getNumberOfProducts())
                .build();
        ProductSku savedSku = productSkuRepository.save(sku);

        for(Attribute attribute : request.getAttributes() ){
            var attr = ProductAttribute.builder()
                    .productSku(savedSku)
                    .name(attribute.name())
                    .value(attribute.value())
                    .build();
            productAttributeRepository.save(attr);
        }
    }

    @Override
    public Page<Product> findAllProducts(String sortBy, String query, int page, int size) {
        String sort = sortBy;
        Page<Product> products;
        if(!sortBy.equals("id")){sort = "createdAt";}
        if (query.equals("_") || query.isEmpty()) {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sort).ascending());
            products = productRepository.findAllProducts(pageable);
        } else {
            Pageable pageable = PageRequest.of(0, size, Sort.by(sort).ascending());
            products = productRepository.findAllProductsByName(query.toLowerCase(), pageable);
        }
        return products;
    }

    @Override
    public Page<Product> findProductsByCategory(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Category category = categoryService.findCategoryById(categoryId);
        return productRepository.findProductsByCategory(category.getId(), pageable);
    }

    @Override
    public void editProduct(Long id, ProductRequest request, UserPrincipal user) {
        var roles = List.of(SystemUserRole.ADMIN.getRoleName(), SystemUserRole.VENDOR.getRoleName(), SystemUserRole.SUPER_ADMIN.getRoleName());

        if(!userService.hasRole(roles, user.getAuthorities())){
            throw new ApiException("You are not authorized to Edit a product");
        }
        var vendor = user.getVendor();
        if(vendor == null) throw new ApiException("You are not a vendor yet");

        Product product = findProductById(id);
        if(!Objects.equals(product.getVendor().getId(), vendor.getId())) throw new ApiException("You are not the owner of this product");
        if(Objects.nonNull(request.getProductName())){
            product.setProductName(request.getProductName());
        }

        if(Objects.nonNull(request.getProductDescription())) product.setProductDescription(request.getProductDescription());
        productRepository.save(product);
    }

    @Override
    public void verifyProduct(Long id, UserPrincipal user) {
        var roles = List.of(SystemUserRole.ADMIN.getRoleName(), SystemUserRole.SUPER_ADMIN.getRoleName());
        var hasAuthority = userService.hasRole(roles, user.getAuthorities());
        if(!hasAuthority) throw new ApiException("You are not authorized to perform this action");
        Product product = findProductById(id);
        product.setPublishable(true);
        product.setOnSale(true);
        productRepository.save(product);
        var vendor = product.getVendor();
        if(vendor == null) throw new ApiException("Vendor for this product is unknown");

        Notification note = Notification.builder()
                .read(false)
                .notificationExpiryDate(null)
                .type(NotificationType.ALERTS)
                .recipient(vendor.getCustomer())
                .message(message)
                .build();

        Notification notification = notificationService.createNotification(note);
        publisher.publishEvent(NotificationEvent.builder().id(notification.getId()).build());
    }

    @Override
    public void deactivateProduct(Long id, UserPrincipal user) {
        var roles = List.of(SystemUserRole.ADMIN.getRoleName(), SystemUserRole.SUPER_ADMIN.getRoleName());
        var hasAuthority = userService.hasRole(roles, user.getAuthorities());
        if(!hasAuthority) throw new ApiException("You are not authorized to perform this action");
        Product product = findProductById(id);
        product.setPublishable(false);
        product.setOnSale(false);
        productRepository.save(product);
    }

    @Override
    public ProductResponse getProductById(Long id) {
        var product = findProductById(id);
        return ProductResponse.create(product);
    }

    @Override
    public Page<Product> findProductsByHost(String vendorId, int page, int size) {
        Customer customer = userService.findFirstByUserId(vendorId);
        var vendor = customer.getVendor();
        if(vendor == null) throw new ResourceNotFoundException("Vendor NOT found");
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findProductsByHost(vendor.getId(), pageable);
    }

    @Override
    public Page<Product> findFeaturedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findFeaturedProducts(pageable);

    }

    @Override
    public Page<Product> findAllPromotedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findPromotedProducts(pageable);

    }

    @Override
    public Page<Product> findAllFeaturedPromotedProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findFeaturedPromotedProducts(pageable);
    }

    @Override
    public Product findProductByPublicId(String publicId) {
        return productRepository.findFirstByPublicId(publicId).orElseThrow(()-> new ApiException("Can not find this product"));
    }

    @Override
    public ProductSku findSkuByProductId(Long productId) {
        return productSkuRepository.findByProductId(productId).orElseThrow(()-> new ResourceNotFoundException("No product Sku found"));
    }

    @Override
    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(
                ()-> new ApiException("No such product found with id " + id)
        );
    }
}