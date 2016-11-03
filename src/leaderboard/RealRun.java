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

import java.util.concurrent.TimeUnit;


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
		while (robotLocation.charAt(0) != "{"){
			String robotLocation = commMgr.recvMsg();
		}
		// String robotLocation = "{\"robotPosition\" : [2,2,4]}";

		//Decode the position
		String[] parts = robotLocation.split(":");
		String[] loc = parts[1].substring(2, parts[1].length()-2).split(",");

		int r = Integer.parseInt(loc[0]);
		int c = Integer.parseInt(loc[1]);
		int h = Integer.parseInt(loc[2]);

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
		Sensor s1 = new Sensor(RobotConstants.SENSOR_SHORT_RANGE, 1, 0, 1);
		Sensor s4 = new Sensor(RobotConstants.SENSOR_SHORT_RANGE, 1, -1, 1);
		Sensor s5 = new Sensor(RobotConstants.SENSOR_SHORT_RANGE, 1, 1, 1);
		//right sensor
		Sensor s2 = new Sensor(RobotConstants.SENSOR_SHORT_RANGE, 2, -1, 1);
		//left sensor
		Sensor s3 = new Sensor(RobotConstants.SENSOR_SHORT_RANGE, 4, 1, 1);

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

				Scanner sc = new Scanner(System.in);
				System.out.print("Select Exploration Mode(1.Right 2.Left): ");
				//int choice = sc.nextInt();
				int choice = 2;

				ExploreAlgo e = new ExploreAlgo(null, realMap, realRobot); 

				switch (choice){
					case 1: e.runRealExplorationRight(); break;
					case 2: e.runRealExplorationLeft();break;
					default: e.runRealExplorationRight(); break;
				}

				//realMap.loadMap("map7.txt");
				//realMap.printMapWithVirtualWall();

				ShortestPathAlgo s = new ShortestPathAlgo(realMap, realRobot);
				s.runRealShortestPath();


				// commMgr.sendMsg("fw4dw2aw1dw1aw1dw1aw9w2dw8q", CommConstants.MSG_TO_ARDUINO);

				// commMgr.sendMsg("1111", CommConstants.MSG_TO_ANDROID);
				// commMgr.sendMsg("2222", CommConstants.MSG_TO_ANDROID);





				// commMgr.sendMsg(CommConstants.ROBOT_RIGHT_CALIBRATION, CommConstants.MSG_TO_ARDUINO);

				// try{
				// 		TimeUnit.MILLISECONDS.sleep(CommConstants.COMM_DELAY_TIME*10);
				// 	} catch(InterruptedException e){
				// 		System.out.println("InterruptedException");
				// }

				// for (int i=0; i<10; i++){
				// 	commMgr.sendMsg(CommConstants.ROBOT_TURN_LEFT, CommConstants.MSG_TO_ARDUINO);
				// 	try{
				// 			TimeUnit.MILLISECONDS.sleep(CommConstants.COMM_DELAY_TIME*10);
				// 		} catch(InterruptedException e){
				// 			System.out.println("InterruptedException");
				// 	}
				// }

				// for (int i=0; i<10; i++){
				// 	commMgr.sendMsg(CommConstants.ROBOT_TURN_RIGHT, CommConstants.MSG_TO_ARDUINO);
				// 	try{
				// 			TimeUnit.MILLISECONDS.sleep(CommConstants.COMM_DELAY_TIME*5);
				// 		} catch(InterruptedException e){
				// 			System.out.println("InterruptedException");
				// 		}
				// }
				// for (int i=0; i<10; i++){
				// 	commMgr.sendMsg(CommConstants.ROBOT_MOVE_FORWARD, CommConstants.MSG_TO_ARDUINO);
				// 	try{
				// 			TimeUnit.MILLISECONDS.sleep(CommConstants.COMM_DELAY_TIME*15);
				// 		} catch(InterruptedException e){
				// 			System.out.println("InterruptedException");
				// 		}
				// }



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