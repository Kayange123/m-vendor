package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.RoleRepository;
import dev.kayange.Multivendor.E.commerce.dto.request.RoleRequest;
import dev.kayange.Multivendor.E.commerce.entity.users.Role;
import dev.kayange.Multivendor.E.commerce.exception.ApiException;
import dev.kayange.Multivendor.E.commerce.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role findFirstByName(String roleName) {
        return roleRepository.findFirstByName(roleName).orElseThrow(() -> new ApiException("User Role not set."));
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findFirstByName(name);
    }

    @Override
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<Role> findAllByName(String name) {
        return roleRepository.findAllByName(name);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Page<Role> findAll(String query, Pageable pageable) {
        return roleRepository.findAllByName(query, pageable);
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role save( Role role) {
        return roleRepository.saveAndFlush(role);
    }

    @Override
    public List<Role> findAllExcept(List<Long> exceptIds) {
        return roleRepository.findAllExcept(exceptIds);
    }

    @Override
    public Page<Role> findAllExcept(List<Long> exceptIds, Pageable pageable) {
        return roleRepository.findAllExcept(exceptIds, pageable);
    }

    @Override
    public List<Role> findAllExcept(String name, List<Long> exceptIds) {
        return roleRepository.findAllExcept(name, exceptIds);
    }

    @Override
    public Page<Role> findAllExcept(String name, List<Long> exceptIds, Pageable pageable) {
        return roleRepository.findAllExcept(name, exceptIds, pageable);
    }

    @Override
    public void saveRole(RoleRequest roleRequest) {
        Optional<Role> optionalRole = findByName(roleRequest.name());
        if(optionalRole.isPresent())return;
        var role = Role.builder().name(roleRequest.name()).build();
        save(role);
    }
}
