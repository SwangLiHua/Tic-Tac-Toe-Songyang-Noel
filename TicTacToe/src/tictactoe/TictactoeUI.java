package tictactoe;

import javax.swing.*;
import java.awt.*;

public class TictactoeUI extends JFrame {

    private CardLayout layout;
    private JPanel mainPanel;

    private JButton[][] buttons;
    private Board board;
    private GameLogic logic;

    private boolean vsAI = false;
    private boolean gameOver = false;

    private char currentPlayer = 'X';

    private int xScore = 0;
    private int oScore = 0;

    private JLabel status;
    private JLabel scoreLabel;

    public TictactoeUI() {

        setTitle("Tic Tac Toe Deluxe");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        mainPanel = new JPanel(layout);

        // ---------------- MENU ----------------

        JPanel menu = new JPanel();
        menu.setLayout(new BoxLayout(menu, BoxLayout.Y_AXIS));
        menu.setBackground(Color.BLACK);

        JLabel title = new JLabel("TIC TAC TOE");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 36));
        title.setForeground(Color.WHITE);

        JButton pvp = new JButton("Player vs Player");
        JButton ai = new JButton("Player vs Computer");
        JButton exit = new JButton("Exit");

        pvp.setAlignmentX(Component.CENTER_ALIGNMENT);
        ai.setAlignmentX(Component.CENTER_ALIGNMENT);
        exit.setAlignmentX(Component.CENTER_ALIGNMENT);

        pvp.setMaximumSize(new Dimension(250, 50));
        ai.setMaximumSize(new Dimension(250, 50));
        exit.setMaximumSize(new Dimension(250, 50));

        menu.add(Box.createVerticalStrut(80));
        menu.add(title);
        menu.add(Box.createVerticalStrut(40));
        menu.add(pvp);
        menu.add(Box.createVerticalStrut(15));
        menu.add(ai);
        menu.add(Box.createVerticalStrut(15));
        menu.add(exit);

        // ---------------- GAME ----------------

        JPanel game = new JPanel(new BorderLayout());

        JPanel grid = new JPanel(new GridLayout(3, 3));
        buttons = new JButton[3][3];

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {

                JButton btn = new JButton();
                btn.setFont(new Font("Arial", Font.BOLD, 50));

                final int row = r;
                final int col = c;

                btn.addActionListener(e -> {

                    if (gameOver) return;

                    if (board.getCell(row, col) != 'E') return;

                    board.setCell(row, col, currentPlayer);

                    // refresh board
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            char val = board.getCell(i, j);
                            buttons[i][j].setText(val == 'E' ? "" : String.valueOf(val));
                        }
                    }

                    // WIN
                    if (logic.checkWin(board, currentPlayer)) {

                        gameOver = true;

                        if (currentPlayer == 'X') xScore++;
                        else oScore++;

                        JOptionPane.showMessageDialog(this, "Player " + currentPlayer + " wins!");

                        // RESET BOARD
                        board.clearBoard();
                        currentPlayer = 'X';
                        gameOver = false;

                        // clear UI
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                buttons[i][j].setText("");
                            }
                        }

                        scoreLabel.setText("X: " + xScore + "   O: " + oScore);
                        status.setText("Player X's Turn");

                        return;
                    }

                    // DRAW
                    if (logic.isDraw(board)) {

                        gameOver = true;

                        JOptionPane.showMessageDialog(this, "Draw!");

                        board.clearBoard();
                        currentPlayer = 'X';
                        gameOver = false;

                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                buttons[i][j].setText("");
                            }
                        }

                        status.setText("Player X's Turn");
                        return;
                    }

                    // switch player
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';

                    // AI MOVE
                    if (vsAI && currentPlayer == 'O') {

                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                if (board.getCell(i, j) == 'E') {
                                    board.setCell(i, j, 'O');
                                    i = 3;
                                    break;
                                }
                            }
                        }

                        // refresh
                        for (int i = 0; i < 3; i++) {
                            for (int j = 0; j < 3; j++) {
                                char val = board.getCell(i, j);
                                buttons[i][j].setText(val == 'E' ? "" : String.valueOf(val));
                            }
                        }

                        if (logic.checkWin(board, 'O')) {

                            gameOver = true;
                            oScore++;

                            JOptionPane.showMessageDialog(this, "Computer wins!");

                            board.clearBoard();
                            currentPlayer = 'X';
                            gameOver = false;

                            for (int i = 0; i < 3; i++) {
                                for (int j = 0; j < 3; j++) {
                                    buttons[i][j].setText("");
                                }
                            }

                            scoreLabel.setText("X: " + xScore + "   O: " + oScore);
                            status.setText("Player X's Turn");

                            return;
                        }

                        currentPlayer = 'X';
                    }

                    status.setText("Player " + currentPlayer + "'s Turn");
                });

                buttons[r][c] = btn;
                grid.add(btn);
            }
        }

        scoreLabel = new JLabel("X: 0   O: 0", SwingConstants.CENTER);
        status = new JLabel("Player X's Turn", SwingConstants.CENTER);

        JButton back = new JButton("Back to Menu");
        back.addActionListener(e -> layout.show(mainPanel, "menu"));

        JPanel bottom = new JPanel(new GridLayout(3, 1));
        bottom.add(scoreLabel);
        bottom.add(status);
        bottom.add(back);

        game.add(grid, BorderLayout.CENTER);
        game.add(bottom, BorderLayout.SOUTH);

        // ---------------- MENU BUTTONS ----------------

        pvp.addActionListener(e -> {
            vsAI = false;
            board = new Board("board.csv");
            logic = new GameLogic();
            gameOver = false;
            currentPlayer = 'X';
            xScore = 0;
            oScore = 0;

            scoreLabel.setText("X: 0   O: 0");
            status.setText("Player X's Turn");

            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    buttons[i][j].setText("");

            layout.show(mainPanel, "game");
        });

        ai.addActionListener(e -> {
            vsAI = true;
            board = new Board("board.csv");
            logic = new GameLogic();
            gameOver = false;
            currentPlayer = 'X';
            xScore = 0;
            oScore = 0;

            scoreLabel.setText("X: 0   O: 0");
            status.setText("Player X's Turn");

            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    buttons[i][j].setText("");

            layout.show(mainPanel, "game");
        });

        exit.addActionListener(e -> System.exit(0));

        // ---------------- ADD ----------------

        mainPanel.add(menu, "menu");
        mainPanel.add(game, "game");

        add(mainPanel);

        layout.show(mainPanel, "menu");

        setVisible(true);
    }

    public static void main(String[] args) {
        new TictactoeUI();
    }
}