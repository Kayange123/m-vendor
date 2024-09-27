package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.category.MainCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {

    Page<MainCategory> findAllByActive(Pageable pageable, boolean active);
}
