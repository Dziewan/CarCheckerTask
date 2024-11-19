package com.md.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum Feature {
    ACCIDENT_FREE("accident_free"),
    MAINTENANCE("maintenance");

    private final String value;

    public static boolean isNotAccidentFree(List<String> features) {
        return !features.contains(ACCIDENT_FREE.getValue());
    }

    public static boolean isNotMaintenance(List<String> features) {
        return !features.contains(MAINTENANCE.getValue());
    }
}
