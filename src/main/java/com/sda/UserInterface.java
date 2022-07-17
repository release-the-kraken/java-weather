package com.sda;

import java.util.Scanner;

public class UserInterface {
    public void run(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Application running\n");

        while (true) {
            System.out.println("Welcome to Weather App.\nWhat would you like to do?");
            System.out.println(" 0. Close app");

            int option = scanner.nextInt();

            switch (option) {
                case 0:
                    return;
            }
        }
    }
}
