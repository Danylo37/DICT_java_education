package CoffeeMachine;

import java.util.Map;
import java.util.Scanner;

public class CoffeeMachine {

    private static final Scanner scanner = new Scanner(System.in);

    private static final Map<String, Integer> ESPRESSO = Map.of(
            "water", 250,
            "milk", 0,
            "coffeeBeans", 16,
            "price", 4
    );

    private static final Map<String, Integer> LATTE = Map.of(
            "water", 350,
            "milk", 75,
            "coffeeBeans", 20,
            "price", 7
    );

    private static final Map<String, Integer> CAPPUCCINO = Map.of(
            "water", 200,
            "milk", 100,
            "coffeeBeans", 12,
            "price", 6
    );

    private int water = 400;
    private int milk = 540;
    private int coffeeBeans = 120;
    private int cups = 9;
    private int money = 550;

    private int positiveIntInput(String message) {
        while (true) {
            try {
                System.out.print(message);
                int input = Integer.parseInt(scanner.nextLine());
                if (input < 0) {
                    System.out.println("Please enter a positive value.");
                } else {
                    return input;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a positive numeric value.");
            }
        }
    }

    private boolean isEnoughResource(Map<String, Integer> resources) {
        if (water < resources.get("water")) {
            System.out.println("Sorry, not enough water!");
            return false;
        } else if (milk < resources.get("milk")) {
            System.out.println("Sorry, not enough milk!");
            return false;
        } else if (coffeeBeans < resources.get("coffeeBeans")) {
            System.out.println("Sorry, not enough coffee beans!");
            return false;
        } else if (cups < 1) {
            System.out.println("Sorry, not enough cups!");
            return false;
        }
        return true;
    }

    private void makeCoffee(Map<String, Integer> resources) {
        water -= resources.get("water");
        milk -= resources.get("milk");
        coffeeBeans -= resources.get("coffeeBeans");
        cups--;
        money += resources.get("price");
        System.out.println("I have enough resources, making you a coffee!");
    }

    private void buy() {
        System.out.println("\nWhat do you want to buy?");
        System.out.println("1 - espresso\n2 - latte\n3 - cappuccino\nback – to main menu");

        System.out.print("> ");
        String choice = scanner.nextLine();
        if (choice.equals("back")) {
            return;
        }

        switch (choice) {
            case "1":
                if (isEnoughResource(ESPRESSO)) {
                    makeCoffee(ESPRESSO);
                }
                break;

            case "2":
                if (isEnoughResource(LATTE)) {
                    makeCoffee(LATTE);
                }
                break;

            case "3":
                if (isEnoughResource(CAPPUCCINO)) {
                    makeCoffee(CAPPUCCINO);
                }
                break;

            default:
                System.out.println("Please choose 1, 2, 3 or \"back\".");
                break;
        }
    }

    private void fill() {
        System.out.println();
        water += positiveIntInput("Write how many ml of water do you want to add:\n> ");
        milk += positiveIntInput("Write how many ml of milk do you want to add:\n> ");
        coffeeBeans += positiveIntInput("Write how many grams of coffee beans do you want to add:\n> ");
        cups += positiveIntInput("Write how many disposable cups of coffee do you want to add:\n> ");
    }

    private void take() {
        System.out.printf("I gave you %d$\n", money);
        money = 0;
    }

    private void getRemaining() {
        System.out.println("\nThe coffee machine has:");
        System.out.println(water + " ml of water");
        System.out.println(milk + " ml of milk");
        System.out.println(coffeeBeans + " g of coffee beans");
        System.out.println(cups + " of disposable cups");
        System.out.println(money + "$ of money");
    }

    public void start() {
        while (true) {
            System.out.print("\nWrite action (buy, fill, take, remaining, exit):\n> ");
            String action = scanner.nextLine();

            switch (action) {
                case "buy":
                    buy();
                    break;

                case "fill":
                    fill();
                    break;

                case "take":
                    take();
                    break;

                case "remaining":
                    getRemaining();
                    break;

                case "exit":
                    return;

                default:
                    System.out.println("Please choose buy, fill, take, remaining, or exit.");
            }
        }
    }

    public static void main(String[] args) {
        CoffeeMachine coffeeMachine = new CoffeeMachine();
        coffeeMachine.start();
    }
}
