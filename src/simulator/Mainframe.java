package simulator;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import map.Map;
import map.MapGrid;
import robot.ExploreAlgo;
import robot.Robot;
import robot.Sensor;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.Graphics;

public class Mainframe extends JFrame {

	/**
	 * Static Variables
	 */
	private final int PROG_MIN = 0;
	private final int PROG_MAX = 0;
	private static int mapXLength;
	private static int mapYLength;
	private final int M = 20;
	private final int N = 15;
	private final List<JButton> list = new ArrayList<JButton>();
	
	/**
	 * Instantiate
	 */
	private static Robot mdpRobot = null;
	private static Map stpMap = null;  //shortest path map
	private static Map trueMap = null;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JButton btnReset;
	JButton btnExplore;
	JButton btnShortestPath;
	JProgressBar progressBar_exp;
	JProgressBar progressBar_sp;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Mainframe frame = new Mainframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the frame.
	 */
	public Mainframe() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 985, 784);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		getContentPane().setLayout(null);
		setTitle("MDP Simulator");

		/**
		 * Buttons
		 */
		// Reset Button
		JButton btnReset = new JButton("Reset");
		btnReset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		btnReset.setBounds(756, 702, 99, 23);
		contentPane.add(btnReset);
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnShortestPath.setEnabled(true);
			}
		});

		// Shortest Path Button
		JButton btnShortestPath = new JButton("Shortest Path");
		btnShortestPath.setBounds(756, 566, 99, 23);
		contentPane.add(btnShortestPath);
		// btnShortestPath.setEnabled(false);

		btnShortestPath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// robot moving, call paint function

				// progress bar
				for (int i = PROG_MIN; i <= PROG_MAX; i++) {
					// add percent calculated
					updateBar_sp(i);

				}
			}
		});
		btnShortestPath.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		// Explore Button
		JButton btnExplore = new JButton("Explore");
		getRootPane().setDefaultButton(btnExplore);
		btnExplore.requestFocus();
		btnExplore.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btnShortestPath.setEnabled(false);
				// robot start moving, call paint 
				ExploreAlgo e = new ExploreAlgo(trueMap, mdpRobot);
				e.runExploration();
				// progress bar
				for (int i = PROG_MIN; i <= PROG_MAX; i++) {
					// add percent calculated
					updateBar_exp(i);
				}

				btnShortestPath.setEnabled(true);
			}
		});
		btnExplore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnExplore.setBounds(756, 412, 99, 23);
		contentPane.add(btnExplore);

		/**
		 * Progress Bar
		 */
		// Explore Progress Bar
		JProgressBar progressBar_exp = new JProgressBar();
		progressBar_exp.setBounds(756, 446, 146, 14);
		progressBar_exp.setMinimum(PROG_MIN);
		progressBar_exp.setMaximum(PROG_MAX);
		contentPane.add(progressBar_exp);

		// Shortest Progress Bar
		JProgressBar progressBar_sp = new JProgressBar();
		progressBar_sp.setBounds(756, 600, 146, 14);
		progressBar_sp.setMinimum(PROG_MIN);
		progressBar_sp.setMaximum(PROG_MAX);
		contentPane.add(progressBar_sp);

		/**
		 * Grid Buttons
		 */
		JPanel p = new JPanel();
		p.setBounds(50, 53, 581, 561);
		contentPane.add(p);
		p.setLayout(new GridLayout(M, N));
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < N; j++) {
				System.out.println("print" + i + " " + j);
				JButton gb = createGridButton(i, j);
				list.add(gb);
				p.add(gb);
			}
		}
		// Set Start Point
		for (int i = 17; i < M; i++) {
			for (int j = 0; j < 3; j++) {
				Mainframe.this.getGridButton(i, j).setBackground(Color.YELLOW);
			}
		}
		// Set End Point
		for (int i = 0; i < 3; i++) {
			for (int j = 12; j < N; j++) {
				Mainframe.this.getGridButton(i, j).setBackground(Color.YELLOW);
			}
		}

		/**
		 * Robot Circle
		 */
		// JPanel r = new JPanel();
		// contentPane.add(r);
		// r.paint(null);

		/**
		 * Text Area indicating Color Representation
		 */
		JTextArea txtrExplored = new JTextArea();
		txtrExplored.setFont(new Font("Britannic Bold", Font.PLAIN, 13));
		txtrExplored.setForeground(Color.WHITE);
		txtrExplored.setBackground(Color.BLUE);
		txtrExplored.setWrapStyleWord(true);
		txtrExplored.setText("Explored");
		txtrExplored.setBounds(756, 95, 67, 24);
		contentPane.add(txtrExplored);

		JTextArea txtrObstacles = new JTextArea();
		txtrObstacles.setFont(new Font("Britannic Bold", Font.PLAIN, 13));
		txtrObstacles.setForeground(Color.WHITE);
		txtrObstacles.setBackground(Color.BLACK);
		txtrObstacles.setText("Obstacles");
		txtrObstacles.setBounds(756, 130, 67, 24);
		contentPane.add(txtrObstacles);

		JTextArea txtrStartGoal = new JTextArea();
		txtrStartGoal.setFont(new Font("Britannic Bold", Font.PLAIN, 13));
		txtrStartGoal.setBackground(Color.YELLOW);
		txtrStartGoal.setText("Start / Goal");
		txtrStartGoal.setBounds(756, 165, 88, 24);
		contentPane.add(txtrStartGoal);

		JTextArea txtrRobot = new JTextArea();
		txtrRobot.setFont(new Font("Britannic Bold", Font.PLAIN, 13));
		txtrRobot.setBackground(Color.PINK);
		txtrRobot.setText("Robot");
		txtrRobot.setBounds(756, 200, 67, 24);
		contentPane.add(txtrRobot);

	}

	JButton getGridButton(int r, int c) {
		int index = r * N + c;
		return list.get(index);
	}

	private JButton createGridButton(final int row, final int col) {
		final JButton b = new JButton("r" + row + ",c" + col);
		b.setBackground(Color.WHITE);
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				JButton gb = Mainframe.this.getGridButton(row, col);
				System.out.println("r" + row + ",c" + col + " " + (b == gb) + " " + (b.equals(gb)));
			}

		});

		b.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent arg0) {
				// when clicked, if true set false, else set true
				JButton btn = Mainframe.this.getGridButton(row, col);
				if (btn.isEnabled() == true) {
					btn.setEnabled(false);
					if (btn.getBackground() == Color.WHITE)
						btn.setBackground(Color.BLACK);
				} else {
					btn.setEnabled(true);
					if (btn.getBackground() == Color.BLACK)
						btn.setBackground(Color.WHITE);
				}
			}
		});
		return b;
	}

	public void updateBar_exp(int newValue) {
		progressBar_exp.setValue(newValue);
	}

	public void updateBar_sp(int newValue) {
		progressBar_sp.setValue(newValue);
	}

	public void paint(Graphics g) {
	    super.paint(g);
	    g.setColor(new Color(0, 255,0));
	    g.fillOval(100, 100, 50, 50);    
	   
	}
}
