package io.jomatt.multitenant.sample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MultiTenantApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> sendRequest(String tenantId) {
        String url = "http://localhost:" + port + "/";
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
        assertTrue(result.getBody().contains("Hello World from tenant1"));
    }

    @Test
    void tenant2() {
        ResponseEntity<String> result = sendRequest("tenant2");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().contains("Hello World from tenant2"));
    }

    @Test
    void unknownTenant() {
        ResponseEntity<String> result = sendRequest("unknown-tenant");

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().contains("Hello World from unknown tenant"));
    }

    @Test
    void noTenant() {
        String url = "http://localhost:" + port + "/";
        String result = restTemplate.getForObject(url, String.class);

        assertEquals("Hello World", result);
    }

}
