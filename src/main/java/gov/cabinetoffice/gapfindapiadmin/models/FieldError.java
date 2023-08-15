package gov.cabinetoffice.gapfindapiadmin.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class FieldError {

        private String field;
        private String message;
}
