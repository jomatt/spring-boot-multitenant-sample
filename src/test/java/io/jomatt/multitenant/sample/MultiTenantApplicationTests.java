package io.jomatt.multitenant.sample;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class MultiTenantApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void tenant1() {
        String url = "http://localhost:" + port + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TENANT-ID", "tenant1");
        var result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
        assertNotNull(result.getBody());
        System.out.println(result.getBody());
        assertTrue(result.getBody().contains("Hello World from tenant1"));
    }

    @Test
    void tenant2() {
        String url = "http://localhost:" + port + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TENANT-ID", "tenant2");
        var result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().contains("Hello World from tenant2"));
    }

    @Test
    void unknownTenant() {
        String url = "http://localhost:" + port + "/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-TENANT-ID", "unknown-tenant");
        var result = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().contains("Hello World from unknown tenant"));
    }

    @Test
    void noTenant() {
        String url = "http://localhost:" + port + "/";
        var result = restTemplate.getForObject(url, String.class);

        assertEquals("Hello World", result);
    }

}
