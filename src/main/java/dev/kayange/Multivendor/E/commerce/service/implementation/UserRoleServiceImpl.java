package dev.kayange.Multivendor.E.commerce.service.implementation;

import dev.kayange.Multivendor.E.commerce.dao.UserRoleRepository;
import dev.kayange.Multivendor.E.commerce.dto.RoleUserDto;
import dev.kayange.Multivendor.E.commerce.entity.users.UserRole;
import dev.kayange.Multivendor.E.commerce.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    @Override
    public List<UserRole> findAllByUserId(Long userId) {
        return userRoleRepository.findAllByUserId(userId);
    }

    @Override
    public Boolean exists(Long userId, Long roleId) {
        return userRoleRepository.countAllByUserIdAndRoleId(userId, roleId) == 1;
    }

    @Override
    public List<String> userRoles(Long userId) {
        List<String> userRoles = new ArrayList<>();
        var roles = findAllByUserId(userId);
        for (UserRole role : roles) {
            userRoles.add(role.getRole().getName());
        }
        return userRoles;
    }

    @Override
    public Page<UserRole> findAllByUserId(Long userId, Pageable pageable) {
        return userRoleRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public UserRole save(UserRole userRole) {
        return userRoleRepository.saveAndFlush(userRole);
    }

    @Override
    public void delete(Long id) {
        userRoleRepository.deleteById(id);
    }

    @Override
    public Optional<UserRole> findById(Long id) {
        return userRoleRepository.findById(id);
    }


    @Override
    public Optional<UserRole> findByUserIdAndRoleId(Long userId, Long roleId) {
        return userRoleRepository.findByUserIdAndRoleId(userId, roleId);
    }
}
