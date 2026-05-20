package tictactoe;

import javax.swing.*;
import java.awt.*;

public class TicTacToeSwingUI extends JFrame {

    private CardLayout layout;
    private JPanel mainPanel;

    private Board board;
    private GameLogic logic;

    private JButton[][] buttons;
    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JLabel modeLabel;
    private JTextArea consoleArea;

    private boolean vsAI;
    private boolean consoleStyle;
    private boolean gameOver;

    private int xScore;
    private int oScore;

    private final Color BACKGROUND = new Color(16, 16, 32);
    private final Color CARD = new Color(10, 10, 25);
    private final Color GRID_CELL = new Color(32, 32, 69);
    private final Color CYAN = new Color(0, 245, 255);
    private final Color PINK = new Color(255, 46, 166);
    private final Color YELLOW = new Color(255, 209, 102);
    private final Color GREEN = new Color(0, 255, 102);

    public TicTacToeSwingUI() {
        board = new Board("board.txt");
        logic = new GameLogic();
        buttons = new JButton[3][3];

        setTitle("Tic Tac Toe Deluxe");
        setSize(620, 760);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        layout = new CardLayout();
        mainPanel = new JPanel(layout);

        createMenuScreen();
        createGameScreen();

        add(mainPanel);
        layout.show(mainPanel, "menu");

        setVisible(true);
    }

