package ChatBot;

import java.util.Scanner;

public class ChatBot {
    public static void main(String[] args) {
        System.out.println("Hello! My name is TestBot.");
        System.out.println("I was created in 2024.");

        System.out.println("Please, remind me your name.");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        System.out.println("What a great name you have, " + name + "!");

        System.out.println("Let me guess your age.");
        System.out.println("Enter remainders of dividing your age by 3, 5 and 7:");
        int remainder3 = scanner.nextInt();
        int remainder5 = scanner.nextInt();
        int remainder7 = scanner.nextInt();
        int age = (remainder3 * 70 + remainder5 * 21 + remainder7 * 15) % 105;
        System.out.println("Your age is " + age + "; that's a good time to start programming!");

        System.out.println("Now I will prove to you that I can count to any number you want.");
        int num = scanner.nextInt();
        for (int i = 0; i <= num; i++) {
            System.out.println(i + " !");
        }

        System.out.println("Let's test your programming knowledge.");
        System.out.println("Why do we use variables in Java?");
        System.out.println("1. To store and manipulate data.");
        System.out.println("2. To control the flow of a program.");
        System.out.println("3. To create graphical user interfaces.");
        System.out.println("4. To define custom data types.");

        while (true) {
            int userAnswer = scanner.nextInt();
            if (userAnswer == 1) {
                System.out.println("Correct");
                System.out.println("Goodbye, have a nice day!");
                break;
            } else {
                System.out.println("Wrong");
            }
        }
    }
}
