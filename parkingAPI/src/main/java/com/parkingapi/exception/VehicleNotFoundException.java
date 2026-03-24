package com.parkingapi.exception;

public class VehicleNotFoundException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;

	public VehicleNotFoundException(String vehicleReg) {
        super("Vehicle '" + vehicleReg + "' is not currently parked.");
    }
}
