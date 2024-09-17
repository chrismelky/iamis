package tz.go.zanemr.authorization_service.modules.role;

import tz.go.zanemr.authorization_service.modules.core.BaseCrudService;

public interface RoleService extends BaseCrudService<RoleDto, Role> {

    RoleDto assignAuthorities(RoleAuthorities roleAuthorities);

}
