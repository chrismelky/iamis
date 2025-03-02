package com.deeptech.iamis.modules.role;

import com.deeptech.iamis.core.BaseCrudService;

public interface RoleService extends BaseCrudService<RoleDto, Role> {

    RoleDto assignAuthorities(RoleAuthoritiesDto roleAuthoritiesDto);

}
