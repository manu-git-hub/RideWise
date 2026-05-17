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

        RideMatchingStrategy matchingStrategy = selectMatchingStrategy(input);
        FareStrategy fareStrategy = selectFareStrategy(input);

        RiderService riderService = new RiderService();
        DriverService driverService = new DriverService();
        RideService rideService = new RideService(driverService, matchingStrategy, fareStrategy);


        new RideWiseApp(riderService, driverService, rideService, input).run();

        scanner.close();
    }


    private static RideMatchingStrategy selectMatchingStrategy(ConsoleInput input) {
        System.out.println();
        System.out.println("  Select driver matching strategy:");
        System.out.println("    1. Nearest Driver        (default)");
        System.out.println("    2. Least Active Driver");
        int choice = input.readInt("  Choice [1/2]: ", 1, 2);
        return choice == 2 ? new LeastActiveDriverStrategy() : new NearestDriverStrategy();
    }

    private static FareStrategy selectFareStrategy(ConsoleInput input) {
        System.out.println();
        System.out.println("  Select fare strategy:");
        System.out.println("    1. Default Fare   (flat rate, always)   (default)");
        System.out.println("    2. Peak-Hour Fare (1.5x surge 08-10 & 17-20)");
        int choice = input.readInt("  Choice [1/2]: ", 1, 2);
        return choice == 2 ? new PeakHourFareStrategy() : new DefaultFareStrategy();
    }


}
