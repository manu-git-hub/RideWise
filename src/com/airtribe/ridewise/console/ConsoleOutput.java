package com.airtribe.ridewise.console;

import com.airtribe.ridewise.model.Driver;
import com.airtribe.ridewise.model.FareReceipt;
import com.airtribe.ridewise.model.Ride;
import com.airtribe.ridewise.model.Rider;

import java.util.Collection;


public final class ConsoleOutput {

    private static final String LINE = "─".repeat(56);
    private static final String OK = "  [OK]  ";
    private static final String ERR = "  [ERR] ";
    private static final String INFO = "  [>>]  ";

    private ConsoleOutput() {
    }


    public static void printBanner(String matchingLabel, String fareLabel) {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════════╗");
        System.out.println("  ║          R I D E W I S E   v1.0                      ║");
        System.out.println("  ╠══════════════════════════════════════════════════════╣");
        System.out.printf("  ║  Matching : %-40s║%n", matchingLabel);
        System.out.printf("  ║  Pricing  : %-40s║%n", fareLabel);
        System.out.println("  ╚══════════════════════════════════════════════════════╝");
    }


    public static void printMenu() {
        System.out.println();
        System.out.println("  " + LINE);
        System.out.println("   MENU");
        System.out.println("  " + LINE);
        System.out.println("   1. Add Rider");
        System.out.println("   2. Add Driver");
        System.out.println("   3. View Available Drivers");
        System.out.println("   4. Request a Ride");
        System.out.println("   5. Complete a Ride");
        System.out.println("   6. Cancel a Ride");
        System.out.println("   7. View All Rides");
        System.out.println("   8. Exit");
        System.out.println("  " + LINE);
    }


    public static void success(String msg) {
        System.out.println(OK + msg);
    }

    public static void error(String msg) {
        System.out.println(ERR + msg);
    }

    public static void info(String msg) {
        System.out.println(INFO + msg);
    }

    public static void sectionHeader(String title) {
        System.out.println();
        System.out.println("  -- " + title.toUpperCase() + " " + "-".repeat(Math.max(0, 48 - title.length())));
    }

    public static void printRider(Rider r) {
        System.out.println("    " + r);
    }

    public static void printDriver(Driver d) {
        System.out.println("    " + d);
    }

    public static void printRide(Ride r) {
        System.out.println("    " + r);
        if (r.getFareReceipt() != null) {
            System.out.println("      └─ " + r.getFareReceipt());
        }
    }

    public static void printReceipt(FareReceipt receipt) {
        System.out.println();
        System.out.println("  ┌─ FARE RECEIPT ─────────────────────────────────────┐");
        System.out.printf("  │  Ride ID    : %-38s│%n", receipt.getRideId());
        System.out.printf("  │  Amount     : Rs %-35.2f│%n", receipt.getAmount());
        System.out.printf("  │  Generated  : %-38s│%n", receipt.getGeneratedAt());
        System.out.println("  └────────────────────────────────────────────────────┘");
    }


    public static void printRiders(Collection<Rider> riders) {
        if (riders.isEmpty()) {
            info("No riders registered yet.");
            return;
        }
        riders.forEach(ConsoleOutput::printRider);
    }

    public static void printDrivers(Collection<Driver> drivers) {
        if (drivers.isEmpty()) {
            info("No available drivers right now.");
            return;
        }
        drivers.forEach(ConsoleOutput::printDriver);
    }

    public static void printRides(Collection<Ride> rides) {
        if (rides.isEmpty()) {
            info("No rides recorded yet.");
            return;
        }
        rides.forEach(ConsoleOutput::printRide);
    }

    public static void printGoodbye() {
        System.out.println();
        System.out.println("  Goodbye! Thank you for using RideWise.");
    }
}
