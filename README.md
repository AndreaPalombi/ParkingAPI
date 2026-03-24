# Parking API - lightweight RESTful web service built with Spring Boot 4 and Java 21

#  OVERVIEW
It manages a car park with 20 spaces entirely in memory. No database or file system is required. The API allows vehicles to be parked, tracked and charged based on how long they stay.

#  WHAT DOES THIS API DO?
Think of this API as the brain behind a simple car park ticketing system. Here is what it can do in plain terms:

•Check how many spaces are free and how many are taken
•Let a car in by recording its registration plate and assigning it a space number
•Calculate how much a driver owes when they leave, based on how long they were parked
•Free up the space so another car can use it

Parking charges are calculated per minute, with an extra £1.00 added for every 5 minutes parked:

Vehicle Type	Rate per Minute	Extra per 5 min
Small Car	£0.10	£1.00
Medium Car	£0.20	£1.00
Big Car	£0.40	£1.00

#  PROJECT STRUCTURE
Project Structure
src/main/java/com/parkingAPI/
  ├── ParkingApiApplication.java     Entry point
  ├── controller/
  │   └── ParkingController.java     REST endpoints
  ├── service/
  │   └── ParkingService.java        Business logic
  ├── model/
  │   ├── ParkingSpace.java          Domain model
  │   ├── VehicleType.java           Enum: small, medium, big
  │   └── Dto.java                   Request/Response records
  └── exception/
      ├── GlobalExceptionHandler.java  Centralised error handling
      ├── ParkingFullException.java
      ├── VehicleNotFoundException.java
      └── VehicleAlreadyParkedException.java


#  SETTING UP LOCALLY
- Prerequisites
Make sure you have the following installed before cloning the project:

•Java 21
•Maven 3.8+
•Git
•An IDE (Eclipse or IntelliJ IDEA)
•Postman (for testing the API manually)

- Clone from GitHub
Open a terminal and run:
git clone https://github.com/AndreaPalombi/parkingAPI.git
cd parkingAPI

- Open in Eclipse
1.Open Eclipse and go to File → Import
2.Select Maven → Existing Maven Projects → Next
3.Browse to the cloned folder and click Finish
4.Right-click pom.xml → Maven → Reload Project to download dependencies

- Run the Application
via Eclipse:
•Right-click ParkingApiApplication.java → Run As → Java Application
or
via terminal:
mvn spring-boot:run

The API starts on default port 8080.

Run the Tests
via Eclipse:
•Right-click the project → Run As → Maven Test



# API ENDPOINTS
-  GET /parking (http://localhost:8080/parking): Check Availability
Returns how many spaces are free and how many are taken. No request body needed.

-  POST /parking (http://localhost:8080/parking): Park a Vehicle
Parks a vehicle in the first available space and returns its assigned space number and entry time.
Valid values for vehicleType: SMALL, MEDIUM, BIG (N.B. The vehicleType MUST be CAPITAL letters).

raw JSON body type:

{
  "vehicleReg": "AB12 CDE",
  "vehicleType": "SMALL"
}

- POST /parking/bill (http://localhost:8080/parking/bill): Check Out a Vehicle
Calculates the parking charge, frees up the space and returns the final bill.

raw JSON body type:
{
  "vehicleReg": "AB12 CDE"
}


# NOTES
•Data is stored in memory only. Restarting the application resets all parked vehicles and spaces
•The car park has 20 spaces by default (configurable via TOTAL_SPACES in ParkingService)
•Vehicle registrations are case-insensitive and stored in upper case
•Dates and times are formatted as: yyyy-MM-dd HH:mm:ss
•The API does not persist data to disk or a database


#  PERSONAL NOTES
This API has been made as a small project sample and it's not perfect. Basic exceptions handling has been implemented and should prevent the majority of errors at runtime, however not all scenarios are handled by the controller or service layers.
Moreover it is a backend solution, so no UI or frontend components have been included, leaving scope or further modification and enhancements, such as the development of persistence/DAO layers, a graphical interface and new features.
