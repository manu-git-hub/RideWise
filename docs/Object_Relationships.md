# Object Relationships (Runtime)

The following sequence details how the objects interact during the `requestRide` and `completeRide` scenarios.

## 1. Bootstrapping (Composition Root)
- `Main` instantiates `DriverService` and `RiderService`.
- `Main` instantiates `SmartMatchingStrategy` and `PeakHourFareStrategy`.
- `Main` instantiates `RideService`, injecting the services and strategies via the constructor.
- `Main` instantiates `RideWiseApp`, passing in the services, and calls `.run()`.

## 2. Request Ride Flow
1. **User Input**: `RideWiseApp` reads Rider ID, requested `VehicleType`, and distance.
2. **Filtering**: `RideService` queries `DriverService.findAvailable()`. It streams the list and strictly filters out any driver whose `VehicleType` does not match the requested type.
3. **Matching**: The filtered list of drivers is passed to `SmartMatchingStrategy.findDriver()`.
4. **Strategy Execution**: 
   - `SmartMatchingStrategy` passes the list to `NearestDriverStrategy`.
   - If the returned driver is overworked, it passes the list to `LeastActiveDriverStrategy` instead.
5. **Entity Creation**: A new `Ride` object is instantiated in the `REQUESTED` state.
6. **State Mutation**: `Ride.assignDriver(driver)` is called (transitioning state to `ASSIGNED`).
7. **Availability Sync**: `DriverService.markUnavailable(driver)` is called.
8. **Storage**: The `Ride` is saved to the in-memory map.

## 3. Complete Ride Flow
1. **User Input**: `RideWiseApp` asks for a Ride ID.
2. **Fare Calculation**: `RideService` passes the `Ride` object to `PeakHourFareStrategy`.
3. **Strategy Execution**: 
   - `PeakHourFareStrategy` delegates the base fare calculation to `DefaultFareStrategy`.
   - It then conditionally applies the 1.5x surge based on `LocalTime.now()`.
4. **Receipt Generation**: A `FareReceipt` object is instantiated with the final amount.
5. **State Mutation**: `Ride.complete(receipt)` is called (transitioning state to `COMPLETED`).
6. **Availability Sync**: `DriverService.markAvailable(driver)` is called. `driver.incrementTrips()` is also invoked.
7. **UI Output**: `RideWiseApp` passes the `FareReceipt` to `ConsoleOutput` for beautiful rendering.
