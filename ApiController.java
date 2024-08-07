package com.example.webfluxapp.controller;

import com.example.webfluxapp.service.ApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ApiController {

    private final ApiService apiService;

    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    @GetMapping("/fetch/{id}")
    public Mono<String> fetch(@PathVariable String id) {
        return apiService.callApplication(id);
    }

    @PostMapping("/other/{id}")
    public Mono<String> callOther(@PathVariable String id, @RequestBody Object requestBody) {
        return apiService.callOtherService(id, requestBody);
    }
}
