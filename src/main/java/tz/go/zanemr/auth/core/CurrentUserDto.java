package tz.go.zanemr.auth.core;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentUserDto {

    private Long id;

    private UUID uuid;

    private String email;

    private String firstName;

    private String lastName;

    private String middleName;

    private UUID facilityUuid;

    private Long facilityId;

    private String facilityName;

    private String facilityCode;

    public String getFullName() {
        return String.valueOf(firstName) + " "
                + String.valueOf(middleName) + " "
                + String.valueOf(lastName);
    }

}
