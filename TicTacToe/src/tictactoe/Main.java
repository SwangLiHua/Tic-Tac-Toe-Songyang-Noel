package tictactoe;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        Board board = new Board("board.txt");
        GameLogic logic = new GameLogic();

        System.out.println("=== Tic Tac Toe ===");

        while (!logic.isGameOver(board)) {

            printBoard(board);

            char player = logic.getCurrentPlayer(board);
            System.out.println("Player " + player + "'s turn");

            System.out.print("Enter row (0-2): ");
            int row = input.nextInt();

            System.out.print("Enter col (0-2): ");
            int col = input.nextInt();

            // validate move
            if (row < 0 || row > 2 || col < 0 || col > 2) {
                System.out.println("Invalid position. Try again.");
                continue;
            }

            if (!logic.makeMove(board, row, col)) {
                System.out.println("Cell already taken. Try again.");
                continue;
            }
        }

        // Final board
        printBoard(board);

        // Result
        if (logic.checkWin(board, 'X')) {
            System.out.println("X wins!");
        } else if (logic.checkWin(board, 'O')) {
            System.out.println("O wins!");
        } else {
            System.out.println("It's a draw!");
        }

        input.close();
    }

    // Clean board display (AP-friendly)
    public static void printBoard(Board board) {

        System.out.println();

        for (int r = 0; r < 3; r++) {

            for (int c = 0; c < 3; c++) {

                char cell = board.getCell(r, c);

                if (cell == 'E') {
                    System.out.print("- ");
                } else {
                    System.out.print(cell + " ");
                }
            }

            System.out.println();
        }

        System.out.println();
    }
}