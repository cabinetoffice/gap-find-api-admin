package gov.cabinetoffice.gapfindapiadmin.helpers;

import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import gov.cabinetoffice.gapfindapiadmin.services.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Component
public class PaginationHelper {

    private final ApiKeyService apiKeyService;
    private static final int API_KEYS_PER_PAGE = 10;

    public List<Integer> getNumberOfPages(int totalPages) {
        return totalPages > 0 ? IntStream.rangeClosed(1, totalPages)
                .boxed()
                .toList()
                : List.of();
    }

    public Page<GapApiKey> getGapApiKeysPage(List<GapApiKey> allApiKeys, int currentPage) {
        return apiKeyService.findPaginated(PageRequest.of(currentPage - 1, API_KEYS_PER_PAGE), allApiKeys);
    }
}
