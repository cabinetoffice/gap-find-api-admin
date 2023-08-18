package gov.cabinetoffice.gapfindapiadmin.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "api_key", indexes = {
        @Index(name = "api_key_value_index", columnList = "api_key_value")
})
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GapApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "api_key_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "funder_id")
    private FundingOrganisation fundingOrganisation;

    @Column(name = "api_gateway_id")
    private String apiGatewayId;

    @Column(name = "api_key_value")
    private String apiKey;

    @Column(name = "api_key_name")
    private String name;

    @Column(name = "api_key_description")
    private String description;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "is_revoked", nullable = false)
    private boolean isRevoked;

    @Column(name = "revocation_date")
    private ZonedDateTime revocationDate;

    @Column(name = "revoked_by", nullable = false)
    private Integer revokedBy;
}
