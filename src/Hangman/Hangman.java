package Hangman;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Hangman {

    static final String[] WORDS = {"python", "java", "javascript", "php"};
    static final int ATTEMPTS = 8;
    static ArrayList<Character> guessedLetters = new ArrayList<>();
    static String rightWord;
    static char[] hiddenWordLetters;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("HANGMAN");

        while (true) {
            rightWord = getRandomWord();
            hiddenWordLetters = createHiddenWord(rightWord);

            System.out.print("Type 'play' to play the game, 'exit' to quit: > ");
            String playOrExit = scanner.nextLine();

            if ("play".equals(playOrExit)) {
                startGame(scanner, ATTEMPTS);
            } else if ("exit".equals(playOrExit)) {
                break;
            }
        }
        scanner.close();
    }

    public static String getRandomWord() {
        Random random = new Random();
        return WORDS[random.nextInt(WORDS.length)];
    }

    public static char[] createHiddenWord(String word) {
        char[] hidden = new char[word.length()];
        Arrays.fill(hidden, '-');
        return hidden;
    }

    public static void startGame(Scanner scanner, int attempts) {
        while (true) {
            if (isWon()) {
                System.out.println("You guessed the word!");
                System.out.println("You survived!\n");
                break;
            }

            System.out.println("\n" + new String(hiddenWordLetters));

            System.out.print("Input a letter: > ");
            String input = scanner.nextLine();

            if (input.length() != 1) {
                System.out.println("You should input a single letter.");
            } else {
                char userLetter = input.charAt(0);

                if (!isLowerCaseEnglish(userLetter)) {
                    System.out.println("Please enter a lowercase English letter.");

                } else if (guessedLetters.contains(userLetter)) {
                    System.out.println("You've already guessed this letter.");

                } else if (rightWord.indexOf(userLetter) != -1) {
                    guessedLetters.add(userLetter);
                    updateWord(userLetter);

                } else {
                    System.out.println("That letter doesn't appear in the word");
                    guessedLetters.add(userLetter);
                    attempts--;

                    if (attempts == 0) {
                        System.out.println("You lost!\n");
                        break;
                    }
                }
            }
        }
        guessedLetters.clear();
    }

    public static void updateWord(char letter) {
        for (int i = 0; i < rightWord.length(); i++) {
            if (rightWord.charAt(i) == letter) {
                hiddenWordLetters[i] = letter;
            }
        }
    }

    public static boolean isLowerCaseEnglish(char letter) {
        return Character.isLowerCase(letter) && letter >= 'a' && letter <= 'z';
    }

    public static boolean isWon() {
        return new String(hiddenWordLetters).indexOf('-') == -1;
    }
}
