# SOLID Principles Reflection

The RideWise application was strictly built adhering to the five SOLID principles of Object-Oriented Design. Below is an elaborated breakdown of how each principle is fulfilled in the final architecture.

---

## 1. Single Responsibility Principle (SRP)
*A class should have one, and only one, reason to change.*

- **`RiderService` & `DriverService`**: Focus purely on the CRUD operations and state tracking for their respective entities.
- **`RideService`**: Focuses purely on orchestrating a ride (booking, completing, cancelling). It does not compute fares, nor does it search for nearest drivers.
- **`ConsoleInput` / `ConsoleOutput`**: Separate concerns of reading sanitized inputs and formatting UI banners. `RideWiseApp` doesn't worry about catching `InputMismatchException` because `ConsoleInput` handles it.

## 2. Open-Closed Principle (OCP)
*Software entities should be open for extension, but closed for modification.*

- **Strategies**: The system defines the interfaces `RideMatchingStrategy` and `FareStrategy`.
- We added `SmartMatchingStrategy` and `PeakHourFareStrategy` without modifying a single line of the core `RideService` class. The engine is entirely closed for modification but infinitely open for new algorithms.
- **Vehicle Filtering**: We added the ability for users to specify a `VehicleType` (BIKE, AUTO, CAR). Instead of altering the `RideMatchingStrategy` interface, we dynamically filtered the stream of available drivers *before* passing them to the strategy. This preserved OCP flawlessly.

## 3. Liskov Substitution Principle (LSP)
*Subtypes must be substitutable for their base types without altering program correctness.*

- Any class implementing `RideMatchingStrategy` (whether it's `NearestDriverStrategy`, `LeastActiveDriverStrategy`, or `SmartMatchingStrategy`) can be injected into `RideService` and the application will behave correctly.
- The `findDriver()` method consistently returns a `Driver` object (or `null`), ensuring the `RideService` doesn't need to perform any type-checks or `instanceof` operations on the strategy.

## 4. Interface Segregation Principle (ISP)
*Clients should not be forced to depend upon interfaces that they do not use.*

- We have highly cohesive, single-method functional interfaces. 
- `FareStrategy` only requires `calculateFare(Ride)`.
- `RideMatchingStrategy` only requires `findDriver(Rider, List<Driver>)`.
- We did not bundle these together into a massive `RideRulesEngine` interface. Classes only implement what they specifically need.

## 5. Dependency Inversion Principle (DIP)
*High-level modules should not depend on low-level modules. Both should depend on abstractions.*

- `RideService` (high-level orchestration) does not depend on `NearestDriverStrategy` or `DefaultFareStrategy` (low-level implementations). 
- It depends entirely on the `RideMatchingStrategy` and `FareStrategy` interfaces.
- The **Composition Root** (`Main.java`) wires the concrete low-level implementations and injects them into the high-level services at startup. This automated injection ensures our codebase remains perfectly decoupled.

---

### Bonus: Design Patterns Used
1. **Strategy Pattern**: For algorithmic interchangeability (`FareStrategy`, `RideMatchingStrategy`).
2. **Composite Pattern**: `SmartMatchingStrategy` encapsulates both the nearest and least active strategies, evaluating them dynamically.
3. **Decorator Pattern**: `PeakHourFareStrategy` encapsulates the `DefaultFareStrategy`, fetching the base rate and selectively applying a multiplier.
