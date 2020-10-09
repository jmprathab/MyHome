package com.myhome.security.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CommunityControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void addAmenityToCommunityWithNoAdminUser() {
        ResponseEntity<String> responseEntity = performRequest(getJWT(),
                "/communities/19dfd456-5a63-4724-86c5-73ff53724c3a/amenities",
                "{ \"amenities\": [] }",
                HttpMethod.POST);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, responseEntity.getStatusCodeValue());
    }

    @Test
    public void addAmenityToCommunityWithAdminUser() {
        performRequest(getJWT(),
                "/communities/19dfd456-5a63-4724-86c5-73ff53724c3a/admins",
                "{ \"admins\": [ \"default-user-id-for-testing\" ] }",
                HttpMethod.POST);

        ResponseEntity<String> responseEntity = performRequest(getJWT(),
                "/communities/19dfd456-5a63-4724-86c5-73ff53724c3a/amenities",
                "{ \"amenities\": [] }",
                HttpMethod.POST);

        assertEquals(HttpServletResponse.SC_OK, responseEntity.getStatusCodeValue());
    }


    private ResponseEntity<String> performRequest(String jwt, String url, String body, HttpMethod httpMethod) {
        HttpEntity<String> requestEntity = new HttpEntity<>(body, getHeaders(MediaType.APPLICATION_JSON, jwt));

        return testRestTemplate.exchange(url, httpMethod, requestEntity, String.class);
    }

    private HttpHeaders getHeaders(MediaType mediaType, String jwt) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(mediaType);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);

        return httpHeaders;
    }


    private String getJWT() {
        String jsonBody = "{\"email\": \"test@test.com\", \"password\": \"testtest\"}";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(
                "/users/login",
                HttpMethod.POST,
                new HttpEntity<>(jsonBody, httpHeaders),
                String.class);

        List<String> token = responseEntity.getHeaders().get("token");

        return (token != null) ? token.get(0x00) : null;
    }

}
