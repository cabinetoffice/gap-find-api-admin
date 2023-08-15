package gov.cabinetoffice.gapfindapiadmin.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import gov.cabinetoffice.gapfindapiadmin.models.JwtPayload;
import gov.cabinetoffice.gapfindapiadmin.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import static org.springframework.util.ObjectUtils.isEmpty;

@RequiredArgsConstructor
public class JwtAuthorisationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    public void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");
        if (isEmpty(header) || !header.startsWith("Bearer ")) {
            throw new UnauthorizedException("Expected Authorization header not provided");
        }

        final String jwtBase64 = header.replace("Bearer ", "");

        final DecodedJWT decodedJWT = this.jwtService.verifyToken(jwtBase64);

        final JwtPayload JWTPayload = this.jwtService.getPayloadFromJwt(decodedJWT);

        if (!JWTPayload.getRoles().contains("TECHNICAL_SUPPORT")) {
            throw new UnauthorizedException("User is not a technical support user");
        }

        final Authentication auth = new UsernamePasswordAuthenticationToken(JWTPayload.getSub(),null,
                Collections.singletonList(new SimpleGrantedAuthority("TECHNICAL_SUPPORT")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}
