package gov.cabinetoffice.gapfindapiadmin.security;

import gov.cabinetoffice.gapfindapiadmin.config.UserServiceConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationEntryPointTest {
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private UserServiceConfig userServiceConfig;

    @InjectMocks
    CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    @Test
    void commence() throws IOException {
        when(request.getContextPath()).thenReturn("/find/api/admin");

        final AuthenticationException authenticationException = new InsufficientAuthenticationException("Access Denied");
        doNothing().when(response).sendRedirect("/find/api/admin/api-keys/error");

        customAuthenticationEntryPoint.commence(request, response, authenticationException);

        verify(response, times(1)).sendRedirect("/find/api/admin/api-keys/error");
    }
}