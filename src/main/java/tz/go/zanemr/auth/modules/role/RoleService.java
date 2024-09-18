package tz.go.zanemr.auth.modules.role;

import tz.go.zanemr.auth.modules.core.BaseCrudService;

public interface RoleService extends BaseCrudService<RoleDto, Role> {

    RoleDto assignAuthorities(RoleAuthorities roleAuthorities);

}
