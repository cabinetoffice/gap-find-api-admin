package gov.cabinetoffice.gapfindapiadmin.security;

import gov.cabinetoffice.gapfindapiadmin.config.UserServiceConfig;
import gov.cabinetoffice.gapfindapiadmin.services.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity()
public class SecurityConfig {

    private static final String[] WHITE_LIST = {
            "/webjars/**",
            "/health",
            "/js/**",
            "/error"
    };
    private final JwtAuthorisationFilter jwtAuthorisationFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPointFilter;


    public SecurityConfig(final JwtService jwtService, UserServiceConfig userServiceConfig) {
        this.authenticationEntryPointFilter = new CustomAuthenticationEntryPoint(userServiceConfig);
        this.jwtAuthorisationFilter = new JwtAuthorisationFilter(jwtService, userServiceConfig);
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
                .authorizeHttpRequests(requests -> requests
                        .anyRequest().authenticated()
                )
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .addFilterBefore(jwtAuthorisationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new AccessDeniedExceptionFilter(), JwtAuthorisationFilter.class)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(authenticationEntryPointFilter)
                                .accessDeniedHandler(new CustomAccessDeniedHandler())
                );

        return http.build();
    }
}
