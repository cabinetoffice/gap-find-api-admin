package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.exceptions.UserNotFoundException;
import gov.cabinetoffice.gapfindapiadmin.models.TechSupportUser;
import gov.cabinetoffice.gapfindapiadmin.repositories.TechSupportUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TechSupportUserService {

    private final TechSupportUserRepository techSupportUserRepository;

    public TechSupportUser getTechSupportUserBySub(String userSub) {
        return techSupportUserRepository.findByUserSub(userSub)
                .orElseThrow(() -> new UserNotFoundException("Tech support not found with sub: ".concat(userSub)));
    }
}
