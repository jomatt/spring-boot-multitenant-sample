package io.jomatt.multitenant.sample.common.tenant;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface TenantRepository extends CrudRepository<Tenant, String> {

    Optional<Tenant> findByIssuer(String issuer);

}
