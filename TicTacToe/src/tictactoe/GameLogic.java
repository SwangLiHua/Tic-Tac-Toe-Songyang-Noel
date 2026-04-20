package tictactoe;

public class GameLogic {


	
	
	public boolean isGameOver(Board board) {
		if(checkWin(board, 'X') == true) {
			return true;
		}
		else if(checkWin(board, 'O') == true) {
			return true;
		} else if(isDraw(board) == true) {
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

public char getCurrentPlayer(Board board)
{
	char[][] grid = board.getGrid();
	int xCount = 0;
	int oCount = 0;
	for(int row = 0; row < grid.length; row++)
	{
		for(int col = 0; col < grid[0].length; col++) 
		{
			if(board.getCell(row, col) == 'X')
			{
				xCount++;
			}
			else if(board.getCell(row, col) == 'O')
			{
				oCount++;
			}
			
		}
		
		}
	if(xCount == oCount)
	{
		return 'X';
	}
	else
	{
		return 'O';
	}
}


public boolean makeMove(Board board, int row, int col) {
	char move = getCurrentPlayer(board);
	if(board.getCell(row, col) != 'O' && board.getCell(row, col) != 'X' && board.isValidBoardFile() && row >= 0 && col >= 0) {
		board.setCell(row, col, move);
		board.saveBoardToFile();
	} else {
		return false;
	}
	return true;
}


}
