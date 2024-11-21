package MatrixProcessing;

import java.util.Scanner;

public class Util {

    public static int getUserChoice(int[] possibleChoices) {
        while (true) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Your choice: > ");
            String choice = scanner.nextLine().trim();

            try {
                int choiceInt = Integer.parseInt(choice);
                for (int value : possibleChoices) {
                    if (choiceInt == value) {
                        return choiceInt;
                    }
                }
            } catch (NumberFormatException ignored) {}

            System.out.println("Please enter a value between "
                    + possibleChoices[0] + " and " + possibleChoices[possibleChoices.length - 1]);
        }
    }
}
