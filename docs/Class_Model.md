# Class Model

The following Mermaid diagram visualizes the structural relationships between the core entities, strategies, and services within the RideWise application.

```mermaid
classDiagram
    %% Core Entities
    class Rider {
        -String id
        -String name
        -String location
    }

    class Driver {
        -String id
        -String name
        -VehicleType vehicleType
        -String currentLocation
        -boolean available
        -int totalTripsCompleted
    }

    class Ride {
        -String id
        -Rider rider
        -Driver driver
        -double distanceKm
        -RideStatus status
        -FareReceipt receipt
    }

    class FareReceipt {
        -String rideId
        -double amount
        -LocalDateTime generatedAt
    }

    %% Enums
    class VehicleType {
        <<enumeration>>
        BIKE
        AUTO
        CAR
    }

    class RideStatus {
        <<enumeration>>
        REQUESTED
        ASSIGNED
        COMPLETED
        CANCELLED
    }

    %% Strategies
    class RideMatchingStrategy {
        <<interface>>
        +findDriver(Rider, List~Driver~): Driver
        +label(): String
    }

    class SmartMatchingStrategy {
        -NearestDriverStrategy nearest
        -LeastActiveDriverStrategy leastActive
        +findDriver(Rider, List~Driver~): Driver
    }

    class FareStrategy {
        <<interface>>
        +calculateFare(Ride): double
        +label(): String
    }

    class PeakHourFareStrategy {
        -DefaultFareStrategy defaultFareStrategy
        +calculateFare(Ride): double
    }

    %% Services
    class RideService {
        -DriverService driverService
        -RideMatchingStrategy matchingStrategy
        -FareStrategy fareStrategy
        +requestRide(Rider, distance, VehicleType): Ride
        +completeRide(rideId): FareReceipt
        +cancelRide(rideId)
    }

    %% Composition & App
    class Main {
        +main(args)
    }

    class RideWiseApp {
        -RiderService riderService
        -DriverService driverService
        -RideService rideService
        +run()
    }

    %% Relationships
    Ride "1" *-- "1" Rider : Has
    Ride "1" *-- "1" Driver : Assigned To
    Ride "1" *-- "1" FareReceipt : Generates
    Ride --> RideStatus : state
    Driver --> VehicleType : type
    
    RideMatchingStrategy <|.. SmartMatchingStrategy : implements
    FareStrategy <|.. PeakHourFareStrategy : implements

    RideService o-- RideMatchingStrategy : uses
    RideService o-- FareStrategy : uses
    RideService o-- DriverService : uses

    RideWiseApp o-- RideService : orchestrates
    Main --> RideWiseApp : composes
```

## Key Architectural Notes
1. **Enums**: `VehicleType` prevents arbitrary string data for vehicles. `RideStatus` strictly limits ride lifecycle states.
2. **Strategy Interfaces**: Ensure `RideService` does not have hardcoded `if-else` loops for calculating pricing or matching drivers.
3. **Composites**: `SmartMatchingStrategy` and `PeakHourFareStrategy` visually demonstrate composition by internally holding references to the base strategies they wrap.
4. **Decoupling**: `Main` is the only class aware of the concrete strategy implementations (`SmartMatchingStrategy`, `PeakHourFareStrategy`). `RideWiseApp` is only aware of the Services.
