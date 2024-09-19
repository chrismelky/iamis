package tz.go.zanemr.auth.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T, Long> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

   Optional<T> findByUuid(UUID uuid);

   void deleteByUuid(UUID uuid);

   T getReferenceByUuid(UUID uuid);
}
