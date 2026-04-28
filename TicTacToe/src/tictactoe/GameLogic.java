package tictactoe;

public class GameLogic {

    public boolean isGameOver(Board board) {
        return checkWin(board, 'X') || checkWin(board, 'O') || isDraw(board);
    }

    public boolean checkWin(Board board, char p) {
        for (int i = 0; i < 3; i++) {
            if (board.getCell(i, 0) == p && board.getCell(i, 1) == p && board.getCell(i, 2) == p) return true;
            if (board.getCell(0, i) == p && board.getCell(1, i) == p && board.getCell(2, i) == p) return true;
        }
        if (board.getCell(0, 0) == p && board.getCell(1, 1) == p && board.getCell(2, 2) == p) return true;
        if (board.getCell(0, 2) == p && board.getCell(1, 1) == p && board.getCell(2, 0) == p) return true;
        return false;
    }

    public boolean isDraw(Board board) {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board.getCell(r, c) == 'E') return false;
            }
        }
        return !checkWin(board, 'X') && !checkWin(board, 'O');
    }

    public char getCurrentPlayer(Board board) {
        int xCount = 0, oCount = 0;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board.getCell(r, c) == 'X') xCount++;
                else if (board.getCell(r, c) == 'O') oCount++;
            }
        }
        return (xCount <= oCount) ? 'X' : 'O';
    }

    public boolean makeMove(Board board, int r, int c) {
        if (board.getCell(r, c) == 'E' && !isGameOver(board)) {
            board.setCell(r, c, getCurrentPlayer(board));
            return true;
        }
        return false;
    }
}