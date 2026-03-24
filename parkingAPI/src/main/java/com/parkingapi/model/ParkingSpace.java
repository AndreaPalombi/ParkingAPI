package com.parkingapi.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ParkingSpace {
		
	private final int spaceNumber;
    private boolean occupied;
    private String vehicleReg;
    private VehicleType vehicleType;
    
	private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private String timeIn;
    
    
    public ParkingSpace(int spaceNumber) {
        this.spaceNumber = spaceNumber;
        this.occupied = false;
    }
    
    public void park(String vehicleReg, VehicleType vehicleType) {
        this.occupied    = true;
        this.vehicleReg  = vehicleReg;
        this.vehicleType = vehicleType;
        this.timeIn      = LocalDateTime.now().format(FORMATTER);
    }
    
    public void clearOut() {
        this.occupied    = false;
        this.vehicleReg  = null;
        this.vehicleType = null;
        this.timeIn      = null;
    }
    
    public int getSpaceNumber() { 
    	return spaceNumber; 
    }
    
    public boolean isOccupied() { 
    	return occupied; 
    	}
    
    public String getVehicleReg() { 
    	return vehicleReg; 
    	}
    
    public VehicleType getVehicleType() { 
    	return vehicleType; 
    	}
    
    public String getTimeIn() { 
    	return timeIn; 
    	}
    
   

}
