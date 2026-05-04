package tictactoe;

import java.io.*;
import java.util.Scanner;

public class Board {

    private char[][] grid;
    private String filename;

    public Board(String filename) {
        this.filename = filename;
        grid = new char[3][3];

        clearBoard();

        if (isValidBoardFile()) {
            loadBoardFromFile();
        }
    }

    // ---------------- CORE ----------------

    public char getCell(int row, int col) {
        return grid[row][col];
    }

    public char[][] getGrid() {
        return grid;
    }

    public void setCell(int row, int col, char player) {
        grid[row][col] = player;
        saveBoardToFile(); // ONLY place saving happens
    }

    public void clearBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                grid[r][c] = 'E';
            }
        }
        saveBoardToFile();
    }

    // ---------------- FILE LOAD ----------------

    public void loadBoardFromFile() {
        try {
            File file = new File("src/tictactoe/" + filename);
            Scanner scanner = new Scanner(file);

            int row = 0;

            while (scanner.hasNextLine() && row < 3) {
                String[] parts = scanner.nextLine().split(",");

                for (int col = 0; col < 3; col++) {
                    grid[row][col] = parts[col].trim().charAt(0);
                }

                row++;
            }

            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- FILE SAVE ----------------

    public void saveBoardToFile() {
        try {
            FileWriter writer = new FileWriter("src/tictactoe/" + filename);

            for (int r = 0; r < 3; r++) {
                writer.write(
                    grid[r][0] + ", " +
                    grid[r][1] + ", " +
                    grid[r][2]
                );

                if (r < 2) writer.write("\n");
            }

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- VALIDATION ----------------

    public boolean isValidBoardFile() {
        try {
            File file = new File("src/tictactoe/" + filename);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");

                if (parts.length != 3) {
                    scanner.close();
                    return false;
                }

                for (String p : parts) {
                    char c = p.trim().charAt(0);
                    if (c != 'X' && c != 'O' && c != 'E') {
                        scanner.close();
                        return false;
                    }
                }
            }

            scanner.close();
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // ---------------- DEBUG ----------------

    public void printGrid() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                System.out.print(grid[r][c] + " ");
            }
            System.out.println();
        }
    }
}