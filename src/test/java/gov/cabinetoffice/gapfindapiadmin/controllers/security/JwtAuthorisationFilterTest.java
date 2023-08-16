package gov.cabinetoffice.gapfindapiadmin.controllers.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import gov.cabinetoffice.gapfindapiadmin.models.JwtPayload;
import gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter;
import gov.cabinetoffice.gapfindapiadmin.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JwtAuthorisationFilterTest {

    private JwtAuthorisationFilter jwtAuthorisationFilter;

    @Mock
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private final String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmdW5kZXJfaWQiOiIxIn0.NbYwfVIpqalW1gM204pQXM7o6voNoO7EyFwl1XjXEQQ";
    private final DecodedJWT decodedJWT = JWT.decode(jwt);


    private final JwtPayload jwtPayload = JwtPayload.builder()
            .sub("sub")
            .roles("TECHNICAL_SUPPORT")
            .build();

    @BeforeEach
    void setup() {
        jwtAuthorisationFilter = new JwtAuthorisationFilter(jwtService);
    }

    @Test
    void doFilterInternal_validToken_JwtPayload() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.verifyToken(jwt)).thenReturn(decodedJWT);
        when(jwtService.getPayloadFromJwt(decodedJWT)).thenReturn(jwtPayload);

        jwtAuthorisationFilter.doFilterInternal(request, response, filterChain);

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        verify(filterChain).doFilter(request, response);
        verify(jwtService, times(1)).getPayloadFromJwt(decodedJWT);
        assertThat(authentication).isNotNull();
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getPrincipal()).isEqualTo(jwtPayload.getSub());
    }

    @Test
    public void doFilterInternal_unexpected_header() {
        when(request.getHeader("Authorization")).thenReturn("");

        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> jwtAuthorisationFilter.doFilterInternal(request, response, filterChain))
                .withMessage("Expected Authorization header not provided");
    }

    @Test
    public void doFilterInternal_non_technical_support() {
        jwtPayload.setRoles("ADMIN");

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.verifyToken(jwt)).thenReturn(decodedJWT);
        when(jwtService.getPayloadFromJwt(decodedJWT)).thenReturn(jwtPayload);

        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> jwtAuthorisationFilter.doFilterInternal(request, response, filterChain))
                .withMessage("User is not a technical support user");
    }

}
