package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.CategoryRepository;
import dev.kayange.Multivendor.E.commerce.dao.MainCategoryRepository;
import dev.kayange.Multivendor.E.commerce.dto.request.CategoryRequest;
import dev.kayange.Multivendor.E.commerce.entity.category.MainCategory;
import dev.kayange.Multivendor.E.commerce.entity.category.Category;
import dev.kayange.Multivendor.E.commerce.enumeration.SystemUserRole;
import dev.kayange.Multivendor.E.commerce.exception.ActionNotPermittedException;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.exception.ResourceNotFoundException;
import dev.kayange.Multivendor.E.commerce.security.UserPrincipal;
import dev.kayange.Multivendor.E.commerce.service.CategoryService;
import dev.kayange.Multivendor.E.commerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final UserService userService;

    @Override
    public Category findByCategoryAndMainCategory(Long categoryId, Long mainCategoryId) {
        return categoryRepository.findCategoryByIdAndParentCategoryId(categoryId, mainCategoryId)
                .orElseThrow(()-> new ResourceNotFoundException("No category matching provided IDs"));
    }

    @Override
    public Boolean existsByCategoryAndMainCategory(Long categoryId, Long mainCategoryId) {
        return categoryRepository.existsByCategoryAndMainCategory(categoryId, mainCategoryId);
    }

    @Override
    public Long createMainCategory(CategoryRequest request) {
        var category = MainCategory.builder()
                .categoryDescription(request.categoryDescription())
                .categoryName(request.categoryName())
                .categoryIcon(request.categoryIcon())
                .active(false)
                .build();
       return saveMainCategory(category).getId();
    }

    @Override
    public MainCategory saveMainCategory(MainCategory category){
        return mainCategoryRepository.save(category);
    }

    @Override
    public Long createNewSubCategory(Long mainCategoryId, CategoryRequest request) {
        var mainCategory = findMainCategoryById(mainCategoryId);
        var subCategory = Category.builder()
                .categoryDescription(request.categoryDescription())
                .categoryName(request.categoryName())
                .categoryIcon(request.categoryIcon())
                .mainCategory(mainCategory)
                .build();
       return saveCategory(subCategory).getId();
    }

    @Override
    public void activateMainCategory(Long categoryId, UserPrincipal user) {
        var category = findMainCategoryById(categoryId);
        var hasAuthority = userService.hasRole(List.of(SystemUserRole.ADMIN.getRoleName(), SystemUserRole.SUPER_ADMIN.getRoleName()), user.getAuthorities());
        if(!hasAuthority) throw new ApiException("You are not authorized to activate a category");
        category.setActive(true);
        saveMainCategory(category);
    }

    @Override
    public Page<Category> searchCategories(String search, Pageable pageable) {
        return categoryRepository.search(search, pageable);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Page<MainCategory> findAllMainCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mainCategoryRepository.findAllByActive(pageable, true);
    }

    @Override
    public Page<Category> findAllCategoryByMainCategoryId(Long mainCategoryId,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findAllCategoryByMainCategoryId(mainCategoryId, pageable);
    }

    @Override
    public void updateCategory(Long categoryId, CategoryRequest categoryRequest) {
        Category category = findCategoryById(categoryId);
        category.setCategoryName(categoryRequest.categoryName());
        category.setCategoryDescription(categoryRequest.categoryDescription());
        category.setCategoryIcon(categoryRequest.categoryIcon());
        saveCategory(category);
    }

    @Override
    public void updateMainCategory(Long categoryId, CategoryRequest categoryRequest) {
        MainCategory mainCategory = findMainCategoryById(categoryId);
        mainCategory.setCategoryName(categoryRequest.categoryName());
        mainCategory.setCategoryIcon(categoryRequest.categoryIcon());
        mainCategory.setCategoryDescription(categoryRequest.categoryDescription());
        saveMainCategory(mainCategory);
    }

    @Override
    public void deleteMainCategory(Long categoryId, UserPrincipal user) {
        MainCategory category = findMainCategoryById(categoryId);
        var hasAuthority = userService.hasRole(List.of(SystemUserRole.ADMIN.getRoleName(), SystemUserRole.SUPER_ADMIN.getRoleName()), user.getAuthorities());
       if(!hasAuthority) throw new ApiException("You are not authorized to delete category");
       mainCategoryRepository.deleteById(category.getId());
    }

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public MainCategory findMainCategoryById(Long mainCategoryId) {
        return mainCategoryRepository.findById(mainCategoryId).orElseThrow(
                ()-> new ResourceNotFoundException("Category not found")
        );
    }
}
