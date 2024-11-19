package com.md.client.mock;

import com.md.client.MaintenanceClient;
import com.md.model.MaintenanceInfo;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
@Requires(env = Environment.TEST)
@Replaces(MaintenanceClient.class)
public class MockMaintenanceClient implements MaintenanceClient {

    @Override
    public Mono<MaintenanceInfo> getMaintenanceInfo(String vin, String requestId) {
        logger.info("Call to 3rd party: request_id={}, service=Maintenance", requestId);
        // Mock implementation
        return Mono.just(new MaintenanceInfo("medium"));
    }
}
