package gov.cabinetoffice.gapfindapiadmin.controllers.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.models.JwtPayload;
import gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter;
import gov.cabinetoffice.gapfindapiadmin.services.GrantAdminService;
import gov.cabinetoffice.gapfindapiadmin.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class JwtAuthorisationFilterTest {

    private final String jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJmdW5kZXJfaWQiOiIxIn0.NbYwfVIpqalW1gM204pQXM7o6voNoO7EyFwl1XjXEQQ";
    private final DecodedJWT decodedJWT = JWT.decode(jwt);
    private final JwtPayload jwtPayload = JwtPayload.builder()
            .sub("sub")
            .roles("TECHNICAL_SUPPORT")
            .build();
    @Captor
    ArgumentCaptor<Authentication> authenticationCaptor;
    private JwtAuthorisationFilter jwtAuthorisationFilter;
    @Mock
    private JwtService jwtService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private FilterChain filterChain;
    @Mock
    private GrantAdminService grantAdminService;
    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setup() {
        jwtAuthorisationFilter = new JwtAuthorisationFilter(jwtService, grantAdminService);
    }

    @Test
    void doFilterInternal_validToken_JwtPayload() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.verifyToken(jwt)).thenReturn(decodedJWT);
        when(jwtService.getPayloadFromJwt(decodedJWT)).thenReturn(jwtPayload);
        final GrantAdmin grantAdmin = GrantAdmin.builder().id(1).build();
        when(grantAdminService.getGrantAdminForUser(jwtPayload.getSub())).thenReturn(grantAdmin);
        SecurityContextHolder.setContext(securityContext);

        jwtAuthorisationFilter.doFilterInternal(request, response, filterChain);

        verify(grantAdminService, times(1)).getGrantAdminForUser(jwtPayload.getSub());
        verify(jwtService, times(1)).getPayloadFromJwt(decodedJWT);
        verify(securityContext).setAuthentication(authenticationCaptor.capture());

        assertThat(authenticationCaptor.getValue().getPrincipal()).isEqualTo(grantAdmin);
    }

    @Test
    void doFilterInternal_unexpected_header() {
        when(request.getHeader("Authorization")).thenReturn("");

        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> jwtAuthorisationFilter.doFilterInternal(request, response, filterChain))
                .withMessage("Expected Authorization header not provided");
    }

    @Test
    void doFilterInternal_non_technical_support() {
        jwtPayload.setRoles("ADMIN");

        when(request.getHeader("Authorization")).thenReturn("Bearer " + jwt);
        when(jwtService.verifyToken(jwt)).thenReturn(decodedJWT);
        when(jwtService.getPayloadFromJwt(decodedJWT)).thenReturn(jwtPayload);

        assertThatExceptionOfType(UnauthorizedException.class)
                .isThrownBy(() -> jwtAuthorisationFilter.doFilterInternal(request, response, filterChain))
                .withMessage("User is not a technical support user");
    }

}
