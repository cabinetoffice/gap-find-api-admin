package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.config.ApiGatewayConfigProperties;
import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import gov.cabinetoffice.gapfindapiadmin.models.FundingOrganisation;
import gov.cabinetoffice.gapfindapiadmin.models.GapUser;
import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.exceptions.ApiKeyDoesNotExistException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateApiKeyResponse;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanKeyRequest;
import software.amazon.awssdk.services.apigateway.model.CreateUsagePlanKeyResponse;

import java.security.Principal;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import software.amazon.awssdk.services.apigateway.ApiGatewayClient;
import software.amazon.awssdk.services.apigateway.model.ApiKey;
import software.amazon.awssdk.services.apigateway.model.GetApiKeysResponse;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiGatewayServiceTest {

    private final String API_KEY_NAME = "apikeyName";
    private final String API_KEY_DESCRIPTION = "apikeyDescription";
    private final ApiKey apiKey = ApiKey.builder().name(API_KEY_NAME).build();
    private final GetApiKeysResponse getApiKeysResponse = GetApiKeysResponse.builder().items(List.of(apiKey)).build();
    @Mock
    ApiGatewayConfigProperties apiGatewayConfigProperties;
    @Mock
    ApiGatewayClient apiGatewayClient;
    @Mock
    Principal principal;
    @Mock
    ApiKeyService apiKeyService;
    @Mock
    GrantAdminService grantAdminService;
    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;
    @Mock
    ZonedDateTime zonedDateTime;
    @InjectMocks
    ApiGatewayService apiGatewayService;

    private final FundingOrganisation fundingOrganisation = FundingOrganisation.builder().id(1).build();
    private final GapUser gapUser = GapUser.builder().id(1).userSub("sub").build();

    private final GrantAdmin grantAdmin = GrantAdmin.builder().gapUser(gapUser).funder(fundingOrganisation).build();

    @Test
    void createApiKeysInAwsAndDb() {
        prepareAuthentication();
        CreateApiKeyResponse apiKeyRequest = CreateApiKeyResponse.builder().name(API_KEY_NAME).value("apiKeyValue").build();
        when(apiGatewayClient.createApiKey(any(CreateApiKeyRequest.class))).thenReturn(apiKeyRequest);
        CreateUsagePlanKeyResponse usagePlanKeyResponse = CreateUsagePlanKeyResponse.builder().build();
        when(apiGatewayClient.createUsagePlanKey(any(CreateUsagePlanKeyRequest.class))).thenReturn(usagePlanKeyResponse);
        when(apiGatewayConfigProperties.getApiGatewayUsagePlanId()).thenReturn("usagePlanId");
        when(apiGatewayService.createApiKeyInAwsApiGateway(API_KEY_NAME)).thenReturn(apiKeyRequest);

        String response = apiGatewayService.createApiKeysInAwsAndDb(API_KEY_NAME);

        assertThat(response).isEqualTo("apiKeyValue");
    }

    @Test
    void createApiKeyInAwsApiGateway() {
        CreateApiKeyResponse apiKeyRequest = CreateApiKeyResponse.builder().name(API_KEY_NAME).value("apiKeyValue").build();
        when(apiGatewayClient.createApiKey(any(CreateApiKeyRequest.class))).thenReturn(apiKeyRequest);
        CreateApiKeyResponse response = apiGatewayService.createApiKeyInAwsApiGateway(API_KEY_NAME);
        assertThat(response).isEqualTo(apiKeyRequest);
    }

    @Test
    void addApiKeyToApiGatewayUsagePlan() {
        CreateApiKeyResponse apiKeyRequest = CreateApiKeyResponse.builder().name(API_KEY_NAME).value("apiKeyValue").build();
        CreateUsagePlanKeyResponse usagePlanKeyResponse = CreateUsagePlanKeyResponse.builder().build();
        when(apiGatewayClient.createUsagePlanKey(any(CreateUsagePlanKeyRequest.class))).thenReturn(usagePlanKeyResponse);
        apiGatewayService.addApiKeyToApiGatewayUsagePlan(apiKeyRequest);
        verify(apiGatewayClient).createUsagePlanKey(any(CreateUsagePlanKeyRequest.class));
    }

    @Test
    void saveKeyInDatabase() {
        SecurityContextHolder.setContext(securityContext);
        CreateApiKeyResponse apiKeyRequest = CreateApiKeyResponse.builder().name(API_KEY_NAME).value("apiKeyValue").build();
        apiGatewayService.saveKeyInDatabase(API_KEY_NAME, apiKeyRequest, grantAdmin);
        verify(apiKeyService).saveApiKey(any(ApiKey.class));
    }

    private void prepareAuthentication() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(grantAdmin);
    }

    @Test
    void deleteApiKeys() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        assertDoesNotThrow(() -> apiGatewayService.deleteApiKey(API_KEY_NAME));
    }

    @Test
    void deleteApiKeys_throwsApiKeyDoesNotExistException() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        assertThrows(ApiKeyDoesNotExistException.class, () -> apiGatewayService.deleteApiKey("anotherKeyName"));
    }


    @Test
    void checkIfKeyExistAlready_returnsTrue() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        boolean result = apiGatewayService.doesKeyExist(API_KEY_NAME);
        assertThat(result).isTrue();
    }

    @Test
    void checkIfKeyExistAlready_returnsFalse() {
        when(apiGatewayClient.getApiKeys()).thenReturn(getApiKeysResponse);
        boolean result = apiGatewayService.doesKeyExist("anotherKeyName");
        assertThat(result).isFalse();
    }
}