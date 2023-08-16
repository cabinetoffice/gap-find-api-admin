package gov.cabinetoffice.gapfindapiadmin.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import gov.cabinetoffice.gapfindapiadmin.config.UserServiceConfig;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.models.JwtPayload;
import gov.cabinetoffice.gapfindapiadmin.services.GrantAdminService;
import gov.cabinetoffice.gapfindapiadmin.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.Collections;

import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.web.util.WebUtils.getCookie;

@RequiredArgsConstructor
public class JwtAuthorisationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final GrantAdminService grantAdminService;
    private final UserServiceConfig userServiceConfig;

    @Override
    public void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final Cookie customJWTCookie = WebUtils.getCookie(request, userServiceConfig.getCookieName());
        if (isEmpty(customJWTCookie) || isEmpty(customJWTCookie.getValue())) {
            throw new UnauthorizedException("Expected JWT cookie not provided");
        }
        final DecodedJWT decodedJWT = this.jwtService.verifyToken(customJWTCookie.getValue());

        final JwtPayload JWTPayload = this.jwtService.getPayloadFromJwt(decodedJWT);

        if (!JWTPayload.getRoles().contains("TECHNICAL_SUPPORT")) {
            throw new UnauthorizedException("User is not a technical support user");
        }

        final GrantAdmin grantAdmin = this.grantAdminService.getGrantAdminForUser(JWTPayload.getSub());

        final Authentication auth = new UsernamePasswordAuthenticationToken(grantAdmin, null,
                Collections.singletonList(new SimpleGrantedAuthority("TECHNICAL_SUPPORT")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
