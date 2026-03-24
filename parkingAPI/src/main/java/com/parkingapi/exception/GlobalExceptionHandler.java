package com.parkingapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.parkingapi.model.ParkingDTO.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	
    @ExceptionHandler(ParkingFullException.class)
    public ResponseEntity<ErrorResponse> handleParkingFull(ParkingFullException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(VehicleNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(VehicleAlreadyParkedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyParked(VehicleAlreadyParkedException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        ex.printStackTrace();
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        return ResponseEntity
                .status(status)
                .body(new ErrorResponse(status.value(), status.getReasonPhrase(), message));
    }
}