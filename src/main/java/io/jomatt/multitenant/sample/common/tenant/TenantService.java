package io.jomatt.multitenant.sample.common.tenant;

import io.quantics.multitenant.tenantdetails.TenantSchemaDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TenantService implements TenantSchemaDetailsService {

    private final TenantRepository repository;

    @Autowired
    public TenantService(TenantRepository repository) {
        this.repository = repository;
    }

    public Iterable<Tenant> getAll() {
        return repository.findAll();
    }

    public Optional<Tenant> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Tenant> getByIssuer(String issuer) {
        return repository.findByIssuer(issuer);
    }

}
