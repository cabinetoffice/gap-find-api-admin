package gov.cabinetoffice.gapfindapiadmin.helpers;

import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaginationHelperTest {

    @Mock
    private ApiKeyService apiKeyService;

    @InjectMocks
    private PaginationHelper paginationHelper;

    private final GapApiKey apiKey = GapApiKey.builder()
            .id(1)
            .apiGatewayId("apiGatewayId")
            .name("Test API Key name")
            .apiKey("Test API Key")
            .isRevoked(false)
            .build();

    private final Page<GapApiKey> apiKeyPage = new PageImpl<>(Collections.singletonList(apiKey), PageRequest.of(0, 1), 1);

    @Test
    void getNumberOfPages_hasPages() {
        final List<Integer> response = paginationHelper.getNumberOfPages(3);
        assertThat(response).isEqualTo(List.of(1,2,3));
    }

    @Test
    void getNumberOfPages_zeroPages() {
        final List<Integer> response = paginationHelper.getNumberOfPages(0);
        assertThat(response).isEqualTo(List.of());
    }

    @Test
    void getGapApiKeysPage() {
        when(apiKeyService.findPaginated(PageRequest.of(0, 10),Collections.singletonList(apiKey))).thenReturn(apiKeyPage);
        final Page<GapApiKey> response =  paginationHelper.getGapApiKeysPage(List.of(apiKey), 1);
        assertThat(response).isEqualTo(apiKeyPage);
    }
}