package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.repositories.GrantAdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GrantAdminService {

    private final GrantAdminRepository grantAdminRepository;

    public GrantAdmin getGrantAdminForUser(String userSub) {
        return grantAdminRepository.findByGapUserUserSub(userSub);
    }
}
