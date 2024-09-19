package tz.go.zanemr.auth.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;
import java.util.UUID;

public interface BaseCrudService <D, E>{

    D save(D dto);

    Page<D> findAll(Pageable pageable, Map<String, Object> search);

    D findById(UUID uuid);

    void delete(UUID uuid);
}
