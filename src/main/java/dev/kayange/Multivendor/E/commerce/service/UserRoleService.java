package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.dto.RoleUserDto;
import dev.kayange.Multivendor.E.commerce.entity.users.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserRoleService {
    List<UserRole> findAllByUserId(Long userId);

    Boolean exists(Long userId, Long roleId);

    List<String> userRoles(Long userId);

    Page<UserRole> findAllByUserId(Long userId, Pageable pageable);

    UserRole save(UserRole userRole);

    void delete(Long id);

    Optional<UserRole> findById(Long id);

    Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId);
}
