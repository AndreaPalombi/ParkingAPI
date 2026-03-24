package com.parkingapi.model;


public final class ParkingDTO {

    private ParkingDTO() {}

    public record ParkRequest(
        String vehicleReg,

        VehicleType vehicleType
    ) {}

    public record BillRequest(
        String vehicleReg
    ) {}

    public record ParkingStatusResponse(
        int availableSpaces,
        int occupiedSpaces
    ) {}

    public record ParkResponse(
        String vehicleReg,
        int spaceNumber,
        String timeIn
    ) {}

    public record BillResponse(
        String billId,
        String vehicleReg,
        double vehicleCharge,
        String timeIn,
        String timeOut
    ) {}

    public record ErrorResponse(
        int status,
        String error,
        String message
    ) {}
}