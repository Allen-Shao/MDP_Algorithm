package simulator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Circle_test extends JFrame {

	private JPanel contentPane;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		Circle_test frame = new Circle_test();
		
	}

	/**
	 * Create the frame.
	 */
	public Circle_test() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Circle Test");
		setVisible(true);
		setSize(960,960);

		
	}
	public void paint(Graphics g){
		g.setColor(Color.GREEN);
		g.fillOval(240, 240, 200, 200);
	}
	
}
