package tz.go.zanemr.auth.modules.external_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacilityDto {
    private Long id;
    private String name;
    private String code;
}
