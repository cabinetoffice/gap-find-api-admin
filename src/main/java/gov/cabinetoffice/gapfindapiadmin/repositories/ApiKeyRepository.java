package gov.cabinetoffice.gapfindapiadmin.repositories;

import gov.cabinetoffice.gapfindapiadmin.models.ApiKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ApiKeyRepository extends CrudRepository<ApiKey, Integer> {

    List<ApiKey> findByFundingOrganisationId(Integer id);
    Optional<ApiKey> findByName(String name);
}
