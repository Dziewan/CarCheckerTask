package com.md.service;

import com.md.client.InsuranceClient;
import com.md.client.MaintenanceClient;
import com.md.model.InsuranceReport;
import com.md.model.MaintenanceInfo;
import com.md.model.Response;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@MicronautTest
public class VehicleStatusServiceTest {

    InsuranceClient insuranceClient = Mockito.mock(InsuranceClient.class);

    MaintenanceClient maintenanceClient = Mockito.mock(MaintenanceClient.class);

    VehicleStatusService vehicleStatusService = new VehicleStatusService(insuranceClient, maintenanceClient);

    @Test
    void testCheckVehicleStatus() {
        String vin = "4Y1SL65848Z411439";
        List<String> features = List.of("accident_free", "maintenance");

        String requestId = UUID.randomUUID().toString();

        when(insuranceClient.getAccidentReport(anyString(), anyString()))
                .thenReturn(Mono.just(new InsuranceReport(0)));

        when(maintenanceClient.getMaintenanceInfo(anyString(), anyString()))
                .thenReturn(Mono.just(new MaintenanceInfo("medium")));

        Mono<Response> responseMono = vehicleStatusService.checkVehicleStatus(vin, features, requestId);

        StepVerifier.create(responseMono)
                .expectNextMatches(response -> response.getAccidentFree() && "average".equals(response.getMaintenanceScore()))
                .verifyComplete();
    }
}
