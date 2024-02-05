package gov.cabinetoffice.gapfindapiadmin.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import gov.cabinetoffice.gapfindapiadmin.config.UserServiceConfig;
import gov.cabinetoffice.gapfindapiadmin.exceptions.UnauthorizedException;
import gov.cabinetoffice.gapfindapiadmin.models.JwtPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.RestTemplate;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter.SUPER_ADMIN_ROLE;
import static gov.cabinetoffice.gapfindapiadmin.security.JwtAuthorisationFilter.TECHNICAL_SUPPORT_ROLE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Mock
    private UserServiceConfig userServiceConfig;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private JwtService jwtService;

    private String encodedJwt;

    @Nested
    class validJWTTests {

        @BeforeEach
        void beforeEach() {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                KeyPair keyPair = keyPairGenerator.genKeyPair();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Date tomorrowDate = calendar.getTime();

                encodedJwt = JWT.create().withExpiresAt(tomorrowDate).withIssuer("TEST_DOMAIN")
                        .withKeyId(keyPair.getPublic().toString()).withSubject("106b1a34-cd3a-45d7-924f-beedc33acc70")
                        .withClaim("roles", "[TECHNICAL_SUPPORT]")
                        .withClaim("email", "test.user@and.digital")
                        .withClaim("department", "Cabinet Office")
                        .withClaim("iat", 1234567890)
                        .sign(Algorithm.RSA256(null, (RSAPrivateKey) keyPair.getPrivate()));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

        @Test
        void getPayloadFromJwt_HappyPathTest() {
            JwtPayload payloadFromJwt = jwtService.getPayloadFromJwt(JWT.decode(encodedJwt));
            assertThat(payloadFromJwt.getDepartmentName()).isEqualTo("Cabinet Office");
            assertThat(payloadFromJwt.getEmailAddress()).isEqualTo("test.user@and.digital");
            assertThat(payloadFromJwt.getRoles()).contains(TECHNICAL_SUPPORT_ROLE);
            assertThat(payloadFromJwt.getSub()).isEqualTo("106b1a34-cd3a-45d7-924f-beedc33acc70");
        }

    }

    @Nested
    class JwtTests {

        @Test
        void verifyToken_ReturnsTrue() {
            when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Boolean.class)))
                    .thenReturn(ResponseEntity.of(Optional.of(Boolean.TRUE)));

            try (MockedStatic<JWT> mockedJwt = mockStatic(JWT.class)) {
                DecodedJWT decodedJWT = mock(DecodedJWT.class);
                mockedJwt.when(() -> JWT.decode("testToken")).thenReturn(decodedJWT);

                final DecodedJWT response = jwtService.verifyToken("testToken");

                assertThat(response).isEqualTo(decodedJWT);
            }
        }

        @Test
        void verifyToken_ExpiredJwtTest() {
            when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Boolean.class)))
                    .thenReturn(ResponseEntity.of(Optional.of(Boolean.FALSE)));

            assertThatThrownBy(() -> jwtService.verifyToken(encodedJwt)).isInstanceOf(UnauthorizedException.class)
                    .hasMessage("Token is not valid");
        }

    }

    @Nested
    class generateSimpleGrantedAuthorityList{
        @Test
        void testGenerateSimpleGrantedAuthorityListSuperAdmin() {
            final List<SimpleGrantedAuthority> result = jwtService.generateSimpleGrantedAuthorityList(true, false);

            assertThat(result.size()).isEqualTo(3 );
            assertThat(result.get(0).getAuthority()).isEqualTo(SUPER_ADMIN_ROLE);
        }

        @Test
        void testGenerateSimpleGrantedAuthorityListAdmin() {
            final List<SimpleGrantedAuthority> expected = List.of(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority(TECHNICAL_SUPPORT_ROLE));

            List<SimpleGrantedAuthority> result = jwtService.generateSimpleGrantedAuthorityList(false, true);

            assertThat(result.size()).isEqualTo(2);
            assertThat(result).isEqualTo(expected);
        }

        @Test
        void testGenerateSimpleGrantedAuthorityListTechnicalSupport() {
            final List<SimpleGrantedAuthority> result = jwtService.generateSimpleGrantedAuthorityList(false, false);

            assertThat(result.size()).isEqualTo(1);
            assertThat(result.get(0).getAuthority()).isEqualTo(TECHNICAL_SUPPORT_ROLE);
        }
    }

}
