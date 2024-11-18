package TicTacToe;

import java.util.Scanner;

public class TicTacToe {

    private static final char EMPTY_CELL = '_';
    private char currentPlayer = 'X';
    private final char[][] BOARD = {
            {'_', '_', '_'},
            {'_', '_', '_'},
            {'_', '_', '_'}};

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe();
        game.start();
    }

    public void start() {
        printBoard();

        for (int move = 0; move < 9; move++) {
            while (true) {
                System.out.print("Enter the coordinates (row and column): ");
                Scanner scanner = new Scanner(System.in);
                String userInput = scanner.nextLine();

                if (isCorrectInput(userInput)) {
                    String[] coordinates = userInput.split(" ");
                    int x = Integer.parseInt(coordinates[0]) - 1;
                    int y = Integer.parseInt(coordinates[1]) - 1;

                    BOARD[x][y] = currentPlayer;
                    break;
                }
            }

            printBoard();

            String winner = getWinner();
            if (winner != null) {
                System.out.println(winner + " wins");
                return;
            }

            currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
        }

        System.out.println("Draw");
    }

    private void printBoard() {
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(BOARD[i][j] + " ");
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }

    private boolean isCorrectInput(String input) {
        try {
            String[] coordinates = input.split(" ");
            if (coordinates.length != 2) {
                throw new NumberFormatException();
            }

            int x = Integer.parseInt(coordinates[0]);
            int y = Integer.parseInt(coordinates[1]);

            if (x < 1 || x > 3 || y < 1 || y > 3) {
                System.out.println("Coordinates should be from 1 to 3!");
                return false;
            }

            if (BOARD[x - 1][y - 1] != EMPTY_CELL) {
                System.out.println("This cell is occupied! Choose another one!");
                return false;
            }

        } catch (NumberFormatException e) {
            System.out.println("You should enter numbers!");
            return false;
        }

        return true;
    }

    private String getWinner() {
        for (int i = 0; i < 3; i++) {
            if (BOARD[i][0] == BOARD[i][1] &&
                    BOARD[i][1] == BOARD[i][2] &&
                    BOARD[i][0] != EMPTY_CELL) {
                return String.valueOf(BOARD[i][0]);
            }

            if (BOARD[0][i] == BOARD[1][i] &&
                    BOARD[1][i] == BOARD[2][i] &&
                    BOARD[0][i] != EMPTY_CELL) {
                return String.valueOf(BOARD[0][i]);
            }
        }

        if (BOARD[0][0] == BOARD[1][1] &&
                BOARD[1][1] == BOARD[2][2] &&
                BOARD[0][0] != EMPTY_CELL) {
            return String.valueOf(BOARD[0][0]);
        }

        if (BOARD[0][2] == BOARD[1][1] &&
                BOARD[1][1] == BOARD[2][0] &&
                BOARD[0][2] != EMPTY_CELL) {
            return String.valueOf(BOARD[0][2]);
        }

        return null;
    }
}
