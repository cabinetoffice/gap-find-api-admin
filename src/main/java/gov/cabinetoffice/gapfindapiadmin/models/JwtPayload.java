package gov.cabinetoffice.gapfindapiadmin.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtPayload {

    private String sub;

    private String departmentName;

    private String emailAddress;

    private String roles;

    private String iss;

    private String aud;

    private int exp;

    private int iat;

}
