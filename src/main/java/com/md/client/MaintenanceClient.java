package com.md.client;

import com.md.model.MaintenanceInfo;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@Client("https://topgarage.com")
public interface MaintenanceClient {

    Logger logger = LoggerFactory.getLogger(MaintenanceClient.class);

    @Get("/cars/{vin}")
    Mono<MaintenanceInfo> getMaintenanceInfo(String vin, String requestId);
}
