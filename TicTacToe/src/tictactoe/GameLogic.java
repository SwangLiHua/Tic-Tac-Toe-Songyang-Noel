package tictactoe;

public class GameLogic {

	
	//holds game play data in an instance variable
	private char[][] grid;
	
	//holds game play data in a CSV file
	private String filename;
	
	
	
	public boolean isGameOver(Board board) {
		if(board.checkWin(board, 'X') == true) {
			return true;
		}
		else if(board.checkWin(board, 'O') == true) {
			return true;
		} else if(board.isDraw(board) == true) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean checkWin(Board board, char player)
	{
		
		
		for (int row = 0; row < 3; row++) {
	        if (board.getCell(row, 0) == player &&
	            board.getCell(row, 1) == player &&
	            board.getCell(row, 2) == player) {
	            return true;
	        }
	    }

	    
	    for (int col = 0; col < 3; col++) {
	        if (board.getCell(0, col) == player &&
	            board.getCell(1, col) == player &&
	            board.getCell(2, col) == player) {
	            return true;
	        }
	    }

	    
	    if (board.getCell(0, 0) == player &&
	        board.getCell(1, 1) == player &&
	        board.getCell(2, 2) == player) {
	        return true;
	    }

	   
	    if (board.getCell(0, 2) == player &&
	        board.getCell(1, 1) == player &&
	        board.getCell(2, 0) == player) {
	        return true;
	    }

	    return false;
	}

	
public boolean isDraw(Board board) {
		
		char[][] grid = board.getGrid();
		for(int row = 0; row < grid.length; row++) {
			for(int col = 0; col < grid[0].length; col++) {
				if(grid[row][col] != 'E') {
					return false;
				}
			}
		}
		if(checkWin(board, 'X') == true) {
			return false;
		}
		else if(checkWin(board, 'O') == true){
			return false;
		}
		return true;
	}
}
