package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TictactoeUI extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private Board board;
    private GameLogic logic;

    public TictactoeUI() {
        // Initialize your backend logic
        board = new Board("board.csv");
        logic = new GameLogic();

        // Setup Window
        setTitle("Tic Tac Toe - Swing UI");
        setSize(450, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3, 5, 5)); // 3x3 grid with 5px gaps

        initializeGrid();
        refreshUI(); // Sync UI with the existing file state
        setVisible(true);
    }

    private void initializeGrid() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                buttons[r][c] = new JButton("");
                buttons[r][c].setFont(new Font("SansSerif", Font.BOLD, 50));
                buttons[r][c].setFocusPainted(false);
                
                // Track row/col for the action listener
                int row = r;
                int col = c;

                buttons[r][c].addActionListener(e -> handleMove(row, col));
                add(buttons[r][c]);
            }
        }
    }

    private void handleMove(int row, int col) {
        // Use your GameLogic to attempt the move
        if (logic.makeMove(board, row, col)) {
            refreshUI();
            checkGameState();
        } else {
            // Shake or alert if move is invalid
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private void refreshUI() {
        // Sync the buttons with the Board's grid (which came from the CSV)
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                char val = board.getCell(r, c);
                if (val == 'X') {
                    buttons[r][c].setText("X");
                    buttons[r][c].setForeground(Color.RED);
                } else if (val == 'O') {
                    buttons[r][c].setText("O");
                    buttons[r][c].setForeground(Color.BLUE);
                } else {
                    buttons[r][c].setText("");
                }
            }
        }
    }

    private void checkGameState() {
        if (logic.checkWin(board, 'X')) {
            endGame("Player X Wins!");
        } else if (logic.checkWin(board, 'O')) {
            endGame("Player O Wins!");
        } else if (logic.isDraw(board)) {
            endGame("It's a Draw!");
        }
    }

    private void endGame(String message) {
        JOptionPane.showMessageDialog(this, message);
        board.clearBoard(); // Resets CSV to 'E's
        refreshUI();
    }

    public static void main(String[] args) {
        // Run UI on the Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> new TictactoeUI());
    }
}