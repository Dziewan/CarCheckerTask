package com.md.controller;

import com.md.model.Response;
import io.micronaut.context.env.Environment;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.rxjava2.http.client.RxHttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

@MicronautTest(environments = Environment.TEST)
public class VehicleStatusControllerTest {

    @Inject
    @Client("/")
    RxHttpClient client;

    @Test
    void testCheckVehicleStatusEndpoint() {
        String vin = "4Y1SL65848Z411439";
        List<String> features = List.of("accident_free", "maintenance");

        HttpRequest<?> request = HttpRequest.GET("/vehicle-status?vin=" + vin + "&features=" + String.join(",", features));

        Mono<Response> responseMono = Mono.from(client.retrieve(request, Response.class));

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.getAccidentFree() != null && response.getMaintenanceScore() != null)
                .verifyComplete();
    }
}
