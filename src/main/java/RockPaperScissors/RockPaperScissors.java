package RockPaperScissors;

import java.io.*;
import java.util.*;

public class RockPaperScissors {
    public static void main(String[] args) {
        startMenu();
    }

    public static void startMenu() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter your name: > ");
        String name = scanner.nextLine();
        System.out.println("Hello, " + name);

        System.out.print("Enter the options in format \"rock,paper,scissors\": > ");
        String optionsInput = scanner.nextLine();
        System.out.println("Okay, let's start");

        int userRating = getUserRating(name);

        Map<String, List<String>> rules = optionsInput.isEmpty()
                ? getDefaultRules()
                : getUserRules(Arrays.asList(optionsInput.
                replaceAll(" ", "").
                split(",")));

        while (true) {
            System.out.print("> ");
            String userChoice = scanner.nextLine();

            if (userChoice.equals("!exit")) {
                System.out.println("Bye!");
                break;

            } else if (userChoice.equals("!rules")) {
                printRules(rules);

            } else if (userChoice.equals("!rating")) {
                System.out.println("Your rating: " + userRating);

            } else if (rules.containsKey(userChoice)) {
                userRating += startGame(userChoice, rules);

            } else {
                System.out.println("Invalid input");
            }
        }
    }

    private static int getUserRating(String username) {
        File ratingFile = new File("src/RockPaperScissors/rating");

        try (BufferedReader reader = new BufferedReader(new FileReader(ratingFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts[0].equals(username)) {
                    return Integer.parseInt(parts[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the rating file: " + e.getMessage());
        }
        return 0;
    }

    private static Map<String, List<String>> getDefaultRules() {
        Map<String, List<String>> defaultRules = new HashMap<>();

        defaultRules.put("rock", List.of("scissors"));
        defaultRules.put("paper", List.of("rock"));
        defaultRules.put("scissors", List.of("paper"));

        return defaultRules;
    }

    private static Map<String, List<String>> getUserRules(List<String> options) {
        Map<String, List<String>> rules = new HashMap<>();
        int optionsSize = options.size();

        for (int i = 0; i < optionsSize; i++) {
            List<String> beats = new ArrayList<>();

            for (int j = 1; j <= optionsSize / 2; j++) {
                int k = (i - j + optionsSize) % optionsSize;
                beats.add(options.get(k));
            }
            rules.put(options.get(i), beats);
        }
        return rules;
    }

    private static void printRules(Map<String, List<String>> rules) {
        rules.forEach((option, beats) ->
                System.out.println(option + " can beat: " + String.join(", ", beats)));
    }

    private static int startGame(String userOption, Map<String, List<String>> rules) {
        List<String> options = new ArrayList<>(rules.keySet());
        String computerOption = options.get(new Random().nextInt(options.size()));

        if (rules.get(userOption).contains(computerOption)) {
            System.out.println("Well done. The computer chose " + computerOption + " and failed");
            return 100;
        } else if (userOption.equals(computerOption)) {
            System.out.println("There is a draw (" + userOption + ")");
            return 50;
        } else {
            System.out.println("Sorry, but the computer chose " + computerOption);
            return 0;
        }
    }
}
