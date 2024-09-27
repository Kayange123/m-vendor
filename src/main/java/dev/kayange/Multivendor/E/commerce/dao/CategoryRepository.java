package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.category.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("""
    SELECT c
    FROM Category c
    WHERE c.id = :categoryId
    AND c.mainCategory.id = :parentCategory
    AND c.mainCategory.active = true
    """)
    Optional<Category> findCategoryByIdAndParentCategoryId(Long categoryId, Long parentCategory);

    @Query("""
        FROM Category c
        WHERE (
        lower(c.categoryName) LIKE %:search% OR
        lower(c.categoryDescription) LIKE %:search%)
        AND c.mainCategory.active = true
        ORDER BY c.categoryName ASC
    """)
    Page<Category> search(String search, Pageable pageable);

    @Query("""
        FROM Category c
        WHERE c.mainCategory.id = :mainCategoryId
        AND c.mainCategory.active = true
        ORDER BY c.categoryName ASC
    """)
    Page<Category> findAllCategoryByMainCategoryId(Long mainCategoryId, Pageable pageable);

    @Query("""
        SELECT count(*) FROM Category c
        WHERE c.id = :categoryId
        AND c.mainCategory.id = :mainCategoryId
        AND c.mainCategory.active = true
    """)
    Boolean existsByCategoryAndMainCategory(Long categoryId, Long mainCategoryId);
}
