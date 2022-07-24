package io.jomatt.multitenant.sample;

import io.quantics.multitenant.TenantContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String get() {
        StringBuilder result = new StringBuilder("Hello World");

        if (TenantContext.getTenantId() != null) {
            result.append(" from ").append(TenantContext.getTenantId());
        }

        return result.toString();
    }

}
