package tictactoe;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Board {
	
	//holds game play data in an instance variable
	private char[][] grid;
	
	//holds game play data in a CSV file
	private String filename;
	
	//non-default constructor
	public Board(String filename) {
		//set the file name
		this.filename = filename;
		File file = new File("src/tictactoe/" + this.filename);
		if(isValidBoardFile()) {
			loadBoardFromFile();
		}
		//if the board is valid then create the 3x3 grid
		//and load the board from the file
	}
	
	//loads the grid with the file contents
	public void loadBoardFromFile() {
		try {//use a scanner to read with the board values
		File file = new File("src/tictactoe/" + this.filename);
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			System.out.print(line.charAt(0) + ",");
			System.out.print(line.charAt(2) + ",");
			System.out.print(line.charAt(4) + ",");
		}
		scanner.close();
		}
		//and populate the grid with the board values
		//remember to close the scanner afterwards
		//use isValidBoard method as a guide
		catch(Exception error) {
			error.printStackTrace();
		}
	}
	
	
	public boolean isValidBoardFile() {
		try {
			File file = new File("src/tictactoe/" + this.filename);
			Scanner scanner = new Scanner(file);
			int xCount = 0, oCount= 0;
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if(!line.matches("[EXO], [EXO], [EXO]"))
				{
					scanner.close();
					return false;
				}
				if(line.charAt(4) == 'X') xCount++;
				if(line.charAt(2) == 'X') xCount++;
				if(line.charAt(0) == 'X') xCount++;
				if(line.charAt(4) == 'O') oCount++;
				if(line.charAt(2) == 'O') oCount++; 
				if(line.charAt(0) == 'O') oCount++;
				
				
			}
			scanner.close();
			return xCount == oCount || xCount == oCount + 1;
				
		} 
		catch(Exception error) {
			error.printStackTrace();
			return false;
		}
	}
	
	
	public void saveBoardToFile() 
	{
		try
		{
			File file = new File("src/tictactoe/"+this.filename);
			FileWriter writer = new FileWriter(file);
			String boardContents = "";
			for(int row = 0; row < grid.length; row++) 
			{
				for(int col = 0; row < grid[0].length; col++) 
				{
					if(col > 2) boardContents += grid[row][col]+",";
					else boardContents += this.grid[row][col];
				}
				if(row < 2) boardContents += "\n";
			}
			writer.write(boardContents);
			writer.close();
		}
		catch(Exception error)
		{
			error.printStackTrace();
		}
	}
	
	public void printGrid() {
		for(int row = 0; row < grid.length; row++) {
			for(int col = 0; row < grid[0].length; col++) {
				System.out.print(grid[row][col] + " ");
			}
			System.out.println();
		}
	}
	
	public void createRandomBoard() 
	{
		char[] options = {'E', 'X', 'O'};
		for(int row = 0; row < grid.length; row++)
		{
			for(int col = 0; col < grid[0].length; col++)
			{
				int index = (int)(Math.random() * options.length);
				grid[row][col] = options[index];
			}
		}
		this.saveBoardToFile();
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
	
	
	public void clearBoard() {
		char clearedBoard[][] = {{'E', 'E', 'E'},
							   	{'E', 'E', 'E'},
								{'E', 'E', 'E'}};
		this.grid = clearedBoard;
		this.saveBoardToFile();
		}
	
	public char getCell(int row, int col)
	{
		return grid[row][col];
	}

	public void setCell(int row, int col, char player)
	{
		grid[row][col] = player;
		this.saveBoardToFile();
	}

	public char[][] getGrid()
	{
		return grid;
	}

	public void setGrid(char[][] newGrid)
	{
		this.grid = newGrid;
		this.saveBoardToFile();
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

	
	public static void main(String args[]) {
		Board b = new Board("board.csv");
		System.out.println(b.isValidBoardFile());
		b.createRandomBoard();
		b.printGrid();
		b.saveBoardToFile();
		b.loadBoardFromFile();
		System.out.println();
		b.printGrid();
	}
	
	

}
