package sudoku;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Sudoku {

	private JFrame frame;
	private Solver solver;
	private int game[][];
	private JButton buttons [][];
	
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
		frame = new JFrame();
		frame.setBounds(100, 100, 632, 485);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnSolve = new JButton("Solve");
		btnSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				solver = new Solver();
				int [][] solution = solver.solve(game);
				
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
		
		game = Generator.generateGame();
		
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
				buttons[i][j].addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent arg0) {
					}
				});
				buttons[i][j].setBounds(j*50, i*50, 50, 50);
				frame.getContentPane().add(buttons[i][j]);
			}
		}
	}
}
