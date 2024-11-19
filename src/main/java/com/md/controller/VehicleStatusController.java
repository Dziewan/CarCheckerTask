package com.md.controller;

import com.md.model.Response;
import com.md.service.VehicleStatusService;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.validation.Validated;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Controller("/vehicle-status")
@Validated
public class VehicleStatusController {

    private static final Logger logger = LoggerFactory.getLogger(VehicleStatusController.class);

    @Inject
    private VehicleStatusService vehicleStatusService;

    @Get
    public Mono<Response> checkVehicleStatus(@QueryValue @Pattern(regexp = "[A-HJ-NPR-Z0-9]{17}", message = "Invalid VIN format") String vin,
                                             @QueryValue @NotEmpty List<String> features) {
        String requestId = UUID.randomUUID().toString();
        logger.info("Request received: request_id={}, vin={}", requestId, vin);

        return vehicleStatusService.checkVehicleStatus(vin, features, requestId)
                .doOnSuccess(response -> logger.info("Request processed: request_id={}", requestId))
                .doOnError(error -> logger.error("Request failed: request_id={}, error={}", requestId, error.getMessage()));
    }
}
