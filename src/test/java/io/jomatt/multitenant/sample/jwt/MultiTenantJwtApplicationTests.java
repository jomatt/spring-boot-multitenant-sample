package io.jomatt.multitenant.sample.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({ "prod", "test" })
@AutoConfigureMockMvc
@Sql(scripts = { "/create-tenants.sql", "/insert-data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = { "/delete-data.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MultiTenantJwtApplicationTests {

    private static final String JWT_TENANT_1 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2lkcC5leGFtcGxlLm9yZy90ZW5hbnQtMSIsImlhdCI6MTY2NDU4MjQwMCwiZXhwIjoyNTI0NjA4MDAwLCJzdWIiOiJlNDg5ZmZlNy0yMmQyLTQwZWYtYmRlMS1mZDA2OWFjOGFmMGIiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsidXNlciJdfSwibmFtZSI6ImFsaWNlIn0.byGgJ1neIoYYE0gID2gWOo-PQDfHy3wasJ3NoD7iyz4";
    private static final String JWT_TENANT_2 = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2lkcC5leGFtcGxlLm9yZy90ZW5hbnQtMiIsImlhdCI6MTY2NDU4MjQwMCwiZXhwIjoyNTI0NjA4MDAwLCJzdWIiOiI0YWViYzRkMS0yNDFjLTQ5MzQtYjBiNi1lNjFmMGI1NmRjNzciLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsidXNlciJdfSwibmFtZSI6ImJvYiJ9.wHknw6r-vFO6pzz8DrHhLnxc0X428qGXpq3vxl1ygus";
    private static final String JWT_UNKNOWN_TENANT = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJodHRwczovL2lkcC5leGFtcGxlLm9yZy91bmtub3duLXRlbmFudCIsImlhdCI6MTY2NDU4MjQwMCwiZXhwIjoyNTI0NjA4MDAwLCJzdWIiOiIxNjU4MzE3Yy0wZWFhLTQwZTYtYWY3YS0wNGJiN2NlMmU5ZDkiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsidXNlciJdfSwibmFtZSI6Imdob3N0In0.e_MudNxU98pc2a8cboGjbvebZBwMux7P7wKP1rTkr7M";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void tenant1() throws Exception {
        when(jwtDecoder.decode(JWT_TENANT_1))
                .thenReturn(new Jwt(JWT_TENANT_1, Instant.now(), Instant.now().plusSeconds(60),
                        Map.of("alg", "HS256", "typ", "JWT"),
                        Map.of("realm_access", Map.of("roles", List.of("user")),
                                "iss", "https://idp.example.org/tenant-1",
                                "sub", "e489ffe7-22d2-40ef-bde1-fd069ac8af0b")));

        sendRequest(JWT_TENANT_1)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("alice")))
                .andExpect(content().string(containsString("alex")));
    }

    @Test
    void tenant2() throws Exception {
        when(jwtDecoder.decode(JWT_TENANT_2))
                .thenReturn(new Jwt(JWT_TENANT_2, Instant.now(), Instant.now().plusSeconds(60),
                        Map.of("alg", "HS256", "typ", "JWT"),
                        Map.of("realm_access", Map.of("roles", List.of("user")),
                                "iss", "https://idp.example.org/tenant-2",
                                "sub", "4aebc4d1-241c-4934-b0b6-e61f0b56dc77")));

        sendRequest(JWT_TENANT_2)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("bob")))
                .andExpect(content().string(containsString("bella")));
    }

    @Test
    void unknownTenant() throws Exception {
        sendRequest(JWT_UNKNOWN_TENANT)
                .andExpect(status().isUnauthorized());
    }

    @Test
    void noTenant() throws Exception {
        sendRequest("")
                .andExpect(status().isUnauthorized());
    }

    private ResultActions sendRequest(String token) throws Exception {
        return mvc.perform(get("/users").with(bearerToken(token)));
    }

    private static BearerTokenRequestPostProcessor bearerToken(String token) {
        return new BearerTokenRequestPostProcessor(token);
    }

    private static class BearerTokenRequestPostProcessor implements RequestPostProcessor {

        private final String token;

        BearerTokenRequestPostProcessor(String token) {
            this.token = token;
        }

        @Override
        @NonNull
        public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
            request.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
            return request;
        }

    }

}
