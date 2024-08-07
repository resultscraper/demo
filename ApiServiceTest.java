package com.example.webfluxapp.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class ApiServiceTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private ApiService apiService;

    @Value("${client.api.key}")
    private String clientApiKey;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTokenSuccess() {
        String tokenResponse = "{\"token\":\"sample-token\"}";
        when(webClient.post()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.contentType(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(tokenResponse));

        Mono<String> result = apiService.getToken("123");

        StepVerifier.create(result)
                .expectNext("sample-token")
                .verifyComplete();
    }

    @Test
    public void testGetTokenError() {
        when(webClient.post()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.contentType(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        Mono<String> result = apiService.getToken("123");

        StepVerifier.create(result)
                .expectError(WebClientResponseException.class)
                .verify();
    }

    @Test
    public void testCallApplicationSuccess() {
        String applicationResponse = "application-response";
        String tokenResponse = "{\"token\":\"sample-token\"}";

        when(webClient.post()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.contentType(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(tokenResponse));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq(HttpHeaders.AUTHORIZATION), any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(applicationResponse));

        Mono<String> result = apiService.callApplication("123");

        StepVerifier.create(result)
                .expectNext(applicationResponse)
                .verifyComplete();
    }

    @Test
    public void testCallApplicationError() {
        String tokenResponse = "{\"token\":\"sample-token\"}";

        when(webClient.post()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.contentType(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(tokenResponse));

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq(HttpHeaders.AUTHORIZATION), any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new WebClientResponseException(404, "Not Found", null, null, null)));

        Mono<String> result = apiService.callApplication("123");

        StepVerifier.create(result)
                .expectError(WebClientResponseException.class)
                .verify();
    }

    @Test
    public void testCallOtherServiceSuccess() {
        String otherServiceResponse = "other-service-response";
        String tokenResponse = "{\"token\":\"sample-token\"}";

        when(webClient.post()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.contentType(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(tokenResponse));

        when(webClient.post()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq(HttpHeaders.AUTHORIZATION), any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.contentType(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(otherServiceResponse));

        Mono<String> result = apiService.callOtherService("123", new Object());

        StepVerifier.create(result)
                .expectNext(otherServiceResponse)
                .verifyComplete();
    }

    @Test
    public void testCallOtherServiceError() {
        String tokenResponse = "{\"token\":\"sample-token\"}";

        when(webClient.post()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class), any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.contentType(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just(tokenResponse));

        when(webClient.post()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.header(eq(HttpHeaders.AUTHORIZATION), any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.contentType(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

        Mono<String> result = apiService.callOtherService("123", new Object());

        StepVerifier.create(result)
                .expectError(WebClientResponseException.class)
                .verify();
    }
}
