package gov.cabinetoffice.gapfindapiadmin.repositories;

import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import org.springframework.data.repository.CrudRepository;

public interface GrantAdminRepository extends CrudRepository<GrantAdmin, Integer> {
    GrantAdmin findByGapUserUserSub(String userSub);

}
