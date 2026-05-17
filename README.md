# RideWise - Ride-Sharing System (LLD)

RideWise is a console-based Ride-Sharing System (similar to Uber/Ola) developed in Java. It is designed to demonstrate high-quality Low-Level Design (LLD), adhering strictly to Object-Oriented Programming (OOP) principles, SOLID design, and the Strategy pattern.

The goal of this project is not to build a full-fledged database-backed web application, but to showcase maintainable, decoupled, and extensible domain modeling.

---

## Functional Requirements Implemented
The application fully implements the following core flows:
1. **Register Riders**: Add a rider with a name and location.
2. **Register Drivers**: Add a driver with a name, location, and `VehicleType` (BIKE, AUTO, CAR).
3. **Show Available Drivers**: View drivers currently ready to take a ride.
4. **Request a Ride**: A rider specifies a distance, and the system finds a driver.
5. **Match Ride to Driver**: Uses an interchangeable `RideMatchingStrategy` to allocate drivers.
6. **Calculate Fare**: Uses an interchangeable `FareStrategy` to calculate the final trip cost upon completion.
7. **Track Ride Status**: Rides move strictly through `REQUESTED` -> `ASSIGNED` -> `COMPLETED` or `CANCELLED`.

---

##  Project Structure



### Directory Tree
```text
src/com/airtribe/ridewise/
├── Main.java                        # Composition root
│
├── console/                         # UI Layer
│   ├── RideWiseApp.java             # Main interactive menu loop
│   ├── ConsoleInput.java            # Robust input reading and validation
│   └── ConsoleOutput.java           # Standardized UI printing and banners
│
├── model/                           # Domain Layer (State & Entities)
│   ├── Rider.java                   # Value object: id, name, location
│   ├── Driver.java                  # Entity: id, name, loc, vehicleType, available, trips
│   ├── Ride.java                    # Entity: life-cycle state machine
│   ├── FareReceipt.java             # Immutable receipt (composed in Ride)
│   ├── RideStatus.java              # Enum: REQUESTED | ASSIGNED | COMPLETED | CANCELLED
│   └── VehicleType.java             # Enum: BIKE | AUTO | CAR
│
├── strategy/                        # Algorithm Layer
│   ├── RideMatchingStrategy.java    # Interface — ISP: single method
│   ├── NearestDriverStrategy.java   # Impl: lexicographic proximity heuristic
│   ├── LeastActiveDriverStrategy.java # Impl: fewest completed trips
│   ├── FareStrategy.java            # Interface — ISP: single method
│   ├── DefaultFareStrategy.java     # Impl: ₹15 + ₹12/km
│   └── PeakHourFareStrategy.java    # Impl: 1.5× surge during peak hours
│
├── service/                         # Orchestration Layer
│   ├── RiderService.java            # Rider CRUD
│   ├── DriverService.java           # Driver CRUD + availability management
│   └── RideService.java             # Ride orchestration (depends on abstractions)
│
├── exception/                       # Custom Errors
│   ├── EntityNotFoundException.java 
│   └── NoDriverAvailableException.java  
│
└── util/
    └── IdGenerator.java             # Thread-safe (AtomicInteger) ID generation
```

---

## Source Code Architecture (`src/com/airtribe/ridewise`)

The source code is strictly divided into focused packages to ensure high cohesion and low coupling:

### 1. `model/` (Domain Entities)
Contains the core plain Java objects representing the business.
- `Rider.java`: Stores rider identity and location.
- `Driver.java`: Stores driver identity, vehicle type, current location, and availability state.
- `Ride.java`: The core state machine. Maintains the association between a Rider and a Driver, and prevents invalid state transitions (e.g., cannot complete a cancelled ride).
- `FareReceipt.java`: An immutable receipt generated upon ride completion.
- `RideStatus.java` / `VehicleType.java`: Enums governing strictly typed states.

### 2. `strategy/` (Algorithm Implementations)
Implements the **Strategy Pattern** to ensure the **Open-Closed Principle (OCP)**.
- **`RideMatchingStrategy.java`**: Interface for matching logic.
  - `NearestDriverStrategy.java`: Matches the driver physically closest to the rider using a string-heuristic comparison.
  - `LeastActiveDriverStrategy.java`: Matches the driver with the fewest total completed trips to ensure fair workload distribution.
- **`FareStrategy.java`**: Interface for pricing logic.
  - `DefaultFareStrategy.java`: Calculates based on a flat Rs 15 base + Rs 12 per kilometer.
  - `PeakHourFareStrategy.java`: Applies a 1.5x surge multiplier during specific hours.

### 3. `service/` (Business Logic Orchestration)
Services orchestrate the entities but depend on abstractions (interfaces) rather than concrete algorithms, fulfilling the **Dependency Inversion Principle (DIP)**.
- `RiderService.java`: Manages the in-memory store of riders.
- `DriverService.java`: Manages drivers and toggles their availability status.
- `RideService.java`: The core orchestrator. It receives `DriverService`, `RideMatchingStrategy`, and `FareStrategy` via constructor injection. It handles booking, cancelling, and completing rides.

### 4. `console/` (User Interface)
Isolates the terminal I/O from the business logic.
- `RideWiseApp.java`: Contains the main `while(running)` menu loop and handles routing user choices to the correct service methods.
- `ConsoleInput.java`: Robust input wrapper that prevents the app from crashing when a user types letters instead of numbers.
- `ConsoleOutput.java`: Centralized formatting logic for printing banners, menus, and receipts.

### 5. `exception/` & `util/`
- `EntityNotFoundException.java` & `NoDriverAvailableException.java`: Custom semantic exceptions replacing generic Java errors.
- `IdGenerator.java`: Uses thread-safe `AtomicInteger` to guarantee unique, sequential IDs (`RDR-001`, `DRV-001`, `RIDE-0001`).

---

## Entry Point (`Main.java`)
The `Main.java` file acts exclusively as the **Composition Root**. It does not contain business logic. Instead, it:
1. Prompts the user to select which concrete strategies to use for this session.
2. Instantiates the Services and injects the chosen strategies.
3. Passes the services into `RideWiseApp` to begin the interactive menu.

---

## How to Run the Application

1. Open a terminal and navigate to the `src` directory of the project:
   ```bash
   cd "Modular Ride/src"
   ```
2. Compile all Java files into a separate `out` directory:
   ```bash
   javac -d ../out $(find . -name "*.java")
   ```
3. Run the compiled `Main` class:
   ```bash
   java -cp ../out com.airtribe.ridewise.Main
   ```
4. You will be prompted to select your Matching and Fare strategies. After selection, the main menu will appear allowing you to register users and book rides.
