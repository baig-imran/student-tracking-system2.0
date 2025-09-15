package com.sts.service.interfaces;

import java.util.Set;

import com.sts.entity.Roles;

public interface RolesService {
    Set<Roles> getRolesByNames(Set<String> roleNames);
}