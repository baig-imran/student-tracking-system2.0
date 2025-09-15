package com.sts.service.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.sts.entity.Roles;
import com.sts.repository.RoleRepository;
import com.sts.service.interfaces.RolesService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RolesService {

    private final RoleRepository roleRepository;

  
    @Override
    @Transactional
    public Set<Roles> getRolesByNames(Set<String> roleNames) {
        Set<Roles> roles = new HashSet<>();

        for (String roleName : roleNames) {
            Roles role = roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    Roles newRole = new Roles();
                    newRole.setName(roleName);
                    return roleRepository.save(newRole);
                });
            roles.add(role);
        }

        return roles;
    }
}
