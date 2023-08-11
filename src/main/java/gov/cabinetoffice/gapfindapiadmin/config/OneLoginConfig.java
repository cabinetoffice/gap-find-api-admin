package gov.cabinetoffice.gapfindapiadmin.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Configuration("OneLoginConfigurationProperties")
@ConfigurationProperties(prefix = "feature")
public class OneLoginConfig {

    @NonNull
    private boolean oneLoginEnabled;
}
