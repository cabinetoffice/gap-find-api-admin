package gov.cabinetoffice.gapfindapiadmin.repositories;

import gov.cabinetoffice.gapfindapiadmin.models.GapApiKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApiKeyRepository extends CrudRepository<GapApiKey, Integer> {

    List<GapApiKey> findByFundingOrganisationId(Integer id);

    GapApiKey findByName(String name);

    Long countByIsRevokedFalse();

    @Query("select distinct fundingOrganisation.name from GapApiKey")
    List<String> findByUniqueFundingOrganisationNames();

    List<GapApiKey> findByFundingOrganisationName(String name);


}
