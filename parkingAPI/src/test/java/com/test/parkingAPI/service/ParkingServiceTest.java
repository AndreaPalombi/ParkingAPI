package com.test.parkingAPI.service;

import com.parkingapi.model.VehicleType;
import com.parkingapi.model.ParkingDTO.BillResponse;
import com.parkingapi.model.ParkingDTO.ParkResponse;
import com.parkingapi.service.ParkingService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ParkingSimpleTest {

    private ParkingService service;

    @BeforeEach
    void setUp() {
        service = new ParkingService();
    }

    @Test
    void parkVehicle_returnsSpaceAndReg() {
        ParkResponse response = service.parkVehicle("AB12 CDE", VehicleType.SMALL);

        assertThat(response.vehicleReg()).isEqualTo("AB12 CDE");
        assertThat(response.spaceNumber()).isEqualTo(1);
        assertThat(response.timeIn()).isNotBlank();
    }

    @Test
    void checkOut_returnsBill() {
        service.parkVehicle("AB12 CDE", VehicleType.SMALL);
        BillResponse bill = service.checkOut("AB12 CDE");

        assertThat(bill.vehicleReg()).isEqualTo("AB12 CDE");
        assertThat(bill.billId()).isNotBlank();
        assertThat(bill.vehicleCharge()).isGreaterThanOrEqualTo(0.0);
        assertThat(bill.timeIn()).isNotBlank();
        assertThat(bill.timeOut()).isNotBlank();
    }
}