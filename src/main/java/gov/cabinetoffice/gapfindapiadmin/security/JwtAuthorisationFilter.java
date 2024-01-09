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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class JwtAuthorisationFilter extends OncePerRequestFilter {

    public static final String ADMIN_ROLE = "ADMIN";
    public static final String SUPER_ADMIN_ROLE = "SUPER_ADMIN";
    public static final String TECHNICAL_SUPPORT_ROLE = "TECHNICAL_SUPPORT";
    private final JwtService jwtService;
    private final GrantAdminService grantAdminService;
    private final UserServiceConfig userServiceConfig;

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final Cookie customJWTCookie = WebUtils.getCookie(request, userServiceConfig.getCookieName());
        if (isEmpty(customJWTCookie) || isEmpty(customJWTCookie.getValue())) {
            throw new UnauthorizedException("Expected JWT cookie not provided");
        }
        final DecodedJWT decodedJWT = this.jwtService.verifyToken(customJWTCookie.getValue());

        final JwtPayload jwtPayload = this.jwtService.getPayloadFromJwt(decodedJWT);
        final boolean isSuperAdmin = jwtPayload.getRoles().contains(SUPER_ADMIN_ROLE);
        final Pattern pattern = Pattern.compile("\\b" + ADMIN_ROLE + "\\b");
        final boolean isAdmin = pattern.matcher(jwtPayload.getRoles()).find();
        if (!jwtPayload.getRoles().contains(TECHNICAL_SUPPORT_ROLE) && !isSuperAdmin) {
            throw new AccessDeniedException("User does not have the required roles to access this resource");
        }
        final List<SimpleGrantedAuthority> simpleGrantedAuthorityList = jwtService.generateSimpleGrantedAuthorityList(isSuperAdmin, isAdmin);

        final GrantAdmin grantAdmin = this.grantAdminService.getGrantAdminForUser(jwtPayload.getSub());

        final Authentication auth = new UsernamePasswordAuthenticationToken(grantAdmin, null,
                simpleGrantedAuthorityList);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
