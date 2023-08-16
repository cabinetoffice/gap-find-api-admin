package gov.cabinetoffice.gapfindapiadmin.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grant_funding_organisation")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundingOrganisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "funder_id")
    private Integer id;

    @Column(name = "organisation_name", nullable = false)
    private String name;

}
