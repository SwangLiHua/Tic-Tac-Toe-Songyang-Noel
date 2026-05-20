package tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TictactoeUI extends JFrame {

    private Board board;
    private GameLogic logic;

    private JButton[][] buttons;
    private JLabel statusLabel;
    private JLabel scoreLabel;

    private char currentPlayer;
    private int xScore;
    private int oScore;
    private boolean gameOver;

    public TictactoeUI() {
        board = new Board("board.txt");
        logic = new GameLogic();

        buttons = new JButton[3][3];
        currentPlayer = logic.getCurrentPlayer(board);

        setTitle("Tic Tac Toe Deluxe");
        setSize(500, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        createUI();

        setVisible(true);
    }

    private void createUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(20, 20, 35));

        JLabel title = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 42));
        title.setForeground(new Color(0, 255, 255));
        title.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));

        mainPanel.add(title, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 10, 10));
        gridPanel.setBackground(new Color(20, 20, 35));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton button = new JButton("");
                button.setFont(new Font("Arial", Font.BOLD, 70));
                button.setFocusPainted(false);
                button.setBackground(new Color(40, 40, 70));
                button.setForeground(Color.WHITE);
                button.setBorder(BorderFactory.createLineBorder(new Color(0, 255, 255), 3));

                int row = r;
                int col = c;

                button.addActionListener((ActionEvent e) -> handleMove(row, col));

                buttons[r][c] = button;
                gridPanel.add(button);
            }
        }

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        bottomPanel.setBackground(new Color(20, 20, 35));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        scoreLabel = new JLabel("X: 0    O: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 22));
        scoreLabel.setForeground(Color.WHITE);

        statusLabel = new JLabel("Player " + currentPlayer + "'s turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 22));
        statusLabel.setForeground(new Color(255, 220, 80));

        JButton resetButton = new JButton("New Round");
        styleMenuButton(resetButton);
        resetButton.addActionListener(e -> resetBoard());

        JButton clearScoreButton = new JButton("Reset Score");
        styleMenuButton(clearScoreButton);
        clearScoreButton.addActionListener(e -> {
            xScore = 0;
            oScore = 0;
            updateScore();
            resetBoard();
        });

        bottomPanel.add(scoreLabel);
        bottomPanel.add(statusLabel);
        bottomPanel.add(resetButton);
        bottomPanel.add(clearScoreButton);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
        refreshBoard();
    }

    private void handleMove(int row, int col) {
        if (gameOver) {
            return;
        }

        if (board.getCell(row, col) != 'E') {
            playErrorSound();
            statusLabel.setText("That spot is already taken!");
            return;
        }

        playClickSound();

        board.setCell(row, col, currentPlayer);
        refreshBoard();

        if (logic.checkWin(board, currentPlayer)) {
            gameOver = true;

            if (currentPlayer == 'X') {
                xScore++;
            } else {
                oScore++;
            }

            updateScore();
            statusLabel.setText("Player " + currentPlayer + " wins!");
            highlightWinningButtons(currentPlayer);
            playWinSound();
            return;
        }

        if (logic.isDraw(board)) {
            gameOver = true;
            statusLabel.setText("It's a draw!");
            playDrawSound();
            return;
        }

        currentPlayer = currentPlayer == 'X' ? 'O' : 'X';
        statusLabel.setText("Player " + currentPlayer + "'s turn");
    }

    private void refreshBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                char cell = board.getCell(r, c);

                if (cell == 'E') {
                    buttons[r][c].setText("");
                    buttons[r][c].setBackground(new Color(40, 40, 70));
                } else {
                    buttons[r][c].setText(String.valueOf(cell));

                    if (cell == 'X') {
                        buttons[r][c].setForeground(new Color(0, 255, 255));
                    } else {
                        buttons[r][c].setForeground(new Color(255, 80, 160));
                    }
                }
            }
        }
    }

    private void resetBoard() {
        board.clearBoard();
        currentPlayer = 'X';
        gameOver = false;
        statusLabel.setText("Player X's turn");
        refreshBoard();
    }

    private void updateScore() {
        scoreLabel.setText("X: " + xScore + "    O: " + oScore);
    }

    private void styleMenuButton(JButton button) {
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setBackground(new Color(0, 180, 220));
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    }

    private void highlightWinningButtons(char player) {
        Color winColor = new Color(80, 255, 120);

        for (int r = 0; r < 3; r++) {
            if (board.getCell(r, 0) == player &&
                board.getCell(r, 1) == player &&
                board.getCell(r, 2) == player) {

                buttons[r][0].setBackground(winColor);
                buttons[r][1].setBackground(winColor);
                buttons[r][2].setBackground(winColor);
            }
        }

        for (int c = 0; c < 3; c++) {
            if (board.getCell(0, c) == player &&
                board.getCell(1, c) == player &&
                board.getCell(2, c) == player) {

                buttons[0][c].setBackground(winColor);
                buttons[1][c].setBackground(winColor);
                buttons[2][c].setBackground(winColor);
            }
        }

        if (board.getCell(0, 0) == player &&
            board.getCell(1, 1) == player &&
            board.getCell(2, 2) == player) {

            buttons[0][0].setBackground(winColor);
            buttons[1][1].setBackground(winColor);
            buttons[2][2].setBackground(winColor);
        }

        if (board.getCell(0, 2) == player &&
            board.getCell(1, 1) == player &&
            board.getCell(2, 0) == player) {

            buttons[0][2].setBackground(winColor);
            buttons[1][1].setBackground(winColor);
            buttons[2][0].setBackground(winColor);
        }
    }

    private void playClickSound() {
        Toolkit.getDefaultToolkit().beep();
    }

    private void playErrorSound() {
        Toolkit.getDefaultToolkit().beep();
    }

    private void playWinSound() {
        Toolkit.getDefaultToolkit().beep();
    }

    private void playDrawSound() {
        Toolkit.getDefaultToolkit().beep();
    }

    public static void main(String[] args) {
        new TictactoeUI();
    }
}