package com.mindfire.backend.service;

import com.mindfire.backend.entity.Role;

public interface RoleService {
    /**
     * retrives a Role object by name
     *
     * @param roleName takes the role name as input
     *
     * @return Role containing the id and role name
     */
    Role getRoleByName(String roleName);
}
