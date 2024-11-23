package MatrixProcessing;

import java.util.Scanner;

public class MatrixProcessing {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        MatrixProcessing mp = new MatrixProcessing();
        mp.start();
    }

    public void start() {
        int[] possibleChoices = {0, 1, 2, 3, 4, 5, 6};

        while (true) {
            System.out.println("""
                    
                    1. Add matrices
                    2. Multiply matrix by a constant
                    3. Multiply matrices
                    4. Transpose matrix
                    5. Calculate a determinant
                    6. Inverse matrix
                    0. Exit""");

            int userChoice = Util.getUserChoice(possibleChoices);

            switch (userChoice) {
                case 0: // Exit
                    scanner.close();
                    return;

                case 1:
                {
                    double[][] matrixA = createMatrix();
                    double[][] matrixB = createMatrix();
                    double[][] resultMatrix = add(matrixA, matrixB);
                    if (resultMatrix != null) {
                        printMatrix(resultMatrix);
                    } else {
                        System.out.println("Adding is impossible");
                    }
                    break;
                }

                case 2:
                {
                    double[][] matrix = createMatrix();
                    System.out.print("Enter the constant: > ");
                    double constant;
                    while (true) {
                        try {
                            constant = Double.parseDouble(scanner.nextLine());
                            break;
                        } catch (NumberFormatException e) {
                            System.out.print("Please enter a valid number: > ");
                        }
                    }

                    double[][] resultMatrix = multiplyByConstant(matrix, constant);
                    printMatrix(resultMatrix);
                    break;
                }

                case 3:
                {
                    double[][] matrixA = createMatrix();
                    double[][] matrixB = createMatrix();
                    double[][] resultMatrix = multiplyByMatrix(matrixA, matrixB);
                    if (resultMatrix != null) {
                        printMatrix(resultMatrix);
                    } else {
                        System.out.println("Multiplying is impossible");
                    }
                    break;
                }

                case 4:
                {
                    double[][] transposeMatrix = transposeMatrix();
                    if (transposeMatrix != null) {
                        printMatrix(transposeMatrix);
                    }
                    break;
                }

                case 5:
                {
                    double[][] matrix = createMatrix();
                    double determinant = calcDeterminant(matrix);

                    System.out.println("The result is:");
                    if ((determinant % 1) == 0) {
                        System.out.println((int) determinant);
                    } else {
                        System.out.printf("%.1f\n", determinant);
                    }

                    break;
                }

                case 6:
                {
                    double[][] matrix = createMatrix();
                    double[][] inverseMatrix = inverseMatrix(matrix);
                    if (inverseMatrix != null) {
                        printMatrix(inverseMatrix);
                    } else {
                        System.out.println("This matrix doesn't have an inverse.");
                    }
                    break;
                }

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    public static double[][] createEmptyMatrix(int rows, int cols) {
        return new double[rows][cols];
    }

    public static double[][] createMatrix() {
        int[] rowsAndColumns = getRowsAndColumns();
        int rows = rowsAndColumns[0];
        int columns = rowsAndColumns[1];
        double[][] matrix = createEmptyMatrix(rows, columns);

        System.out.println("Enter the matrix:");
        for (int i = 0; i < rows; i++) {
            while (true) {
                try {
                    System.out.print("> ");
                    String[] input = scanner.nextLine().split(" ");
                    if (input.length != columns) {
                        System.out.println("The input must contain " + columns + " numeric values");
                    }
                    else {
                        for (int j = 0; j < columns; j++) {
                            matrix[i][j] = Double.parseDouble(input[j]);
                        }
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("The input must contain numeric values");
                }
            }
        }
        return matrix;
    }

    private static int[] getRowsAndColumns() {
        while (true) {
            System.out.print("Enter the size of the matrix: > ");
            try {
                String[] input = scanner.nextLine().split(" ");
                if (input.length != 2) {
                    throw new NumberFormatException();
                }

                int rows = Integer.parseInt(input[0]);
                int cols = Integer.parseInt(input[1]);

                if (rows <= 0 || cols <= 0) {
                    System.out.println("The input must contain two positive numeric values");
                } else {
                    return new int[]{rows, cols};
                }
            } catch (NumberFormatException e) {
                System.out.println("The input must contain two numeric values");
            }
        }
    }

    public static void printMatrix(double[][] matrix) {
        System.out.println("The result is:");

        for (double[] doubles : matrix) {
            for (double num : doubles) {
                // Check if the number is an integer
                if (num == (int) num) {
                    System.out.print((int) num + " ");
                }
                // Check if the number has one decimal place
                else if (Math.round(num * 10) == num * 10) {
                    System.out.printf("%.1f ", num);
                }
                // Otherwise, format with two decimal places
                else {
                    System.out.printf("%.2f ", num);
                }
            }
            System.out.println();
        }
    }

    public static double[][] add(double[][] matrixA, double[][] matrixB) {
        int rowsA = matrixA.length;
        int colsA = matrixA[0].length;

        int rowsB = matrixB.length;
        int colsB = matrixB[0].length;

        if (rowsA != rowsB || colsA != colsB) {
            return null;
        }

        double[][] result = createEmptyMatrix(rowsA, colsA);

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsA; j++) {
                result[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }

        return result;
    }

    public static double[][] multiplyByConstant(double[][] matrix, double constant) {
        int rows = matrix.length;
        int cols = matrix[0].length;

        double[][] result = createEmptyMatrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = matrix[i][j] * constant;
            }
        }
        return result;
    }

    public static double[][] multiplyByMatrix(double[][] matrixA, double[][] matrixB) {
        int rowsA = matrixA.length;
        int columnsA = matrixA[0].length;

        int rowsB = matrixB.length;
        int columnsB = matrixB[0].length;

        if (columnsA != rowsB) {
            return null;
        }

        double[][] result = createEmptyMatrix(rowsA, columnsB);

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < columnsB; j++) {
                for (int k = 0; k < rowsB; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    public static double[][] transposeMatrix() {
        int[] possibleChoices = {0, 1, 2, 3, 4, 5, 6};

        while (true) {
            System.out.println("""
                    
                    1. Main diagonal
                    2. Side diagonal
                    3. Vertical line
                    4. Horizontal line
                    0. Back""");

            int userChoice = Util.getUserChoice(possibleChoices);

            if (userChoice == 0) {
                return null;
            }

            double[][] matrix = createMatrix();
            double[][] result;

            switch (userChoice) {
                case 1:
                    result = mainDiagonalTranspose(matrix);
                    break;
                case 2:
                    result = sideDiagonalTranspose(matrix);
                    break;
                case 3:
                    result = verticalLineTranspose(matrix);
                    break;
                case 4:
                    result = horizontalLineTranspose(matrix);
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
                    continue;
            }
            return result;
        }
    }

    public static double[][] mainDiagonalTranspose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] result = createEmptyMatrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    public static double[][] sideDiagonalTranspose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] result = createEmptyMatrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[cols - 1 - j][rows - 1 - i] = matrix[i][j];
            }
        }
        return result;
    }

    public static double[][] verticalLineTranspose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] result = createEmptyMatrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][cols - 1 - j] = matrix[i][j];
            }
        }
        return result;
    }

    public static double[][] horizontalLineTranspose(double[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        double[][] result = createEmptyMatrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            System.arraycopy(matrix[i], 0, result[rows - 1 - i], 0, cols);
        }
        return result;
    }

    public static double calcDeterminant(double[][] matrix) {
        int size = matrix.length;

        if (size == 1) {
            return matrix[0][0];
        }

        if (size == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        }

        double det = 0;
        for (int i = 0; i < size; i++) {
            double sign = (i % 2 == 0) ? 1 : -1;
            double[][] subMatrix = getSubMatrix(matrix, 0, i);
            det += sign * matrix[0][i] * calcDeterminant(subMatrix);
        }

        return det;
    }

    public static double[][] getSubMatrix(double[][] matrix, int row, int column) {
        int size = matrix.length;
        double[][] subMatrix = createEmptyMatrix(size-1, size-1);
        int subRow = 0;

        for (int i = 0; i < size; i++) {
            if (i == row) {
                continue;
            }

            int subCol = 0;
            for (int j = 0; j < size; j++) {
                if (j == column) {
                    continue;
                }
                subMatrix[subRow][subCol++] = matrix[i][j];
            }
            subRow++;
        }

        return subMatrix;
    }

    public static double[][] inverseMatrix(double[][] matrix) {
        double determinant = calcDeterminant(matrix);

        if (determinant == 0) {
            return null;
        }

        int size = matrix.length;

        if (size == 1) {
            return new double[][]{{1 / determinant}};
        }

        double[][] adjugateMatrix = createEmptyMatrix(size, size);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                double sign = ((i + j) % 2 == 0) ? 1 : -1;
                double[][] subMatrix = getSubMatrix(matrix, i, j);
                adjugateMatrix[j][i] = sign * calcDeterminant(subMatrix);
            }
        }

        return multiplyByConstant(adjugateMatrix, 1 / determinant);
    }
}
