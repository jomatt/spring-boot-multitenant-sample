package io.jomatt.multitenant.sample;

import io.jomatt.multitenant.sample.common.Tenant;
import io.jomatt.multitenant.sample.common.TenantService;
import io.quantics.multitenant.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class HomeController {

    private final TenantService tenantService;

    @Autowired
    public HomeController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @GetMapping
    public String get() {
        String tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            return "Hello World";
        }

        Optional<Tenant> tenant = tenantService.getById(tenantId);
        if (tenant.isEmpty()) {
            return "Hello World from unknown tenant";
        }

        return "Hello World from " + tenant.get().getId() + " (" + tenant.get().getName() + ")";
    }

}
