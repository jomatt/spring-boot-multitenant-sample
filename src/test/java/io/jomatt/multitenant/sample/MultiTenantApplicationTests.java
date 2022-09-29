package io.jomatt.multitenant.sample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql(scripts = { "/create-tenants.sql", "/insert-data.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = { "/delete-data.sql" }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MultiTenantApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> sendRequest(String tenantId) {
        String url = "http://localhost:" + port + "/users";
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TENANT-ID", tenantId);
        return restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    @Test
    void tenant1() {
        ResponseEntity<String> result = sendRequest("tenant1");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().contains("alice"));
        assertTrue(result.getBody().contains("alex"));
    }

    @Test
    void tenant2() {
        ResponseEntity<String> result = sendRequest("tenant2");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().contains("bob"));
        assertTrue(result.getBody().contains("bella"));
    }

    @Test
    void unknownTenant() {
        ResponseEntity<String> result = sendRequest("unknown-tenant");

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

    @Test
    void noTenant() {
        ResponseEntity<String> result = sendRequest(null);

        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
    }

}
