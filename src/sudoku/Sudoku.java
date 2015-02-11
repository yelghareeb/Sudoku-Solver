package sudoku;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Sudoku {

	private JFrame frame;
	
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
		frame.setBounds(100, 100, 465, 485);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		int game[][] = Generator.generateGame();
		
		JButton buttons [][] = new JButton[9][9];
		for (int i=0;i<9;i++) {
			for (int j=0; j<9; j++) {
				buttons[i][j] = new JButton ();
				if (game[i][j]!=-1) buttons[i][j].setText(Integer.toString(game[i][j]));
				
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
