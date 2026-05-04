package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TictactoeUI extends JFrame {

    private JButton[][] buttons;
    private Board board;
    private GameLogic logic;
    private boolean gameOver = false;

    public TictactoeUI(String filename) {

        board = new Board(filename);
        logic = new GameLogic();

        buttons = new JButton[3][3];

        setTitle("Tic Tac Toe");
        setSize(350, 350);
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initButtons();
        refreshBoard();

        setVisible(true);
    }

    // ---------------- UI SETUP ----------------

    private void initButtons() {

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {

                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 40));

                final int row = r;
                final int col = c;

                btn.addActionListener(e -> handleMove(row, col));

                buttons[r][c] = btn;
                add(btn);
            }
        }
    }

    // ---------------- GAME PLAY ----------------

    private void handleMove(int row, int col) {

        if (gameOver) return;

        if (!logic.makeMove(board, row, col)) {
            JOptionPane.showMessageDialog(this, "Invalid move!");
            return;
        }

        refreshBoard();

        // WIN CHECK
        if (logic.checkWin(board, 'X') || logic.checkWin(board, 'O')) {

            refreshBoard();

            String winner = logic.checkWin(board, 'X') ? "X" : "O";
            JOptionPane.showMessageDialog(this, "Player " + winner + " wins!");

            endGame();
            return;
        }

        // DRAW CHECK
        if (logic.isDraw(board)) {

            refreshBoard();
            JOptionPane.showMessageDialog(this, "It's a draw!");

            endGame();
        }
    }

    // ---------------- RESET GAME ----------------

    private void endGame() {

        gameOver = true;

        // small pause so player sees result
        try { Thread.sleep(500); } catch (Exception ignored) {}

        board.clearBoard();
        gameOver = false;

        refreshBoard();
    }

    // ---------------- UI UPDATE ----------------

    private void refreshBoard() {

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {

                char value = board.getCell(r, c);

                buttons[r][c].setText(value == 'E' ? "" : String.valueOf(value));
            }
        }
    }

    // ---------------- MAIN ----------------

    public static void main(String[] args) {
        new TictactoeUI("board.csv");
    }
}