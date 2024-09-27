package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.dto.request.CategoryRequest;
import dev.kayange.Multivendor.E.commerce.entity.category.MainCategory;
import dev.kayange.Multivendor.E.commerce.entity.category.Category;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Category findByCategoryAndMainCategory(Long categoryId, Long mainCategoryId);
    Boolean existsByCategoryAndMainCategory(Long categoryId, Long mainCategoryId);
    Category findCategoryById(Long categoryId);
    MainCategory findMainCategoryById(Long mainCategoryId);
    MainCategory saveMainCategory(MainCategory category);
    Long createMainCategory(CategoryRequest request);
    Long createNewSubCategory(Long categoryId, CategoryRequest request);
    void activateMainCategory(Long categoryId, UserPrincipal user);
    Page<Category> searchCategories(String search, Pageable pageable);
    Category saveCategory(Category category);
    Page<MainCategory> findAllMainCategories(int page, int size);
    Page<Category> findAllCategoryByMainCategoryId(Long mainCategoryId, int page, int size);
    void updateCategory(Long categoryId, CategoryRequest categoryRequest);

    void updateMainCategory(Long categoryId, CategoryRequest categoryRequest);

    void deleteMainCategory(Long categoryId, UserPrincipal user);
}
