package gov.cabinetoffice.gapfindapiadmin.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateApiKeyDTO {

	@NotBlank(message = "Enter a key name")
	@Pattern(regexp = "^(?!\\s).*", message = "Key name must not start with empty space")
	@Pattern(regexp = "^[a-zA-Z0-9\\s]*$", message = "Key name must be alphanumeric")
	@Pattern(regexp = "^(.*\\S)?$", message = "Key name must not end with empty space")
	@Size(max = 1024, message = "Key name must be max 1024 characters")
	private String keyName;

}
