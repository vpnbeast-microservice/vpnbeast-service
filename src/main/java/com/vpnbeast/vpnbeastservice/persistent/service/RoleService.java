package com.vpnbeast.vpnbeastservice.persistent.service;

import com.vpnbeast.vpnbeastservice.model.enums.RoleType;
import com.vpnbeast.vpnbeastservice.persistent.entity.Role;
import java.util.List;

public interface RoleService extends CRUDService<Role> {

    void insert(Role entity);
    List<Role> getRoleListByRoleTypes(RoleType ... type);

}