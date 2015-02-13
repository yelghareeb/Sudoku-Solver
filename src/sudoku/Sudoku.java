/*
 * Sudoku Solver using backtracking
 * Author: Youssef ElGhareeb
 */

package sudoku;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Sudoku {

	private JFrame frame;
	private Solver solver;
	private int game[][];
	private JButton buttons [][];
	private ExecutorService threadExecutor;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sudoku window = new Sudoku();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Sudoku() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//1. Generate and solve the game
		//------------------------------
		game = Generator.generateGame();
		
		solver = new Solver();
		solver.setGrid(game);
		
		threadExecutor = Executors.newCachedThreadPool();
		threadExecutor.execute( solver ); // start task1
		threadExecutor.shutdown();
		
		//2. Create the UI 
		//----------------
		frame = new JFrame();
		frame.setBounds(100, 100, 632, 485);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//Solve button
		JButton btnSolve = new JButton("Solve");
		btnSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int [][] solution = solver.getSolution();
				
				if (solution!=null) {
					for (int i=0;i<9;i++) {
						for (int j=0;j<9;j++) {
							buttons[i][j].setText(Integer.toString(solution[i][j]));
						}
					}
				}
				else {
					System.out.println ("The solution is Nil");
				}
			}
		});
		btnSolve.setBounds(501, 207, 89, 23);
		frame.getContentPane().add(btnSolve);
		
		
		//Grid buttons
		//------------
		buttons = new JButton[9][9];
		for (int i=0;i<9;i++) {
			for (int j=0; j<9; j++) {
				buttons[i][j] = new JButton ();
				if (game[i][j]!=-1) {
					buttons[i][j].setText(Integer.toString(game[i][j]));
					buttons[i][j].setForeground(Color.blue);
				}
				else {
					buttons[i][j].setForeground(Color.RED);
				}
				
				buttons[i][j].setBounds(j*50, i*50, 50, 50);
				frame.getContentPane().add(buttons[i][j]);
			}
		}
	}
}
