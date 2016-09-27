package simulator;

import java.awt.Color;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import map.Map;
import map.MapConstants;
import map.MapGrid;
import robot.ExploreAlgo;
import robot.Robot;
import robot.ShortestPathAlgo;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.awt.GridLayout;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JSpinner;

public class Mainframe extends JFrame {

	/*
	 * Static Variables
	 */
	private final int PROG_MIN = 0;
	private final int PROG_MAX = 100;
	private int percentage = 0;
	private final int M = 20;
	private final int N = 15;
	private int row;
	private int col;
	private final List<JButton> list = new ArrayList<JButton>();

	/*
	 * Instantiate
	 */
	private static Robot mdpRobot = null;
	private static Robot stpRobot = null;
	private static Map stpMap = null; // shortest path map
	private static Map trueMap = null;
	private static Map newMap = new Map();
	private MapGrid grids[][] = new MapGrid[MapConstants.MAP_ROW][MapConstants.MAP_COL];
	private Stack<MapGrid> stpStack = new Stack<MapGrid>();

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
	JLabel speedLabel;

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
		createGridButtons(); //create gird buttons
		setStartPoint(); // set the start and end point
		setEndPoint();
		
		/*
		 * JLabel indicating Color Representation
		 */
		createText();
		
		/*
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
				// btnShortestPath.setEnabled(true);
				trueMap.resetMap();
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

				ShortestPathAlgo s = new ShortestPathAlgo(stpMap, stpRobot);
				s.runShortestPath();
				int original_s_size = stpStack.size();
				while (!stpStack.empty()) {
					MapGrid next = stpStack.pop();
					int r = next.getRow() - 1;
					int c = next.getCol() - 1;
					for (int i = -1; i < 2; i++) {
						for (int j = -1; j < 2; j++) {
							RobotMoving(r, c);
							r++;
							c++;
						}
					}
					percentage = stpStack.size()/original_s_size;
					updateBar_sp(percentage);
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
				// save to grid
				// set button to explored(blue)
				// set map with obstacles
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
		
		// Spinner
		speedLabel = new JLabel("Speed: ", JLabel.CENTER);    
		speedLabel.setLocation(228, 658);
		speedLabel.setSize(99, 67);
		contentPane.add(speedLabel);
		SpinnerModel spinnerModel = new SpinnerNumberModel(1, 1, 5, 1);
		JSpinner spinner = new JSpinner(spinnerModel);
		spinner.setBounds(337, 665, 67, 47);
		spinner.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e){
				speedLabel.setText("Speed: " + ((JSpinner)e.getSource()).getValue());
			}
		});
		contentPane.add(spinner);
		
		/*
		 * Robot Circle
		 */
		// r.paint(null);
	}

	JButton getGridButton(int r, int c) {
		int index = r * N + c;
		return list.get(index);
		// return grids[r][c];
	}

	private JButton RobotMoving(int r, int c) {
		final JButton b = new JButton("r" + row + ",c" + col);
		if (b.getBackground() == Color.BLACK) {
			System.out.println("Hit Obstacle");
		} else {
			b.setBackground(Color.BLUE);
			System.out.println("Robot passed" + " r" + row + ", c" + col);
		}
		return b;
	}

	private JButton createGridButton(final int row, final int col) {
		final JButton b = new JButton("r" + row + ",c" + col);
		b.setBackground(Color.WHITE);
		b.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// MapGrid gb = Mainframe.this.getGridButton(row, col);
				JButton gb = Mainframe.this.getGridButton(row, col);
				System.out.println("r" + row + ", c" + col + " " + (b == gb) + " " + (b.equals(gb)));
			}
		});

		b.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				// when clicked, if true set false, else set true
				JButton btn = Mainframe.this.getGridButton(row, col);
				if (btn.isEnabled() == true) {
					btn.setEnabled(false);
					if (btn.getBackground() == Color.WHITE) {
						btn.setBackground(Color.BLACK);
						newMap.addObstacle(row, col);
						System.out.println("r" + row + ", c" + col + " set false");
					}
				} else {
					btn.setEnabled(true);
					if (btn.getBackground() == Color.BLACK) {
						btn.setBackground(Color.WHITE);
						// to add a remove obstacle method
						System.out.println("r" + row + ", c" + col + " set true");
					}
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
		g.setColor(new Color(0, 255, 0));
		g.fillOval(100, 100, 50, 50);
	}

	public void setStartPoint() {
		for (int i = 17; i < M; i++) {
			for (int j = 0; j < 3; j++) {
				Mainframe.this.getGridButton(i, j).setBackground(Color.YELLOW);
			}
		}
	}

	public void setEndPoint() {
		for (int i = 0; i < 3; i++) {
			for (int j = 12; j < N; j++) {
				Mainframe.this.getGridButton(i, j).setBackground(Color.YELLOW);
			}
		}
	}

	public void createGridButtons() {
		
		JPanel p = new JPanel();
		p.setBounds(50, 53, 581, 561);
		contentPane.add(p);
		p.setLayout(new GridLayout(M, N));
		for (row = 0; row < M; row++) {
			for (col = 0; col < N; col++) {
				System.out.println("print" + row + " " + col);
				JButton gb = createGridButton(row, col);
				list.add(gb);
				// grids[row][col] = new MapGrid(row, col);
				p.add(gb);
			}
		}
	}

	public void createProgressBar() {
		
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
	}

	public void createText() {
		
		JLabel txtrExplored = new JLabel("Explored", JLabel.CENTER);
		txtrExplored.setFont(new Font("Britannic Bold", Font.PLAIN, 13));
		txtrExplored.setForeground(Color.WHITE);
		txtrExplored.setOpaque(true);
		txtrExplored.setBackground(Color.BLUE);
		txtrExplored.setText("Explored");
		txtrExplored.setBounds(756, 95, 67, 24);
		contentPane.add(txtrExplored);

		JLabel txtrObstacles = new JLabel("Obstacles", JLabel.CENTER);
		txtrObstacles.setFont(new Font("Britannic Bold", Font.PLAIN, 13));
		txtrObstacles.setForeground(Color.WHITE);
		txtrObstacles.setOpaque(true);
		txtrObstacles.setBackground(Color.BLACK);
		txtrObstacles.setText("Obstacles");
		txtrObstacles.setBounds(756, 130, 67, 24);
		contentPane.add(txtrObstacles);

		JLabel txtrStartGoal = new JLabel("Start/Goal", JLabel.CENTER);
		txtrStartGoal.setFont(new Font("Britannic Bold", Font.PLAIN, 13));
		txtrStartGoal.setBackground(Color.YELLOW);
		txtrStartGoal.setOpaque(true);
		txtrStartGoal.setText("Start / Goal");
		txtrStartGoal.setBounds(756, 165, 88, 24);
		contentPane.add(txtrStartGoal);

		JLabel txtrRobot = new JLabel("Robot", JLabel.CENTER);
		txtrRobot.setFont(new Font("Britannic Bold", Font.PLAIN, 13));
		txtrRobot.setBackground(Color.PINK);
		txtrRobot.setOpaque(true);
		txtrRobot.setText("Robot");
		txtrRobot.setBounds(756, 200, 67, 24);
		contentPane.add(txtrRobot);

	}
}
