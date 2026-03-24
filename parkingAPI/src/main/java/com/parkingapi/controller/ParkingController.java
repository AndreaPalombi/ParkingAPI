package com.parkingapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.parkingapi.model.ParkingDTO.BillRequest;
import com.parkingapi.model.ParkingDTO.BillResponse;
import com.parkingapi.model.ParkingDTO.ParkRequest;
import com.parkingapi.model.ParkingDTO.ParkResponse;
import com.parkingapi.model.ParkingDTO.ParkingStatusResponse;
import com.parkingapi.service.ParkingService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/parking")
public class ParkingController {
	
	private final ParkingService parkingService;
	
	private static final Logger log = LoggerFactory.getLogger(ParkingController.class);

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    /**
     * GET /parking
     * Returns the number of available and occupied spaces.
     */
    @GetMapping
    public ResponseEntity<ParkingStatusResponse> getStatus() {
    	log.info("GET /parking - Fetching parking status");
    	ParkingStatusResponse response = parkingService.getStatus();
    	 
        log.info("GET /parking - Available: {}, Occupied: {}",
                response.availableSpaces(), response.occupiedSpaces());
 
        return ResponseEntity.ok(response);
    }

    /**
     * POST /parking
     * Parks the vehicle in the first available space.
     */
    @PostMapping
    public ResponseEntity<ParkResponse> parkVehicle(@RequestBody ParkRequest request) {
    	log.info("POST /parking - Parking request received for vehicle: [{}], type: [{}]",
                request.vehicleReg(), request.vehicleType());
 
        ParkResponse response = parkingService.parkVehicle(request.vehicleReg(), request.vehicleType());
        
        if(response.vehicleReg() != "INVALID"){
        log.info("POST /parking - Vehicle [{}] parked successfully at space [{}], timeIn: [{}]",
                response.vehicleReg(), response.spaceNumber(), response.timeIn());
        }
 
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
        
    }

    /**
     * POST /parking/bill
     * Checks the vehicle out, frees its space and returns the calculated bill.
     */
    @PostMapping("/bill")
    public ResponseEntity<BillResponse> checkOut(@Valid @RequestBody BillRequest request) {
    	log.info("POST /parking/bill - Checkout request received for vehicle: [{}]",
                request.vehicleReg());
 
        BillResponse response = parkingService.checkOut(request.vehicleReg());
        if(response.vehicleReg() != "INVALID"){
        log.info("POST /parking/bill - Vehicle [{}] checked out. Charge: £{}, timeIn: [{}], timeOut: [{}]",
                response.vehicleReg(), response.vehicleCharge(), response.timeIn(), response.timeOut());
        }
        return ResponseEntity.ok(response);
    }

}
