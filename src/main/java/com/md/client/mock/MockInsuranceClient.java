package com.md.client.mock;

import com.md.client.InsuranceClient;
import com.md.model.InsuranceReport;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
@Requires(env = Environment.TEST)
@Replaces(InsuranceClient.class)
public class MockInsuranceClient implements InsuranceClient {

    @Override
    public Mono<InsuranceReport> getAccidentReport(String vin, String requestId) {
        logger.info("Call to 3rd party: request_id={}, service=Insurance", requestId);
        // Mock implementation
        return Mono.just(new InsuranceReport(0));
    }
}
