package gov.cabinetoffice.gapfindapiadmin.config;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Configuration("swaggerConfigurationProperties")
@ConfigurationProperties(prefix = "swagger")
public class SwaggerConfigProperties {
    @NotNull
    private String documentationLink;
}
