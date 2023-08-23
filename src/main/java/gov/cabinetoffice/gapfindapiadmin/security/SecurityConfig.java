package gov.cabinetoffice.gapfindapiadmin.security;

import gov.cabinetoffice.gapfindapiadmin.config.UserServiceConfig;
import gov.cabinetoffice.gapfindapiadmin.services.GrantAdminService;
import gov.cabinetoffice.gapfindapiadmin.services.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

    private static final String[] WHITE_LIST = {
            "/webjars/**",
            "/health",
            "/js/**"
    };
    private final JwtAuthorisationFilter jwtAuthorisationFilter;


    public SecurityConfig(final JwtService jwtService, final GrantAdminService grantAdminService, UserServiceConfig userServiceConfig) {
        this.jwtAuthorisationFilter = new JwtAuthorisationFilter(jwtService, grantAdminService, userServiceConfig);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // specify any paths you don't want subject to JWT validation/authentication
        return web -> web.ignoring().requestMatchers(WHITE_LIST);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request ->
                        request.anyRequest()
                                .authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtAuthorisationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(new CustomAccessDeniedHandler()));

        return http.build();
    }
}
