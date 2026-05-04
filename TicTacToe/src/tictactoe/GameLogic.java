package tictactoe;

public class GameLogic {

    public boolean makeMove(Board board, int row, int col) {

        if (board.getCell(row, col) != 'E') {
            return false;
        }

        char move = getCurrentPlayer(board);
        board.setCell(row, col, move);

        return true;
    }

    public char getCurrentPlayer(Board board) {

        int x = 0, o = 0;

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board.getCell(r, c) == 'X') x++;
                if (board.getCell(r, c) == 'O') o++;
            }
        }

        return (x == o) ? 'X' : 'O';
    }

    public boolean checkWin(Board board, char p) {

        for (int i = 0; i < 3; i++) {
            if (board.getCell(i,0)==p && board.getCell(i,1)==p && board.getCell(i,2)==p)
                return true;

            if (board.getCell(0,i)==p && board.getCell(1,i)==p && board.getCell(2,i)==p)
                return true;
        }

        if (board.getCell(0,0)==p && board.getCell(1,1)==p && board.getCell(2,2)==p)
            return true;

        if (board.getCell(0,2)==p && board.getCell(1,1)==p && board.getCell(2,0)==p)
            return true;

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

    public boolean isGameOver(Board board) {
        return checkWin(board,'X') ||
               checkWin(board,'O') ||
               isDraw(board);
    }
}