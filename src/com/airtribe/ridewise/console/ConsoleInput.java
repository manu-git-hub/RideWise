package com.airtribe.ridewise.console;

import java.util.Scanner;


public final class ConsoleInput {

    private static final String WARN = "  [!] ";

    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public int readInt(String prompt, int min, int max) {
        while (true) {
            String raw = prompt(prompt);
            try {
                int v = Integer.parseInt(raw);
                if (v >= min && v <= max) return v;
                System.out.printf("%sEnter a number between %d and %d.%n", WARN, min, max);
            } catch (NumberFormatException e) {
                System.out.println(WARN + "Not a valid number — try again.");
            }
        }
    }

    public double readDouble(String prompt, double min, double max) {
        while (true) {
            String raw = prompt(prompt);
            try {
                double v = Double.parseDouble(raw);
                if (v >= min && v <= max) return v;
                System.out.printf("%sEnter a value between %.1f and %.1f.%n", WARN, min, max);
            } catch (NumberFormatException e) {
                System.out.println(WARN + "Not a valid number — try again.");
            }
        }
    }

    public String readNonBlank(String prompt) {
        while (true) {
            String v = prompt(prompt);
            if (!v.isEmpty()) return v;
            System.out.println(WARN + "Input cannot be blank.");
        }
    }


    public String readLine(String prompt) {
        return prompt(prompt);
    }

    private String prompt(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }
}
