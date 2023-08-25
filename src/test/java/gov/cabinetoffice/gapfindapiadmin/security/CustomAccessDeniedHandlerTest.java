package gov.cabinetoffice.gapfindapiadmin.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomAccessDeniedHandlerTest {

    @InjectMocks
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Test
    void testHandleAccessDenied() throws IOException, ServletException {
        final AccessDeniedException accessDeniedException = new AccessDeniedException("Access Denied");

        when(request.getContextPath()).thenReturn("/app-context");
        doNothing().when(response).sendRedirect("/app-context/api-keys/error");

        customAccessDeniedHandler.handle(request, response, accessDeniedException);

        verify(response, times(1)).sendRedirect("/app-context/api-keys/error");
    }
}