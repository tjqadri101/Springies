package main;
import java.awt.BorderLayout;
import javax.swing.JFrame;

/*
 * Main class for springies_team04
 * Run this class
 */

public class Main {
	// Constants
	public static final String TITLE = "springies_team04";

	public static void main(String[] args) {
		final Simulation sim = new Simulation();		
		
		JFrame frame = new JFrame(TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(sim, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
}