    private JPanel createBackgroundPanel() {
        JPanel panel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(BACKGROUND);
                g2.fillRect(0, 0, getWidth(), getHeight());

                g2.setColor(new Color(0, 245, 255, 45));
                g2.fillOval(-120, -100, 320, 320);

                g2.setColor(new Color(255, 46, 166, 45));
                g2.fillOval(getWidth() - 180, getHeight() - 220, 350, 350);
            }
        };

        panel.setLayout(new GridBagLayout());
        return panel;
    }

    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CYAN, 2),
            BorderFactory.createEmptyBorder(35, 35, 35, 35)
        ));

        return card;
    }

    private void createMenuScreen() {
        JPanel background = createBackgroundPanel();
        JPanel card = createCardPanel();

        JLabel title = new JLabel("TIC TAC TOE");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 50));
        title.setForeground(CYAN);

        JLabel subtitle = new JLabel("Choose your game mode");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setFont(new Font("Arial", Font.BOLD, 20));
        subtitle.setForeground(YELLOW);

        JButton pvpButton = createNeonButton("Local Player vs Player");
        JButton aiButton = createNeonButton("Play Against AI");
        JButton consoleButton = createNeonButton("Console Style UI");
        JButton exitButton = createNeonButton("Exit");

        pvpButton.addActionListener(e -> startGame(false, false));
        aiButton.addActionListener(e -> startGame(true, false));
        consoleButton.addActionListener(e -> startGame(false, true));
        exitButton.addActionListener(e -> dispose());

        card.add(title);
        card.add(Box.createVerticalStrut(12));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(35));
        card.add(pvpButton);
        card.add(Box.createVerticalStrut(15));
        card.add(aiButton);
        card.add(Box.createVerticalStrut(15));
        card.add(consoleButton);
        card.add(Box.createVerticalStrut(15));
        card.add(exitButton);

        background.add(card);
        mainPanel.add(background, "menu");
    }

    private JButton createNeonButton(String text) {
        JButton button = new JButton(text);

        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(360, 58));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setFocusPainted(false);
        button.setForeground(new Color(10, 10, 25));
        button.setBackground(CYAN);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        return button;
    }

    private JButton createSmallButton(String text) {
        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setForeground(new Color(10, 10, 25));
        button.setBackground(CYAN);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        return button;
    }

    private void createGameScreen() {
        JPanel background = createBackgroundPanel();

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CYAN, 2),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        card.setPreferredSize(new Dimension(530, 690));

        JPanel topPanel = new JPanel(new GridLayout(4, 1, 0, 4));
        topPanel.setBackground(CARD);

        JLabel title = new JLabel("TIC TAC TOE", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 42));
        title.setForeground(CYAN);

        modeLabel = new JLabel("Mode: Local Player vs Player", SwingConstants.CENTER);
        modeLabel.setFont(new Font("Arial", Font.BOLD, 17));
        modeLabel.setForeground(Color.WHITE);

        statusLabel = new JLabel("Player X's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        statusLabel.setForeground(YELLOW);

        scoreLabel = new JLabel("X: 0 | O: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 19));
        scoreLabel.setForeground(Color.WHITE);

        topPanel.add(title);
        topPanel.add(modeLabel);
        topPanel.add(statusLabel);
        topPanel.add(scoreLabel);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(CARD);

        consoleArea = new JTextArea();
        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Courier New", Font.BOLD, 23));
        consoleArea.setBackground(Color.BLACK);
        consoleArea.setForeground(GREEN);
        consoleArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(GREEN, 2),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        consoleArea.setVisible(false);

        JPanel consoleWrapper = new JPanel(new BorderLayout());
        consoleWrapper.setBackground(CARD);
        consoleWrapper.setBorder(BorderFactory.createEmptyBorder(12, 0, 8, 0));
        consoleWrapper.add(consoleArea, BorderLayout.CENTER);

        JPanel gridPanel = new JPanel(new GridLayout(3, 3, 12, 12));
        gridPanel.setBackground(CARD);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton button = createCellButton();

                int row = r;
                int col = c;

                button.addActionListener(e -> handlePlayerMove(row, col));

                buttons[r][c] = button;
                gridPanel.add(button);
            }
        }

        centerPanel.add(consoleWrapper, BorderLayout.NORTH);
        centerPanel.add(gridPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 12, 0));
        bottomPanel.setBackground(CARD);

        JButton newRoundButton = createSmallButton("New Round");
        JButton menuButton = createSmallButton("Menu");
        JButton resetScoreButton = createSmallButton("Reset Score");

        newRoundButton.addActionListener(e -> resetBoard());
        menuButton.addActionListener(e -> layout.show(mainPanel, "menu"));

        resetScoreButton.addActionListener(e -> {
            xScore = 0;
            oScore = 0;
            updateScore();
            resetBoard();
        });

        bottomPanel.add(newRoundButton);
        bottomPanel.add(menuButton);
        bottomPanel.add(resetScoreButton);

        card.add(topPanel, BorderLayout.NORTH);
        card.add(centerPanel, BorderLayout.CENTER);
        card.add(bottomPanel, BorderLayout.SOUTH);

        background.add(card);
        mainPanel.add(background, "game");
    }

    private JButton createCellButton() {
        JButton button = new JButton("");

        button.setFont(new Font("Arial", Font.BOLD, 72));
        button.setFocusPainted(false);
        button.setBackground(GRID_CELL);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(CYAN, 3));

        return button;
    }

    private void startGame(boolean aiMode, boolean consoleMode) {
        vsAI = aiMode;
        consoleStyle = consoleMode;

        xScore = 0;
        oScore = 0;

        if (vsAI) {
            modeLabel.setText("Mode: Player vs AI");
        } else if (consoleStyle) {
            modeLabel.setText("Mode: Console Style UI");
        } else {
            modeLabel.setText("Mode: Local Player vs Player");
        }

        consoleArea.setVisible(consoleStyle);

        resetBoard();
        updateScore();

        layout.show(mainPanel, "game");
    }

    private void handlePlayerMove(int row, int col) {
        if (gameOver) {
            return;
        }

        char currentPlayer = logic.getCurrentPlayer(board);

        if (vsAI && currentPlayer == 'O') {
            return;
        }

        boolean success = logic.makeMove(board, row, col, currentPlayer);

        if (!success) {
            Toolkit.getDefaultToolkit().beep();
            statusLabel.setText("That spot is already taken!");
            return;
        }

        Toolkit.getDefaultToolkit().beep();
        refreshBoard();

        if (checkEnd(currentPlayer)) {
            return;
        }

        if (vsAI) {
            makeAIMove();
        } else {
            char nextPlayer = logic.getCurrentPlayer(board);
            statusLabel.setText("Player " + nextPlayer + "'s turn");
        }
    }

    private void makeAIMove() {
        Timer timer = new Timer(450, e -> {
            if (gameOver) {
                return;
            }

            int[] move = logic.getAIMove(board);

            if (move != null) {
                logic.makeMove(board, move[0], move[1], 'O');
            }

            refreshBoard();

            if (!checkEnd('O')) {
                statusLabel.setText("Player X's turn");
            }
        });

        timer.setRepeats(false);
        timer.start();
    }

    private boolean checkEnd(char player) {
        if (logic.checkWin(board, player)) {
            gameOver = true;

            if (player == 'X') {
                xScore++;
            } else {
                oScore++;
            }

            updateScore();
            highlightWinner(player);

            if (vsAI && player == 'O') {
                showWinScreen("AI Wins!", "The computer won this round.");
            } else {
                showWinScreen("Player " + player + " Wins!", "Great game!");
            }

            return true;
        }

        if (logic.isDraw(board)) {
            gameOver = true;
            showWinScreen("It's a Draw!", "Nobody won this round.");
            return true;
        }

        return false;
    }

    private void showWinScreen(String title, String message) {
        JDialog dialog = new JDialog(this, true);
        dialog.setUndecorated(true);
        dialog.setSize(430, 260);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(17, 17, 43));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CYAN, 3),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 34));
        titleLabel.setForeground(YELLOW);

        JLabel messageLabel = new JLabel(message);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 17));
        messageLabel.setForeground(Color.WHITE);

        JButton playAgainButton = createNeonButton("Play Again");
        JButton menuButton = createNeonButton("Back to Menu");

        playAgainButton.setMaximumSize(new Dimension(260, 45));
        menuButton.setMaximumSize(new Dimension(260, 45));

        playAgainButton.addActionListener(e -> {
            dialog.dispose();
            resetBoard();
        });

        menuButton.addActionListener(e -> {
            dialog.dispose();
            layout.show(mainPanel, "menu");
        });

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(12));
        panel.add(messageLabel);
        panel.add(Box.createVerticalStrut(25));
        panel.add(playAgainButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(menuButton);

        dialog.add(panel);

        Toolkit.getDefaultToolkit().beep();
        dialog.setVisible(true);
    }

    private void refreshBoard() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                char cell = board.getCell(r, c);

                buttons[r][c].setBackground(GRID_CELL);

                if (cell == 'E') {
                    buttons[r][c].setText("");
                } else {
                    buttons[r][c].setText(String.valueOf(cell));

                    if (cell == 'X') {
                        buttons[r][c].setForeground(CYAN);
                    } else {
                        buttons[r][c].setForeground(PINK);
                    }
                }
            }
        }

        updateConsoleArea();
    }

    private void updateConsoleArea() {
        String text = "";

        text += "   0   1   2\n";
        text += "  -----------\n";

        for (int r = 0; r < 3; r++) {
            text += r + " | ";

            for (int c = 0; c < 3; c++) {
                char cell = board.getCell(r, c);

                if (cell == 'E') {
                    text += "- | ";
                } else {
                    text += cell + " | ";
                }
            }

            text += "\n";
            text += "  -----------\n";
        }

        consoleArea.setText(text);
    }

    private void resetBoard() {
        board.clearBoard();
        gameOver = false;
        statusLabel.setText("Player X's turn");
        refreshBoard();
    }

    private void updateScore() {
        scoreLabel.setText("X: " + xScore + " | O: " + oScore);
    }

    private void highlightWinner(char player) {
        Color winColor = new Color(90, 255, 120);

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
}