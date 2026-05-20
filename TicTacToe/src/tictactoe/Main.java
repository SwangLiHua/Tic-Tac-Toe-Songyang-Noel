package tictactoe;

import java.util.Scanner;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        boolean running = true;

        while (running) {
            System.out.println("================================");
            System.out.println("          TIC TAC TOE           ");
            System.out.println("================================");
            System.out.println("1. Console UI - Player vs Player");
            System.out.println("2. Console UI - Player vs AI");
            System.out.println("3. Java Swing UI");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = input.nextInt();

            if (choice == 1) {
                ConsoleGame game = new ConsoleGame(false);
                game.start();
            } else if (choice == 2) {
                ConsoleGame game = new ConsoleGame(true);
                game.start();
            } else if (choice == 3) {
                SwingUtilities.invokeLater(() -> new TicTacToeSwingUI());
            } else if (choice == 4) {
                running = false;
                System.out.println("Goodbye!");
            } else {
                System.out.println("Invalid choice. Try again.");
            }

            System.out.println();
        }

        input.close();
    }
}