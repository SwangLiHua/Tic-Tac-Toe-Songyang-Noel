package tictactoe;

import java.util.Scanner;

public class ConsoleGame {

    private Board board;
    private GameLogic logic;
    private Scanner input;
    private boolean vsAI;

    public ConsoleGame(boolean vsAI) {
        this.vsAI = vsAI;
        board = new Board("board.txt");
        logic = new GameLogic();
        input = new Scanner(System.in);

        board.clearBoard();
    }

    public void start() {
        System.out.println();
        System.out.println("================================");
        System.out.println("        CONSOLE TIC TAC TOE     ");
        System.out.println("================================");

        if (vsAI) {
            System.out.println("Mode: Player vs AI");
        } else {
            System.out.println("Mode: Player vs Player");
        }

        while (!logic.isGameOver(board)) {
            printBoard();

            char player = logic.getCurrentPlayer(board);

            if (vsAI && player == 'O') {
                makeAIMove();
            } else {
                makePlayerMove(player);
            }
        }

        printBoard();
        printResult();
    }

    private void makePlayerMove(char player) {
        boolean validMove = false;

        while (!validMove) {
            System.out.println("Player " + player + "'s turn");

            System.out.print("Enter row (0-2): ");
            int row = input.nextInt();

            System.out.print("Enter col (0-2): ");
            int col = input.nextInt();

            validMove = logic.makeMove(board, row, col, player);

            if (!validMove) {
                System.out.println("Invalid move. Try again.");
            }
        }
    }

    private void makeAIMove() {
        int[] move = logic.getAIMove(board);

        if (move != null) {
            logic.makeMove(board, move[0], move[1], 'O');
            System.out.println("AI chose row " + move[0] + ", col " + move[1]);
        }
    }

    private void printBoard() {
        System.out.println();

        System.out.println("   0   1   2");
        System.out.println("  -----------");

        for (int r = 0; r < 3; r++) {
            System.out.print(r + " | ");

            for (int c = 0; c < 3; c++) {
                char cell = board.getCell(r, c);

                if (cell == 'E') {
                    System.out.print("- | ");
                } else {
                    System.out.print(cell + " | ");
                }
            }

            System.out.println();
            System.out.println("  -----------");
        }

        System.out.println();
    }

    private void printResult() {
        System.out.println("================================");

        if (logic.checkWin(board, 'X')) {
            System.out.println("           PLAYER X WINS!       ");
        } else if (logic.checkWin(board, 'O')) {
            if (vsAI) {
                System.out.println("              AI WINS!          ");
            } else {
                System.out.println("           PLAYER O WINS!       ");
            }
        } else {
            System.out.println("             IT'S A DRAW!       ");
        }

        System.out.println("================================");
        System.out.println();
    }
}