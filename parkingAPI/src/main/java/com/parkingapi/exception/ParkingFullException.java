package com.parkingapi.exception;

public class ParkingFullException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ParkingFullException() {
	        super("Parking lot is full – no available spaces.");
	    }

}
