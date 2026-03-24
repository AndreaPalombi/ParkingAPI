package com.parkingapi.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VehicleType {

	SMALL("small", 0.10), 
	MEDIUM("medium", 0.20), 
	BIG("big", 0.40);

	private final String label;
	private final double ratePerMinute;

	VehicleType(String label, double ratePerMinute) {

		this.label = label;
		this.ratePerMinute = ratePerMinute;

	}

	public double getRatePerMinute() {
		return ratePerMinute;
	}

	public String getLabel() {
		return label;
	}
	
	@JsonValue
	public static VehicleType fromLabel(String label) {
        for (VehicleType type : values()) {
            if (type.label.equalsIgnoreCase(label)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
            "Unknown vehicleType '" + label + "'. Valid values: small, medium, big"
        );
    }

}
