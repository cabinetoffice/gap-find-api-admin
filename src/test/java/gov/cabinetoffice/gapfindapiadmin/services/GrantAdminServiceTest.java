package gov.cabinetoffice.gapfindapiadmin.services;

import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.repositories.GrantAdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GrantAdminServiceTest {

    @Mock
    GrantAdminRepository grantAdminRepository;

    @InjectMocks
    GrantAdminService grantAdminService;

    @Test
    void getGrantAdminForUser() {
        GrantAdmin grantAdmin = GrantAdmin.builder().id(1).build();
        when(grantAdminRepository.findByGapUserUserSub("sub")).thenReturn(grantAdmin);
        GrantAdmin result = grantAdminService.getGrantAdminForUser("sub");
        assertThat(result).isEqualTo(grantAdmin);
    }
}