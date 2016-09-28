package simulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import java.util.*;
import java.io.*;
import map.Map;
import map.MapConstants;
import map.MapGrid;
import robot.Robot;
import robot.Sensor;
import robot.RobotConstants;
import robot.ExploreAlgo;
import robot.ShortestPathAlgo;
import communication.*;

public class Simulator extends JFrame{

	private static JFrame appFrame = null;

	private static JPanel mainCards = null;
	private static JPanel buttonsCards = null;

	private static JPanel mainButtons = null;



	private static Map stpMap = null;  //shortest path map

	private static Map trueMap = null;

	private static Map exploredMap = null;

	private static Robot mdpRobot = null;

	private static Map exploredCoverLimitMap = null;

	private static Map exploredTimeLimitMap = null;

	private static double coverLimit = 0.0;

	private static int timeLimit = 0;


	

	public static void main(String[] args){
		//This is non-gui version for testing
		/*Shortest Path Test*/

		// stpMap = new Map();

		// stpMap.loadMap("map.txt");

		// stpMap.printMapWithVirtualWall();

		// mdpRobot = new Robot(new MapGrid(2,2), 1);

		// ShortestPathAlgo s = new ShortestPathAlgo(stpMap, mdpRobot);

		// Stack<MapGrid> result = s.runShortestPath();

		// s.printPath(result);

		/*-----------------------------------------------*/

		/*Exploration Test*/

		// trueMap = new Map();

		// mdpRobot = new Robot(new MapGrid(2,2), 1);

		// Sensor s1 = new Sensor(3, 1, 0, 1);
		// Sensor s2 = new Sensor(3, 2, -1, 0);
		// Sensor s3 = new Sensor(5, 4, 1, 0);

		// mdpRobot.addSensor(s1);
		// mdpRobot.addSensor(s2);
		// mdpRobot.addSensor(s3);

		// trueMap.loadMap("map.txt");
		// trueMap.removeVirtualWall();

		// ExploreAlgo e = new ExploreAlgo(trueMap, mdpRobot);
							
		// exploredMap = e.runExploration();

		/*----------------------------------------------*/

		// CommMgr.getCommMgr().setConnection(10000);

		// CommMgr.getCommMgr().sendMsg("test", CommMgr.MSG_TYPE_ANDROID);

		// // while (true){
			
			
		// // 	String test = CommMgr.getCommMgr().recvMsg();


		// // }
		// System.out.println(CommMgr.getCommMgr().isConnected());

		/*-----------------------------------------------*/

		//initialize robot and map
		mdpRobot = new Robot(new MapGrid(2,2), 1);
		//front sensor
		Sensor s1 = new Sensor(3, 1, 0, 1);
		Sensor s4 = new Sensor(3, 1, -1, 1);
		Sensor s5 = new Sensor(3, 1, 1, 1);


		//left sensor
		Sensor s2 = new Sensor(3, 2, -1, 1);

		//right sensor
		Sensor s3 = new Sensor(5, 4, 1, 1);


		mdpRobot.addSensor(s1);
		mdpRobot.addSensor(s2);
		mdpRobot.addSensor(s3);
		mdpRobot.addSensor(s4);
		mdpRobot.addSensor(s5);


		stpMap = new Map(mdpRobot);
		

		trueMap = new Map(mdpRobot);
		

		exploredMap = new Map(mdpRobot);

		exploredCoverLimitMap = new Map(mdpRobot);

		exploredTimeLimitMap = new Map(mdpRobot);


		displayEverythings();

		// ExploreAlgo e = new ExploreAlgo(trueMap, exploredMap, mdpRobot);
		// e.runExploration();

		// trueMap.loadMap("map1.txt");

		// ExploreAlgo e = new ExploreAlgo(trueMap, exploredCoverLimitMap, mdpRobot);
		// e.runExploration();

		//To do list
		//1. load map (select file, input dialog)
		//2. cover limit
		//3. set speed (done)
		//4. time limit
		//5. map descriptor (done)

	}


