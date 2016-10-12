package leaderboard;

import map.Map;
import map.MapGrid;
import map.MapConstants;
import robot.Robot;
import robot.RobotConstants;
import robot.Sensor;
import robot.ShortestPathAlgo;
import robot.ExploreAlgo;
import java.util.*;
import java.io.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class RealRun extends JFrame{

	private static JFrame appFrame = null;
	private static JPanel mainCards = null;
	private static JPanel buttonsCards = null;
	private static JPanel mainButtons = null;

	private static Robot realRobot;

	private static Map realMap;

	private static CommMgr commMgr = CommMgr.getCommMgr();

	public static void main(String args[]){




		//set connection
		commMgr.setConnection();


		System.out.println("Start RealRun!\n");

		System.out.println("Waiting Android to send Robot Location...\n");

		String robotLocation = commMgr.recvMsg();

		//String robotLocation = "{\"robotPosition\" : [10,11,1]}";

		//Decode the position
		String[] parts = robotLocation.split(":");
		String[] loc = parts[1].substring(2, parts[1].length()-2).split(",");

		int r = Integer.parseInt(loc[0]);
		int c = Integer.parseInt(loc[1]);
		int h = Integer.parseInt(loc[2]);

		//System.out.println(parts[1].substring(2, parts[1].length()-2));

		//System Log
		System.out.printf("Setting Robot Location: (%d, %d)\n", r, c);
		String dir = "";
		switch (h){
			case 1: dir = "Right"; break;
			case 2: dir = "Down"; break;
			case 3: dir = "Left"; break;
			case 4: dir = "Up"; break;
			default: dir = "error direction"; break;
		}
		System.out.println("Robot Heading " + dir);

		//initialize robot
		realRobot = new Robot(new MapGrid(r, c), h);

		// realRobot = new Robot (new MapGrid(2, 2), 1);

		//front sensor
		Sensor s1 = new Sensor(3, 1, 0, 1);
		Sensor s4 = new Sensor(3, 1, -1, 1);
		Sensor s5 = new Sensor(3, 1, 1, 1);
		//right sensor
		Sensor s2 = new Sensor(3, 2, -1, 1);
		//left sensor
		Sensor s3 = new Sensor(3, 4, 1, 1);

		realRobot.addSensor(s5);
		realRobot.addSensor(s1);
		realRobot.addSensor(s4);
		realRobot.addSensor(s3);
		realRobot.addSensor(s2);

		//initialize map
		realMap = new Map(realRobot);
		realMap.addBorder();

		// realMap.addObstacle(5,5);
		// realMap.addObstacle(5,6);
		// realMap.addObstacle(5,7);

		// realMap.printMapWithVirtualWall();

		// realMap.removeObstacle(5, 7);
		// realMap.printMapWithVirtualWall();

		// String[] stream = realMap.generateMapStreamToAndroid();

		// System.out.println(stream[0]);
		// System.out.println(stream[1]);


		// commMgr.sendMsg(stream[0], CommConstants.MSG_TO_ANDROID);
		// commMgr.sendMsg(stream[1], CommConstants.MSG_TO_ANDROID);


		//ExploreAlgo e = new ExploreAlgo(null, realMap, realRobot); 

		//e.runRealExploration();

		//ShortestPathAlgo s = new ShortestPathAlgo(realMap, realRobot);


		//Real-time GUI

		System.out.println("Showing GUI.\n");

		displayEverythings();

		class LeaderboardRun extends SwingWorker<Integer, String>{
			protected Integer doInBackground() throws Exception{

				CardLayout cl = ((CardLayout) mainCards.getLayout());
				cl.show(mainCards, "MAIN");
				realMap.repaint();

				ExploreAlgo e = new ExploreAlgo(null, realMap, realRobot); 
				e.runRealExploration();

				ShortestPathAlgo s = new ShortestPathAlgo(realMap, realRobot);
				s.runRealShortestPath();

				return 1;
			}
		}

		new LeaderboardRun().execute();


	}

	private static void displayEverythings(){
		// Main frame for displaying everything
		appFrame = new JFrame();
		appFrame.setTitle("MDP Group 2");
		appFrame.setSize(new Dimension(800, 870));
		appFrame.setResizable(false);
		
		// Center the app frame
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		appFrame.setLocation(dim.width/2 - appFrame.getSize().width/2, dim.height/2 - appFrame.getSize().height/2);
		
		// Create the CardLayouts for storing the different views
		mainCards = new JPanel(new CardLayout());
		
		// Initialize the main CardLayout
		initMainLayout();
		
		// Add CardLayouts to content pane
		Container contentPane = appFrame.getContentPane();
		contentPane.add(mainCards, BorderLayout.CENTER);
		//contentPane.add(buttonsCards, BorderLayout.SOUTH);
		
		// Display the application
		appFrame.setVisible(true);
		appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static void initMainLayout() {
		
		// Initialize the Map for simulation
		mainCards.add(realMap, "MAIN");
		
		CardLayout cl = ((CardLayout) mainCards.getLayout());
	    cl.show(mainCards, "MAIN");		
	}





}