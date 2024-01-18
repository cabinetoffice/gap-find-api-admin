package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.repositories.GrantAdminRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrantAdminService {

    private final GrantAdminRepository grantAdminRepository;

    public GrantAdmin getGrantAdminForUser(String userSub) {
        log.info("Getting grant admin for user sub: {}", userSub);

        return grantAdminRepository.findByGapUserUserSub(userSub);
    }
}
