package tz.go.zanemr.auth.core;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Setter
@Getter
@MappedSuperclass
@NoArgsConstructor
public abstract class BaseDto implements Serializable {
    private Long id;
    private UUID uuid;
}
