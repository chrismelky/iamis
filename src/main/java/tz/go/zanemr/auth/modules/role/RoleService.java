package tz.go.zanemr.auth.modules.role;

import tz.go.zanemr.auth.core.BaseCrudService;

public interface RoleService extends BaseCrudService<RoleDto, Role> {

    RoleDto assignAuthorities(RoleAuthoritiesDto roleAuthoritiesDto);

}
