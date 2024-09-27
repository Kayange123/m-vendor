package dev.kayange.Multivendor.E.commerce.service;

import dev.kayange.Multivendor.E.commerce.dto.request.RoleRequest;
import dev.kayange.Multivendor.E.commerce.entity.users.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role findFirstByName(String roleName);
    Optional<Role> findByName(String name);
    Optional<Role> findById(Long id);
    List<Role> findAllByName(String name);
    List<Role> findAll();
    Page<Role> findAll(Pageable pageable);
    Page<Role> findAll(String query, Pageable pageable);
    void delete(Long id);
    Role save( Role role);

    List<Role> findAllExcept(List<Long> exceptIds);
    Page<Role> findAllExcept(List<Long> exceptIds, Pageable pageable);
    List<Role> findAllExcept(String name, List<Long> exceptIds);
    Page<Role> findAllExcept(String name, List<Long> exceptIds, Pageable pageable);

    void saveRole(RoleRequest roleRequest);
}
