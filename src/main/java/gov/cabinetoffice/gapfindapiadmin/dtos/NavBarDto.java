package gov.cabinetoffice.gapfindapiadmin.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NavBarDto {
    private String name;
    private String link;
}
