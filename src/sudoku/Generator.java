package sudoku;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Generator {
	public static int [][] generateGame () {
		int game[][] = new int[9][9];
		Scanner input;
		try {
			input = new Scanner (new FileReader ("Puzzles/puzzle_1.txt") );
			String s = input.next();
			for (int i=0;i<9;i++) {
				for (int j=0;j<9;j++) {
					char c = s.charAt(i*9+j);
					if (c=='.') game[i][j] = -1;
					else game[i][j] = c-'0';
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return game;
	}
}
