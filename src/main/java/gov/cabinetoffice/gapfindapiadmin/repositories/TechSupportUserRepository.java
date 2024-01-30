package gov.cabinetoffice.gapfindapiadmin.repositories;

import gov.cabinetoffice.gapfindapiadmin.models.GrantAdmin;
import gov.cabinetoffice.gapfindapiadmin.models.TechSupportUser;
import org.springframework.data.repository.CrudRepository;

public interface TechSupportUserRepository extends CrudRepository<TechSupportUser, Integer> {

    TechSupportUser findByUserSub(String userSub);
}
