package gov.cabinetoffice.gapfindapiadmin.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Configuration("cognitoConfigurationProperties")
@ConfigurationProperties(prefix = "user-service")
public class UserServiceConfig {

    @NonNull
    private String domain;

    @NonNull
    private String cookieName;

}
