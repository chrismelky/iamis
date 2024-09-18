package tz.go.zanemr.auth.modules.menu_item;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tz.go.zanemr.auth.modules.core.BaseRepository;

import java.util.List;
import java.util.Set;

public interface MenuItemRepository extends BaseRepository<MenuItem, Long> {

    @Query(
            value =
                    "select distinct mi.* from menu_items mi join menu_item_authorities ma  on"
                            + " ma.menu_item_id = mi.id where ma.authority_id in (:authorities) and"
                            + " mi.menu_group_id is not null ",
            nativeQuery = true)
    Set<MenuItem> findByAuthorities(@Param("authorities") List<Long> authorities);

    @Query(
            value =
                    "select distinct mi.* from menu_items mi join menu_item_authorities ma  on"
                            + " ma.menu_item_id = mi.id where ma.authority_id in (:authorities) and"
                            + " mi.menu_group_id is null",
            nativeQuery = true)
    Set<MenuItem> findWithNoGroupByAuthorities(@Param("authorities") List<Long> authorities);

    @Query(
            "Select distinct  i from MenuItem  i where i.menuGroup.id=:id and i.id in :itemIds"
                    + " order by i.sortOrder")
    List<MenuItem> byGroupAndIds(@Param("id") Long id, @Param("itemIds") List<Long> itemIds);
}