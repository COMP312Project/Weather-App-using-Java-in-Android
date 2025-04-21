package com.cli;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=====Welcome to Weather App!=====");
        System.out.println("Type a city name to get weather (or 'exit' to quit).");
        System.out.println("Please enter your weather details: ");

        while (true) {
            System.out.print("Enter your city name: ");
            String cityName = scanner.nextLine();

            // Leave app if user types 'exit'...
            if (cityName.equals("exit")) {
                System.out.println("Goodbye!");
                break;
            }
            System.out.println("Pretend we're going to get the weather for " + cityName + "...");
        }
        scanner.close(); // close scanner
    }
}