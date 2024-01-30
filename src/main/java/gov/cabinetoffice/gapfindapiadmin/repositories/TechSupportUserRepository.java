package gov.cabinetoffice.gapfindapiadmin.repositories;

import gov.cabinetoffice.gapfindapiadmin.models.TechSupportUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TechSupportUserRepository extends CrudRepository<TechSupportUser, Integer> {

    TechSupportUser findByUserSub(String userSub);
}
