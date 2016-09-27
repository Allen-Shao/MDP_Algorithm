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

	// private static int mapXLength;
	// private static int mapYLength;

	// private static JPanel robotConfig = null;

	// private static JPanel robotMap = null;

	// private static JPanel robotMapButtons = null;

	private static Map stpMap = null;  //shortest path map

	private static Map trueMap = null;

	private static Map exploredMap = null;

	private static Robot mdpRobot = null;

	// private static int startPosRow = 2;
	// private static int startPosCol = 2;

	// private static int startDir = 1;

	

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
		Sensor s1 = new Sensor(3, 1, 0, 1);
		Sensor s2 = new Sensor(3, 2, -1, 0);
		Sensor s3 = new Sensor(5, 4, 1, 0);

		stpMap = new Map(mdpRobot);
		stpMap.loadMap("map.txt");

		trueMap = new Map(mdpRobot);
		trueMap.loadMap("map.txt");
		trueMap.removeVirtualWall();

		exploredMap = new Map(mdpRobot);


		displayEverythings();

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
		mainCards.add(exploredMap, "TIMEEXPLO");
		mainCards.add(exploredMap, "COVERAGEEXPLO");
		
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

	}

}














	// public static void main(String[] args) {
		

		
	// 	// Initialize robot
	// 	if(smartRobot == null) {
	// 		smartRobot = new Robot();  //!! parameters to be added
	// 	}
		
	// 	// --------------------------------------------------------------------
	// 	// Everything below is just for the layout
		
	// 	// Calculate map width & height based on grid size
	// 	mapXLength = MapConstants.MAP_COL * MapConstants.GRID_SIZE;
	// 	mapYLength = MapConstants.MAP_ROW * MapConstants.GRID_SIZE;
		
	// 	// Main frame for displaying everything
	// 	appFrame = new JFrame();
	// 	appFrame.setTitle("MDP Simulator");
	// 	appFrame.setSize(new Dimension(886, 771));
	// 	appFrame.setResizable(false);

	// 	mainCards = new JPanel(new CardLayout());
	// 	buttonsCards = new JPanel(new CardLayout());
