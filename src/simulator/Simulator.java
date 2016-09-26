package simulator;

import javax.swing.*;

import java.util.*;
import java.io.*;
import map.Map;
import map.MapConstants;
import map.MapGrid;
import robot.*;
import communication.*;

public class Simulator extends JFrame{

	// private static JFrame appFrame = null;

	// private static JPanel mainCards = null;
	// private static JPanel buttonCards = null;

	// private static int mapXLength;
	// private static int mapYLength;

	// private static JPanel robotConfig = null;

	// private static JPanel robotMap = null;

	// private static JPanel mainButtons = null;

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

		CommMgr mgr = CommMgr.getCommMgr();

		mgr.setConnection(1000);

		while(true){
			//do nothing
		}

		// while (true){
			

		// 	//CommMgr.getCommMgr().sendMsg("test", CommMgr.MSG_TYPE_ANDROID);

		// 	String test = CommMgr.getCommMgr().recvMsg();

		// }






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
