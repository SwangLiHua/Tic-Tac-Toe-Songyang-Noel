package tictactoe;

public class GameLogic {

    public boolean makeMove(Board board, int row, int col) {
        char player = getCurrentPlayer(board);
        return makeMove(board, row, col, player);
    }

    public boolean makeMove(Board board, int row, int col, char player) {
        if (row < 0 || row > 2 || col < 0 || col > 2) {
            return false;
        }

        if (board.getCell(row, col) != 'E') {
            return false;
        }

        board.setCell(row, col, player);
        return true;
    }

    public char getCurrentPlayer(Board board) {
        int x = 0;
        int o = 0;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board.getCell(r, c) == 'X') {
                    x++;
                } else if (board.getCell(r, c) == 'O') {
                    o++;
                }
            }
        }

        if (x == o) {
            return 'X';
        } else {
            return 'O';
        }
    }

    public boolean checkWin(Board board, char p) {
        for (int i = 0; i < 3; i++) {
            if (board.getCell(i, 0) == p &&
                board.getCell(i, 1) == p &&
                board.getCell(i, 2) == p) {
                return true;
            }

            if (board.getCell(0, i) == p &&
                board.getCell(1, i) == p &&
                board.getCell(2, i) == p) {
                return true;
            }
        }

        if (board.getCell(0, 0) == p &&
            board.getCell(1, 1) == p &&
            board.getCell(2, 2) == p) {
            return true;
        }

        if (board.getCell(0, 2) == p &&
            board.getCell(1, 1) == p &&
            board.getCell(2, 0) == p) {
            return true;
        }

        return false;
    }

    public boolean isDraw(Board board) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board.getCell(r, c) == 'E') {
                    return false;
                }
            }
        }

        return !checkWin(board, 'X') && !checkWin(board, 'O');
    }

    public boolean isGameOver(Board board) {
        return checkWin(board, 'X') || checkWin(board, 'O') || isDraw(board);
    }

    public int[] getAIMove(Board board) {
        int[] winningMove = findWinningMove(board, 'O');

        if (winningMove != null) {
            return winningMove;
        }

        int[] blockingMove = findWinningMove(board, 'X');

        if (blockingMove != null) {
            return blockingMove;
        }

        if (board.getCell(1, 1) == 'E') {
            return new int[] {1, 1};
        }

        int[][] corners = {
            {0, 0},
            {0, 2},
            {2, 0},
            {2, 2}
        };

        for (int i = 0; i < corners.length; i++) {
            int row = corners[i][0];
            int col = corners[i][1];

            if (board.getCell(row, col) == 'E') {
                return new int[] {row, col};
            }
        }

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board.getCell(r, c) == 'E') {
                    return new int[] {r, c};
                }
            }
        }

        return null;
    }

    private int[] findWinningMove(Board board, char player) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board.getCell(r, c) == 'E') {
                    board.setCellNoSave(r, c, player);

                    boolean wins = checkWin(board, player);

                    board.setCellNoSave(r, c, 'E');

                    if (wins) {
                        return new int[] {r, c};
                    }
                }
            }
        }

        return null;
    }
}