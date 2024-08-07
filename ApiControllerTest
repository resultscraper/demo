package com.example.webfluxapp.controller;

import com.example.webfluxapp.service.ApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ApiControllerTest {

    @Mock
    private ApiService apiService;

    @InjectMocks
    private ApiController apiController;

    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(apiController).build();
    }

    @Test
    public void testFetchSuccess() {
        String applicationResponse = "application-response";
        when(apiService.callApplication(any(String.class))).thenReturn(Mono.just(applicationResponse));

        webTestClient.get()
                .uri("/fetch/123")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(applicationResponse);
    }

    @Test
    public void testFetchError() {
        when(apiService.callApplication(any(String.class))).thenReturn(Mono.error(new RuntimeException("Application error")));

        webTestClient.get()
                .uri("/fetch/123")
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("500 Internal Server Error from GET /fetch/123");
    }

    @Test
    public void testCallOtherServiceSuccess() {
        String otherServiceResponse = "other-service-response";
        when(apiService.callOtherService(any(String.class), any(Object.class))).thenReturn(Mono.just(otherServiceResponse));

        webTestClient.post()
                .uri("/other/123")
                .bodyValue(new Object())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo(otherServiceResponse);
    }

    @Test
    public void testCallOtherServiceError() {
        when(apiService.callOtherService(any(String.class), any(Object.class))).thenReturn(Mono.error(new RuntimeException("Other service error")));

        webTestClient.post()
                .uri("/other/123")
                .bodyValue(new Object())
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .isEqualTo("500 Internal Server Error from POST /other/123");
    }
}
