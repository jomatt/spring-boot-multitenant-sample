package io.jomatt.multitenant.sample.header;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.lang.NonNull;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = { "/create-tenants.sql", "/insert-data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = { "/delete-data.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MultiTenantHeaderApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void tenant1() throws Exception {
        sendRequest("tenant1")
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("alice")))
                .andExpect(content().string(containsString("alex")));
    }

    @Test
    void tenant2() throws Exception {
        sendRequest("tenant2")
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("bob")))
                .andExpect(content().string(containsString("bella")));
    }

    @Test
    void unknownTenant() throws Exception {
        sendRequest("unknown-tenant")
                .andExpect(status().isUnauthorized());
    }

    @Test
    void noTenant() throws Exception {
        sendRequest("")
                .andExpect(status().isUnauthorized());
    }

    private ResultActions sendRequest(String tenantId) throws Exception {
        return mvc.perform(get("/users").with(tenantHeader(tenantId)));
    }

    private static TenantHeaderRequestPostProcessor tenantHeader(String token) {
        return new TenantHeaderRequestPostProcessor(token);
    }

    private static class TenantHeaderRequestPostProcessor implements RequestPostProcessor {

        private final String tenantId;

        TenantHeaderRequestPostProcessor(String tenantId) {
            this.tenantId = tenantId;
        }

        @Override
        @NonNull
        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
            request.addHeader("X-TENANT-ID", tenantId);
            return request;
        }

    }

}
