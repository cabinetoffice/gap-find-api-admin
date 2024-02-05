package gov.cabinetoffice.gapfindapiadmin.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import gov.cabinetoffice.gapfindapiadmin.config.UserServiceConfig;
import gov.cabinetoffice.gapfindapiadmin.exceptions.InvalidJwtException;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import gov.cabinetoffice.gapfindapiadmin.models.JwtPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter.ADMIN_ROLE;
import static gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter.SUPER_ADMIN_ROLE;
import static gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter.TECHNICAL_SUPPORT_ROLE;

@RequiredArgsConstructor
@Service
public class JwtService {

    private final UserServiceConfig userServiceConfig;

    private final RestTemplate restTemplate;

    public DecodedJWT verifyToken(final String jwt) {
        final String url = userServiceConfig.getDomain() + "/is-user-logged-in";
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", userServiceConfig.getCookieName() + "=" + jwt);
        final HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
        final ResponseEntity<Boolean> isJwtValid = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                Boolean.class);
        if (Boolean.FALSE.equals(isJwtValid.getBody())) {
            throw new UnauthorizedException("Token is not valid");
        }
        return JWT.decode(jwt);
    }

    public JwtPayload getPayloadFromJwt(DecodedJWT decodedJWT) throws IllegalArgumentException {
        final String sub = decodedJWT.getSubject();
        final String roles = decodedJWT.getClaim("roles").asString();
        final String department = decodedJWT.getClaim("department").asString();
        final String emailAddress = decodedJWT.getClaim("email").asString();
        final String iss = decodedJWT.getClaim("iss").asString();
        final String aud = decodedJWT.getClaim("aud").asString();
        int exp = decodedJWT.getClaim("exp").asInt();
        int iat = decodedJWT.getClaim("iat").asInt();

        if (roles == null || emailAddress == null) {
            throw new InvalidJwtException("JWT is missing expected properties");
        }

        return JwtPayload.builder().sub(sub).roles(roles).emailAddress(emailAddress).departmentName(department).iss(iss)
                .aud(aud).exp(exp).iat(iat).build();
    }

    public List<SimpleGrantedAuthority> generateSimpleGrantedAuthorityList(boolean isSuperAdmin, boolean isAdmin) {
        List<SimpleGrantedAuthority> simpleGrantedAuthorityList;

        if (isSuperAdmin) {
            simpleGrantedAuthorityList = List.of(new SimpleGrantedAuthority(SUPER_ADMIN_ROLE),
                    new SimpleGrantedAuthority(ADMIN_ROLE),
                    new SimpleGrantedAuthority(TECHNICAL_SUPPORT_ROLE));
        } else if (isAdmin) {
            simpleGrantedAuthorityList = List.of(
                    new SimpleGrantedAuthority(ADMIN_ROLE),
                    new SimpleGrantedAuthority(TECHNICAL_SUPPORT_ROLE)
            );
        } else {
            simpleGrantedAuthorityList = List.of(new SimpleGrantedAuthority(TECHNICAL_SUPPORT_ROLE));
        }

        return simpleGrantedAuthorityList;
    }

}
