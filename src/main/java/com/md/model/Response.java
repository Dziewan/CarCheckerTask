package com.md.model;

import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Serdeable
public class Response {
    private String requestId;
    private String vin;
    private Boolean accidentFree;
    private String maintenanceScore;
}
