package dev.kayange.Multivendor.E.commerce.dao;

import dev.kayange.Multivendor.E.commerce.dto.RoleUserDto;
import dev.kayange.Multivendor.E.commerce.entity.users.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query("FROM UserRole ur WHERE ur.user.id =:userId")
    List<UserRole> findAllByUserId(@Param("userId") Long userId);

    Optional<UserRole> findByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int countAllByUserIdAndRoleId(@Param("userId") Long userId, @Param("roleId") Long roleId);

    @Query("SELECT ur.role.name FROM UserRole ur WHERE ur.user.id =:userId")
    List<String> userRoles(@Param("userId") Long userId);

    Page<UserRole> findAllByUserId(@Param("userId") Long userId, Pageable pageable);
}
