package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.entity.users.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findFirstByName(String name);

    @Query("FROM Role f where lower(f.name) LIKE %:query%")
    List<Role> findAllByName(@Param("query") String query);

    @Query("FROM Role f where f.id not in (:exceptIds)")
    List<Role> findAllExcept(@Param("exceptIds") List<Long> exceptIds);

    @Query("FROM Role f where f.id not in (:exceptIds)")
    Page<Role> findAllExcept(@Param("exceptIds") List<Long> exceptIds, Pageable pageable);

    @Query("FROM Role f where lower(f.name) LIKE %:query% and f.id not in (:exceptIds)")
    List<Role> findAllExcept(@Param("query") String query, @Param("exceptIds") List<Long> exceptIds);

    @Query("FROM Role f where lower(f.name) LIKE %:query% and f.id not in (:exceptIds)")
    Page<Role> findAllExcept(@Param("query") String query, @Param("exceptIds") List<Long> exceptIds, Pageable pageable);

    @Query("FROM Role f where lower(f.name) LIKE %:query%")
    Page<Role> findAllByName(@Param("query") String query, Pageable pageable);

    @Query(value = "Select role_id from user_roles where user_id=:userId", nativeQuery = true)
    List<Long> findRoleIdsByUser(@Param("userId") Long userId);
}
