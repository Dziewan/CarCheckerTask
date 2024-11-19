package com.md.client;

import com.md.model.InsuranceReport;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@Client("https://insurance.com")
public interface InsuranceClient {
    Logger logger = LoggerFactory.getLogger(InsuranceClient.class);

    @Get("/accidents/report?vin={vin}")
    Mono<InsuranceReport> getAccidentReport(String vin, String requestId);
}
