package com.example.webfluxapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);
    private final WebClient webClient;
    private final AtomicReference<String> tokenCache = new AtomicReference<>();

    @Value("${client.api.key}")
    private String clientApiKey;

    @Value("${authorization.url}")
    private String authorizationUrl;

    @Value("${application.url}")
    private String applicationUrl;

    @Value("${other.url}")
    private String otherUrl;

    public ApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<String> getToken(String id) {
        return webClient.post()
                .uri(authorizationUrl, id)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"clientKey\":\"" + clientApiKey + "\"}")
                .retrieve()
                .bodyToMono(String.class)
                .map(response -> {
                    // Extract token from JSON response
                    return response.replaceAll("^.*\"token\":\"([^\"]+)\".*$", "$1");
                })
                .doOnError(WebClientResponseException.class, e -> {
                    logger.error("Error fetching token: {}", e.getMessage());
                });
    }

    public Mono<String> getCachedToken(String id) {
        String cachedToken = tokenCache.get();
        if (cachedToken != null) {
            return Mono.just(cachedToken);
        } else {
            return getToken(id)
                    .doOnNext(tokenCache::set);
        }
    }

    public Mono<String> callApplication(String id) {
        return getCachedToken(id)
                .flatMap(token -> webClient.get()
                        .uri(applicationUrl)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .retrieve()
                        .bodyToMono(String.class)
                        .doOnError(WebClientResponseException.class, e -> {
                            logger.error("Error calling application: {}", e.getMessage());
                        }));
    }

    public Mono<String> callOtherService(String id, Object requestBody) {
        return getCachedToken(id)
                .flatMap(token -> webClient.post()
                        .uri(otherUrl)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(requestBody)
                        .retrieve()
                        .bodyToMono(String.class)
                        .doOnError(WebClientResponseException.class, e -> {
                            logger.error("Error calling other service: {}", e.getMessage());
                        }));
    }
}
