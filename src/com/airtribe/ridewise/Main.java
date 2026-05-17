package com.airtribe.ridewise;

import com.airtribe.ridewise.console.ConsoleInput;
import com.airtribe.ridewise.console.RideWiseApp;
import com.airtribe.ridewise.service.DriverService;
import com.airtribe.ridewise.service.RideService;
import com.airtribe.ridewise.service.RiderService;
import com.airtribe.ridewise.strategy.*;

import java.util.Scanner;

public final class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsoleInput input = new ConsoleInput(scanner);

        RideMatchingStrategy matchingStrategy = new SmartMatchingStrategy();
        FareStrategy fareStrategy = new PeakHourFareStrategy();

        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();
        RideService rideService = new RideService(driverService, matchingStrategy, fareStrategy);

        new RideWiseApp(riderService, driverService, rideService, input).run();

        scanner.close();
    }


}
