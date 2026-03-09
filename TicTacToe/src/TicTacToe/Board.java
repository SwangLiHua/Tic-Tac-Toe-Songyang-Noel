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
		//if the board is valid then create the 3x3 grid
		//and load the board from the file
	}
	
	//loads the grid with the file contents
	public void loadBoardFromFile() {
		//use a scanner to read with the board values
		//and populate the grid with the board values
		//remember to close the scanner afterwards
		//use isValidBoard method as a guide
	}
	
	
	public boolean isValidBoardFile() {
		
	}
	
	
	public void saveBoardToFile() {
		
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
	
	
	public void clearBoard() {
		char clearedBoard[][] = {{'E', 'E', 'E'},
							   	{'E', 'E', 'E'},
								{'E', 'E', 'E'}};
		this.grid = clearedBoard;
		this.saveBoardToFile();
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
