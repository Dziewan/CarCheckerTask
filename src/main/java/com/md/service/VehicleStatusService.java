package com.md.service;

import com.md.client.InsuranceClient;
import com.md.client.MaintenanceClient;
import com.md.exception.ExternalServiceException;
import com.md.model.Response;
import com.md.model.enumeration.Feature;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class VehicleStatusService {
    private static final Logger logger = LoggerFactory.getLogger(VehicleStatusService.class);
    private static final String EMPTY = "";
    private static final Integer MAX_RETRIES = 3;

    private final InsuranceClient insuranceClient;

    private final MaintenanceClient maintenanceClient;

    public Mono<Response> checkVehicleStatus(String vin, List<String> features, String requestId) {
        return Mono.zip(getAccidentFree(vin, features, requestId), getMaintenance(vin, features, requestId))
                .map(tuple -> Response.builder()
                        .requestId(requestId)
                        .vin(vin)
                        .accidentFree(tuple.getT1())
                        .maintenanceScore(tuple.getT2())
                        .build());
    }

    private Mono<Boolean> getAccidentFree(String vin, List<String> features, String requestId) {
        if (Feature.isNotAccidentFree(features)) {
            return Mono.just(true);
        }
        return insuranceClient.getAccidentReport(vin, requestId)
                .map(report -> report.getClaims() == 0)
                .retry(MAX_RETRIES)
                .doOnError(error -> logger.error("Error in Insurance service: request_id={}, error={}", requestId, error.getMessage()))
                .onErrorResume(error -> Mono.error(new ExternalServiceException("Failed to retrieve accident information")));
    }

    private Mono<String> getMaintenance(String vin, List<String> features, String requestId) {
        if (Feature.isNotMaintenance(features)) {
            return Mono.just(EMPTY);
        }
        return maintenanceClient.getMaintenanceInfo(vin, requestId)
                .mapNotNull(info -> mapMaintenanceScore(info.getMaintenanceFrequency()))
                .retry(MAX_RETRIES)
                .doOnError(error -> logger.error("Error in Maintenance service: request_id={}, error={}", requestId, error.getMessage()))
                .onErrorResume(error -> Mono.error(new ExternalServiceException("Failed to retrieve maintenance information")));
    }

    private String mapMaintenanceScore(String frequency) {
        return switch (frequency) {
            case "very_low", "low" -> "poor";
            case "medium" -> "average";
            case "high" -> "good";
            default -> null;
        };
    }
}
