package tz.go.zanemr.auth.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class SearchService<T> {

    public Specification<T> createSpecification(Class<T> entity, Map<String, Object> search) {
        Specification<T> specification = Specification.where(null);

        boolean isOr = search.containsKey("searchType") && search.get("searchType").equals("or");

        List<String> allowedProps =
                Arrays.stream(entity.getDeclaredFields()).map(Field::getName).toList();
        if (!search.isEmpty()) {
            for (String key : search.keySet()) {
                if (allowedProps.contains(key) && !search.get(key).toString().isEmpty()) {
                    try {
                        Field field = entity.getDeclaredField(key);
                        specification =
                                isOr
                                        ? specification.or(
                                        (root, query, builder) -> {
                                            if (field.getType() == Long.class) {
                                                return builder.equal(root.get(key), search.get(key));
                                            } else if (field.getType() == Boolean.class) {
                                                Boolean bool = search.get(key).toString().equalsIgnoreCase("true");
                                                return builder.equal(root.get(key), bool);
                                            }
                                            if (field.getType().isEnum()) {
                                                return builder.equal(root.get(key), search.get(key));
                                            } else {
                                                return builder.like(
                                                        builder.lower(root.get(key)),
                                                        "%" + search.get(key).toString().toLowerCase() + "%");
                                            }
                                        })
                                        : specification.and(
                                        (root, query, builder) -> {
                                            if (field.getType() == Long.class) {
                                                return builder.equal(root.get(key), search.get(key));
                                            } else if (field.getType() == Boolean.class) {
                                                Boolean bool = search.get(key).toString().equalsIgnoreCase("true");
                                                return builder.equal(root.get(key), bool);
                                            }
                                            if (field.getType().isEnum()) {
                                                return builder.equal(root.get(key), search.get(key));
                                            } else {
                                                return builder.like(
                                                        builder.lower(root.get(key)),
                                                        "%" + search.get(key).toString().toLowerCase() + "%");
                                            }
                                        });
                    } catch (Exception e) {
                        log.error(e.toString());
                    }
                }
            }
        }
        Specification<T> all = Specification.where(null);
        return all.and(specification);
    }
}