	private static void displayEverythings(){
		// Main frame for displaying everything
		appFrame = new JFrame();
		appFrame.setTitle("MDP Group 2 Simulator");
		appFrame.setSize(new Dimension(800, 870));
		appFrame.setResizable(false);
		
		// Center the app frame
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		appFrame.setLocation(dim.width/2 - appFrame.getSize().width/2, dim.height/2 - appFrame.getSize().height/2);
		
		// Create the CardLayouts for storing the different views
		mainCards = new JPanel(new CardLayout());
		buttonsCards = new JPanel(new CardLayout());
		
		// Initialize the main CardLayout
		initMainLayout();
		

		// Initialize the buttons CardLayout
		initButtonsLayout();
		
		// Add CardLayouts to content pane
		Container contentPane = appFrame.getContentPane();
		contentPane.add(mainCards, BorderLayout.CENTER);
		contentPane.add(buttonsCards, BorderLayout.SOUTH);
		
		// Display the application
		appFrame.setVisible(true);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static void initMainLayout() {
		
		// Initialize the Map for simulation
		mainCards.add(stpMap, "MAIN");
		mainCards.add(exploredMap, "EXPLO");
		mainCards.add(exploredCoverLimitMap, "COVERAGEEXPLO");
		mainCards.add(exploredTimeLimitMap, "TIMEEXPLO");
		
		CardLayout cl = ((CardLayout) mainCards.getLayout());
	    cl.show(mainCards, "MAIN");		
	}

	private static void initButtonsLayout() {		
		// Initialize the buttons used in main menu
		mainButtons = new JPanel();

		addMainMenuButtons();

		buttonsCards.add(mainButtons, "MAIN_BUTTONS");
		
		// Show the real map (main menu) buttons by default
		CardLayout cl = ((CardLayout) buttonsCards.getLayout());
		cl.show(buttonsCards, "MAIN_BUTTONS");
	}

	private static void addMainMenuButtons(){

		// Load Map
		JButton btnLoadMap = new JButton("Load Map");
		btnLoadMap.setFont(new Font("Arial", Font.BOLD, 13));
		btnLoadMap.setFocusPainted(false);
		btnLoadMap.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				stpMap.loadMap("map1.txt");
				stpMap.setAllExplored();
				trueMap.loadMap("map1.txt");
				trueMap.removeVirtualWall();
				CardLayout cl = ((CardLayout) mainCards.getLayout());
	   			cl.show(mainCards, "MAIN");
				stpMap.repaint();
			}
		});
		mainButtons.add(btnLoadMap);


		//Exploration
		class Exploration extends SwingWorker<Integer, String>{
			protected Integer doInBackground() throws Exception{
				mdpRobot.setPosition(new MapGrid(2, 2));
				mdpRobot.setHeading(1);

				exploredMap.setUnExplored();

				CardLayout cl = ((CardLayout) mainCards.getLayout());
				cl.show(mainCards, "EXPLO");
				exploredMap.repaint();
				ExploreAlgo e = new ExploreAlgo(trueMap, exploredMap, mdpRobot);
				e.runExploration();

				String mapDescriptor = generateMapDescriptor();

				System.out.println(mapDescriptor);

				return 1;

			}
		}

		JButton btnExploration = new JButton("Exploration");
		btnExploration.setFont(new Font("Arial", Font.BOLD, 13));
		btnExploration.setFocusPainted(false);
		btnExploration.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				CardLayout cl = ((CardLayout) mainCards.getLayout());
				cl.show(mainCards, "EXPLO");
				new Exploration().execute();
			}
		});
		mainButtons.add(btnExploration);


		//Shortest Path
		class ShortestPath extends SwingWorker<Integer, String>{
			protected Integer doInBackground() throws Exception
			{
				mdpRobot.setPosition(new MapGrid(2, 2));
				mdpRobot.setHeading(4);
				stpMap.repaint();
				ShortestPathAlgo s = new ShortestPathAlgo(stpMap, mdpRobot);
				Stack<MapGrid> result = s.runShortestPath();
				s.printPath(result);

				stpMap.printMap();

				return 1;
			}
		}

		JButton btnShortestPath = new JButton("Fastest Path");
		btnShortestPath.setFont(new Font("Arial", Font.BOLD, 13));
		btnShortestPath.setFocusPainted(false);
		btnShortestPath.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				CardLayout cl = ((CardLayout) mainCards.getLayout());
			    cl.show(mainCards, "MAIN");
			    new ShortestPath().execute();
			}
		});
		mainButtons.add(btnShortestPath);

		

		// Set robot speed (X steps per second)
		JButton btnSpeed = new JButton("Robot Speed");
		btnSpeed.setFont(new Font("Arial", Font.BOLD, 13));
		btnSpeed.setFocusPainted(false);
		btnSpeed.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				JDialog d1=new JDialog(appFrame,"Change Robot Speed",true);
				d1.setSize(400,400);
				d1.setLayout(new FlowLayout());
				JTextField speedTF = new JTextField(5);
				JButton speedSaveButton = new JButton("Save");
				
				speedSaveButton.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
						mdpRobot.setSpeed(Integer.parseInt(speedTF.getText()));
						System.out.println("set robot speed= " + mdpRobot.getSpeed());
						d1.setVisible(false);
					}
				});

		        d1.add(new JLabel("Enter Speed (steps per second): "));
		        d1.add(speedTF);
		        d1.add(speedSaveButton);

		        d1.setVisible(true);
			}
		});
		mainButtons.add(btnSpeed);

		//Coverlimit Exploration
		class CoverLimitedExploration extends SwingWorker<Integer, String>{
			protected Integer doInBackground() throws Exception{
				mdpRobot.setPosition(new MapGrid(2, 2));
				mdpRobot.setHeading(1);

				exploredCoverLimitMap.setUnExplored();

				CardLayout cl = ((CardLayout) mainCards.getLayout());
				cl.show(mainCards, "COVERAGEEXPLO");
				exploredCoverLimitMap.repaint();
				ExploreAlgo e = new ExploreAlgo(trueMap, exploredCoverLimitMap, mdpRobot, 0.5, 1000000);
				e.runExploration();

				//String mapDescriptor = generateMapDescriptor();

				//System.out.println(mapDescriptor);

				return 1;

			}
		}

		JButton btnCoverLimitedExploration = new JButton("Coverage Limited");
		btnCoverLimitedExploration.setFont(new Font("Arial", Font.BOLD, 13));
		btnCoverLimitedExploration.setFocusPainted(false);
		btnCoverLimitedExploration.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				CardLayout cl = ((CardLayout) mainCards.getLayout());
				cl.show(mainCards, "COVERAGEEXPLO");
				new CoverLimitedExploration().execute();
			}
		});
		mainButtons.add(btnCoverLimitedExploration);

		//Timelimit Exploration
	}

	private static String generateMapDescriptor(){
		String mapDescriptor = "";

		//part1
		mapDescriptor += "11\n";
		for (int i=1;i<MapConstants.MAP_ROW-1;i++){
			for (int j=1; j<MapConstants.MAP_COL-1;j++){
				if (exploredMap.getGrid(i, j).isExplored()){
					mapDescriptor += "1";
				}
				else {
					mapDescriptor += "0";
				}
			}
			mapDescriptor += "\n"; 
		}
		mapDescriptor += "11\n";
		//part2
		for (int i=1;i<MapConstants.MAP_ROW-1;i++){
			for (int j=1; j<MapConstants.MAP_COL-1;j++){
				if (exploredMap.getGrid(i, j).isExplored()){
					if (exploredMap.getGrid(i, j).isObstacle()){
						mapDescriptor += "1";
					}
					else {
						mapDescriptor += "0";
					}
				}
			}
			mapDescriptor += "\n"; 
		}

		System.out.println("Map mapDescriptor:");
		System.out.println(mapDescriptor);

		//converto hexadecimal
		String mapDescriptorHex = "";
		int digitCount = 0;
		int sum = 0;
		String tempDigit = "";
		for (int i = mapDescriptor.length()-1; i>=0; i--){
			if (mapDescriptor.charAt(i) != '\n'){
				digitCount++;
				tempDigit = mapDescriptor.charAt(i) + tempDigit;
				if (mapDescriptor.charAt(i) == '1'){
					int temp = 1;
					for (int k = 0; k < digitCount-1; k++){
						temp *= 2;
					}
					sum += temp;
				}

			}

			if (digitCount == 4){

				if (sum < 10){
					mapDescriptorHex = Integer.toString(sum) + mapDescriptorHex;
				}
				else {
					switch (sum){
						case 10:
							mapDescriptorHex = "A" + mapDescriptorHex;
							break;
						case 11:
							mapDescriptorHex = "B" + mapDescriptorHex;
							break;
						case 12:
							mapDescriptorHex = "C" + mapDescriptorHex;
							break;
						case 13:
							mapDescriptorHex = "D" + mapDescriptorHex;
							break;
						case 14:
							mapDescriptorHex = "E" + mapDescriptorHex;
							break;
						case 15:
							mapDescriptorHex = "F" + mapDescriptorHex;
							break;
					}
				}
				digitCount = 0;
				sum = 0;
				tempDigit = "";
			}
		}



		return mapDescriptorHex;

	}

}

	// 	class timeExploration extends SwingWorker<Integer, String>{
	// 	    protected Integer doInBackground() throws Exception{
	// 			bot.setRobotPos(1,1);
	// 			CardLayout cl = ((CardLayout) _mainCards.getLayout());
	// 		    cl.show(_mainCards, "TIMEEXPLO");
	// 			simTimeExMap.repaint();
	// 			ExplorationAlgo timeExpo = new ExplorationAlgo(simTimeExMap, simShortestPathMap, bot);
	// 			timeExpo.runExploration(timeLimited);
	// 			return 333;
	// 		}
	// 	}
	// 	// Time-limited Exploration button
	// 	JButton btn_TimeExploration = new JButton("Time-limited");
	// 	btn_TimeExploration.setFont(new Font("Arial", Font.BOLD, 13));
	// 	btn_TimeExploration.setFocusPainted(false);
	// 	btn_TimeExploration.addMouseListener(new MouseAdapter() {
	// 		public void mousePressed(MouseEvent e) {
	// 			JDialog d2=new JDialog(_appFrame,"Time Limit Exploration",true);
	// 			d2.setSize(400,400);
	// 			d2.setLayout(new FlowLayout());
	// 			JTextField timeTF = new JTextField(5);
	// 			JButton timeSaveButton = new JButton("Save");
				
	// 			timeSaveButton.addMouseListener(new MouseAdapter() {
	// 			public void mousePressed(MouseEvent e) {
	// 					timeLimited = (Integer.parseInt(timeTF.getText()));
	// 					CardLayout cl = ((CardLayout) _mainCards.getLayout());
	// 				    cl.show(_mainCards, "TIMEEXPLO");
	// 				    new timeExploration().execute();
	// 				}
	// 			});

	// 	        d2.add(new JLabel("Enter time for exploration (in Second): "));
	// 	        d2.add(timeTF);
	// 	        d2.add(timeSaveButton);

	// 	        d2.setVisible(true);
		        
	// 		}
	// 	});
	// 	_mainButtons.add(btn_TimeExploration);

	// 	// for multithreading
	// 	class coverageExploration extends SwingWorker<Integer, String>{
	// 	    protected Integer doInBackground() throws Exception{
	// 			bot.setRobotPos(1,1);
	// 			CardLayout cl = ((CardLayout) _mainCards.getLayout());
	// 		    cl.show(_mainCards, "COVERAGEEXPLO");
	// 			simCoverageExMap.repaint();

	// 			ExplorationAlgo coverageExpo = new ExplorationAlgo(simCoverageExMap, simShortestPathMap, bot);
	// 			coverageExpo.runExploration(coverageLimited);
	// 			return 444;
	// 		}
	// 	}
	// 	// Coverage-limited Exploration button
	// 	JButton btn_CoverageExploration = new JButton("Coverage-limited");
	// 	btn_CoverageExploration.setFont(new Font("Arial", Font.BOLD, 13));
	// 	btn_CoverageExploration.setFocusPainted(false);
	// 	btn_CoverageExploration.addMouseListener(new MouseAdapter() {
	// 		public void mousePressed(MouseEvent e) {
	// 			JDialog d3=new JDialog(_appFrame,"Coverage Limit Exploration",true);
	// 			d3.setSize(400,400);
	// 			d3.setLayout(new FlowLayout());
	// 			JTextField coverageTF = new JTextField(5);
	// 			JButton coverageSaveButton = new JButton("Save");
				
	// 			coverageSaveButton.addMouseListener(new MouseAdapter() {
	// 			public void mousePressed(MouseEvent e) {
	// 					coverageLimited = (Integer.parseInt(coverageTF.getText()));
	// 					new coverageExploration().execute();
	// 				}
	// 			});
	// 	        d3.add(new JLabel("Enter coverage for exploration ( % of the maze squares): "));
	// 	        d3.add(coverageTF);
	// 	        d3.add(coverageSaveButton);
	// 	        d3.setVisible(true);
	// 		}
	// 	});
	// 	_mainButtons.add(btn_CoverageExploration);
	// }


