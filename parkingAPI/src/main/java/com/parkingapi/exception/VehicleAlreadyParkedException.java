package com.parkingapi.exception;

public class VehicleAlreadyParkedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public VehicleAlreadyParkedException(String vehicleReg) {
        super("Vehicle '" + vehicleReg + "' is already parked.");
    }

}
