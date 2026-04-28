package tictactoe;

import java.io.*;
import java.util.Scanner;

public class Board {
    private char[][] grid;
    private String filename;

    public Board(String filename) {
        this.filename = filename;
        this.grid = new char[3][3];
        // Default the board to Empty
        for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) grid[i][j] = 'E';
        }
        
        if(isValidBoardFile()) {
            loadBoardFromFile();
        } else {
            saveBoardToFile();
        }
    }

    public void loadBoardFromFile() {
        try {
            File file = new File("src/tictactoe/" + this.filename);
            if (!file.exists()) return;
            Scanner scanner = new Scanner(file);
            int row = 0;
            while(scanner.hasNextLine() && row < 3) {
                String[] parts = scanner.nextLine().split(", ");
                for(int col = 0; col < 3; col++) {
                    grid[row][col] = parts[col].charAt(0);
                }
                row++;
            }
            scanner.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void saveBoardToFile() {
        try {
            File file = new File("src/tictactoe/" + this.filename);
            FileWriter writer = new FileWriter(file);
            StringBuilder sb = new StringBuilder();
            for(int r = 0; r < 3; r++) {
                for(int c = 0; c < 3; c++) {
                    sb.append(grid[r][c]);
                    if(c < 2) sb.append(", ");
                }
                if(r < 2) sb.append("\n");
            }
            writer.write(sb.toString());
            writer.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isValidBoardFile() {
        File file = new File("src/tictactoe/" + this.filename);
        if (!file.exists()) return false;
        try (Scanner scanner = new Scanner(file)) {
            int lines = 0;
            while(scanner.hasNextLine()) {
                if(!scanner.nextLine().trim().matches("[EXO], [EXO], [EXO]")) return false;
                lines++;
            }
            return lines == 3;
        } catch(Exception e) {
            return false;
        }
    }

    public char getCell(int r, int c) { return grid[r][c]; }
    public void setCell(int r, int c, char p) { grid[r][c] = p; saveBoardToFile(); }
    public char[][] getGrid() { return grid; }
    
    public void clearBoard() {
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++) grid[i][j] = 'E';
        saveBoardToFile();
    }
}