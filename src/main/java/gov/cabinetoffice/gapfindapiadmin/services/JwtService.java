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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

}
