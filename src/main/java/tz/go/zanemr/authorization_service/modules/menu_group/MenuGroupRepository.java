package tz.go.zanemr.authorization_service.modules.menu_group;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tz.go.zanemr.authorization_service.modules.core.BaseRepository;

import java.util.List;

public interface MenuGroupRepository extends BaseRepository<MenuGroup, Long> {

    @Query(
            "select distinct g from MenuGroup g where g.id in :ids order by g.sortOrder")
    List<MenuGroup> byIds(@Param("ids") List<Long> ids);
}