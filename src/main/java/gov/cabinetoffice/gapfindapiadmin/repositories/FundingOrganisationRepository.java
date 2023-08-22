package gov.cabinetoffice.gapfindapiadmin.repositories;

import gov.cabinetoffice.gapfindapiadmin.models.FundingOrganisation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FundingOrganisationRepository extends CrudRepository<FundingOrganisation, Integer> {
}
