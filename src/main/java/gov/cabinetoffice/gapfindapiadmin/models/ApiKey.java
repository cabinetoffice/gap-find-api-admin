package gov.cabinetoffice.gapfindapiadmin.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ApiKey {

    @Id
    private Integer apiKeyId;

    private Integer funderId;

    private String apiKeyValue;

    private String apiKeyName;

    private String apiKeyDescription;
    //TODO: see how this is actually done in the database
    private boolean isRevoked;
}
