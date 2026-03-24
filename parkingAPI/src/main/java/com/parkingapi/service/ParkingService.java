package com.parkingapi.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.parkingapi.exception.ParkingFullException;
import com.parkingapi.exception.VehicleAlreadyParkedException;
import com.parkingapi.exception.VehicleNotFoundException;
import com.parkingapi.model.ParkingSpace;
import com.parkingapi.model.VehicleType;
import com.parkingapi.model.ParkingDTO.*;

@Service
public class ParkingService {
	
    static final int TOTAL_SPACES = 20;

    static final double SURCHARGE_PER_5_MINUTES = 1.00;

    private final List<ParkingSpace> spaces;
    
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    private static final Logger log = LoggerFactory.getLogger(ParkingService.class);

    public ParkingService() {
        spaces = IntStream.rangeClosed(1, TOTAL_SPACES)
                .mapToObj(ParkingSpace::new)
                .toList();
        log.info("Parking lot initialised with {} spaces", TOTAL_SPACES);
    }
    
    public ParkingStatusResponse getStatus() {
        long occupied = spaces.stream().filter(ParkingSpace::isOccupied).count();
        return new ParkingStatusResponse(
                (int) (TOTAL_SPACES - occupied),
                (int) occupied
        );
    }
    
    public ParkResponse parkVehicle(String vehicleReg, VehicleType vehicleType) {
    	if(vehicleReg == null || vehicleReg.isBlank()) {
    		
    		log.info("Park request rejected. Vehicle Reg field is missing or blank");
    		return new ParkResponse("INVALID", 0, "N/A");
    	}
    	
    	if(vehicleType == null) {
    		log.info("Park requested rejected. Vehicle Type is missing for vehicle [{}]",vehicleReg);
    				return new ParkResponse(vehicleReg,0,"N/A");
    	}
    	
        return parkVehicle(vehicleReg, vehicleType, LocalDateTime.now());
    }
    
    public ParkResponse parkVehicle(String vehicleReg, VehicleType vehicleType, LocalDateTime timeIn) {
        
        boolean alreadyParked = spaces.stream()
                .filter(ParkingSpace::isOccupied)
                .anyMatch(s -> s.getVehicleReg().equalsIgnoreCase(vehicleReg));
        if (alreadyParked) {
        	log.info("Vehicle [{}] is already parked", vehicleReg);
            throw new VehicleAlreadyParkedException(vehicleReg);
        }

        // Find the first free space
        ParkingSpace free = spaces.stream()
                .filter(s -> !s.isOccupied())
                .findFirst()
                .orElseThrow(() -> {
                    log.info("Parking lot is full, cannot park vehicle [{}]", vehicleReg);
                    return new ParkingFullException();
                });

        free.park(vehicleReg.toUpperCase(), vehicleType);

        return new ParkResponse(free.getVehicleReg(), free.getSpaceNumber(), free.getTimeIn());
    }
    

    public BillResponse checkOut(String vehicleReg) {
    	
    	if (vehicleReg == null || vehicleReg.isBlank()) {
            log.info("Checkout request rejected - vehicleReg is missing or blank");
            return new BillResponse("N/A", "INVALID", 0.0, "N/A", "N/A");
        }
    	
    	ParkingSpace space = spaces.stream()
                .filter(ParkingSpace::isOccupied)
                .filter(s -> s.getVehicleReg().equalsIgnoreCase(vehicleReg))
                .findFirst()
                .orElseThrow(() -> {
                	log.info("Vehicle [{}] not found in parking lot", vehicleReg);
                	return new VehicleNotFoundException(vehicleReg);
                });

        String timeIn  = space.getTimeIn();
        String timeOut = LocalDateTime.now().format(FORMATTER);
        double charge  = calculateCharge(space.getVehicleType(), timeIn, timeOut);

        space.clearOut();

        return new BillResponse(
                UUID.randomUUID().toString(),
                vehicleReg.toUpperCase(),
                Math.round(charge * 100.0) / 100.0,
                timeIn,
                timeOut
        );
    
    }
    
    
    double calculateCharge(VehicleType vehicleType, String timeIn, String timeOut) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime in  = LocalDateTime.parse(timeIn, formatter);
        LocalDateTime out = LocalDateTime.parse(timeOut, formatter);
        long minutes = ChronoUnit.MINUTES.between(in, out);

        double base      = minutes * vehicleType.getRatePerMinute();
        double surcharge = Math.floor(minutes / 5.0) * 1.00;
        return base + surcharge;
    }

}
